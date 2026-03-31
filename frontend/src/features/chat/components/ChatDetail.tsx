import React, { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { ArrowLeft, Loader2, AlertTriangle } from "lucide-react";

import { Button } from "@/components/ui/button";
import { BidNegotiationChat } from "./BidNegotiationChat";
import { ContractChat } from "./ContractChat";
import { fetchUserChatRooms } from "@/store/slices/chat-slice";
import type { AppDispatch, RootState } from "@/store";

export const ChatDetail: React.FC = () => {
  const { chatRoomId } = useParams<{ chatRoomId: string }>();
  const navigate = useNavigate();
  const dispatch = useDispatch<AppDispatch>();
  const { authToken } = useSelector((state: RootState) => state.auth);
  const chatRooms = useSelector((state: RootState) => state.chat.chatRooms);
  const loading = useSelector(
    (state: RootState) => state.chat.loading.chatRooms,
  );

  const chatRoom = chatRooms.find((room) => room.id === Number(chatRoomId));

  useEffect(() => {
    if (authToken && (!chatRooms.length || !chatRoom)) {
      dispatch(fetchUserChatRooms({ authToken }));
    }
  }, [authToken, dispatch, chatRooms.length, chatRoom]);

  const handleBack = () => navigate("/dashboard/chats");

  if (loading && chatRooms.length === 0 && !chatRoom) {
    return (
      <div className="h-full flex flex-col">
        <div className="flex items-center gap-2 px-2 py-3 border-b">
          <Button variant="ghost" size="sm" onClick={handleBack} className="-ml-1">
            <ArrowLeft className="h-4 w-4 mr-1" />
            Back
          </Button>
        </div>
        <div className="flex-1 flex items-center justify-center">
          <Loader2 className="h-6 w-6 animate-spin text-muted-foreground" />
        </div>
      </div>
    );
  }

  if (!chatRoom) {
    return (
      <div className="h-full flex flex-col">
        <div className="flex items-center gap-2 px-2 py-3 border-b">
          <Button variant="ghost" size="sm" onClick={handleBack} className="-ml-1">
            <ArrowLeft className="h-4 w-4 mr-1" />
            Back
          </Button>
        </div>
        <div className="flex-1 flex flex-col items-center justify-center gap-3 text-center px-6">
          <AlertTriangle className="h-8 w-8 text-destructive" />
          <div>
            <h3 className="font-semibold mb-1">Chat not found</h3>
            <p className="text-sm text-muted-foreground">
              This conversation doesn't exist or you don't have access to it.
            </p>
          </div>
          <Button size="sm" onClick={handleBack}>Return to Chats</Button>
        </div>
      </div>
    );
  }

  const chatTypeLabel =
    chatRoom.chatType === "BID_NEGOTIATION" ? "Bid Discussion" : "Contract Chat";

  return (
    <div className="h-full flex flex-col overflow-hidden">
      {/* Header */}
      <div className="flex items-center gap-3 px-3 py-2.5 border-b bg-background shrink-0">
        <Button
          variant="ghost"
          size="sm"
          onClick={handleBack}
          className="-ml-1 h-8 px-2 text-muted-foreground hover:text-foreground"
        >
          <ArrowLeft className="h-4 w-4 mr-1" />
          Back
        </Button>

        <div className="h-4 w-px bg-border" />

        {/* Avatar */}
        <div className="h-8 w-8 rounded-full bg-primary/10 text-primary flex items-center justify-center text-sm font-semibold shrink-0">
          {chatRoom.otherParty.name.charAt(0).toUpperCase()}
        </div>

        <div className="min-w-0">
          <p className="font-semibold text-sm leading-tight truncate">
            {chatRoom.otherParty.name}
          </p>
          <p className="text-xs text-muted-foreground leading-tight">{chatTypeLabel}</p>
        </div>
      </div>

      {/* Chat content — no extra Card wrapper */}
      <div className="flex-1 min-h-0 overflow-hidden">
        {chatRoom.chatType === "BID_NEGOTIATION" ? (
          <BidNegotiationChat
            chatRoomId={Number(chatRoomId)}
            className="h-full"
          />
        ) : (
          <ContractChat chatRoomId={Number(chatRoomId)} className="h-full" />
        )}
      </div>
    </div>
  );
};

export default ChatDetail;
