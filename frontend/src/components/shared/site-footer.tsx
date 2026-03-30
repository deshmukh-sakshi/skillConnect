import { siteConfigs } from "@/apis";
import { Github, Twitter, Linkedin, Heart } from "lucide-react";

const SiteFooter = () => {
  return (
    <footer className="relative wave-separator bg-[#1A1A2E] text-white mt-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-8 lg:px-12 py-16 pt-24">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-12">
          {/* Brand Column */}
          <div className="space-y-4">
            <h3
              className="text-2xl font-bold tracking-tight"
              style={{ fontFamily: "'Space Grotesk', sans-serif" }}
            >
              {siteConfigs.name}
            </h3>
            <p className="text-gray-400 text-sm leading-relaxed max-w-xs">
              The modern platform connecting skilled freelancers with
              ambitious projects. Work from anywhere, earn everywhere.
            </p>
          </div>

          {/* Quick Links */}
          <div className="space-y-4">
            <h4
              className="text-sm font-semibold uppercase tracking-widest text-gray-400"
              style={{ fontFamily: "'Space Grotesk', sans-serif" }}
            >
              Quick Links
            </h4>
            <ul className="space-y-3">
              <li>
                <a
                  href="/"
                  className="text-gray-300 hover:text-[#FF6B47] transition-colors text-sm"
                >
                  Home
                </a>
              </li>
              <li>
                <a
                  href="/dashboard/browse-projects"
                  className="text-gray-300 hover:text-[#FF6B47] transition-colors text-sm"
                >
                  Browse Projects
                </a>
              </li>
              <li>
                <a
                  href="/auth/sign-up"
                  className="text-gray-300 hover:text-[#FF6B47] transition-colors text-sm"
                >
                  Get Started
                </a>
              </li>
            </ul>
          </div>

          {/* Social Links */}
          <div className="space-y-4">
            <h4
              className="text-sm font-semibold uppercase tracking-widest text-gray-400"
              style={{ fontFamily: "'Space Grotesk', sans-serif" }}
            >
              Connect
            </h4>
            <div className="flex items-center space-x-4">
              <a
                href={siteConfigs.links.github}
                target="_blank"
                rel="noreferrer"
                className="w-10 h-10 rounded-full bg-white/10 flex items-center justify-center hover:bg-[#FF6B47] transition-all duration-300 hover:scale-110"
              >
                <Github className="size-4" />
              </a>
              <a
                href="#"
                className="w-10 h-10 rounded-full bg-white/10 flex items-center justify-center hover:bg-[#2EC4B6] transition-all duration-300 hover:scale-110"
              >
                <Twitter className="size-4" />
              </a>
              <a
                href="#"
                className="w-10 h-10 rounded-full bg-white/10 flex items-center justify-center hover:bg-[#FF6B47] transition-all duration-300 hover:scale-110"
              >
                <Linkedin className="size-4" />
              </a>
            </div>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="mt-12 pt-8 border-t border-white/10 flex flex-col sm:flex-row items-center justify-between gap-4">
          <p className="text-gray-500 text-sm">
            © {new Date().getFullYear()} {siteConfigs.name}. All rights reserved.
          </p>
          <p className="text-gray-500 text-sm flex items-center gap-1">
            Made with <Heart className="size-3 text-[#FF6B47] fill-[#FF6B47]" /> by the team
          </p>
        </div>
      </div>
    </footer>
  );
};

export default SiteFooter;
