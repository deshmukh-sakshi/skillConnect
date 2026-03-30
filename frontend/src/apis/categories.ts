import { request } from "@/apis";
import type { ApiError, ProjectCountsResponse } from "@/types";

/**
 * Fetches project counts by category from the backend API
 * @returns Promise<ProjectCountsResponse> - Project counts grouped by category
 */
export const getProjectCounts = async (): Promise<ProjectCountsResponse> => {
  try {
    const response = await request({
      method: "GET",
      url: "/projects/counts-by-category",
    });

    return response.data.data || response.data;
  } catch (error) {
    const apiError = error as ApiError;

    // Enhanced error handling with specific error types
    if (apiError.response?.status === 404) {
      throw new Error("Project counts endpoint not found");
    } else if (apiError.response?.status === 500) {
      throw new Error("Server error while fetching project counts");
    } else if (apiError.code === "NETWORK_ERROR" || !apiError.response) {
      throw new Error("Network error: Unable to connect to server");
    } else if (apiError.response?.data?.error?.message) {
      throw new Error(apiError.response.data.error.message);
    } else {
      throw new Error(apiError.message || "Failed to fetch project counts");
    }
  }
};
