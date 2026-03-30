import { toast } from "sonner";
import { useMutation } from "react-query";
import { useDispatch } from "react-redux";

import { setAuth } from "@/store/slices/auth-slice";
import type { ApiError } from "@/types";

import apis from "../../apis";

const useSignUpClient = () => {
  const dispatch = useDispatch();
  const { isLoading, mutate } = useMutation({
    mutationFn: ({
      data,
    }: {
      data: { email: string; name: string; password: string };
    }) => apis.registerClient({ data }),
    onSuccess: ({ data: response }) => {
      toast.success("Client registered successfully");
      dispatch(
        setAuth({
          user: response?.data,
          authToken: response?.data?.token,
        }),
      );
    },
    onError: (err) => {
      const apiError = err as ApiError;
      toast.error("Something went wrong", {
        description:
          apiError?.response?.data?.error?.password ||
          apiError?.response?.data?.error?.message ||
          apiError?.response?.data?.message ||
          "Signup failed",
      });
    },
    retry: false,
  });

  return { isLoading, mutate };
};

export default useSignUpClient;
