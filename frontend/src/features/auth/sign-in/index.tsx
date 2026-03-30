import { Link } from "react-router-dom";
import { motion } from "framer-motion";
import { Zap, ArrowRight } from "lucide-react";

import SignInForm from "./components/sign-in-form";

const SignInPage = () => {
  return (
    <div className="flex min-h-screen">
      {/* Left panel — animated brand showcase */}
      <div className="relative hidden lg:flex lg:w-1/2 items-center justify-center overflow-hidden bg-gradient-to-br from-[#1A1A2E] to-[#2D2D44]">
        {/* Floating shapes */}
        <div className="absolute inset-0 pointer-events-none">
          <motion.div
            className="absolute top-20 left-20 w-32 h-32 rounded-full border-2 border-white/10"
            animate={{ rotate: 360 }}
            transition={{ duration: 20, repeat: Infinity, ease: "linear" }}
          />
          <motion.div
            className="absolute bottom-32 right-16 w-24 h-24 rounded-2xl bg-[#FF6B47]/20"
            animate={{ y: [-10, 10, -10], rotate: [0, 5, 0] }}
            transition={{ duration: 6, repeat: Infinity, ease: "easeInOut" }}
          />
          <motion.div
            className="absolute top-1/3 right-1/4 w-16 h-16 rounded-full bg-[#2EC4B6]/20"
            animate={{ y: [10, -15, 10] }}
            transition={{ duration: 5, repeat: Infinity, ease: "easeInOut", delay: 1 }}
          />
          <motion.div
            className="absolute bottom-1/4 left-1/3 w-6 h-6 rounded-full bg-[#FF6B47]/40"
            animate={{ scale: [1, 1.5, 1], opacity: [0.4, 0.8, 0.4] }}
            transition={{ duration: 3, repeat: Infinity, ease: "easeInOut" }}
          />
        </div>

        <div className="relative z-10 text-center px-12 max-w-lg">
          {/* Logo */}
          <motion.div
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
            className="w-16 h-16 rounded-2xl bg-gradient-to-br from-[#FF6B47] to-[#FF8A6F] flex items-center justify-center mx-auto mb-8 shadow-2xl"
          >
            <Zap className="size-8 text-white" fill="currentColor" />
          </motion.div>

          <motion.h2
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.2 }}
            className="text-3xl font-bold text-white mb-4"
            style={{ fontFamily: "'Space Grotesk', sans-serif" }}
          >
            Welcome Back!
          </motion.h2>

          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
            className="text-white/60 text-lg leading-relaxed"
          >
            Continue your journey. Your projects and connections are waiting for you.
          </motion.p>

          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.5 }}
            className="mt-12 flex items-center justify-center gap-3 text-white/40 text-sm"
          >
            <div className="w-8 h-[1px] bg-white/20" />
            <span>Trusted by thousands</span>
            <div className="w-8 h-[1px] bg-white/20" />
          </motion.div>
        </div>
      </div>

      {/* Right panel — sign in form */}
      <div className="flex-1 flex items-center justify-center px-4 sm:px-8 bg-[#FAF8F5]">
        <motion.div
          initial={{ opacity: 0, x: 30 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.6, ease: [0.16, 1, 0.3, 1] }}
          className="w-full max-w-md"
        >
          <div className="mb-8">
            <h1
              className="text-3xl font-bold text-[#1A1A2E] mb-2"
              style={{ fontFamily: "'Space Grotesk', sans-serif" }}
            >
              Sign In
            </h1>
            <p className="text-[#1A1A2E]/50">
              Enter your credentials to access your account
            </p>
          </div>

          <SignInForm />

          <div className="mt-8 flex items-center justify-between text-sm">
            <Link
              to="/auth/sign-up"
              className="text-[#1A1A2E]/50 hover:text-[#FF6B47] transition-colors inline-flex items-center gap-1 group"
            >
              Don&apos;t have an account?
              <ArrowRight className="size-3 group-hover:translate-x-0.5 transition-transform" />
            </Link>
            <Link
              to="/auth/forgot-password"
              className="text-[#1A1A2E]/50 hover:text-[#FF6B47] transition-colors"
            >
              Forgot Password?
            </Link>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default SignInPage;
