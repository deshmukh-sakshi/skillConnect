import { useState } from "react";
import { useForm } from "react-hook-form";
import { ArrowRight, LoaderCircle } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { zodResolver } from "@hookform/resolvers/zod";

import useSignUpClient from "./use-sign-up-client";
import useSignUpFreelancer from "./use-sign-up-freelancer";
import { signUpFormSchema, type SignUpFormValues } from "./sign-up-schema";

const SignUpForm = () => {
  const { isLoading, mutate: mutateAsClient } = useSignUpClient();
  const { isLoading: loading, mutate: mutateAsFreelancer } =
    useSignUpFreelancer();

  const [userType, setUserType] = useState<"freelancer" | "client">(
    "freelancer",
  );

  const form = useForm<SignUpFormValues>({
    resolver: zodResolver(signUpFormSchema),
    mode: "onChange",
  });

  const onSubmit = (data: SignUpFormValues) => {
    if (userType === "client") {
      mutateAsClient({ data });
    } else {
      mutateAsFreelancer({ data });
    }
  };

  return (
    <div className="space-y-6">
      {/* User type toggle */}
      <div className="w-full">
        <div className="p-1 rounded-xl flex w-full bg-white border border-[#E5E0D8] shadow-sm">
          <button
            type="button"
            onClick={() => setUserType("freelancer")}
            className={`flex-1 px-4 py-2.5 cursor-pointer rounded-lg text-sm font-semibold transition-all duration-300 ${
              userType === "freelancer"
                ? "bg-[#FF6B47] text-white shadow-md"
                : "text-[#1A1A2E]/50 hover:text-[#1A1A2E]/70 hover:bg-[#FAF8F5]"
            }`}
          >
            💼 &nbsp; Freelancer
          </button>
          <button
            type="button"
            onClick={() => setUserType("client")}
            className={`flex-1 px-4 cursor-pointer py-2.5 rounded-lg text-sm font-semibold transition-all duration-300 ${
              userType === "client"
                ? "bg-[#2EC4B6] text-white shadow-md"
                : "text-[#1A1A2E]/50 hover:text-[#1A1A2E]/70 hover:bg-[#FAF8F5]"
            }`}
          >
            👤 &nbsp; Client
          </button>
        </div>
      </div>

      <Form {...form}>
        <form
          className="flex flex-col space-y-5 md:px-0"
          onSubmit={form.handleSubmit(onSubmit)}
        >
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-[#1A1A2E]/70 text-sm font-medium">Full Name</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Jon Doe"
                    className="h-12 bg-white border-[#E5E0D8] rounded-xl focus:border-[#FF6B47] focus:ring-[#FF6B47]/20 focus-visible:ring-[#FF6B47]/20 transition-all placeholder:text-[#1A1A2E]/30"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-[#1A1A2E]/70 text-sm font-medium">Email</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="you@example.com"
                    className="h-12 bg-white border-[#E5E0D8] rounded-xl focus:border-[#FF6B47] focus:ring-[#FF6B47]/20 focus-visible:ring-[#FF6B47]/20 transition-all placeholder:text-[#1A1A2E]/30"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-[#1A1A2E]/70 text-sm font-medium">Password</FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    placeholder="••••••••"
                    className="h-12 bg-white border-[#E5E0D8] rounded-xl focus:border-[#FF6B47] focus:ring-[#FF6B47]/20 focus-visible:ring-[#FF6B47]/20 transition-all placeholder:text-[#1A1A2E]/30"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Button
            className="cursor-pointer h-12 rounded-xl shimmer-btn text-white font-semibold text-base shadow-lg hover:shadow-xl transition-all duration-300"
            type="submit"
            disabled={loading || isLoading}
          >
            Create Account
            {isLoading || loading ? (
              <LoaderCircle className="ml-2 size-4 animate-spin" />
            ) : (
              <ArrowRight className="ml-2 size-4" />
            )}
          </Button>
        </form>
      </Form>
    </div>
  );
};

export default SignUpForm;
