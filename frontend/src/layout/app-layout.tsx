import { Outlet } from "react-router-dom";

import { SiteFooter, SiteHeader } from "@/components";

const AppLayout = () => {
  return (
    <div className="min-h-screen w-full bg-[#FAF8F5] overflow-x-hidden">
      <div className="flex flex-1 flex-col">
        <SiteHeader />
        <div className="flex flex-1 flex-col">
          <Outlet />
        </div>
        <SiteFooter />
      </div>
    </div>
  );
};

export default AppLayout;
