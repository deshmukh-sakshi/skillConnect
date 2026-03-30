import {
  FileText,
  Users,
  FileCheck,
  CheckCircle,
  CreditCard,
} from "lucide-react";
import { motion, useScroll, useTransform } from "framer-motion";
import { useRef } from "react";
import useScrollReveal from "@/hooks/use-scroll-reveal";

import type { WorkflowStep, WorkflowDiagramProps } from "@/types/workflow";

const workflowSteps: WorkflowStep[] = [
  {
    id: 1,
    title: "Project Posting",
    description:
      "Clients post detailed project requirements with budget and timeline",
    icon: FileText,
    color: "#FF6B47",
  },
  {
    id: 2,
    title: "Bidding",
    description: "Freelancers review projects and submit competitive proposals",
    icon: Users,
    color: "#2EC4B6",
  },
  {
    id: 3,
    title: "Contract Creation",
    description:
      "Accepted bids create secure contracts and lock project status",
    icon: FileCheck,
    color: "#6C63FF",
  },
  {
    id: 4,
    title: "Project Completion",
    description:
      "Freelancers deliver work and clients review the final results",
    icon: CheckCircle,
    color: "#FFB900",
  },
  {
    id: 5,
    title: "Payment",
    description: "Secure payment processing through the platform wallet system",
    icon: CreditCard,
    color: "#FF6B47",
  },
];

const TimelineStep = ({
  step,
  index,
  isLast,
}: {
  step: WorkflowStep;
  index: number;
  isLast: boolean;
}) => {
  const { ref, isVisible } = useScrollReveal({
    threshold: 0.3,
    rootMargin: "0px 0px -80px 0px",
  });
  const isLeft = index % 2 === 0;

  return (
    <div
      ref={ref}
      className={`relative flex items-center ${
        isLeft ? "md:flex-row" : "md:flex-row-reverse"
      } flex-row gap-6 md:gap-12`}
    >
      {/* Content card */}
      <div
        className={`flex-1 ${isLeft ? "md:text-right" : "md:text-left"} ${
          isLeft ? "scroll-reveal-right" : "scroll-reveal-left"
        } ${isVisible ? "visible" : ""}`}
        style={{ transitionDelay: `${index * 100}ms` }}
      >
        <div
          className="glass-card rounded-2xl p-6 md:p-8 inline-block hover:shadow-xl transition-all duration-500 hover:-translate-y-1"
        >
          <div
            className={`flex items-center gap-3 mb-3 ${
              isLeft ? "md:justify-end" : "md:justify-start"
            }`}
          >
            <div
              className="w-10 h-10 rounded-xl flex items-center justify-center"
              style={{ background: `${step.color}15` }}
            >
              <step.icon className="w-5 h-5" style={{ color: step.color }} />
            </div>
            <h3
              className="text-xl font-bold text-[#1A1A2E]"
              style={{ fontFamily: "'Space Grotesk', sans-serif" }}
            >
              {step.title}
            </h3>
          </div>
          <p className="text-[#1A1A2E]/60 text-sm leading-relaxed max-w-sm">
            {step.description}
          </p>
        </div>
      </div>

      {/* Timeline center line + circle */}
      <div className="relative flex flex-col items-center z-10">
        <div
          className={`w-14 h-14 rounded-full flex items-center justify-center text-white text-lg font-bold shadow-lg
            transition-all duration-700 ${
              isVisible ? "scale-100 opacity-100" : "scale-50 opacity-0"
            }`}
          style={{
            background: step.color,
            fontFamily: "'Space Grotesk', sans-serif",
            transitionDelay: `${index * 100 + 200}ms`,
          }}
        >
          {step.id}
        </div>
        {/* Connecting line */}
        {!isLast && (
          <div
            className={`w-[2px] h-20 md:hidden transition-all duration-1000 ${
              isVisible ? "opacity-100" : "opacity-0"
            }`}
            style={{
              background: `linear-gradient(to bottom, ${step.color}, ${workflowSteps[index + 1]?.color || step.color})`,
              transitionDelay: `${index * 100 + 400}ms`,
            }}
          />
        )}
      </div>

      {/* Empty space for alternating layout - only on md+ */}
      <div className="flex-1 hidden md:block" />
    </div>
  );
};

const WorkflowDiagram = ({
  steps = workflowSteps,
  className = "",
}: WorkflowDiagramProps) => {
  const timelineRef = useRef<HTMLDivElement>(null);
  const { scrollYProgress } = useScroll({
    target: timelineRef,
    offset: ["start 80%", "end 30%"],
  });
  const lineScaleY = useTransform(scrollYProgress, [0, 1], [0, 1]);
  const lineOpacity = useTransform(scrollYProgress, [0, 0.05], [0.25, 1]);

  return (
    <div className={`w-full max-w-4xl mx-auto ${className}`}>
      <div ref={timelineRef} className="relative">
        <motion.div
          className="hidden md:block absolute left-1/2 top-7 bottom-7 w-[2px] -translate-x-1/2 origin-top bg-gradient-to-b from-[#FF6B47]/80 via-[#2EC4B6]/70 to-[#FF6B47]/80"
          style={{ scaleY: lineScaleY, opacity: lineOpacity }}
        />
        {steps.map((step, index) => (
          <TimelineStep
            key={step.id}
            step={step}
            index={index}
            isLast={index === steps.length - 1}
          />
        ))}
      </div>
    </div>
  );
};

export default WorkflowDiagram;
