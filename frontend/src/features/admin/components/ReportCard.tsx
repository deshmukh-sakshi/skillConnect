import { useState } from "react";
import {
  AlertTriangle,
  Calendar,
  ChevronRight,
  Clock,
  Eye,
  Flag,
  Mail,
  Tag,
  Trash2,
  User,
  X,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { type Report, formatDate, formatTime, getReasonStyle } from "./types";

interface ReportCardProps {
  report: Report;
  onViewDetails: (report: Report) => void;
  onDelete: (report: Report) => void;
  onDismiss: (reportId: number) => void;
  isDismissing: boolean;
}

const ReportCard = ({ report, onViewDetails, onDelete, onDismiss, isDismissing }: ReportCardProps) => {
  const [expanded, setExpanded] = useState(false);
  const longDesc = report.description && report.description.length > 160;

  return (
    <div className="bg-white rounded-2xl border border-gray-200/80 shadow-sm overflow-hidden hover:shadow-md transition-shadow">
      <div className="flex">
        <div className="w-1 bg-[#FF6B47] flex-shrink-0" />
        <div className="flex-1 p-6">
          {/* Header */}
          <div className="flex items-start justify-between gap-4 mb-4">
            <div className="flex-1">
              <div className="flex flex-wrap items-center gap-2 mb-2.5">
                <span
                  className={`inline-flex items-center gap-1.5 text-xs font-semibold px-2.5 py-1 rounded-full border ${getReasonStyle(
                    report.reason
                  )}`}
                >
                  <AlertTriangle className="w-3 h-3" />
                  {report.reason}
                </span>
                <span className="inline-flex items-center gap-1.5 text-xs text-gray-500 bg-gray-100 px-2.5 py-1 rounded-full">
                  <Tag className="w-3 h-3" />
                  {report.projectCategory}
                </span>
              </div>
              <h2 className="text-base font-bold text-gray-900 leading-snug">{report.projectTitle}</h2>
            </div>
            <div className="text-right flex-shrink-0">
              <div className="flex items-center gap-1 text-xs text-gray-400 justify-end">
                <Calendar className="w-3 h-3" />
                {formatDate(report.createdAt)}
              </div>
              <div className="flex items-center gap-1 text-xs text-gray-300 justify-end mt-0.5">
                <Clock className="w-3 h-3" />
                {formatTime(report.createdAt)}
              </div>
            </div>
          </div>

          {/* Reporter */}
          <div className="flex items-center gap-4 text-xs text-gray-500 mb-4">
            <span className="flex items-center gap-1.5">
              <div className="w-5 h-5 bg-gray-100 rounded-full flex items-center justify-center">
                <User className="w-3 h-3 text-gray-400" />
              </div>
              <span className="font-medium text-gray-700">{report.reportedByName ?? "Anonymous"}</span>
            </span>
            <span className="flex items-center gap-1.5">
              <Mail className="w-3 h-3" />
              {report.reportedByEmail}
            </span>
          </div>

          {/* Report description */}
          {report.description && (
            <div className="mb-4">
              <div
                className={`relative bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 ${
                  !expanded && longDesc ? "max-h-16 overflow-hidden" : ""
                }`}
              >
                <Flag className="w-3 h-3 text-gray-300 absolute top-3 right-3" />
                <p className="text-sm text-gray-600 leading-relaxed pr-5">{report.description}</p>
              </div>
              {longDesc && (
                <button
                  className="text-xs text-[#FF6B47] mt-1.5 hover:underline"
                  onClick={() => setExpanded(!expanded)}
                >
                  {expanded ? "Show less" : "Read more"}
                </button>
              )}
            </div>
          )}

          {/* Actions */}
          <div className="flex items-center justify-between pt-2 border-t border-gray-100">
            <button
              className="flex items-center gap-1.5 text-xs text-gray-400 hover:text-[#FF6B47] transition-colors"
              onClick={() => onViewDetails(report)}
            >
              <Eye className="w-3.5 h-3.5" />
              View project content
              <ChevronRight className="w-3 h-3" />
            </button>
            <div className="flex gap-3">
              <Button
                size="sm"
                variant="outline"
                className="text-gray-500 border-gray-200 hover:bg-gray-50 text-xs h-8"
                onClick={() => onDismiss(report.id)}
                disabled={isDismissing}
              >
                <X className="w-3.5 h-3.5 mr-1.5" />
                Dismiss
              </Button>
              <Button
                size="sm"
                className="bg-[#FF6B47] hover:bg-[#e55a38] text-white text-xs h-8 shadow-sm shadow-[#FF6B47]/30"
                onClick={() => onDelete(report)}
              >
                <Trash2 className="w-3.5 h-3.5 mr-1.5" />
                Delete Project
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportCard;
