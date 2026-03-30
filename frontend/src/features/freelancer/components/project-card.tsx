import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Calendar, ArrowRight, IndianRupee, Tag, Timer } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { cn } from "@/lib/utils";

type Project = {
  id: number;
  title: string;
  category: string;
  description: string;
  budget: number;
  status: string;
  deadline: string;
};

const statusConfig = {
  OPEN: {
    className: "bg-[#2EC4B6]/15 text-[#1A1A2E] border-[#2EC4B6]/35",
  },
  CLOSED: {
    className: "bg-[#FF6B47]/15 text-[#1A1A2E] border-[#FF6B47]/35",
  },
};

const ProjectCard = ({ project }: { project: Project }) => {
  const navigate = useNavigate();

  const handleViewAndBid = () => {
    navigate(`/dashboard/browse-projects/${project.id}`);
  };

  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("en-IN", {
      day: "2-digit",
      month: "short",
      year: "numeric",
    });
  };

  const getDaysUntilDeadline = (deadline: string) => {
    const today = new Date();
    const deadlineDate = new Date(deadline);
    const diffTime = deadlineDate.getTime() - today.getTime();
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    return diffDays;
  };

  const daysLeft = getDaysUntilDeadline(project.deadline);
  const isUrgent = daysLeft <= 3 && daysLeft > 0;
  const statusInfo = statusConfig[project.status as keyof typeof statusConfig];

  return (
    <Card className="w-full max-w-sm flex flex-col border border-[#E5E0D8] rounded-xl shadow-md hover:shadow-lg transition-all duration-300 bg-gradient-to-br from-[#FAF8F5] via-white to-[#FAF8F5]">
      <CardHeader className="p-5 pb-3 space-y-3">
        <div className="flex items-start justify-between gap-3">
          <CardTitle className="text-lg font-semibold text-gray-900 leading-snug line-clamp-2 flex-1">
            {project.title}
          </CardTitle>
          <Badge
            className={cn(
              "text-xs font-semibold px-3 py-0.5 rounded-full border shadow-sm",
              statusInfo?.className,
            )}
          >
            {project.status}
          </Badge>
        </div>
        <div className="flex items-center gap-2 text-sm text-gray-600">
          <Tag className="h-4 w-4 text-gray-400" />
          {project.category}
        </div>
      </CardHeader>

      <CardContent className="px-5 flex-1 space-y-4">
        {/* Budget Section */}
        <div className="flex items-center justify-between p-3 bg-[#2EC4B6]/10 rounded-md border border-[#2EC4B6]/25">
          <div className="flex items-center gap-2.5">
            <div className="p-1.5 bg-white rounded-md shadow">
              <IndianRupee className="h-4 w-4 text-[#2EC4B6]" />
            </div>
            <span className="text-sm font-medium text-[#1A1A2E]">Budget</span>
          </div>
          <span className="text-base font-bold text-gray-900">
            ₹{project.budget.toLocaleString()}
          </span>
        </div>

        {/* Deadline Section */}
        <div className="flex items-center justify-between p-3 bg-[#FF6B47]/10 rounded-md border border-[#FF6B47]/25">
          <div className="flex items-center gap-2.5">
            <div className="p-1.5 bg-white rounded-md shadow">
              <Calendar className="h-4 w-4 text-[#FF6B47]" />
            </div>
            <span className="text-sm font-medium text-[#1A1A2E]">Deadline</span>
          </div>
          <div className="flex items-center gap-2">
            <span className="text-sm font-semibold text-gray-800">
              {formatDate(project.deadline)}
            </span>
            {daysLeft > 0 && (
              <Badge
                variant="outline"
                className={cn(
                  "text-xs px-2 py-0.5 rounded-full font-medium border",
                  isUrgent
                    ? "bg-[#FF6B47]/20 text-[#1A1A2E] border-[#FF6B47]/40 animate-pulse"
                    : "bg-[#2EC4B6]/20 text-[#1A1A2E] border-[#2EC4B6]/40",
                )}
              >
                <Timer className="h-3 w-3 mr-1" />
                {daysLeft}d
              </Badge>
            )}
          </div>
        </div>
      </CardContent>

      <CardFooter className="p-5 pt-3 mt-auto">
        <Button
          className="w-full h-10 font-medium text-sm bg-gradient-to-r from-[#1A1A2E] to-[#2D2D44] hover:from-[#2D2D44] hover:to-[#1A1A2E] text-white group rounded-lg shadow-sm"
          onClick={handleViewAndBid}
          aria-label="View and bid on project"
        >
          <span>View & Bid</span>
          <ArrowRight className="h-4 w-4 ml-2 group-hover:translate-x-1 transition-transform duration-200" />
        </Button>
      </CardFooter>
    </Card>
  );
};

export default ProjectCard;
