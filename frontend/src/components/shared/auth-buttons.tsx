import { useNavigate } from "react-router-dom";

import { Button } from "../ui/button";
import useAuth from "@/hooks/use-auth";

const AuthButtons = () => {
  const { isAuth } = useAuth();
  const navigate = useNavigate();
  const redirect = (url: string) => navigate(url);

  if (isAuth) return null;

  return (
    <div className="flex items-center justify-center space-x-3">
      <Button
        className="cursor-pointer text-[#FF6B47] hover:text-[#E55A38] hover:bg-[#FF6B47]/5 font-medium"
        size="sm"
        variant="ghost"
        onClick={() => redirect("/auth/sign-in")}
      >
        Sign in
      </Button>
      <Button
        className="cursor-pointer bg-[#FF6B47] hover:bg-[#E55A38] text-white rounded-full px-6 shadow-md hover:shadow-lg transition-all duration-300"
        size="sm"
        onClick={() => redirect("/auth/sign-up")}
      >
        Sign up
      </Button>
    </div>
  );
};

export default AuthButtons;
