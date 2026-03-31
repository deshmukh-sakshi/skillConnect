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

import { signInFormSchema, type SignInFormValues } from "./sign-in-schema";
import useSignIn from "./use-sign-in";

const SignInForm = () => {
  const { isLoading, mutate } = useSignIn();
  const onSubmit = (data: SignInFormValues) => {
    mutate({ data });
  };

  const form = useForm<SignInFormValues>({
    resolver: zodResolver(signInFormSchema),
    mode: "onChange",
  });

  return (
    <Form {...form}>
      <form
        className="flex flex-col space-y-6 md:px-0"
        onSubmit={form.handleSubmit(onSubmit)}
      >
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
          disabled={isLoading}
        >
          Sign In
          {isLoading ? (
            <LoaderCircle className="ml-2 size-4 animate-spin" />
          ) : (
            <ArrowRight className="ml-2 size-4" />
          )}
        </Button>
      </form>
    </Form>
  );
};

export default SignInForm;
