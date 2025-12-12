import "../css/home.css";
import { useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

export default function Home() {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const target = location.state && location.state.scrollTo;
    if (target) {
      // 작은 지연을 둬서 라우트가 렌더링된 뒤 스크롤 실행
      setTimeout(() => {
        const offsetEl = document.getElementById("header");
        const headerOffset = offsetEl ? offsetEl.offsetHeight : 80;
        const el = document.getElementById(target);
        if (el) {
          const top = el.getBoundingClientRect().top + window.scrollY - headerOffset;
          window.scrollTo({ top, behavior: "smooth" });
        }
        // 상태 제거 (같은 페이지로 다시 진입 시 반복 방지)
        navigate(location.pathname, { replace: true, state: null });
      }, 50);
    }
  }, [location, navigate]);

  return (
    <main id="home">
      {/* Hero Section */}
      <section id="hero" className="hero">
        <h1 className='title'><span>IMAGINE. </span><span>CREATE. </span><span>ACHIEVE. </span></h1>
        
        {/* <button className="cta">가입하기</button> */}
      </section>

      {/* About Section */}
      <section id="about" className="about">
        <h2>동아리 소개</h2>
        <p>
          IT Network는 개발, 보안, 네트워크, 디자인 등 다양한 분야에 관심 있는 학생들이 모여
          프로젝트를 하고 지식을 공유하는 동아리입니다.
        </p>
      </section>

      {/* Projects Section */}
      <section id="projects-section" className="projects">
        <h2>주요 활동</h2>
        <div className="project-list">
          <div className="project-card">프로젝트 1</div>
          <div className="project-card">프로젝트 2</div>
          <div className="project-card">프로젝트 3</div>
        </div>
      </section>

      {/* Board Section */}
      <section id="board" className="board">
        <h2>게시판 운영</h2>
        <p>게시판을 운영하고 있습니다. ㄱㄱㄱ</p>
        <Link to="/board">이동</Link>
      </section>

      {/* Recruit Section */}
      <section id="recruit" className="recruit">
        <h2>신입 부원 모집</h2>
        <p>신입 기수 모집 기간입니다. 지금 바로 지원하세요!</p>
        <button className="cta">지원하기</button>
      </section>
    </main>
  );
}
