import { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { format } from "date-fns";
import { MessageSquare, Search, RefreshCw } from "lucide-react";

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Skeleton } from "@/components/ui/skeleton";
import { cn } from "@/lib/utils";
import { toast } from "sonner";

import {
  fetchUserChatRooms,
  fetchActiveChatRooms,
  selectChatRooms,
  selectChatLoading,
  selectChatError,
} from "@/store/slices/chat-slice";
import type { AppDispatch, RootState } from "@/store";
import type { ChatRoom } from "@/types";

interface BidDetails {
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

interface ExtendedChatRoom extends ChatRoom {
  bidDetails?: BidDetails;
}

export const ChatList = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const chatRooms = useSelector(selectChatRooms);
  const loading = useSelector(selectChatLoading);
  const error = useSelector(selectChatError);
  const { authToken } = useSelector((state: RootState) => state.auth);

  const [searchQuery, setSearchQuery] = useState("");
  const [activeTab, setActiveTab] = useState<"active" | "all">("active");
  const intervalIdRef = useRef<NodeJS.Timeout | null>(null);

  const fetchChatRooms = useCallback(() => {
    if (!authToken) return;
    if (activeTab === "all") {
      dispatch(fetchUserChatRooms({ authToken }));
    } else {
      dispatch(fetchActiveChatRooms({ authToken }));
    }
  }, [authToken, dispatch, activeTab]);

  useEffect(() => {
    if (!authToken) return;
    fetchChatRooms();
    const pollingInterval = document.hidden ? 30000 : 10000;
    intervalIdRef.current = setInterval(fetchChatRooms, pollingInterval);

    const handleVisibilityChange = () => {
      if (intervalIdRef.current) clearInterval(intervalIdRef.current);
      const interval = document.hidden ? 30000 : 10000;
      intervalIdRef.current = setInterval(fetchChatRooms, interval);
    };

    document.addEventListener("visibilitychange", handleVisibilityChange);
    return () => {
      if (intervalIdRef.current) clearInterval(intervalIdRef.current);
      document.removeEventListener("visibilitychange", handleVisibilityChange);
    };
  }, [authToken, fetchChatRooms]);

  const formatTime = (dateString: string) => {
    try {
      const date = new Date(dateString);
      const now = new Date();
      if (date.toDateString() === now.toDateString())
        return format(date, "h:mm a");
      if (now.getTime() - date.getTime() < 7 * 24 * 60 * 60 * 1000)
        return format(date, "EEE");
      return format(date, "MMM d");
    } catch {
      return "";
    }
  };

  const filteredRooms: ExtendedChatRoom[] = chatRooms
    .filter((room) => {
      if (activeTab === "active" && room.status !== "ACTIVE") return false;
      if (!searchQuery) return true;
      const q = searchQuery.toLowerCase();
      return (
        room.otherParty.name.toLowerCase().includes(q) ||
        (room.lastMessage?.content.toLowerCase().includes(q) ?? false)
      );
    })
    .map((room) => room as ExtendedChatRoom);

  return (
    <div className="flex flex-col h-full">
      {/* Search */}
      <div className="px-4 pt-4 pb-3 shrink-0">
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-3.5 w-3.5 text-muted-foreground" />
          <input
            type="text"
            placeholder="Search conversations…"
            className="w-full pl-9 pr-4 py-2 text-sm rounded-lg border bg-muted/40 focus:outline-none focus:ring-1 focus:ring-primary/50 focus:bg-background transition-colors"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
      </div>

      {/* Filter tabs — pill style */}
      <div className="px-4 pb-3 shrink-0">
        <div className="flex gap-1 bg-muted rounded-lg p-1 w-fit">
          <button
            onClick={() => setActiveTab("active")}
            className={cn(
              "px-4 py-1.5 rounded-md text-xs font-medium transition-all",
              activeTab === "active"
                ? "bg-background shadow-sm text-foreground"
                : "text-muted-foreground hover:text-foreground",
            )}
          >
            Active
          </button>
          <button
            onClick={() => setActiveTab("all")}
            className={cn(
              "px-4 py-1.5 rounded-md text-xs font-medium transition-all",
              activeTab === "all"
                ? "bg-background shadow-sm text-foreground"
                : "text-muted-foreground hover:text-foreground",
            )}
          >
            All Chats
          </button>
        </div>
      </div>

      {/* List */}
      <div className="flex-1 overflow-y-auto">
        <ChatRoomsList
          chatRooms={filteredRooms}
          isLoading={loading.chatRooms}
          error={error.chatRooms}
          onChatRoomClick={(id) => navigate(`/dashboard/chats/${id}`)}
          formatTime={formatTime}
          authToken={authToken}
          dispatch={dispatch}
        />
      </div>
    </div>
  );
};

