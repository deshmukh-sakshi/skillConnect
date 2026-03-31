import { useState, useRef, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { SendHorizonal, Loader2, AlertCircle, RefreshCcw } from "lucide-react";
import { cn } from "@/lib/utils";

interface MessageInputProps {
  onSendMessage: (content: string) => Promise<void>;
  onRetry?: () => Promise<void>;
  disabled?: boolean;
  placeholder?: string;
  sendingState: "idle" | "sending" | "error";
  errorMessage?: string;
}

export const MessageInput = ({
  onSendMessage,
  onRetry,
  disabled = false,
  placeholder = "Type a message…",
  sendingState = "idle",
  errorMessage,
}: MessageInputProps) => {
  const [message, setMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    const textarea = textareaRef.current;
    if (!textarea) return;
    textarea.style.height = "auto";
    textarea.style.height = `${Math.min(textarea.scrollHeight, 120)}px`;
  }, [message]);

  useEffect(() => {
    if (sendingState !== "sending") setIsSubmitting(false);
  }, [sendingState]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!message.trim() || disabled || isSubmitting || sendingState === "sending") return;

    setIsSubmitting(true);
    try {
      await onSendMessage(message);
      setMessage("");
      if (textareaRef.current) textareaRef.current.style.height = "auto";
    } catch (error) {
      console.error("Failed to send message:", error);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e);
    }
  };

  const isSending = isSubmitting || sendingState === "sending";
  const isError = sendingState === "error";

  return (
    <div className="flex-shrink-0 px-4 py-3 border-t bg-background">
      {isError && errorMessage && (
        <div className="flex items-center gap-2 text-destructive text-xs mb-2">
          <AlertCircle className="h-3.5 w-3.5 shrink-0" />
          <span className="flex-1">{errorMessage}</span>
          {onRetry && (
            <button
              type="button"
              onClick={onRetry}
              className="text-xs underline underline-offset-2 hover:no-underline flex items-center gap-1"
            >
              <RefreshCcw className="h-3 w-3" />
              Retry
            </button>
          )}
        </div>
      )}

      <form onSubmit={handleSubmit} className="flex items-end gap-2">
        <div
          className={cn(
            "flex-1 flex items-end rounded-2xl border bg-muted/40 px-3 py-2 transition-colors",
            "focus-within:border-primary/50 focus-within:bg-background",
            disabled && "opacity-50",
          )}
        >
          <textarea
            ref={textareaRef}
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onKeyDown={handleKeyDown}
            placeholder={placeholder}
            disabled={disabled || isSending}
            className="flex-1 resize-none bg-transparent text-sm leading-relaxed outline-none placeholder:text-muted-foreground min-h-[24px] max-h-[120px]"
            rows={1}
            aria-label="Message input"
          />
        </div>

        <Button
          type="submit"
          size="icon"
          disabled={!message.trim() || disabled || isSending}
          className={cn(
            "h-9 w-9 rounded-xl shrink-0 transition-all",
            isError && "bg-destructive hover:bg-destructive/90",
          )}
          aria-label="Send message"
        >
          {isSending ? (
            <Loader2 className="h-4 w-4 animate-spin" />
          ) : (
            <SendHorizonal className="h-4 w-4" />
          )}
        </Button>
      </form>
    </div>
  );
};
