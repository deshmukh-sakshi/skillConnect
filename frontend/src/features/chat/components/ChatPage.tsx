import React, { useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

import { ChatList } from "./ChatList";
import { ChatDetail } from "./ChatDetail";
import { fetchUserChatRooms } from "@/store/slices/chat-slice";
import type { AppDispatch, RootState } from "@/store";

const ChatPage: React.FC = () => {
  return (
    <div className="container py-6 max-w-7xl mx-auto h-[calc(100vh-6rem)]">
      <Routes>
        <Route path="/" element={<ChatListPage />} />
        <Route path="/:chatRoomId" element={<ChatDetailPage />} />
      </Routes>
    </div>
  );
};

const ChatListPage: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { authToken } = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    if (authToken) dispatch(fetchUserChatRooms({ authToken }));
  }, [authToken, dispatch]);

  return (
    <div className="h-full flex flex-col">
      <div className="mb-4 shrink-0">
        <h1 className="text-xl font-bold">Messages</h1>
        <p className="text-sm text-muted-foreground mt-0.5">
          Your conversations with clients and freelancers
        </p>
      </div>

      <div className="flex-1 min-h-0 rounded-xl border bg-background overflow-hidden">
        <ChatList />
      </div>
    </div>
  );
};

const ChatDetailPage: React.FC = () => {
  return (
    <div className="h-full rounded-xl border bg-background overflow-hidden">
      <ChatDetail />
    </div>
  );
};

export default ChatPage;
