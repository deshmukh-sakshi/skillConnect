import { useQuery } from "react-query";
import {
  AlertTriangle,
  Calendar,
  Clock,
  DollarSign,
  ExternalLink,
  FileText,
  Mail,
  Tag,
  Trash2,
  User,
  X,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import adminApis from "../apis";
import {
  type Report,
  type ProjectDetails,
  formatDate,
  formatBudget,
  getReasonStyle,
} from "./types";

interface ProjectDetailPanelProps {
  report: Report;
  authToken: string;
  onClose: () => void;
  onDelete: (report: Report) => void;
  onDismiss: (reportId: number) => void;
  isDeleting: boolean;
  isDismissing: boolean;
}

const ProjectDetailPanel = ({
  report,
  authToken,
  onClose,
  onDelete,
  onDismiss,
  isDeleting,
  isDismissing,
}: ProjectDetailPanelProps) => {
  const { data, isLoading, isError, refetch } = useQuery(
    ["admin-project-details", report.projectId],
    () => adminApis.getProjectDetails({ authToken, projectId: report.projectId }),
    {
      select: (res) => res.data?.data as ProjectDetails,
      retry: 1,
    }
  );

  return (
    <>
      {/* Backdrop */}
      <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-40" onClick={onClose} />

      {/* Panel */}
      <div className="fixed right-0 top-0 h-full w-[480px] bg-white shadow-2xl z-50 flex flex-col">
        {/* Panel header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-gray-100">
          <div className="flex items-center gap-2">
            <FileText className="w-4 h-4 text-gray-400" />
            <span className="text-sm font-semibold text-gray-800">Project Details</span>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-lg hover:bg-gray-100 text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Report context banner */}
        <div className="mx-4 mt-4 px-4 py-3 bg-[#FF6B47]/8 border border-[#FF6B47]/20 rounded-xl">
          <div className="flex items-start gap-2">
            <AlertTriangle className="w-4 h-4 text-[#FF6B47] mt-0.5 flex-shrink-0" />
            <div>
              <p className="text-xs font-semibold text-[#FF6B47] mb-0.5">
                Reported for: {report.reason}
              </p>
              <p className="text-xs text-gray-500">
                by {report.reportedByName ?? report.reportedByEmail} · {formatDate(report.createdAt)}
              </p>
              {report.description && (
                <p className="text-xs text-gray-600 mt-1.5 italic">"{report.description}"</p>
              )}
            </div>
          </div>
        </div>

        {/* Project content */}
        <div className="flex-1 overflow-y-auto px-6 py-4">
          {isLoading ? (
            <div className="flex flex-col items-center justify-center py-20">
              <div className="w-8 h-8 border-2 border-gray-200 border-t-[#FF6B47] rounded-full animate-spin mb-3" />
              <p className="text-sm text-gray-400">Loading project...</p>
            </div>
          ) : isError || !data ? (
            <div className="flex flex-col items-center justify-center py-20 text-center">
              <div className="w-12 h-12 bg-red-50 rounded-full flex items-center justify-center mb-3">
                <FileText className="w-5 h-5 text-red-300" />
              </div>
              <p className="text-sm font-medium text-gray-600">Failed to load project</p>
              <p className="text-xs text-gray-400 mt-1 mb-4">
                Restart the backend and try again.
              </p>
              <button
                onClick={() => refetch()}
                className="text-xs text-[#FF6B47] border border-[#FF6B47]/30 px-3 py-1.5 rounded-lg hover:bg-[#FF6B47]/5 transition-colors"
              >
                Retry
              </button>
            </div>
          ) : (
            <ProjectContent project={data} />
          )}
        </div>

        {/* Action footer */}
        <div className="px-6 py-4 border-t border-gray-100 bg-gray-50/50 flex gap-3">
          <Button
            variant="outline"
            size="sm"
            className="flex-1 text-gray-500 border-gray-200 text-xs"
            onClick={() => onDismiss(report.id)}
            disabled={isDismissing || isDeleting}
          >
            <X className="w-3.5 h-3.5 mr-1.5" />
            Dismiss Report
          </Button>
          <Button
            size="sm"
            className="flex-1 bg-red-500 hover:bg-red-600 text-white text-xs shadow-sm shadow-red-200"
            onClick={() => onDelete(report)}
            disabled={isDeleting || isDismissing || !data}
          >
            <Trash2 className="w-3.5 h-3.5 mr-1.5" />
            Delete Project
          </Button>
        </div>
      </div>
    </>
  );
};

const ProjectContent = ({ project }: { project: ProjectDetails }) => (
  <div className="space-y-5">
    {/* Title + status */}
    <div>
      <div className="flex items-start justify-between gap-3 mb-2">
        <h2 className="text-lg font-bold text-gray-900 leading-snug">{project.title}</h2>
        <span
          className={`flex-shrink-0 text-xs font-semibold px-2.5 py-1 rounded-full border ${
            project.status === "OPEN"
              ? "bg-emerald-50 text-emerald-600 border-emerald-200"
              : "bg-gray-100 text-gray-500 border border-gray-200"
          }`}
        >
          {project.status}
        </span>
      </div>
      <div className="flex items-center gap-3 text-xs text-gray-400">
        <span className="flex items-center gap-1">
          <Tag className="w-3 h-3" />
          {project.category}
        </span>
        {project.bidCount !== undefined && (
          <span className="flex items-center gap-1">
            <ExternalLink className="w-3 h-3" />
            {project.bidCount} bid{project.bidCount !== 1 ? "s" : ""}
          </span>
        )}
      </div>
    </div>

    {/* Stats */}
    <div className="grid grid-cols-2 gap-3">
      <div className="bg-gray-50 rounded-xl p-3 border border-gray-100">
        <div className="flex items-center gap-2 mb-1">
          <DollarSign className="w-3.5 h-3.5 text-emerald-500" />
          <span className="text-xs text-gray-400 font-medium">Budget</span>
        </div>
        <p className="text-base font-bold text-gray-900">{formatBudget(project.budget)}</p>
      </div>
      <div className="bg-gray-50 rounded-xl p-3 border border-gray-100">
        <div className="flex items-center gap-2 mb-1">
          <Calendar className="w-3.5 h-3.5 text-blue-500" />
          <span className="text-xs text-gray-400 font-medium">Deadline</span>
        </div>
        <p className="text-sm font-bold text-gray-900">{formatDate(project.deadline)}</p>
      </div>
    </div>

    {/* Description */}
    <div>
      <h3 className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
        Project Description
      </h3>
      <div className="bg-gray-50 border border-gray-100 rounded-xl p-4">
        <p className="text-sm text-gray-700 leading-relaxed whitespace-pre-wrap">{project.description}</p>
      </div>
    </div>

    {/* Client */}
    {project.client && (
      <div>
        <h3 className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">Posted By</h3>
        <div className="flex items-center gap-3 bg-gray-50 border border-gray-100 rounded-xl p-3">
          <div className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center flex-shrink-0">
            <User className="w-4 h-4 text-gray-400" />
          </div>
          <div>
            <p className="text-sm font-semibold text-gray-800">{project.client.name ?? "Client"}</p>
            {project.client.email && (
              <p className="text-xs text-gray-400 flex items-center gap-1">
                <Mail className="w-3 h-3" />
                {project.client.email}
              </p>
            )}
          </div>
        </div>
      </div>
    )}

    {project.createdAt && (
      <p className="text-xs text-gray-300 flex items-center gap-1">
        <Clock className="w-3 h-3" />
        Posted on {formatDate(project.createdAt)}
      </p>
    )}
  </div>
);

export default ProjectDetailPanel;
