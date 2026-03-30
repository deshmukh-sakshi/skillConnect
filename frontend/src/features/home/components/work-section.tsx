import useScrollReveal from "@/hooks/use-scroll-reveal";
import WorkflowDiagram from "./workflow-diagram";

const WorkSection = () => {
  const { ref: titleRef, isVisible: titleVisible } = useScrollReveal();

  return (
    <section className="py-20 md:py-32 relative">
      {/* Section header */}
      <div className="max-w-7xl mx-auto px-4 sm:px-8 lg:px-12">
        <div
          ref={titleRef}
          className={`text-center mb-20 scroll-reveal ${titleVisible ? "visible" : ""}`}
        >
          <span
            className="inline-flex items-center gap-2 px-5 py-2 rounded-full mb-6
              bg-[#2EC4B6]/10 text-[#2EC4B6] text-sm font-semibold uppercase tracking-widest"
          >
            <div className="w-1.5 h-1.5 rounded-full bg-[#2EC4B6]" />
            How It Works
          </span>

          <h2
            className="text-4xl md:text-5xl lg:text-6xl font-bold text-[#1A1A2E] mb-4"
            style={{ fontFamily: "'Space Grotesk', sans-serif" }}
          >
            It's Easy to Get{" "}
            <span className="text-[#FF6B47]">Work Done</span>
          </h2>

          <p className="text-lg text-[#1A1A2E]/60 max-w-xl mx-auto">
            Connect with clients, submit bids, and get paid — all in one place.
          </p>
        </div>

        {/* Workflow timeline */}
        <WorkflowDiagram className="mt-8" />
      </div>
    </section>
  );
};

export default WorkSection;
