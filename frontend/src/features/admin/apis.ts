import request from "@/apis/request";
import adminUrls from "./urls";

const adminApis = {
  getPendingReports: ({ authToken }: { authToken: string }) =>
    request({
      method: "GET",
      url: adminUrls.getPendingReports,
      authToken,
    }),

  deleteProject: ({
    authToken,
    projectId,
    reportId,
  }: {
    authToken: string;
    projectId: number;
    reportId: number;
  }) =>
    request({
      method: "DELETE",
      url: `${adminUrls.deleteProject}/${projectId}/reports/${reportId}`,
      authToken,
    }),

  dismissReport: ({
    authToken,
    reportId,
  }: {
    authToken: string;
    reportId: number;
  }) =>
    request({
      method: "PUT",
      url: `${adminUrls.dismissReport}/${reportId}/dismiss`,
      authToken,
    }),

  getProjectDetails: ({
    authToken,
    projectId,
  }: {
    authToken: string;
    projectId: number;
  }) =>
    request({
      method: "GET",
      url: `${adminUrls.getProjectDetails}/${projectId}`,
      authToken,
    }),
};

export default adminApis;
