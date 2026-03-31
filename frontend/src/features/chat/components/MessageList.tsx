import { useRef, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { cn } from "@/lib/utils";
import {
  Clock,
  RefreshCcw,
  AlertCircle,
  Check,
  CheckCheck,
  ChevronDown,
} from "lucide-react";
import type { ChatMessage } from "@/types";
import type { RootState } from "@/store";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

interface EnhancedChatMessage extends ChatMessage {
  status: "pending" | "delivered" | "error";
  clientId?: string;
}

interface MessageListProps {
  messages: EnhancedChatMessage[];
  isLoading: boolean;
  hasMore: boolean;
  onLoadMore: () => void;
  onRetry?: (clientId: string) => void;
}

const groupMessagesByDate = (messages: EnhancedChatMessage[]) => {
  const groups: { [key: string]: EnhancedChatMessage[] } = {};

  messages.forEach((message) => {
    try {
      const date = new Date(message.createdAt);
      if (isNaN(date.getTime())) {
        const dateKey = new Date().toLocaleDateString();
        if (!groups[dateKey]) groups[dateKey] = [];
        groups[dateKey].push(message);
        return;
      }
      const dateKey = date.toLocaleDateString();
      if (!groups[dateKey]) groups[dateKey] = [];
      groups[dateKey].push(message);
    } catch {
      const dateKey = new Date().toLocaleDateString();
      if (!groups[dateKey]) groups[dateKey] = [];
      groups[dateKey].push(message);
    }
  });

  return Object.entries(groups).map(([date, messages]) => ({
    date,
    messages: messages.sort((a, b) => {
      try {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      } catch {
        return 0;
      }
    }),
  }));
};

export const MessageList = ({
  messages,
  isLoading,
  hasMore,
  onLoadMore,
  onRetry,
}: MessageListProps) => {
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const messagesContainerRef = useRef<HTMLDivElement>(null);
  const user = useSelector((state: RootState) => state.auth?.user);
  const [autoScroll, setAutoScroll] = useState(true);
  const [newMessagesCount, setNewMessagesCount] = useState(0);
  const [lastSeenMessageCount, setLastSeenMessageCount] = useState(0);

  const messageGroups = groupMessagesByDate(messages);

  useEffect(() => {
    if (messages.length > lastSeenMessageCount) {
      if (autoScroll) {
        messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
        setLastSeenMessageCount(messages.length);
        setNewMessagesCount(0);
      } else {
        setNewMessagesCount(messages.length - lastSeenMessageCount);
      }
    }
  }, [messages.length, autoScroll, lastSeenMessageCount]);

  useEffect(() => {
    const container = messagesContainerRef.current;
    if (!container) return;

    const handleScroll = () => {
      const distanceFromBottom =
        container.scrollHeight - container.scrollTop - container.clientHeight;

      if (distanceFromBottom > 100 && autoScroll) setAutoScroll(false);

      if (distanceFromBottom < 10 && !autoScroll) {
        setAutoScroll(true);
        setLastSeenMessageCount(messages.length);
        setNewMessagesCount(0);
      }
    };

    container.addEventListener("scroll", handleScroll);
    return () => container.removeEventListener("scroll", handleScroll);
  }, [autoScroll, messages.length]);

  useEffect(() => {
    const container = messagesContainerRef.current;
    if (!container) return;

    const handleScroll = () => {
      if (container.scrollTop < 100 && !isLoading && hasMore) {
        const scrollHeight = container.scrollHeight;
        onLoadMore();
        setTimeout(() => {
          if (container) {
            container.scrollTop = container.scrollHeight - scrollHeight;
          }
        }, 100);
      }
    };

    container.addEventListener("scroll", handleScroll);
    return () => container.removeEventListener("scroll", handleScroll);
  }, [isLoading, hasMore, onLoadMore]);

  const formatTime = (dateString: string) => {
    try {
      return new Date(dateString).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    } catch {
      return "";
    }
  };

  const formatDate = (dateString: string) => {
    const today = new Date().toLocaleDateString();
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);

    if (dateString === today) return "Today";
    if (dateString === yesterday.toLocaleDateString()) return "Yesterday";
    return new Date(dateString).toLocaleDateString(undefined, {
      weekday: "long",
      month: "long",
      day: "numeric",
    });
  };

  const renderStatusIcon = (message: EnhancedChatMessage) => {
    switch (message.status) {
      case "pending":
        return (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Clock className="h-3 w-3 text-primary-foreground/50" />
              </TooltipTrigger>
              <TooltipContent side="left" className="text-xs">Sending…</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        );
      case "delivered":
        return message.isRead ? (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <CheckCheck className="h-3 w-3 text-blue-300" />
              </TooltipTrigger>
              <TooltipContent side="left" className="text-xs">Read</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        ) : (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Check className="h-3 w-3 text-primary-foreground/60" />
              </TooltipTrigger>
              <TooltipContent side="left" className="text-xs">Delivered</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        );
      case "error":
        return (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <Button
                  variant="ghost"
                  size="sm"
                  className="h-4 w-4 p-0 text-destructive hover:text-destructive"
                  onClick={() => message.clientId && onRetry?.(message.clientId)}
                >
                  <RefreshCcw className="h-3 w-3" />
                </Button>
              </TooltipTrigger>
              <TooltipContent side="left" className="text-xs">Failed — click to retry</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        );
      default:
        return null;
    }
  };

  const renderSystemMessage = (message: EnhancedChatMessage) => {
    const icon =
      message.messageType === "BID_ACTION" ? (
        <AlertCircle className="h-3 w-3" />
      ) : message.messageType === "MILESTONE_UPDATE" ? (
        <CheckCheck className="h-3 w-3 text-emerald-600" />
      ) : null;

    return (
      <div className="flex items-center gap-2 text-xs text-muted-foreground">
        <div className="flex-1 h-px bg-border" />
        <span className="flex items-center gap-1 px-2 py-0.5 rounded-full bg-muted/60 font-medium shrink-0">
          {icon}
          {message.content}
        </span>
        <div className="flex-1 h-px bg-border" />
      </div>
    );
  };

  return (
    <div className="relative flex-1 min-h-0 overflow-hidden">
      {/* New messages button */}
      {newMessagesCount > 0 && (
        <div className="absolute bottom-4 left-1/2 -translate-x-1/2 z-10">
          <button
            onClick={() => {
              setAutoScroll(true);
              setLastSeenMessageCount(messages.length);
              setNewMessagesCount(0);
              messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
            }}
            className="flex items-center gap-1.5 bg-primary text-primary-foreground text-xs font-medium px-3 py-1.5 rounded-full shadow-lg hover:bg-primary/90 transition-colors"
          >
            <ChevronDown className="h-3 w-3" />
            {newMessagesCount} new
          </button>
        </div>
      )}

      <div
        ref={messagesContainerRef}
        className="flex flex-col gap-1 overflow-y-auto px-4 py-4 h-full"
      >
        {isLoading && messages.length === 0 && (
          <div className="flex justify-center py-4">
            <span className="text-xs text-muted-foreground">Loading messages…</span>
          </div>
        )}

        {messageGroups.map(({ date, messages: groupMessages }) => (
          <div key={date} className="flex flex-col gap-1">
            {/* Date separator */}
            <div className="flex items-center gap-2 my-3 text-xs text-muted-foreground">
              <div className="flex-1 h-px bg-border" />
              <span className="shrink-0">{formatDate(date)}</span>
              <div className="flex-1 h-px bg-border" />
            </div>

            {groupMessages.map((message) => {
              try {
                const isCurrentUser =
                  (user?.role === "ROLE_CLIENT" && message.senderType === "CLIENT") ||
                  (user?.role === "ROLE_FREELANCER" && message.senderType === "FREELANCER");
                const isSystemMessage = message.messageType !== "TEXT";

                if (isSystemMessage) {
                  return (
                    <div key={message.id} className="my-2">
                      {renderSystemMessage(message)}
                    </div>
                  );
                }

                return (
                  <div
                    key={message.id}
                    className={cn(
                      "flex gap-2 max-w-[75%] mb-0.5",
                      isCurrentUser ? "ml-auto flex-row-reverse" : "mr-auto",
                    )}
                  >
                    {!isCurrentUser && (
                      <Avatar className="h-7 w-7 shrink-0 mt-auto">
                        <AvatarFallback className="text-xs bg-muted text-muted-foreground">
                          {message.senderName.charAt(0).toUpperCase()}
                        </AvatarFallback>
                      </Avatar>
                    )}

                    <div className={cn("flex flex-col", isCurrentUser ? "items-end" : "items-start")}>
                      {!isCurrentUser && (
                        <span className="text-[11px] text-muted-foreground mb-1 ml-1">
                          {message.senderName}
                        </span>
                      )}

                      <div
                        className={cn(
                          "px-3 py-2 rounded-2xl text-sm leading-relaxed",
                          isCurrentUser
                            ? "bg-primary text-primary-foreground rounded-tr-sm"
                            : "bg-muted text-foreground rounded-tl-sm",
                          message.status === "error" && isCurrentUser && "bg-destructive/80",
                          message.status === "pending" && isCurrentUser && "opacity-70",
                        )}
                      >
                        {message.content}
                      </div>

                      <div
                        className={cn(
                          "flex items-center gap-1 mt-0.5 px-1",
                          isCurrentUser ? "flex-row-reverse" : "flex-row",
                        )}
                      >
                        <span className="text-[10px] text-muted-foreground">
                          {formatTime(message.createdAt)}
                        </span>
                        {isCurrentUser && renderStatusIcon(message)}
                      </div>
                    </div>
                  </div>
                );
              } catch {
                return (
                  <div key={message.id} className="flex justify-center my-1">
                    <span className="text-xs text-muted-foreground">Error displaying message</span>
                  </div>
                );
              }
            })}
          </div>
        ))}

        <div ref={messagesEndRef} />
      </div>
    </div>
  );
};
