// Project count API response interfaces
import type { LucideIcon } from "lucide-react";

type ApiErrorDetailValue =
  | string
  | number
  | boolean
  | null
  | ApiErrorDetailValue[]
  | { [key: string]: ApiErrorDetailValue };

export interface ProjectCountResponse {
  categoryId: number;
  activeProjectCount: number;
  lastUpdated: Date;
}

export interface ProjectCountsResponse {
  counts: ProjectCountResponse[];
  totalActiveProjects: number;
}

// Enhanced category type with project count
export interface CategoryWithCount {
  id: number;
  title: string;
  available: number;
  Icon: LucideIcon;
  color: string;
  activeProjectCount?: number;
  isLoading?: boolean;
}

// API error response interface
export interface ApiErrorResponse {
  error: {
    message: string;
    code?: string;
    details?: { [key: string]: ApiErrorDetailValue };
  };
  timestamp: string;
  status: number;
}

// Generic API response wrapper
export interface ApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
}

// Loading state interface for API calls
export interface LoadingState {
  isLoading: boolean;
  error: string | null;
  lastFetched?: Date;
}

// Project counts hook state
export interface ProjectCountsState extends LoadingState {
  counts: ProjectCountResponse[];
  totalActiveProjects: number;
}
