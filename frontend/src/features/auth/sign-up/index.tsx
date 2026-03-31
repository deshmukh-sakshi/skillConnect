import { Link } from "react-router-dom";
import { motion } from "framer-motion";
import { Zap, ArrowRight } from "lucide-react";

import SignUpForm from "./components/sign-up-form";

const SignUpPage = () => {
  return (
    <div className="flex min-h-screen">
      {/* Left panel — animated brand showcase */}
      <div className="relative hidden lg:flex lg:w-1/2 items-center justify-center overflow-hidden bg-gradient-to-br from-[#2EC4B6] to-[#1A9E92]">
        {/* Floating shapes */}
        <div className="absolute inset-0 pointer-events-none">
          <motion.div
            className="absolute top-16 right-20 w-40 h-40 rounded-full border-2 border-white/10"
            animate={{ rotate: -360 }}
            transition={{ duration: 25, repeat: Infinity, ease: "linear" }}
          />
          <motion.div
            className="absolute bottom-24 left-16 w-28 h-28 rounded-3xl bg-white/10"
            animate={{ y: [-12, 12, -12], rotate: [0, -5, 0] }}
            transition={{ duration: 7, repeat: Infinity, ease: "easeInOut" }}
          />
          <motion.div
            className="absolute top-1/4 left-1/4 w-20 h-20 rounded-full bg-[#FF6B47]/20"
            animate={{ y: [8, -12, 8] }}
            transition={{ duration: 6, repeat: Infinity, ease: "easeInOut", delay: 0.5 }}
          />
          <motion.div
            className="absolute bottom-1/3 right-1/3 w-4 h-4 rounded-full bg-white/40"
            animate={{ scale: [1, 1.8, 1], opacity: [0.4, 0.8, 0.4] }}
            transition={{ duration: 4, repeat: Infinity, ease: "easeInOut" }}
          />
          <motion.div
            className="absolute top-2/3 right-1/4 w-8 h-8 rounded-lg bg-[#FF6B47]/30"
            animate={{ rotate: [0, 90, 0] }}
            transition={{ duration: 8, repeat: Infinity, ease: "easeInOut" }}
          />
        </div>

        <div className="relative z-10 text-center px-12 max-w-lg">
          {/* Logo */}
          <motion.div
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
            className="w-16 h-16 rounded-2xl bg-white/20 backdrop-blur-sm flex items-center justify-center mx-auto mb-8 shadow-2xl"
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
            Start Your Journey
          </motion.h2>

          <motion.p
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
            className="text-white/80 text-lg leading-relaxed"
          >
            Join the community of freelancers and clients making work happen seamlessly.
          </motion.p>

          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.5 }}
            className="mt-12 grid grid-cols-3 gap-6 text-center"
          >
            {[
              { number: "10K+", label: "Freelancers" },
              { number: "5K+", label: "Projects" },
              { number: "99%", label: "Satisfaction" },
            ].map((stat, i) => (
              <motion.div
                key={i}
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.6 + i * 0.1 }}
              >
                <div
                  className="text-2xl font-bold text-white"
                  style={{ fontFamily: "'Space Grotesk', sans-serif" }}
                >
                  {stat.number}
                </div>
                <div className="text-white/60 text-xs mt-1">{stat.label}</div>
              </motion.div>
            ))}
          </motion.div>
        </div>
      </div>

      {/* Right panel — sign up form */}
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
              Create Account
            </h1>
            <p className="text-[#1A1A2E]/50">
              Enter your information to get started
            </p>
          </div>

          <SignUpForm />

          <div className="mt-8 flex items-center justify-center">
            <Link
              to="/auth/sign-in"
              className="text-sm text-[#1A1A2E]/50 hover:text-[#FF6B47] transition-colors inline-flex items-center gap-1 group"
            >
              Already have an account?
              <ArrowRight className="size-3 group-hover:translate-x-0.5 transition-transform" />
            </Link>
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default SignUpPage;
