import { useEffect, useState } from "react";
import { useLocation, Link, useNavigate } from "react-router-dom";

export default function Header() {
  const location = useLocation();
  const navigate = useNavigate();
  const isHome = location.pathname === "/";
  const isBoard = location.pathname.startsWith("/board");

  const [isTop, setIsTop] = useState(true);

  useEffect(() => {
    if (!isHome) {
      setIsTop(false);
      return;
    }

    const handleScroll = () => {
      const threshold = 200;
      setIsTop(window.scrollY < threshold);
    };

    handleScroll();
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [isHome]);

  const headerClass = isBoard
    ? "solid"
    : isHome
    ? isTop
      ? "top"
      : "scrolled"
    : "solid";

  const scrollToSection = (id) => {
    const offsetEl = document.getElementById("header");
    const headerOffset = offsetEl ? offsetEl.offsetHeight : 80;
    const el = document.getElementById(id);
    if (!el) return;
    const top = el.getBoundingClientRect().top + window.scrollY - headerOffset;
    window.scrollTo({ top, behavior: "smooth" });
  };

  const handleNav = (id) => {
    if (isHome) {
      scrollToSection(id);
    } else {
      navigate("/", { state: { scrollTo: id } });
    }
  };

  return (
    <header id="header" className={headerClass}>
      <div className="header-inner">
        <h1>
          <Link to="/">IT Network</Link>
        </h1>
        <nav>
          <ul>
            <li><button className="nav-link" onClick={() => handleNav("about")}>소개</button></li>
            <li><button className="nav-link" onClick={() => handleNav("projects-section")}>활동</button></li>
            <li><button className="nav-link" onClick={() => handleNav("board")}>게시판</button></li>
            <li><button className="nav-link" onClick={() => handleNav("member")}>멤버</button></li>
            <li><button className="nav-link" onClick={() => handleNav("faq")}>FAQ</button></li>
            <li><button className="nav-link" onClick={() => handleNav("recruit")}>지원</button></li>
          </ul>
        </nav>
      </div>
    </header>
  );
}
