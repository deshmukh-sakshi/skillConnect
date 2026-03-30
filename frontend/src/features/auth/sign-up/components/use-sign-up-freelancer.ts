import { toast } from "sonner";
import { useMutation } from "react-query";
import { useDispatch } from "react-redux";

import { setAuth } from "@/store/slices/auth-slice";
import type { ApiError } from "@/types";

import apis from "../../apis";

const useSignUpFreelancer = () => {
  const dispatch = useDispatch();
  const { isLoading, mutate } = useMutation({
    mutationFn: ({
      data,
    }: {
      data: { email: string; name: string; password: string };
    }) => apis.registerFreelancer({ data }),
    onSuccess: ({ data: response }) => {
      toast.success("Freelancer registered successfully");
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

export default useSignUpFreelancer;
