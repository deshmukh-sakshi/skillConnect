import { useState } from "react";
import {
  Calendar,
  Clock,
  IndianRupee,
  FileText,
  Trash2,
  Edit3,
  Users,
  MoreHorizontal,
  TrendingUp,
  Award,
} from "lucide-react";
import { useMutation } from "react-query";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent } from "@/components/ui/card";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import type { BidType } from "@/types";
import useAuth from "@/hooks/use-auth";
import apis from "../../apis";
import UpdateProposalDialog from "./update-dialog";

interface BidCardProps {
  bid: BidType;
  onRefetch: () => void;
}

const BidCard = ({ bid, onRefetch }: BidCardProps) => {
  const [isUpdateDialogOpen, setIsUpdateDialogOpen] = useState(false);
  const { user, authToken } = useAuth();

  const { mutate: deleteBid, isLoading: isDeleting } = useMutation({
    mutationFn: (bidId: string) =>
      apis.deleteBid({
        authToken: authToken!,
        freelancerId: String(user?.id),
        bidId,
      }),
    onSuccess: () => {
      toast.success("Bid deleted successfully");
      onRefetch();
    },
    onError: () => {
      toast.error("Failed to delete bid");
    },
  });

  const handleDelete = () => {
    deleteBid(String(bid.bidId));
  };

  const getStatusConfig = (status: string) => {
    switch (status) {
      case "Pending":
        return {
          variant: "secondary" as const,
          borderColor: "border-l-[#FFB900]",
          bgGradient: "bg-gradient-to-r from-[#F0EDE8] to-[#FAF8F5]",
          textColor: "text-[#1A1A2E]",
          iconBg: "bg-gradient-to-r from-[#FFB900] to-[#FF6B47]",
        };
      case "Accepted":
        return {
          variant: "default" as const,
          borderColor: "border-l-[#2EC4B6]",
          bgGradient: "bg-gradient-to-r from-[#2EC4B6]/10 to-[#FAF8F5]",
          textColor: "text-[#1A1A2E]",
          iconBg: "bg-gradient-to-r from-[#2EC4B6] to-[#5ED4C9]",
        };
      case "Rejected":
        return {
          variant: "destructive" as const,
          borderColor: "border-l-[#FF6B47]",
          bgGradient: "bg-gradient-to-r from-[#FF6B47]/10 to-[#FAF8F5]",
          textColor: "text-[#1A1A2E]",
          iconBg: "bg-gradient-to-r from-[#FF6B47] to-[#FF8A6F]",
        };
      default:
        return {
          variant: "outline" as const,
          borderColor: "border-l-slate-400",
          bgGradient: "bg-gradient-to-r from-slate-50 to-gray-50",
          textColor: "text-slate-700",
          iconBg: "bg-gradient-to-r from-slate-400 to-gray-400",
        };
    }
  };

  const statusConfig = getStatusConfig(bid.status);
  const isPending = bid.status === "Pending";

  // Calculate bid difference percentage
  const bidDifference =
    ((bid.bidAmount - bid.project.budget) / bid.project.budget) * 100;
  const isCompetitive = bidDifference <= 0;

  return (
    <>
      <Card
        className={`${statusConfig.borderColor} border-l-4 hover:shadow-lg transition-all duration-200 group bg-gradient-to-br from-white via-[#FAF8F5]/60 to-[#F0EDE8]/40 backdrop-blur-sm border border-[#E5E0D8] rounded-lg overflow-hidden hover:scale-[1.01]`}
      >
        <CardContent className="p-3">
          {/* Header Section */}
          <div className="flex justify-between items-start mb-3">
            <div className="flex-1 min-w-0">
              <div className="flex items-start gap-2 mb-2">
                <div
                  className={`p-1.5 rounded-md ${statusConfig.iconBg} shadow-sm`}
                >
                  <Award className="h-3 w-3 text-white" />
                </div>
                <div className="flex-1">
                  <h3 className="text-sm font-bold text-slate-800 mb-1 line-clamp-1 group-hover:text-slate-900 transition-colors">
                    {bid.project.title}
                  </h3>
                  <div className="flex items-center gap-1.5 flex-wrap">
                    <Badge className="bg-gradient-to-r from-[#FF6B47]/15 to-[#2EC4B6]/15 text-[#1A1A2E] border-[#E5E0D8] text-xs font-medium px-2 py-0.5">
                      {bid.project.category}
                    </Badge>
                    <Badge
                      variant={statusConfig.variant}
                      className={`text-xs font-medium px-2 py-0.5 ${statusConfig.bgGradient} ${statusConfig.textColor} border-0 shadow-sm`}
                    >
                      {bid.status}
                    </Badge>
                    {isCompetitive && (
                      <Badge className="bg-gradient-to-r from-[#2EC4B6]/20 to-[#2EC4B6]/10 text-[#1A1A2E] border-[#2EC4B6]/30 text-xs font-medium px-1.5 py-0.5">
                        <TrendingUp className="h-2.5 w-2.5 mr-0.5" />
                        Competitive
                      </Badge>
                    )}
                  </div>
                </div>
              </div>
            </div>

            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  variant="ghost"
                  size="sm"
                  className="h-6 w-6 p-0 opacity-0 group-hover:opacity-100 transition-all duration-200 hover:bg-slate-100 rounded-md"
                >
                  <MoreHorizontal className="h-3 w-3 text-slate-600" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-40">
                {isPending && (
                  <DropdownMenuItem
                    onClick={() => setIsUpdateDialogOpen(true)}
                    className="text-[#FF6B47] focus:text-[#E55A38] focus:bg-[#FF6B47]/10 text-xs"
                  >
                    <Edit3 className="h-3 w-3 mr-1.5" />
                    Edit Proposal
                  </DropdownMenuItem>
                )}
                <DropdownMenuItem
                  onClick={handleDelete}
                  disabled={isDeleting}
                  className="text-[#FF6B47] focus:text-[#E55A38] focus:bg-[#FF6B47]/10 text-xs"
                >
                  <Trash2 className="h-3 w-3 mr-1.5" />
                  {isDeleting ? "Deleting..." : "Delete"}
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>

          {/* Compact Stats Grid */}
          <div className="grid grid-cols-3 lg:grid-cols-5 gap-2 mb-3">
            {/* Project Budget */}
            <div className="bg-gradient-to-br from-slate-50 to-slate-100 p-2 rounded-lg border border-slate-200/50 text-center">
              <div className="flex items-center justify-center mb-1">
                <IndianRupee className="h-3 w-3 text-slate-600" />
              </div>
              <p className="text-xs text-slate-600 font-medium mb-0.5">
                Budget
              </p>
              <p className="text-xs font-bold text-slate-800">
                ₹{(bid.project.budget / 1000).toFixed(0)}k
              </p>
            </div>

            {/* Your Bid */}
            <div className="bg-gradient-to-br from-[#2EC4B6]/12 to-[#FAF8F5] p-2 rounded-lg border border-[#2EC4B6]/30 text-center">
              <div className="flex items-center justify-center mb-1">
                <IndianRupee className="h-3 w-3 text-[#2EC4B6]" />
              </div>
              <p className="text-xs text-[#1A1A2E] font-medium mb-0.5">
                Your Bid
              </p>
              <p className="text-xs font-bold text-[#1A1A2E]">
                ₹{(bid.bidAmount / 1000).toFixed(0)}k
              </p>
              {bidDifference !== 0 && (
                <p
                  className={`text-xs font-medium ${
                    bidDifference > 0 ? "text-[#FF6B47]" : "text-[#2EC4B6]"
                  }`}
                >
                  {bidDifference > 0 ? "+" : ""}
                  {bidDifference.toFixed(1)}%
                </p>
              )}
            </div>

            {/* Duration */}
            <div className="bg-gradient-to-br from-[#FF6B47]/12 to-[#FAF8F5] p-2 rounded-lg border border-[#FF6B47]/30 text-center">
              <div className="flex items-center justify-center mb-1">
                <Clock className="h-3 w-3 text-[#FF6B47]" />
              </div>
              <p className="text-xs text-[#1A1A2E] font-medium mb-0.5">
                Duration
              </p>
              <p className="text-xs font-bold text-[#1A1A2E]">
                {bid.durationDays}d
              </p>
            </div>

            {/* Team Size - Hidden on mobile */}
            <div className="bg-gradient-to-br from-[#2EC4B6]/10 to-[#F0EDE8] p-2 rounded-lg border border-[#2EC4B6]/25 text-center lg:block hidden">
              <div className="flex items-center justify-center mb-1">
                <Users className="h-3 w-3 text-[#2EC4B6]" />
              </div>
              <p className="text-xs text-[#1A1A2E] font-medium mb-0.5">Team</p>
              <p className="text-xs font-bold text-[#1A1A2E]">
                {bid.teamSize || 1}
              </p>
            </div>

            {/* Deadline - Hidden on mobile */}
            <div className="bg-gradient-to-br from-[#FFB900]/15 to-[#FAF8F5] p-2 rounded-lg border border-[#FFB900]/35 text-center lg:block hidden">
              <div className="flex items-center justify-center mb-1">
                <Calendar className="h-3 w-3 text-[#FFB900]" />
              </div>
              <p className="text-xs text-[#1A1A2E] font-medium mb-0.5">
                Deadline
              </p>
              <p className="text-xs font-bold text-[#1A1A2E]">
                {new Date(bid.project.deadline).toLocaleDateString("en-IN", {
                  month: "short",
                  day: "numeric",
                })}
              </p>
            </div>
          </div>

          {/* Compact Proposal Section */}
          <div className="bg-gradient-to-r from-slate-50 to-gray-50 p-2.5 rounded-lg border border-slate-200/50">
            <div className="flex items-center mb-1.5">
              <FileText className="h-3 w-3 text-slate-600 mr-1.5" />
              <span className="text-xs font-semibold text-slate-700">
                Proposal
              </span>
            </div>
            <p className="text-xs text-slate-600 leading-relaxed line-clamp-2">
              {bid.proposal}
            </p>
          </div>

          {/* Mobile Action Buttons */}
          <div className="flex justify-end gap-2 pt-2 lg:hidden">
            {isPending && (
              <Button
                variant="outline"
                size="sm"
                onClick={() => setIsUpdateDialogOpen(true)}
                className="bg-gradient-to-r from-[#FF6B47]/10 to-[#2EC4B6]/10 text-[#1A1A2E] border-[#E5E0D8] hover:from-[#FF6B47]/20 hover:to-[#2EC4B6]/20 font-medium px-3 py-1 h-6 text-xs rounded-md transition-all duration-200"
              >
                <Edit3 className="h-3 w-3 mr-1" />
                Edit
              </Button>
            )}

            <Button
              variant="outline"
              size="sm"
              onClick={handleDelete}
              disabled={isDeleting}
              className="bg-gradient-to-r from-[#FF6B47]/10 to-[#FF8A6F]/10 text-[#1A1A2E] border-[#FF6B47]/30 hover:from-[#FF6B47]/20 hover:to-[#FF8A6F]/20 font-medium px-3 py-1 h-6 text-xs rounded-md transition-all duration-200"
            >
              <Trash2 className="h-3 w-3 mr-1" />
              {isDeleting ? "..." : "Delete"}
            </Button>
          </div>
        </CardContent>
      </Card>

      <UpdateProposalDialog
        bid={bid}
        open={isUpdateDialogOpen}
        onOpenChange={setIsUpdateDialogOpen}
        onSuccess={onRefetch}
      />
    </>
  );
};

export default BidCard;
