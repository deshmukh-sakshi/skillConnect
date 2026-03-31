import { CATEGORIES } from "@/constants/categories";
import type { CategoryType } from "@/types";
import { useProjectCounts } from "@/hooks/useProjectCounts";
import CategoryCard from "./category-card";
import useScrollReveal from "@/hooks/use-scroll-reveal";

const Categories = () => {
  const { isLoading, error, getCountForCategory } = useProjectCounts();
  const { ref: titleRef, isVisible: titleVisible } = useScrollReveal();

  // Double the array for seamless infinite marquee
  const doubledCategories = [...CATEGORIES, ...CATEGORIES];

  return (
    <section className="py-20 md:py-32 relative overflow-hidden">
      <div className="max-w-7xl mx-auto px-4 sm:px-8 lg:px-12">
        <div
          ref={titleRef}
          className={`text-center mb-16 scroll-reveal ${titleVisible ? "visible" : ""}`}
        >
          <span
            className="inline-flex items-center gap-2 px-5 py-2 rounded-full mb-6
              bg-[#FF6B47]/10 text-[#FF6B47] text-sm font-semibold uppercase tracking-widest"
          >
            <div className="w-1.5 h-1.5 rounded-full bg-[#FF6B47]" />
            Categories
          </span>

          <h2
            className="text-4xl md:text-5xl lg:text-6xl font-bold text-[#1A1A2E] mb-4"
            style={{ fontFamily: "'Space Grotesk', sans-serif" }}
          >
            Browse by{" "}
            <span className="text-[#2EC4B6]">Categories</span>
          </h2>

          <p className="text-lg text-[#1A1A2E]/60 max-w-xl mx-auto">
            Browse categories to find your perfect project.
          </p>
        </div>
      </div>

      {/* Horizontal scrolling marquee */}
      <div className="relative">
        {/* Fade edges */}
        <div className="absolute left-0 top-0 bottom-0 w-24 z-10 bg-gradient-to-r from-[#FAF8F5] to-transparent pointer-events-none" />
        <div className="absolute right-0 top-0 bottom-0 w-24 z-10 bg-gradient-to-l from-[#FAF8F5] to-transparent pointer-events-none" />

        <div className="flex animate-marquee w-max gap-6 py-4">
          {doubledCategories.map((item: CategoryType, idx: number) => {
            const actualCount = getCountForCategory(item.id);
            return (
              <div key={`${item.id}-${idx}`} className="flex-shrink-0 w-72">
                <CategoryCard
                  item={item}
                  activeProjectCount={actualCount}
                  isCountLoading={isLoading}
                  hasCountError={!!error}
                />
              </div>
            );
          })}
        </div>
      </div>
    </section>
  );
};

export default Categories;
