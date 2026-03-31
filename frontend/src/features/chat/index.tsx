import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { Loader2 } from "lucide-react";
import { chatApis } from "./apis";
import { ChatInterface, BidNegotiationChat, ContractChat } from "./components";
import { cn } from "@/lib/utils";
import type { RootState } from "@/store";
import type { ChatRoom } from "@/types";

export default function ChatPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const authToken = useSelector((state: RootState) => state.auth?.authToken);

  const pathParts = location.pathname.split("/");
  const chatRoomId =
    pathParts.length > 3 && pathParts[3] !== "" ? pathParts[3] : undefined;
  const [chatRooms, setChatRooms] = useState<ChatRoom[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!authToken) return;

    const loadChatRooms = async () => {
      try {
        setIsLoading(true);
        const response = await chatApis.getUserChatRooms(authToken);
        setChatRooms(response.data.data);

        if (!chatRoomId && response.data.data.length > 0) {
          navigate(`/dashboard/chats/${response.data.data[0].id}`, {
            replace: true,
          });
        }
      } catch (err) {
        console.error("Error loading chat rooms:", err);
        setError(
          err?.response?.data?.error?.message || "Failed to load chat rooms",
        );
      } finally {
        setIsLoading(false);
      }
    };

    loadChatRooms();
  }, [authToken, chatRoomId, navigate]);

  const currentChatRoom = chatRoomId
    ? chatRooms.find((room) => room.id === parseInt(chatRoomId, 10))
    : null;

  useEffect(() => {
    if (chatRoomId && authToken) {
      const chatRoomIdNum = parseInt(chatRoomId, 10);
      chatApis
        .markAsRead({ chatRoomId: chatRoomIdNum, authToken })
        .catch((err) => console.error("Failed to mark messages as read:", err));
    }
  }, [chatRoomId, authToken]);

  if (isLoading && chatRooms.length === 0) {
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

  if (chatRooms.length === 0) {
    return (
      <div className="flex items-center justify-center h-full text-muted-foreground text-sm">
        No conversations yet.
      </div>
    );
  }

  const renderChatComponent = () => {
    if (!chatRoomId) {
      return (
        <div className="flex items-center justify-center h-full text-muted-foreground text-sm">
          Select a conversation to start chatting.
        </div>
      );
    }

    const chatRoomIdNum = parseInt(chatRoomId, 10);

    if (!currentChatRoom) {
      return (
        <div className="flex items-center justify-center h-full text-muted-foreground text-sm">
          Chat room not found.
        </div>
      );
    }

    switch (currentChatRoom.chatType) {
      case "BID_NEGOTIATION":
        return <BidNegotiationChat chatRoomId={chatRoomIdNum} />;
      case "CONTRACT":
        return <ContractChat chatRoomId={chatRoomIdNum} />;
      default:
        return (
          <ChatInterface
            chatRoomId={chatRoomIdNum}
            chatType={currentChatRoom.chatType}
          />
        );
    }
  };

  const getChatTypeLabel = (chatType: string) => {
    if (chatType === "BID_NEGOTIATION") return "Bid";
    if (chatType === "CONTRACT") return "Contract";
    return "Chat";
  };

  return (
    <div className="flex h-[calc(100vh-7rem)] max-w-screen-2xl mx-auto rounded-xl border overflow-hidden bg-background">
      {/* Sidebar */}
      <div className="w-72 shrink-0 flex flex-col border-r">
        <div className="px-4 py-4 border-b">
          <h1 className="font-semibold text-base">Messages</h1>
          <p className="text-xs text-muted-foreground mt-0.5">
            {chatRooms.length} conversation{chatRooms.length !== 1 ? "s" : ""}
          </p>
        </div>

        <ul className="overflow-y-auto flex-1">
          {chatRooms.map((room) => {
            const isActive = chatRoomId && parseInt(chatRoomId, 10) === room.id;
            const initials = room.otherParty.name
              .split(" ")
              .map((n: string) => n[0])
              .join("")
              .toUpperCase()
              .slice(0, 2);

            return (
              <li key={room.id}>
                <button
                  onClick={(e) => {
                    e.preventDefault();
                    navigate(`/dashboard/chats/${room.id}`, { replace: true });
                  }}
                  className={cn(
                    "w-full px-4 py-3 text-left flex items-center gap-3 transition-colors hover:bg-muted/60",
                    isActive && "bg-muted",
                  )}
                >
                  {/* Avatar */}
                  <div className="h-10 w-10 rounded-full bg-primary/10 text-primary flex items-center justify-center text-sm font-semibold shrink-0">
                    {initials}
                  </div>

                  <div className="flex-1 min-w-0">
                    <div className="flex items-center justify-between gap-1">
                      <span className="font-medium text-sm truncate">
                        {room.otherParty.name}
                      </span>
                      {room.unreadCount > 0 && (
                        <span className="shrink-0 bg-primary text-primary-foreground rounded-full h-4 min-w-4 px-1 flex items-center justify-center text-[10px] font-bold">
                          {room.unreadCount}
                        </span>
                      )}
                    </div>
                    <p className="text-xs text-muted-foreground truncate mt-0.5">
                      {room.lastMessage?.content || "No messages yet"}
                    </p>
                    <span className="text-[10px] text-muted-foreground/70 mt-0.5 inline-block">
                      {getChatTypeLabel(room.chatType)}
                    </span>
                  </div>
                </button>
              </li>
            );
          })}
        </ul>
      </div>

      {/* Chat panel */}
      <div className="flex-1 min-w-0 h-full">
        {renderChatComponent()}
      </div>
    </div>
  );
}
