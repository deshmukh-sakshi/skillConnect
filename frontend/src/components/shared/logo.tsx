import { Zap } from "lucide-react";

const Logo = () => {
  return (
    <div className="flex aspect-square size-9 items-center justify-center rounded-xl bg-gradient-to-br from-[#FF6B47] to-[#FF8A6F] text-white shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105">
      <Zap className="size-5" fill="currentColor" />
    </div>
  );
};

export default Logo;
