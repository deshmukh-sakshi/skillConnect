import { useMutation } from "react-query";
import { useDispatch } from "react-redux";

import apis from "../../apis";
import { toast } from "sonner";
import { setAuth } from "@/store/slices/auth-slice";
import type { ApiError } from "@/types";

const useSignIn = () => {
  const dispatch = useDispatch();
  const { mutate, isLoading } = useMutation({
    mutationFn: ({ data }: { data: { email: string; password: string } }) =>
      apis.login({ data }),
    onSuccess: ({ data: response }) => {
      toast.success("Logged in successfully");
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
          apiError?.response?.data?.error?.message ||
          apiError?.response?.data?.message ||
          "Login failed",
      });
    },
    retry: false,
  });

  return { isLoading, mutate };
};

export default useSignIn;
