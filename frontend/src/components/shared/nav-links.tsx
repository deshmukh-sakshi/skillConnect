import useAuth from "@/hooks/use-auth";
import { cn } from "@/lib/utils";
import type { NavLinkType } from "@/types";
import { NavLink } from "react-router-dom";

interface NavLinksProps {
  links: NavLinkType[];
}
const NavLinks = ({ links }: NavLinksProps) => {
  const { isAuth } = useAuth();

  return (
    <div className="items-center justify-center space-x-6 hidden md:flex">
      {links.map((link) => {
        // Skip protected links if user is not authenticated
        if (link.isProtected && !isAuth) {
          return null;
        }

        if (link.action && !link.path) {
          return (
            <button
              key={link.id}
              onClick={link.action}
              className={cn(
                "nav-link-hover transition-all space-x-1 flex items-center justify-center",
                "text-[#1A1A2E]/70 hover:text-[#FF6B47] font-medium",
              )}
            >
              <span className="text-sm">{link.title}</span>
            </button>
          );
        }

        return (
          <NavLink
            key={link.id}
            to={link.path!}
            className={({ isActive }) =>
              cn(
                "nav-link-hover transition-all space-x-1 flex items-center justify-center font-medium",
                "text-[#1A1A2E]/70 hover:text-[#FF6B47]",
                isActive && "text-[#FF6B47] font-semibold",
              )
            }
          >
            <span className="text-sm">{link.title}</span>
          </NavLink>
        );
      })}
    </div>
  );
};

export default NavLinks;
