import Hero from "./components/hero";
import Categories from "./components/categories";
import WorkSection from "./components/work-section";
import DeveloperIllustration from "./components/developer-illustration";

const HomePage = () => {
  return (
    <div className="relative overflow-x-hidden">
      <Hero />
      <WorkSection />
      <DeveloperIllustration />
      <Categories />
    </div>
  );
};

export default HomePage;
