import { AlertTriangle, Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { type Report, getReasonStyle } from "./types";

interface DeleteConfirmModalProps {
  report: Report;
  isDeleting: boolean;
  onConfirm: (projectId: number, reportId: number) => void;
  onCancel: () => void;
}

const DeleteConfirmModal = ({ report, isDeleting, onConfirm, onCancel }: DeleteConfirmModalProps) => (
  <div className="fixed inset-0 bg-black/40 backdrop-blur-sm flex items-center justify-center z-50 p-4">
    <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full p-7">
      <div className="flex items-center justify-center w-14 h-14 bg-red-50 rounded-2xl mx-auto mb-5">
        <Trash2 className="w-7 h-7 text-red-500" />
      </div>
      <h2 className="text-xl font-bold text-gray-900 text-center mb-1">Delete this project?</h2>
      <p className="text-sm text-gray-400 text-center mb-5">This action cannot be undone.</p>

      <div className="bg-gray-50 rounded-xl p-4 mb-5 border border-gray-100">
        <p className="text-sm font-semibold text-gray-800 mb-1">{report.projectTitle}</p>
        <div className="flex items-center gap-2 text-xs text-gray-400">
          <span
            className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-full border text-xs font-medium ${getReasonStyle(
              report.reason
            )}`}
          >
            <AlertTriangle className="w-3 h-3" />
            {report.reason}
          </span>
          <span>·</span>
          <span>by {report.reportedByName ?? report.reportedByEmail}</span>
        </div>
      </div>

      <div className="flex gap-3">
        <Button
          variant="outline"
          className="flex-1 text-sm"
          onClick={onCancel}
          disabled={isDeleting}
        >
          Cancel
        </Button>
        <Button
          className="flex-1 bg-red-500 hover:bg-red-600 text-white text-sm shadow-lg shadow-red-200"
          onClick={() => onConfirm(report.projectId, report.id)}
          disabled={isDeleting}
        >
          {isDeleting ? (
            <span className="flex items-center gap-2">
              <span className="w-3.5 h-3.5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              Deleting...
            </span>
          ) : (
            <>
              <Trash2 className="w-3.5 h-3.5 mr-1.5" />
              Yes, Delete
            </>
          )}
        </Button>
      </div>
    </div>
  </div>
);

export default DeleteConfirmModal;
