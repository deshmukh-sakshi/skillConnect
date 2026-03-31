import { useState, useEffect, useCallback } from "react";
import { useSelector } from "react-redux";
import {
  IndianRupee,
  CalendarDays,
  Users,
  CheckIcon,
  XIcon,
  Loader2,
  AlertTriangleIcon,
} from "lucide-react";
import { format } from "date-fns";

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";

import { ChatInterface } from "./ChatInterface";
import { chatApis } from "../apis";
import type { RootState } from "@/store";
import { cn } from "@/lib/utils";
import type { ApiError } from "@/types";

interface BidDetailsResponse {
  bidId: number;
  projectId: number;
  projectTitle: string;
  freelancerId: number;
  freelancerName: string;
  proposal: string;
  bidAmount: number;
  durationDays: number;
  teamSize: number;
  status: string;
  createdAt: string;
  canAccept: boolean;
  canReject: boolean;
}

interface BidNegotiationChatProps {
  chatRoomId: number;
  className?: string;
}

export const BidNegotiationChat = ({
  chatRoomId,
  className,
}: BidNegotiationChatProps) => {
  const [bidDetails, setBidDetails] = useState<BidDetailsResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [isProcessing, setIsProcessing] = useState<boolean>(false);
  const [transitionState, setTransitionState] = useState<
    "none" | "accepting" | "rejecting" | "accepted" | "rejected"
  >("none");
  const [transitionError, setTransitionError] = useState<string | null>(null);
  const authToken = useSelector((state: RootState) => state.auth?.authToken);

  useEffect(() => {
    const fetchBidDetails = async () => {
      if (!authToken || bidDetails) return;
      try {
        setIsLoading(true);
        const response = await chatApis.getBidDetailsForChat(chatRoomId, authToken);
        setBidDetails(response.data.data);
      } catch (err) {
        const apiError = err as ApiError;
        setError(
          apiError?.response?.data?.error?.message ||
            apiError?.message ||
            "Failed to load bid details",
        );
      } finally {
        setIsLoading(false);
      }
    };
    fetchBidDetails();
  }, [chatRoomId, authToken, bidDetails]);

  const handleAcceptBid = useCallback(async () => {
    if (!authToken || !bidDetails) return;
    try {
      setIsProcessing(true);
      setTransitionState("accepting");
      setTransitionError(null);
      await chatApis.acceptBidInChat(bidDetails.bidId, authToken);
      setBidDetails((prev) =>
        prev ? { ...prev, status: "Accepted", canAccept: false, canReject: false } : null,
      );
      setTransitionState("accepted");
    } catch (err) {
      const apiError = err as ApiError;
      const msg =
        apiError?.response?.data?.error?.message ||
        apiError?.message ||
        "Failed to accept bid";
      setError(msg);
      setTransitionError(msg);
      setTransitionState("none");
    } finally {
      setIsProcessing(false);
    }
  }, [authToken, bidDetails]);

  const handleRejectBid = useCallback(async () => {
    if (!authToken || !bidDetails) return;
    try {
      setIsProcessing(true);
      setTransitionState("rejecting");
      setTransitionError(null);
      await chatApis.rejectBidInChat(bidDetails.bidId, authToken);
      setBidDetails((prev) =>
        prev ? { ...prev, status: "Rejected", canAccept: false, canReject: false } : null,
      );
      setTransitionState("rejected");
    } catch (err) {
      const apiError = err as ApiError;
      const msg =
        apiError?.response?.data?.error?.message ||
        apiError?.message ||
        "Failed to reject bid";
      setError(msg);
      setTransitionError(msg);
      setTransitionState("none");
    } finally {
      setIsProcessing(false);
    }
  }, [authToken, bidDetails]);

  const formatDate = (dateString: string) => {
    try {
      return format(new Date(dateString), "MMM d, yyyy");
    } catch {
      return dateString;
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "Accepted":
        return "bg-emerald-50 text-emerald-700 border-emerald-200 dark:bg-emerald-950 dark:text-emerald-300";
      case "Rejected":
        return "bg-red-50 text-red-700 border-red-200 dark:bg-red-950 dark:text-red-300";
      default:
        return "bg-amber-50 text-amber-700 border-amber-200 dark:bg-amber-950 dark:text-amber-300";
    }
  };

  if (isLoading && !bidDetails) {
    return (
      <div className="flex items-center justify-center h-full">
        <Loader2 className="h-6 w-6 animate-spin text-muted-foreground" />
      </div>
    );
  }

  if (error && !bidDetails) {
    return (
      <div className="flex items-center justify-center h-full text-destructive text-sm px-4 text-center">
        {error}
      </div>
    );
  }

  if (!bidDetails) {
    return (
      <div className="flex items-center justify-center h-full text-muted-foreground text-sm">
        Bid details not available.
      </div>
    );
  }

  const isDisabled =
    transitionState === "accepting" ||
    transitionState === "rejecting" ||
    transitionState === "accepted" ||
    transitionState === "rejected";

  return (
    <div className={cn("flex flex-col h-full max-h-full", className)}>
      {/* Bid details header — compact strip */}
      <div className="shrink-0 border-b bg-background px-4 py-3 space-y-2">
        {/* Transition / error banners */}
        {transitionState !== "none" && (
          <div
            className={cn(
              "flex items-center gap-2 text-xs px-3 py-2 rounded-lg",
              transitionState === "accepting" || transitionState === "rejecting"
                ? "bg-blue-50 text-blue-700 dark:bg-blue-950 dark:text-blue-300"
                : transitionState === "accepted"
                  ? "bg-emerald-50 text-emerald-700 dark:bg-emerald-950 dark:text-emerald-300"
                  : "bg-amber-50 text-amber-700 dark:bg-amber-950 dark:text-amber-300",
            )}
          >
            {(transitionState === "accepting" || transitionState === "rejecting") && (
              <Loader2 className="h-3 w-3 animate-spin" />
            )}
            {transitionState === "accepted" && <CheckIcon className="h-3 w-3" />}
            {transitionState === "rejected" && <XIcon className="h-3 w-3" />}
            <span>
              {transitionState === "accepting" && "Processing acceptance…"}
              {transitionState === "rejecting" && "Processing rejection…"}
              {transitionState === "accepted" && "Bid accepted — contract created."}
              {transitionState === "rejected" && "Bid rejected."}
            </span>
          </div>
        )}

        {transitionError && (
          <div className="flex items-center gap-2 text-xs px-3 py-2 rounded-lg bg-destructive/10 text-destructive">
            <AlertTriangleIcon className="h-3 w-3" />
            {transitionError}
          </div>
        )}

        {/* Bid title row */}
        <div className="flex items-center justify-between gap-3">
          <div className="min-w-0">
            <p className="font-semibold text-sm truncate">{bidDetails.projectTitle}</p>
            <p className="text-[11px] text-muted-foreground mt-0.5">
              Submitted {formatDate(bidDetails.createdAt)}
            </p>
          </div>
          <Badge
            variant="outline"
            className={cn("text-xs shrink-0", getStatusColor(bidDetails.status))}
          >
            {bidDetails.status}
          </Badge>
        </div>

        {/* Stats row */}
        <div className="flex items-center gap-5 text-xs text-muted-foreground">
          <span className="flex items-center gap-1">
            <IndianRupee className="h-3 w-3" />
            {bidDetails.bidAmount.toFixed(0)}
          </span>
          <span className="flex items-center gap-1">
            <CalendarDays className="h-3 w-3" />
            {bidDetails.durationDays} days
          </span>
          <span className="flex items-center gap-1">
            <Users className="h-3 w-3" />
            {bidDetails.teamSize} {bidDetails.teamSize === 1 ? "person" : "people"}
          </span>
        </div>

        {/* Proposal preview */}
        <p className="text-xs text-muted-foreground truncate">
          {bidDetails.proposal.split("\n")[0]}
        </p>

        {/* Accept / Reject buttons */}
        {(bidDetails.canAccept || bidDetails.canReject) && transitionState === "none" && (
          <div className="flex gap-2 pt-1">
            {bidDetails.canAccept && (
              <AlertDialog>
                <AlertDialogTrigger asChild>
                  <Button
                    size="sm"
                    className="h-7 text-xs bg-emerald-600 hover:bg-emerald-700"
                    disabled={isProcessing}
                  >
                    {isProcessing ? (
                      <Loader2 className="h-3 w-3 animate-spin" />
                    ) : (
                      <>
                        <CheckIcon className="h-3 w-3 mr-1" />
                        Accept Bid
                      </>
                    )}
                  </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle>Accept this bid?</AlertDialogTitle>
                    <AlertDialogDescription>
                      This will create a contract and close the project to other
                      bidders. The chat will become a contract chat.
                    </AlertDialogDescription>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction onClick={handleAcceptBid}>Accept</AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
            )}

            {bidDetails.canReject && (
              <AlertDialog>
                <AlertDialogTrigger asChild>
                  <Button
                    size="sm"
                    variant="destructive"
                    className="h-7 text-xs"
                    disabled={isProcessing}
                  >
                    {isProcessing ? (
                      <Loader2 className="h-3 w-3 animate-spin" />
                    ) : (
                      <>
                        <XIcon className="h-3 w-3 mr-1" />
                        Reject
                      </>
                    )}
                  </Button>
                </AlertDialogTrigger>
                <AlertDialogContent>
                  <AlertDialogHeader>
                    <AlertDialogTitle>Reject this bid?</AlertDialogTitle>
                    <AlertDialogDescription>
                      This action cannot be undone. The freelancer will be
                      notified and this chat will be closed.
                    </AlertDialogDescription>
                  </AlertDialogHeader>
                  <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction onClick={handleRejectBid}>Reject</AlertDialogAction>
                  </AlertDialogFooter>
                </AlertDialogContent>
              </AlertDialog>
            )}
          </div>
        )}
      </div>

      {/* Chat */}
      <div className="flex-1 min-h-0">
        <ChatInterface
          chatRoomId={chatRoomId}
          chatType="BID_NEGOTIATION"
          referenceId={bidDetails.bidId}
          className="h-full"
          disabled={isDisabled}
        />
      </div>
    </div>
  );
};
