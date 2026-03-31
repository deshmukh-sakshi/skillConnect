import { NavLink } from "react-router-dom";

import { siteConfigs } from "@/apis";

import Logo from "./logo";

const LogoLink = () => {
  return (
    <NavLink
      to="/"
      className="flex items-center justify-center space-x-2 sm:space-x-3 group"
    >
      <Logo />
      <div className="grid flex-1 text-left leading-tight">
        <span
          className="truncate font-bold text-lg tracking-tight"
          style={{
            fontFamily: "'Space Grotesk', sans-serif",
            color: 'var(--color-charcoal)',
          }}
        >
          {siteConfigs.name.toUpperCase()}
        </span>
      </div>
    </NavLink>
  );
};

export default LogoLink;
