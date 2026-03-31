import { useState, useEffect, useCallback } from "react";
import { useSelector } from "react-redux";
import {
  Loader2,
  CalendarDays,
  IndianRupee,
  CheckCircle2,
  LayoutList,
  MessageSquare,
} from "lucide-react";

import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { Alert, AlertDescription } from "@/components/ui/alert";

import { ChatInterface } from "./ChatInterface";
import { MilestonePanel } from "./MilestonePanel";
import { chatApis } from "../apis";
import type { RootState } from "@/store";
import { cn } from "@/lib/utils";
import type { ApiError } from "@/types";

interface ContractDetailsResponse {
  contractId: number;
  projectId: number;
  projectTitle: string;
  bidId: number;
  clientId: number;
  clientName: string;
  freelancerId: number;
  freelancerName: string;
  contractAmount: number;
  durationDays: number;
  contractStatus: string;
  createdAt: string;
  updatedAt: string;
}

interface MilestoneNotification {
  id: number;
  title: string;
  status: string;
  action: string;
}

interface ContractChatProps {
  chatRoomId: number;
  className?: string;
}

export const ContractChat = ({ chatRoomId, className }: ContractChatProps) => {
  const [contractDetails, setContractDetails] =
    useState<ContractDetailsResponse | null>(null);
  const [completionPercentage, setCompletionPercentage] = useState<number>(0);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTab, setActiveTab] = useState<string>("chat");
  const [overdueMilestones, setOverdueMilestones] = useState<number>(0);
  const [milestoneNotification, setMilestoneNotification] =
    useState<MilestoneNotification | null>(null);

  const authToken = useSelector((state: RootState) => state.auth?.authToken);

  const refreshMilestoneData = useCallback(async () => {
    if (!authToken || !contractDetails?.contractId) return;

    try {
      const completionResponse = await chatApis.getMilestoneCompletionForChat(
        chatRoomId,
        authToken,
      );
      if (completionResponse.data.status === "success") {
        setCompletionPercentage(completionResponse.data.data);
      }

      const overdueResponse = await chatApis.getOverdueMilestonesForChat(
        chatRoomId,
        authToken,
      );
      if (overdueResponse.data.status === "success") {
        setOverdueMilestones(overdueResponse.data.data.length);
      }
    } catch (err) {
      console.error("Failed to refresh milestone information:", err);
    }
  }, [chatRoomId, authToken, contractDetails?.contractId]);

  useEffect(() => {
    const fetchContractDetails = async () => {
      if (!authToken) return;

      try {
        setIsLoading(true);
        const response = await chatApis.getContractDetailsForChat(
          chatRoomId,
          authToken,
        );
        setContractDetails(response.data.data);

        if (response.data.data?.contractId) {
          try {
            const completionResponse =
              await chatApis.getMilestoneCompletionForChat(
                chatRoomId,
                authToken,
              );
            if (completionResponse.data.status === "success") {
              setCompletionPercentage(completionResponse.data.data);
            }

            const overdueResponse = await chatApis.getOverdueMilestonesForChat(
              chatRoomId,
              authToken,
            );
            if (overdueResponse.data.status === "success") {
              setOverdueMilestones(overdueResponse.data.data.length);
            }
          } catch (err) {
            console.error("Failed to fetch milestone information:", err);
          }
        }
      } catch (err) {
        const apiError = err as ApiError;
        setError(
          apiError?.response?.data?.error?.message ||
            apiError?.message ||
            "Failed to load contract details",
        );
      } finally {
        setIsLoading(false);
      }
    };

    fetchContractDetails();
  }, [chatRoomId, authToken]);

  const handleMilestoneNotification = useCallback(
    async (notification: MilestoneNotification) => {
      if (!authToken || !chatRoomId) return;

      try {
        setMilestoneNotification(notification);

        let notificationMessage = "";
        switch (notification.action) {
          case "created":
            notificationMessage = `New milestone created: "${notification.title}"`;
            break;
          case "updated":
            notificationMessage = `Milestone "${notification.title}" was updated`;
            break;
          case "status_change":
            notificationMessage = `Milestone "${notification.title}" status changed to ${notification.status}`;
            break;
          case "deleted":
            notificationMessage = `Milestone "${notification.title}" was deleted`;
            break;
          default:
            notificationMessage = `Milestone update: "${notification.title}"`;
        }

        await chatApis.sendMilestoneNotification(
          chatRoomId,
          notificationMessage,
          authToken,
        );

        await refreshMilestoneData();

        setTimeout(() => {
          setMilestoneNotification(null);
        }, 3000);
      } catch (err) {
        console.error("Failed to send milestone notification:", err);
        setError("Failed to send milestone notification to chat");
      }
    },
    [chatRoomId, authToken, refreshMilestoneData],
  );

  const handleMilestoneStatusUpdate = useCallback(
    async (milestoneId: number, title: string, status: string) => {
      if (!authToken || !chatRoomId) return;

      try {
        await chatApis.updateMilestoneStatusFromChat(
          chatRoomId,
          milestoneId,
          status,
          authToken,
        );

        await handleMilestoneNotification({
          id: milestoneId,
          title,
          status,
          action: "status_change",
        });

        const completionResponse = await chatApis.getMilestoneCompletionForChat(
          chatRoomId,
          authToken,
        );
        if (completionResponse.data.status === "success") {
          setCompletionPercentage(completionResponse.data.data);
        }

        const overdueResponse = await chatApis.getOverdueMilestonesForChat(
          chatRoomId,
          authToken,
        );
        if (overdueResponse.data.status === "success") {
          setOverdueMilestones(overdueResponse.data.data.length);
        }
      } catch (err) {
        console.error("Failed to update milestone status:", err);
        setError("Failed to update milestone status");
      }
    },
    [chatRoomId, authToken, handleMilestoneNotification],
  );

  const getStatusColor = (status: string) => {
    switch (status) {
      case "COMPLETED":
        return "bg-emerald-50 text-emerald-700 border-emerald-200 dark:bg-emerald-950 dark:text-emerald-300 dark:border-emerald-800";
      case "IN_PROGRESS":
        return "bg-blue-50 text-blue-700 border-blue-200 dark:bg-blue-950 dark:text-blue-300 dark:border-blue-800";
      case "CANCELLED":
        return "bg-red-50 text-red-700 border-red-200 dark:bg-red-950 dark:text-red-300 dark:border-red-800";
      default:
        return "bg-amber-50 text-amber-700 border-amber-200 dark:bg-amber-950 dark:text-amber-300 dark:border-amber-800";
    }
  };

  if (isLoading && !contractDetails) {
    return (
      <div className="flex items-center justify-center h-full">
        <Loader2 className="h-6 w-6 animate-spin text-muted-foreground" />
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-full text-destructive text-sm">
        {error}
      </div>
    );
  }

  if (!contractDetails) {
    return (
      <div className="flex items-center justify-center h-full text-muted-foreground text-sm">
        Contract details not available.
      </div>
    );
  }

  return (
    <div className={cn("flex flex-col h-full max-h-full", className)}>
      {/* Compact contract header */}
      <div className="flex-shrink-0 border-b bg-background px-4 py-3 space-y-2">
        <div className="flex items-center justify-between">
          <h2 className="font-semibold text-sm truncate pr-4">
            {contractDetails.projectTitle}
          </h2>
          <Badge
            variant="outline"
            className={cn("text-xs font-medium shrink-0", getStatusColor(contractDetails.contractStatus))}
          >
            {contractDetails.contractStatus.replace("_", " ")}
          </Badge>
        </div>

        <div className="flex items-center gap-5 text-xs text-muted-foreground">
          <span className="flex items-center gap-1">
            <IndianRupee className="h-3 w-3" />
            {contractDetails.contractAmount.toFixed(0)}
          </span>
          <span className="flex items-center gap-1">
            <CalendarDays className="h-3 w-3" />
            {contractDetails.durationDays} days
          </span>
          <span className="flex items-center gap-2 flex-1">
            <CheckCircle2 className="h-3 w-3 shrink-0" />
            <Progress value={completionPercentage} className="h-1.5 flex-1 max-w-24" />
            <span className="shrink-0">{Math.round(completionPercentage)}%</span>
          </span>
        </div>

        {/* Milestone notification */}
        {milestoneNotification && (
          <Alert className="py-1.5 px-3 bg-blue-50 dark:bg-blue-950 border-blue-200 dark:border-blue-800">
            <AlertDescription className="text-xs text-blue-700 dark:text-blue-300">
              {milestoneNotification.action === "status_change" ? (
                <>
                  Milestone &quot;{milestoneNotification.title}&quot; →{" "}
                  <span className="font-medium">{milestoneNotification.status}</span>
                </>
              ) : (
                <>Milestone &quot;{milestoneNotification.title}&quot; {milestoneNotification.action}</>
              )}
            </AlertDescription>
          </Alert>
        )}

        {/* Tab switcher — pill style */}
        <div className="flex gap-1 bg-muted rounded-lg p-1 w-fit">
          <button
            onClick={() => setActiveTab("chat")}
            className={cn(
              "flex items-center gap-1.5 px-3 py-1 rounded-md text-xs font-medium transition-all",
              activeTab === "chat"
                ? "bg-background shadow-sm text-foreground"
                : "text-muted-foreground hover:text-foreground",
            )}
          >
            <MessageSquare className="h-3 w-3" />
            Chat
          </button>
          <button
            onClick={() => setActiveTab("milestones")}
            className={cn(
              "flex items-center gap-1.5 px-3 py-1 rounded-md text-xs font-medium transition-all relative",
              activeTab === "milestones"
                ? "bg-background shadow-sm text-foreground"
                : "text-muted-foreground hover:text-foreground",
            )}
          >
            <LayoutList className="h-3 w-3" />
            Milestones
            {overdueMilestones > 0 && (
              <span className="ml-1 flex h-4 w-4 items-center justify-center rounded-full bg-destructive text-[10px] text-white font-bold">
                {overdueMilestones}
              </span>
            )}
          </button>
        </div>
      </div>

      {/* Tab content */}
      <div className="flex-1 min-h-0 overflow-hidden">
        {activeTab === "chat" && (
          <ChatInterface
            chatRoomId={chatRoomId}
            chatType="CONTRACT"
            referenceId={contractDetails?.contractId || 0}
            className="h-full"
            onMilestoneAction={refreshMilestoneData}
          />
        )}
        {activeTab === "milestones" && (
          <div className="h-full overflow-auto">
            <MilestonePanel
              contractId={contractDetails.contractId}
              chatRoomId={chatRoomId}
              onMilestoneNotification={handleMilestoneNotification}
              onMilestoneStatusUpdate={handleMilestoneStatusUpdate}
            />
          </div>
        )}
      </div>
    </div>
  );
};
