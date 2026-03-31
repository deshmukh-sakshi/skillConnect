import { useState, useEffect } from "react";
import { NAV_LINKS } from "@/constants/nav-links";
import type { NavLinkType } from "@/types";
import useAuth from "@/hooks/use-auth";
import AuthButtons from "./auth-buttons";
import LogoLink from "./logo-link";
import NavLinks from "./nav-links";
import UserAvatar from "./user-avatar";
import { ContactFormModal } from "./contact-form-modal";

const SiteHeader = () => {
  const [isContactModalOpen, setIsContactModalOpen] = useState(false);
  const [scrolled, setScrolled] = useState(false);
  const { user } = useAuth();

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };
    window.addEventListener("scroll", handleScroll, { passive: true });
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  // Create navigation links with contact form action
  const navLinksWithActions: NavLinkType[] = NAV_LINKS.map((link) => {
    if (link.title === "Contact Us") {
      return {
        ...link,
        action: () => setIsContactModalOpen(true),
      };
    }
    return link;
  });

  return (
    <>
      <header
        className={`fixed z-50 top-0 w-full transition-all duration-500 ${
          scrolled
            ? "bg-white/80 backdrop-blur-xl shadow-lg border-b border-[#E5E0D8]/50"
            : "bg-transparent"
        }`}
      >
        <div className="px-4 sm:px-8 lg:px-12 flex h-16 items-center mx-auto max-w-7xl justify-between">
          <div className="flex items-center justify-center space-x-4 sm:space-x-8">
            <LogoLink />
            <NavLinks links={navLinksWithActions} />
          </div>
          <div className="flex items-center justify-center space-x-2">
            <AuthButtons />
            <div className="hidden md:block">
              <UserAvatar />
            </div>
          </div>
        </div>
      </header>

      {/* Spacer for fixed header */}
      <div className="h-16" />

      <ContactFormModal
        isOpen={isContactModalOpen}
        onClose={() => setIsContactModalOpen(false)}
        userEmail={user?.email}
      />
    </>
  );
};

export default SiteHeader;
