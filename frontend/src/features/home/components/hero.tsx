import { Link } from "react-router-dom";
import { motion } from "framer-motion";

import { siteConfigs } from "@/apis";

const Hero = () => {
  const words = ["Built", "for", "Freelancers,", "by", "Freelancers."];
  const badges = [
    { text: "Clients Post Projects", color: "#FF6B47" },
    { text: "Freelancers Place Bids", color: "#2EC4B6" },
    { text: "Contracts & Payments Secured", color: "#1A1A2E" },
  ];

  return (
    <section className="relative min-h-[calc(100vh-4rem)] flex items-center justify-center overflow-hidden">
      {/* Animated SVG lines background */}
      <svg
        className="absolute inset-0 w-full h-full pointer-events-none"
        viewBox="0 0 1440 900"
        preserveAspectRatio="xMidYMid slice"
      >
        <motion.path
          d="M-100,400 C200,100 600,700 900,300 S1200,500 1600,200"
          fill="none"
          stroke="#FF6B47"
          strokeWidth="1.5"
          strokeOpacity="0.08"
          initial={{ pathLength: 0 }}
          animate={{ pathLength: 1 }}
          transition={{ duration: 3, ease: "easeInOut" }}
        />
        <motion.path
          d="M-50,600 C300,300 500,800 800,500 S1100,200 1500,400"
          fill="none"
          stroke="#2EC4B6"
          strokeWidth="1.5"
          strokeOpacity="0.08"
          initial={{ pathLength: 0 }}
          animate={{ pathLength: 1 }}
          transition={{ duration: 3, ease: "easeInOut", delay: 0.5 }}
        />
        <motion.path
          d="M0,200 C400,500 700,100 1000,400 S1300,600 1500,300"
          fill="none"
          stroke="#FF6B47"
          strokeWidth="1"
          strokeOpacity="0.05"
          initial={{ pathLength: 0 }}
          animate={{ pathLength: 1 }}
          transition={{ duration: 3.5, ease: "easeInOut", delay: 1 }}
        />
      </svg>

      {/* Floating decorative elements */}
      <div className="absolute inset-0 pointer-events-none">
        <div
          className="absolute top-20 right-[15%] w-72 h-72 rounded-full opacity-[0.04] animate-float"
          style={{ background: 'var(--color-coral)' }}
        />
        <div
          className="absolute bottom-20 left-[10%] w-56 h-56 rounded-full opacity-[0.04] animate-float-slow"
          style={{ background: 'var(--color-teal)' }}
        />
        <div
          className="absolute top-1/3 left-[5%] w-3 h-3 rounded-full opacity-30 animate-float"
          style={{ background: 'var(--color-coral)', animationDelay: '1s' }}
        />
        <div
          className="absolute top-1/4 right-[8%] w-2 h-2 rounded-full opacity-30 animate-float-slow"
          style={{ background: 'var(--color-teal)', animationDelay: '2s' }}
        />
        <div
          className="absolute bottom-1/3 right-[20%] w-4 h-4 rounded-full opacity-20 animate-float"
          style={{ background: 'var(--color-coral)', animationDelay: '0.5s' }}
        />
      </div>

      {/* Main Content */}
      <div className="relative z-10 max-w-5xl mx-auto px-4 sm:px-8 text-center">
        {/* Welcome badge */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="inline-flex items-center gap-2 px-5 py-2 rounded-full mb-8
            bg-white/60 backdrop-blur-sm border border-[#E5E0D8] shadow-sm"
        >
          <div className="w-2 h-2 rounded-full bg-[#2EC4B6] animate-pulse" />
          <span className="text-sm font-medium text-[#1A1A2E]/70">
            Welcome to {siteConfigs.name}
          </span>
        </motion.div>

        {/* Staggered headline */}
        <h1
          className="text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-bold leading-tight mb-6"
          style={{ fontFamily: "'Space Grotesk', sans-serif" }}
        >
          {words.map((word, i) => (
            <motion.span
              key={i}
              initial={{ opacity: 0, y: 40 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{
                duration: 0.5,
                delay: 0.3 + i * 0.12,
                ease: [0.16, 1, 0.3, 1],
              }}
              className={`inline-block mr-3 ${
                word === "Freelancers," || word === "Freelancers."
                  ? "text-[#FF6B47]"
                  : "text-[#1A1A2E]"
              }`}
            >
              {word}
            </motion.span>
          ))}
        </h1>

        {/* Subtitle */}
        <motion.p
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 1.0 }}
          className="text-lg sm:text-xl text-[#1A1A2E]/60 max-w-2xl mx-auto mb-8 leading-relaxed"
        >
          {siteConfigs.name} helps freelancers stay organized, deliver on time,
          and get paid—without the chaos.
        </motion.p>

        {/* Badge pills */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 1.3 }}
          className="flex flex-wrap items-center justify-center gap-3 mb-10"
        >
          {badges.map((badge, i) => (
            <motion.span
              key={i}
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: 1.4 + i * 0.1, duration: 0.4 }}
              className="inline-flex items-center gap-2 px-4 py-2 rounded-full text-sm font-medium
                bg-white/70 backdrop-blur-sm border border-[#E5E0D8] shadow-sm"
            >
              <div
                className="w-2 h-2 rounded-full"
                style={{ background: badge.color }}
              />
              {badge.text}
            </motion.span>
          ))}
        </motion.div>

        {/* CTA Button */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6, delay: 1.6 }}
          className="flex items-center justify-center gap-4"
        >
          <Link
            to="/dashboard/browse-projects"
            className="shimmer-btn inline-flex items-center text-white rounded-full px-8 py-3.5 text-base font-semibold
              shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105"
          >
            Start Freelancing
          </Link>
          <Link
            to="/auth/sign-up"
            className="inline-flex items-center rounded-full px-8 py-3.5 text-base font-semibold
              bg-white border-2 border-[#E5E0D8] text-[#1A1A2E] hover:border-[#FF6B47] hover:text-[#FF6B47]
              transition-all duration-300 hover:scale-105 shadow-sm"
          >
            Join Free
          </Link>
        </motion.div>
      </div>

    </section>
  );
};

export default Hero;
