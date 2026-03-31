import {
  Calendar,
  Tag,
  IndianRupee,
  ArrowLeft,
  Clock,
  User,
  MessageSquare,
  Share2,
  Flag,
  X,
} from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useMutation } from "react-query";
import { useSelector } from "react-redux";
import type { RootState } from "@/store";

import ProjectDetailsSkeleton from "@/components/shared/project-details-skeleton";
import useGetBids from "../hooks/use-get-bids";
import useGetProjectDetails from "../hooks/use-get-project-details";
import BidList from "./bid-list";
import SubmitProposal from "./submit-proposal";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { toast } from "sonner";
import request from "@/apis/request";

const REPORT_REASONS = [
  "Scam / Fake Job",
  "Misleading Description",
  "Inappropriate Content",
  "Spam",
  "Other",
];

const ProjectDetails = () => {
  const navigate = useNavigate();
  const { project, error, isLoading } = useGetProjectDetails();
  const { bids, isLoading: bidIsLoading, refetch } = useGetBids();
  const { user, authToken } = useSelector((state: RootState) => state.auth);

  const [showReportModal, setShowReportModal] = useState(false);
  const [selectedReason, setSelectedReason] = useState("");
  const [reportDescription, setReportDescription] = useState("");

  const reportMutation = useMutation({
    mutationFn: () =>
      request({
        method: "POST",
        url: `/reports/${project?.id}`,
        authToken,
        data: { reason: selectedReason, description: reportDescription },
      }),
    onSuccess: () => {
      toast.success("Report submitted. Our team will review it.");
      setShowReportModal(false);
      setSelectedReason("");
      setReportDescription("");
    },
    onError: (err: { response?: { data?: { error?: { message?: string } } } }) => {
      toast.error(
        err?.response?.data?.error?.message ?? "Failed to submit report"
      );
    },
  });

  const handleShare = async () => {
    try {
      const currentUrl = window.location.href;
      await navigator.clipboard.writeText(currentUrl);
      toast.info("Link copied to clipboard!", {
        description: "You can now share this project with others.",
      });
    } catch {
      toast.error("Failed to copy link", {
        description: "Please try again or copy the URL manually.",
      });
    }
  };

  if (isLoading) return <ProjectDetailsSkeleton />;

  if (error || !project) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center p-4">
        <div className="bg-white rounded-xl shadow-sm p-8 max-w-md w-full text-center border">
          <div className="w-16 h-16 bg-[#FF6B47]/15 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg
              className="w-8 h-8 text-[#FF6B47]"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"
              />
            </svg>
          </div>
          <h2 className="text-xl font-semibold text-gray-900 mb-2">
            Project Not Found
          </h2>
          <p className="text-gray-600 mb-6">
            The project you're looking for doesn't exist or has been removed.
          </p>
          <Button onClick={() => navigate(-1)} className="w-full">
            Go Back
          </Button>
        </div>
      </div>
    );
  }

  const formattedDate = new Date(project.deadline).toLocaleDateString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });

  const timeAgo = new Date(project.createdAt || Date.now()).toLocaleDateString(
    "en-US",
    {
      month: "short",
      day: "numeric",
    },
  );

  return (
    <div className="min-h-screen bg-background">
      {/* Clean Header */}
      <div className="bg-white border-b border-[#E5E0D8]">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-4">
              <Button
                variant="ghost"
                size="sm"
                onClick={() => navigate(-1)}
                className="flex items-center gap-2"
              >
                <ArrowLeft className="h-4 w-4" />
                <span className="hidden sm:inline">Back to Projects</span>
              </Button>
              <div className="h-6 w-px bg-gray-300 hidden sm:block" />
              <h1 className="text-lg font-semibold text-gray-900 hidden sm:block">
                Project Details
              </h1>
            </div>

            <div className="flex items-center gap-2">
              <Button
                variant="ghost"
                size="sm"
                className="hidden sm:flex"
                onClick={handleShare}
              >
                <Share2 className="h-4 w-4 mr-2" />
                Share
              </Button>
              <Button
                variant="ghost"
                size="sm"
                className="sm:hidden"
                onClick={handleShare}
              >
                <Share2 className="h-4 w-4" />
              </Button>
              {user?.role === "ROLE_FREELANCER" && (
                <Button
                  variant="ghost"
                  size="sm"
                  className="text-[#FF6B47] hover:text-[#FF6B47] hover:bg-[#FF6B47]/10"
                  onClick={() => setShowReportModal(true)}
                >
                  <Flag className="h-4 w-4 mr-1.5" />
                  <span className="hidden sm:inline">Report</span>
                </Button>
              )}
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden mb-8">
          <div className="p-8">
            <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between gap-6 mb-8">
              <div className="flex-1">
                <div className="flex items-center gap-3 mb-4">
                  <Badge
                    variant={
                      project.status === "OPEN" ? "default" : "secondary"
                    }
                    className={`${
                      project.status === "OPEN"
                        ? "bg-[#2EC4B6]/20 text-[#1A1A2E]"
                        : "bg-gray-100 text-gray-600"
                    } px-3 py-1`}
                  >
                    {project.status}
                  </Badge>
                  <span className="text-sm text-gray-500">
                    Posted {timeAgo}
                  </span>
                </div>

                <h1 className="text-3xl lg:text-4xl font-bold text-gray-900 mb-4">
                  {project.title}
                </h1>

                <div className="flex items-center gap-3 mb-6">
                  <div className="w-10 h-10 bg-gray-100 rounded-full flex items-center justify-center">
                    <User className="h-5 w-5 text-gray-600" />
                  </div>
                  <div>
                    <p className="font-medium text-gray-900">
                      {project?.client?.name}
                    </p>
                    <p className="text-sm text-gray-600">Client</p>
                  </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
                  <div className="bg-[#F0EDE8] rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <IndianRupee className="h-4 w-4 text-[#2EC4B6]" />
                      <span className="text-sm font-medium text-gray-700">
                        Budget
                      </span>
                    </div>
                    <p className="text-xl font-bold text-gray-900">
                      ₹{project.budget.toLocaleString()}
                    </p>
                  </div>

                  <div className="bg-[#F0EDE8] rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <Calendar className="h-4 w-4 text-[#FF6B47]" />
                      <span className="text-sm font-medium text-gray-700">
                        Deadline
                      </span>
                    </div>
                    <p className="text-sm font-bold text-gray-900">
                      {formattedDate}
                    </p>
                  </div>

                  <div className="bg-[#F0EDE8] rounded-lg p-4">
                    <div className="flex items-center gap-2 mb-2">
                      <Tag className="h-4 w-4 text-[#1A1A2E]" />
                      <span className="text-sm font-medium text-gray-700">
                        Category
                      </span>
                    </div>
                    <p className="text-sm font-bold text-gray-900">
                      {project.category}
                    </p>
                  </div>
                </div>
              </div>

              {project.status === "OPEN" && (
                <div className="lg:w-80 flex-shrink-0">
                  <div className="bg-[#F0EDE8] rounded-xl border border-[#E5E0D8] p-6">
                    <div className="text-center mb-6">
                      <div className="inline-flex items-center gap-2 bg-[#2EC4B6]/20 text-[#1A1A2E] px-3 py-1 rounded-full text-sm font-medium mb-3">
                        <div className="w-2 h-2 bg-[#2EC4B6] rounded-full" />
                        Open for Proposals
                      </div>
                      <p className="text-sm text-gray-600">
                        Submit your proposal to win this project
                      </p>
                    </div>
                    <SubmitProposal
                      projectId={project.id}
                      refetchBids={refetch}
                    />
                  </div>
                </div>
              )}
            </div>

            <div className="border-t border-gray-200 pt-8">
              <h2 className="text-xl font-semibold text-gray-900 mb-4">
                Project Description
              </h2>
              <div className="prose max-w-none">
                <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">
                  {project.description}
                </p>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
          <div className="bg-[#F0EDE8] border-b border-[#E5E0D8] p-6">
            <div className="flex items-center justify-between">
              <div>
                <h2 className="text-xl font-semibold text-gray-900">
                  Project Proposals
                </h2>
                <p className="text-gray-600 text-sm">
                  Review freelancer proposals for this project
                </p>
              </div>

              {Array.isArray(bids) && bids.length > 0 && (
                <div className="text-right">
                  <div className="text-2xl font-bold text-gray-900">
                    {bids.length}
                  </div>
                  <div className="text-sm text-gray-600">
                    proposal{bids.length !== 1 ? "s" : ""}
                  </div>
                </div>
              )}
            </div>
          </div>

          <div className="p-6">
            {bidIsLoading ? (
              <div className="flex flex-col items-center justify-center py-12">
                <div className="w-8 h-8 border-2 border-gray-300 border-t-gray-600 rounded-full animate-spin mb-4"></div>
                <p className="text-gray-600">Loading proposals...</p>
              </div>
            ) : Array.isArray(bids) && bids.length > 0 ? (
              <BidList bids={bids} />
            ) : (
              <div className="text-center py-12">
                <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4">
                  <MessageSquare className="w-8 h-8 text-gray-400" />
                </div>
                <h3 className="text-lg font-semibold text-gray-900 mb-2">
                  No proposals yet
                </h3>
                <p className="text-gray-600 mb-4">
                  This project is waiting for freelancers to submit their
                  proposals.
                </p>
                <div className="flex items-center justify-center gap-2 text-sm text-gray-500">
                  <Clock className="w-4 h-4" />
                  <span>Be the first to apply!</span>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Report Modal */}
      {showReportModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl max-w-md w-full p-6">
            <div className="flex items-center justify-between mb-4">
              <div className="flex items-center gap-2">
                <Flag className="w-5 h-5 text-[#FF6B47]" />
                <h2 className="text-lg font-semibold text-gray-900">
                  Report Project
                </h2>
              </div>
              <button
                onClick={() => setShowReportModal(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <p className="text-sm text-gray-600 mb-4">
              Help us keep SkillConnect safe. Select a reason for reporting this
              project.
            </p>

            <div className="space-y-2 mb-4">
              {REPORT_REASONS.map((reason) => (
                <label
                  key={reason}
                  className={`flex items-center gap-3 p-3 rounded-lg border cursor-pointer transition-colors ${
                    selectedReason === reason
                      ? "border-[#FF6B47] bg-[#FF6B47]/5"
                      : "border-gray-200 hover:border-gray-300"
                  }`}
                >
                  <input
                    type="radio"
                    name="reason"
                    value={reason}
                    checked={selectedReason === reason}
                    onChange={() => setSelectedReason(reason)}
                    className="accent-[#FF6B47]"
                  />
                  <span className="text-sm text-gray-700">{reason}</span>
                </label>
              ))}
            </div>

            <textarea
              placeholder="Additional details (optional)"
              value={reportDescription}
              onChange={(e) => setReportDescription(e.target.value)}
              rows={3}
              className="w-full border border-gray-200 rounded-lg p-3 text-sm text-gray-700 resize-none focus:outline-none focus:border-[#FF6B47] mb-4"
            />

            <div className="flex gap-3">
              <Button
                variant="outline"
                className="flex-1"
                onClick={() => setShowReportModal(false)}
              >
                Cancel
              </Button>
              <Button
                className="flex-1 bg-[#FF6B47] hover:bg-[#e55a38] text-white"
                disabled={!selectedReason || reportMutation.isLoading}
                onClick={() => reportMutation.mutate()}
              >
                {reportMutation.isLoading ? "Submitting..." : "Submit Report"}
              </Button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProjectDetails;
