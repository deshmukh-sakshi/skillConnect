export interface Report {
  id: number;
  projectId: number;
  projectTitle: string;
  projectCategory: string;
  reportedByUserId: number;
  reportedByName: string | null;
  reportedByEmail: string;
  reason: string;
  description: string | null;
  status: string;
  createdAt: string;
}

export interface ProjectDetails {
  id: number;
  title: string;
  description: string;
  category: string;
  deadline: string;
  budget: number;
  status: string;
  client?: { name?: string; email?: string };
  bidCount?: number;
  createdAt?: string;
}

export const reasonColor: Record<string, string> = {
  SPAM: "bg-orange-100 text-orange-700 border-orange-200",
  FRAUD: "bg-red-100 text-red-700 border-red-200",
  INAPPROPRIATE: "bg-red-100 text-red-700 border-red-200",
  MISLEADING: "bg-yellow-100 text-yellow-700 border-yellow-200",
  SCAM: "bg-red-100 text-red-700 border-red-200",
};

export const getReasonStyle = (reason: string) =>
  reasonColor[reason.toUpperCase()] ?? "bg-[#FF6B47]/10 text-[#FF6B47] border-[#FF6B47]/20";

export const formatDate = (iso: string) =>
  new Date(iso).toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });

export const formatTime = (iso: string) =>
  new Date(iso).toLocaleTimeString("en-US", {
    hour: "2-digit",
    minute: "2-digit",
  });

export const formatBudget = (cents: number) =>
  new Intl.NumberFormat("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  }).format(cents / 100);
