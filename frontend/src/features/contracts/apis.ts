import { request } from "@/apis";
import type { RequestType } from "@/types";
import urls from "./urls";
import type { ApiResponse, Contract } from "./types";
import type { AxiosResponse } from "axios";

/**
 * Contract API functions for interacting with the backend
 */
const apis = {
  /**
   * Get all contracts for the current user
   */
  getContracts: ({
    authToken,
  }: RequestType): Promise<AxiosResponse<ApiResponse<Contract[]>>> =>
    request({
      method: "GET",
      url: urls.getContracts,
      authToken,
    }),

  /**
   * Get a specific contract by ID
   */
  getContractById: ({
    params,
    authToken,
  }: {
    params: { id: number };
    authToken: string;
  }): Promise<AxiosResponse<ApiResponse<Contract>>> =>
    request({
      method: "GET",
      url: `${urls.getContract}/${params.id}`,
      authToken,
    }),

  /**
   * Update a contract's status
   */
  updateContractStatus: ({
    params,
    data,
    authToken,
    isPlainText = false,
  }: {
    params: { id: number };
    data: { contractStatus: string; freelancerRating: number | null };
    authToken: string;
    isPlainText?: boolean;
  }): Promise<
    AxiosResponse<ApiResponse<Contract>>
  > =>
    request({
      method: "PUT",
      url: `${urls.updateContract}/${params.id}`,
      data,
      authToken,
      isPlainText,
    }),

  /**
   * Delete a contract
   */
  deleteContract: ({
    params,
    authToken,
  }: {
    params: { id: number };
    authToken: string;
  }): Promise<AxiosResponse<ApiResponse<void>>> =>
    request({
      method: "DELETE",
      url: `${urls.deleteContract}/${params.id}`,
      authToken,
    }),
};

export default apis;
