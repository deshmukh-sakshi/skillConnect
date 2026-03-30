import useAuth from "@/hooks/use-auth";
import { useQuery } from "react-query";
import { toast } from "sonner";
import apis from "../apis";
import type { ApiError } from "@/types";

const useGetWalletDetails = () => {
  const { authToken, user } = useAuth();

  const {
    isLoading,
    refetch,
    data: response,
    error,
  } = useQuery({
    queryKey: ["GET_WALLET_DETAILS"],
    queryFn: () =>
      apis.getWalletDetails({
        id: user?.id as number,
        authToken: authToken as string,
        params: { role: "ROLE_CLIENT" },
      }),
    enabled: !!authToken,
    onError: (err) => {
      const apiError = err as ApiError;
      toast.error("Failed to fetch wallet details", {
        description:
          apiError?.response?.data?.message || "Something went wrong.",
      });
    },
    retry: false,
  });

  return {
    isLoading,
    refetch,
    walletDetails: response?.data?.data,
    error,
  };
};

export default useGetWalletDetails;
