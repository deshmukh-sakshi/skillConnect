import ProjectTable from "./components/project-table";
import useGetClientProjects from "./hooks/use-get-client-projects";

const MyProjects = () => {
  const { clientProjects, isLoading, refetch } = useGetClientProjects();
  const allClientProjects = clientProjects?.projects || [];

  return (
    <div className="px-6 py-8 space-y-6 min-h-screen bg-background">
      <h1 className="text-3xl font-bold tracking-tight text-[#1A1A2E]">
        My Projects
      </h1>
      <ProjectTable
        projects={allClientProjects}
        isLoading={isLoading}
        refetchProjects={refetch}
      />
    </div>
  );
};

export default MyProjects;
