import { Navigate, Outlet, NavLink } from "react-router-dom";
import useAuth from "@/hooks/use-auth";
import { Shield, Flag, LogOut } from "lucide-react";
import { useDispatch } from "react-redux";
import { logout } from "@/store/slices/auth-slice";

const AdminLayout = () => {
  const { isAuth, user } = useAuth();
  const dispatch = useDispatch();

  if (!isAuth || user?.role !== "ROLE_ADMIN") {
    return <Navigate to="/auth/sign-in" />;
  }

  const initials = user?.name
    ? user.name
        .split(" ")
        .map((n: string) => n[0])
        .join("")
        .toUpperCase()
        .slice(0, 2)
    : "A";

  return (
    <div className="min-h-screen bg-[#F0EDE8] flex">
      {/* Sidebar */}
      <aside className="w-56 bg-[#1A1A2E] text-white flex flex-col fixed inset-y-0 left-0 z-10">
        {/* Brand */}
        <div className="px-5 py-5 border-b border-white/10">
          <div className="flex items-center gap-3">
            <div className="w-9 h-9 bg-[#FF6B47] rounded-xl flex items-center justify-center shadow-lg shadow-[#FF6B47]/30">
              <Shield className="w-4 h-4 text-white" />
            </div>
            <div>
              <p className="text-sm font-bold text-white leading-tight">SkillConnect</p>
              <p className="text-[10px] text-white/40 uppercase tracking-widest">Admin Portal</p>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav className="flex-1 px-3 py-5 space-y-1">
          <p className="text-[9px] text-white/30 uppercase tracking-widest px-3 mb-3 font-semibold">
            Moderation
          </p>
          <NavLink
            to="/admin"
            end
            className={({ isActive }) =>
              `flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm transition-all ${
                isActive
                  ? "bg-[#FF6B47] text-white font-semibold shadow-lg shadow-[#FF6B47]/25"
                  : "text-white/50 hover:bg-white/10 hover:text-white/90"
              }`
            }
          >
            <Flag className="w-4 h-4 flex-shrink-0" />
            Reports
          </NavLink>
        </nav>

        {/* User section */}
        <div className="px-3 py-4 border-t border-white/10">
          <div className="flex items-center gap-2.5 px-2 py-2 rounded-xl bg-white/5 mb-2">
            <div className="w-7 h-7 bg-gradient-to-br from-[#FF6B47] to-[#e55a38] rounded-full flex items-center justify-center flex-shrink-0">
              <span className="text-white text-[10px] font-bold">{initials}</span>
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-xs font-semibold text-white/90 truncate">{user?.name ?? "Admin"}</p>
              <p className="text-[10px] text-white/35 truncate">{user?.email}</p>
            </div>
          </div>
          <button
            onClick={() => dispatch(logout())}
            className="flex items-center gap-2 text-white/35 hover:text-white/80 text-xs transition-colors w-full px-2 py-1.5 rounded-lg hover:bg-white/5"
          >
            <LogOut className="w-3.5 h-3.5" />
            Sign out
          </button>
        </div>
      </aside>

      {/* Main content */}
      <div className="ml-56 flex-1 min-h-screen">
        <Outlet />
      </div>
    </div>
  );
};

export default AdminLayout;
