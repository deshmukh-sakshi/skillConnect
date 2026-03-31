import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "react-query";
import { useSelector } from "react-redux";
import type { RootState } from "@/store";
import { toast } from "sonner";
import { CheckCircle, ShieldAlert } from "lucide-react";
import adminApis from "./apis";
import ReportCard from "./components/ReportCard";
import ProjectDetailPanel from "./components/ProjectDetailPanel";
import DeleteConfirmModal from "./components/DeleteConfirmModal";
import type { Report } from "./components/types";

const AdminDashboard = () => {
  const { authToken } = useSelector((state: RootState) => state.auth);
  const queryClient = useQueryClient();
  const [viewReport, setViewReport] = useState<Report | null>(null);
  const [confirmDelete, setConfirmDelete] = useState<Report | null>(null);

  const { data, isLoading } = useQuery(
    ["admin-reports"],
    () => adminApis.getPendingReports({ authToken: authToken! }),
    {
      select: (res) => res.data?.data as Report[],
      enabled: !!authToken,
    }
  );

  const deleteMutation = useMutation({
    mutationFn: ({ projectId, reportId }: { projectId: number; reportId: number }) =>
      adminApis.deleteProject({ authToken: authToken!, projectId, reportId }),
    onSuccess: () => {
      toast.success("Project deleted successfully");
      queryClient.invalidateQueries(["admin-reports"]);
      setConfirmDelete(null);
      setViewReport(null);
    },
    onError: () => toast.error("Failed to delete project"),
  });

  const dismissMutation = useMutation({
    mutationFn: ({ reportId }: { reportId: number }) =>
      adminApis.dismissReport({ authToken: authToken!, reportId }),
    onSuccess: () => {
      toast.success("Report dismissed");
      queryClient.invalidateQueries(["admin-reports"]);
      setViewReport(null);
    },
    onError: () => toast.error("Failed to dismiss report"),
  });

  const reports: Report[] = data ?? [];

  return (
    <div className="min-h-screen bg-[#F0EDE8]">
      {/* Top bar */}
      <div className="bg-white border-b border-gray-200/80 px-8 py-5">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 bg-[#FF6B47]/10 rounded-xl flex items-center justify-center">
              <ShieldAlert className="w-5 h-5 text-[#FF6B47]" />
            </div>
            <div>
              <h1 className="text-lg font-bold text-gray-900">Reported Projects</h1>
              <p className="text-xs text-gray-400">Review and moderate flagged content</p>
            </div>
          </div>
          {!isLoading && (
            <div
              className={`flex items-center gap-2 px-4 py-2 rounded-full text-sm font-semibold border ${
                reports.length > 0
                  ? "bg-[#FF6B47]/8 text-[#FF6B47] border-[#FF6B47]/20"
                  : "bg-emerald-50 text-emerald-600 border-emerald-200"
              }`}
            >
              <div
                className={`w-2 h-2 rounded-full ${
                  reports.length > 0 ? "bg-[#FF6B47] animate-pulse" : "bg-emerald-500"
                }`}
              />
              {reports.length > 0 ? `${reports.length} pending` : "All clear"}
            </div>
          )}
        </div>
      </div>

      {/* Content */}
      <div className="px-8 py-6 max-w-5xl">
        {isLoading ? (
          <div className="flex flex-col items-center justify-center py-32">
            <div className="w-10 h-10 border-2 border-gray-200 border-t-[#FF6B47] rounded-full animate-spin mb-4" />
            <p className="text-gray-400 text-sm">Loading reports...</p>
          </div>
        ) : reports.length === 0 ? (
          <div className="bg-white rounded-2xl border border-gray-200/80 p-20 text-center shadow-sm">
            <div className="w-16 h-16 bg-emerald-50 rounded-full flex items-center justify-center mx-auto mb-4">
              <CheckCircle className="w-8 h-8 text-emerald-500" />
            </div>
            <h3 className="text-xl font-bold text-gray-900 mb-2">All clear!</h3>
            <p className="text-gray-400 text-sm">No pending reports to review.</p>
          </div>
        ) : (
          <div className="space-y-4">
            {reports.map((report) => (
              <ReportCard
                key={report.id}
                report={report}
                onViewDetails={setViewReport}
                onDelete={setConfirmDelete}
                onDismiss={(reportId) => dismissMutation.mutate({ reportId })}
                isDismissing={dismissMutation.isLoading}
              />
            ))}
          </div>
        )}
      </div>

      {viewReport && (
        <ProjectDetailPanel
          report={viewReport}
          authToken={authToken!}
          onClose={() => setViewReport(null)}
          onDelete={(r) => {
            setViewReport(null);
            setConfirmDelete(r);
          }}
          onDismiss={(reportId) => dismissMutation.mutate({ reportId })}
          isDeleting={deleteMutation.isLoading}
          isDismissing={dismissMutation.isLoading}
        />
      )}

      {confirmDelete && (
        <DeleteConfirmModal
          report={confirmDelete}
          isDeleting={deleteMutation.isLoading}
          onConfirm={(projectId, reportId) => deleteMutation.mutate({ projectId, reportId })}
          onCancel={() => setConfirmDelete(null)}
        />
      )}
    </div>
  );
};

export default AdminDashboard;