interface ChatRoomsListProps {
  chatRooms: ExtendedChatRoom[];
  isLoading: boolean;
  error: string | null;
  onChatRoomClick: (id: number) => void;
  formatTime: (d: string) => string;
  authToken: string | null;
  dispatch: AppDispatch;
}

const ChatRoomsList = ({
  chatRooms,
  isLoading,
  error,
  onChatRoomClick,
  formatTime,
  authToken,
  dispatch,
}: ChatRoomsListProps) => {
  if (isLoading && chatRooms.length === 0) {
    return (
      <div className="px-4 space-y-0 divide-y">
        {Array.from({ length: 5 }).map((_, i) => (
          <div key={i} className="flex items-center gap-3 py-3">
            <Skeleton className="h-10 w-10 rounded-full shrink-0" />
            <div className="flex-1 space-y-1.5">
              <div className="flex justify-between">
                <Skeleton className="h-4 w-28" />
                <Skeleton className="h-3 w-10" />
              </div>
              <Skeleton className="h-3 w-full" />
            </div>
          </div>
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex flex-col items-center justify-center h-full gap-3 p-8 text-center">
        <p className="text-sm text-destructive font-medium">
          Failed to load chats
        </p>
        <p className="text-xs text-muted-foreground">{error}</p>
        <Button
          variant="outline"
          size="sm"
          onClick={() => {
            toast.info("Retrying…");
            if (authToken) dispatch(fetchUserChatRooms({ authToken }));
          }}
        >
          <RefreshCw className="mr-2 h-3.5 w-3.5" />
          Retry
        </Button>
      </div>
    );
  }

  if (chatRooms.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center h-full gap-3 p-8 text-center">
        <div className="h-12 w-12 rounded-full bg-muted flex items-center justify-center">
          <MessageSquare className="h-5 w-5 text-muted-foreground" />
        </div>
        <div>
          <h3 className="font-medium text-sm mb-1">No conversations</h3>
          <p className="text-xs text-muted-foreground max-w-xs">
            Your conversations with clients and freelancers will appear here.
          </p>
        </div>
      </div>
    );
  }

  return (
    <ul className="divide-y px-0">
      {chatRooms.map((room) => {
        const initials = room.otherParty?.name
          ? room.otherParty.name
              .split(" ")
              .map((n: string) => n[0])
              .join("")
              .toUpperCase()
              .slice(0, 2)
          : "?";

        const typeLabel =
          room.chatType === "BID_NEGOTIATION" ? "Bid" : "Contract";

        const lastMsg = room.lastMessage?.content || "No messages yet";
        const msgTime = room.lastMessage?.createdAt
          ? formatTime(room.lastMessage.createdAt)
          : null;

        return (
          <li key={room.id}>
            <button
              onClick={() => onChatRoomClick(room.id)}
              className={cn(
                "w-full flex items-center gap-3 px-4 py-3 text-left transition-colors hover:bg-muted/50 active:bg-muted",
                room.unreadCount > 0 && "bg-primary/5 hover:bg-primary/10",
              )}
            >
              {/* Avatar */}
              <div className="relative shrink-0">
                <div className="h-11 w-11 rounded-full bg-primary/10 text-primary flex items-center justify-center text-sm font-semibold">
                  {initials}
                </div>
                {room.unreadCount > 0 && (
                  <span className="absolute -top-0.5 -right-0.5 h-4 min-w-4 px-1 rounded-full bg-primary text-primary-foreground text-[10px] font-bold flex items-center justify-center">
                    {room.unreadCount}
                  </span>
                )}
              </div>

              {/* Content */}
              <div className="flex-1 min-w-0">
                <div className="flex items-center justify-between gap-2 mb-0.5">
                  <span
                    className={cn(
                      "text-sm truncate",
                      room.unreadCount > 0 ? "font-semibold" : "font-medium",
                    )}
                  >
                    {room.otherParty?.name || "Unknown"}
                  </span>
                  {msgTime && (
                    <span className="text-[11px] text-muted-foreground shrink-0">
                      {msgTime}
                    </span>
                  )}
                </div>

                <p
                  className={cn(
                    "text-xs truncate",
                    room.unreadCount > 0
                      ? "text-foreground font-medium"
                      : "text-muted-foreground",
                  )}
                >
                  {lastMsg}
                </p>

                <div className="mt-1">
                  <Badge
                    variant="secondary"
                    className="text-[10px] py-0 h-4 font-normal"
                  >
                    {typeLabel}
                  </Badge>
                </div>
              </div>
            </button>
          </li>
        );
      })}
    </ul>
  );
};

export default ChatList;
