import { cn } from "@/lib/utils";
import type { CategoryType } from "@/types";
import { Skeleton } from "@/components/ui/skeleton";

interface CategoryCardProps {
  item: CategoryType;
  activeProjectCount?: number;
  isCountLoading?: boolean;
  hasCountError?: boolean;
}

const CategoryCard = ({
  item,
  activeProjectCount,
  isCountLoading = false,
  hasCountError = false,
}: CategoryCardProps) => {
  const getDisplayCount = () => {
    if (isCountLoading) {
      return <Skeleton className="h-4 w-8 inline-block" />;
    }

    if (hasCountError) {
      return item.available;
    }

    return activeProjectCount || 0;
  };

  return (
    <div
      className={cn(
        "glass-card rounded-2xl p-6 transition-all duration-500 cursor-pointer",
        "flex items-center gap-5",
        "hover:shadow-xl hover:-translate-y-2",
        "group",
      )}
    >
      <div
        className="w-14 h-14 rounded-xl flex items-center justify-center transition-all duration-300 group-hover:scale-110"
        style={{
          background: `${item.color.includes('blue') ? '#3B82F6' :
            item.color.includes('pink') ? '#EC4899' :
            item.color.includes('yellow') ? '#EAB308' :
            item.color.includes('green') ? '#22C55E' :
            item.color.includes('purple') ? '#A855F7' :
            item.color.includes('red') ? '#EF4444' :
            item.color.includes('gray') ? '#6B7280' :
            item.color.includes('indigo') ? '#6366F1' :
            item.color.includes('teal') ? '#14B8A6' :
            item.color.includes('orange') ? '#F97316' :
            item.color.includes('cyan') ? '#06B6D4' :
            '#FF6B47'}15`,
        }}
      >
        <item.Icon className={cn(item.color)} size={28} />
      </div>

      <div>
        <h2
          className="text-base font-bold text-[#1A1A2E] group-hover:text-[#FF6B47] transition-colors duration-300"
          style={{ fontFamily: "'Space Grotesk', sans-serif" }}
        >
          {item.title}
        </h2>
        <p className="text-sm text-[#1A1A2E]/50 mt-1">
          {getDisplayCount()} projects available
        </p>
      </div>
    </div>
  );
};

export default CategoryCard;
