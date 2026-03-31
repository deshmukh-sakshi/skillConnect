import useAuth from "@/hooks/use-auth";
import { Navigate, Outlet } from "react-router-dom";

const AuthLayout = () => {
  const { isAuth, user } = useAuth();
  if (isAuth) {
    if (user?.role === "ROLE_ADMIN") return <Navigate to="/admin" />;
    return <Navigate to="/" />;
  }

  return (
    <div className="relative min-h-screen bg-[#FAF8F5] overflow-hidden">
      {/* Floating geometric shapes */}
      <div className="absolute inset-0 pointer-events-none overflow-hidden">
        {/* Large coral circle */}
        <div
          className="absolute -top-20 -right-20 w-80 h-80 rounded-full opacity-[0.07] animate-float-slow"
          style={{ background: 'var(--color-coral)' }}
        />
        {/* Teal ring */}
        <div
          className="absolute top-1/3 -left-16 w-48 h-48 rounded-full border-[3px] opacity-[0.12] animate-float"
          style={{ borderColor: 'var(--color-teal)' }}
        />
        {/* Small coral dot */}
        <div
          className="absolute bottom-1/4 right-1/4 w-6 h-6 rounded-full opacity-20 animate-float"
          style={{ background: 'var(--color-coral)' }}
        />
        {/* Charcoal square */}
        <div
          className="absolute top-1/4 right-1/3 w-12 h-12 rounded-lg opacity-[0.04] rotate-45 animate-float-slow"
          style={{ background: 'var(--color-charcoal)' }}
        />
        {/* Teal dot */}
        <div
          className="absolute bottom-1/3 left-1/4 w-4 h-4 rounded-full opacity-20 animate-float-slow"
          style={{ background: 'var(--color-teal)', animationDelay: '2s' }}
        />
        {/* Large teal circle bottom */}
        <div
          className="absolute -bottom-32 -left-32 w-96 h-96 rounded-full opacity-[0.05] animate-float"
          style={{ background: 'var(--color-teal)', animationDelay: '1s' }}
        />
      </div>

      {/* Content */}
      <div className="relative z-10">
        <Outlet />
      </div>
    </div>
  );
};

export default AuthLayout;
