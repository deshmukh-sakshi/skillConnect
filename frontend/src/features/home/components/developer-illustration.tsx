import { useNavigate } from "react-router-dom";
import illustration from "/working-man.png";
import useScrollReveal from "@/hooks/use-scroll-reveal";

import { Button } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";

const DeveloperIllustration = () => {
  const navigate = useNavigate();
  const redirect = (url: string) => navigate(url);
  const { ref: sectionRef, isVisible } = useScrollReveal({ threshold: 0.2 });

  return (
    <div
      ref={sectionRef}
      className="relative py-20 md:py-32 overflow-hidden"
    >
      {/* Subtle background pattern */}
      <div className="absolute inset-0 opacity-[0.02]"
        style={{
          backgroundImage: `radial-gradient(circle at 1px 1px, #1A1A2E 1px, transparent 0)`,
          backgroundSize: '40px 40px',
        }}
      />

      <div className="max-w-7xl mx-auto px-4 sm:px-8 lg:px-12">
        <div className="flex flex-col md:flex-row items-center gap-12 md:gap-16">
          {/* Image — slides from left */}
          <div
            className={`flex-1 scroll-reveal-left ${isVisible ? "visible" : ""}`}
          >
            <div className="relative">
              {/* Decorative background shape */}
              <div
                className="absolute -top-8 -left-8 w-full h-full rounded-3xl opacity-10"
                style={{ background: 'var(--color-teal)' }}
              />
              <img
                src={illustration}
                alt="Freelancer working remotely"
                className="relative w-full max-w-md mx-auto h-auto object-contain drop-shadow-2xl"
              />
            </div>
          </div>

          {/* Text — slides from right */}
          <div
            className={`flex-1 scroll-reveal-right ${isVisible ? "visible" : ""}`}
            style={{ transitionDelay: "200ms" }}
          >
            <span
              className="inline-flex items-center gap-2 px-5 py-2 rounded-full mb-6
                bg-[#FF6B47]/10 text-[#FF6B47] text-sm font-semibold uppercase tracking-widest"
            >
              <div className="w-1.5 h-1.5 rounded-full bg-[#FF6B47]" />
              Join Us
            </span>

            <h2
              className="text-3xl md:text-5xl font-bold leading-tight mb-6 text-[#1A1A2E]"
              style={{ fontFamily: "'Space Grotesk', sans-serif" }}
            >
              Work from{" "}
              <span className="text-[#2EC4B6]">Anywhere.</span>
              <br />
              Earn{" "}
              <span className="text-[#FF6B47]">Everywhere.</span>
            </h2>

            <p className="text-lg mb-8 text-[#1A1A2E]/60 leading-relaxed max-w-lg">
              Join thousands of freelancers building their future — one project at
              a time. Whether you're a developer, designer, or writer, get paid
              for what you do best.
            </p>

            <Button
              onClick={() => redirect("/auth/sign-up")}
              className="shimmer-btn text-white py-3 px-8 rounded-full text-base font-semibold
                hover:shadow-xl transition-all duration-300 hover:scale-105 cursor-pointer
                inline-flex items-center gap-2"
            >
              Become a Freelancer
              <ArrowRight className="size-4" />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeveloperIllustration;
