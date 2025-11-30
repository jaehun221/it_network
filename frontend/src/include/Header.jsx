// Header.jsx
import { useEffect, useState } from "react";
import { useLocation, Link } from "react-router-dom";

export default function Header() {
  const location = useLocation();
  const isHome = location.pathname === "/"; // 메인 페이지인지 확인

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

  const headerClass = isHome
    ? isTop
      ? "top"
      : "scrolled"
    : "solid";

  return (
    <header id="header" className={headerClass}>
      <h1><Link to="/">IT Network</Link></h1>
      <nav>
        <ul>
          <li><Link to="/about">소개</Link></li>
          <li><Link to="/projects">활동</Link></li>
          <li><Link to="/board-home">게시판</Link></li>
          <li><Link to="/recruit">지원</Link></li>
        </ul>
      </nav>
    </header>
  );
}
