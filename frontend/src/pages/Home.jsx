import "../css/home.css";
import "../css/board_page.css";
import { useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import ProfileCard from "../include/ProfileCard";
import BoardListSection from "../components/BoardListSection";
import TextType from "../lib/TextType";

export default function Home() {
  const location = useLocation();
  const navigate = useNavigate();
  const aboutRef = useRef(null);
  const [playTextType, setPlayTextType] = useState(false);

  // 모집 상태 변수와 연락처
  const recruit = true; // true로 변경하면 모집 중 문구가 표시됩니다.
  const recruitPhone = '010-1234-5678';

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

  useEffect(() => {
    const el = aboutRef.current;
    if (!el) return;
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0]?.isIntersecting) {
          setPlayTextType(true); // 화면에 보이면 시작
          observer.disconnect(); // 한 번만 트리거
        }
      },
      { root: null, threshold: 0.25 }
    );
    observer.observe(el);
    return () => observer.disconnect();
  }, []);

  return (
    <main id="home">
      {/* Hero Section */}
      <section id="hero" className="hero">
        <h1 className='title'><span>IMAGINE. </span><span>CREATE. </span><span>ACHIEVE. </span></h1>
        
      </section>

      {/* About Section */}
      <section id="about" className="about" ref={aboutRef}>
        <h1 style={{ textAlign: "center", fontSize: "3rem" }} className="title">
          <TextType
            texts={["Welcome!", "About?"]}
            typingMs={100}
            deletingMs={35}
            pauseMs={3000}
            loop={false}
            cursorChar="_"
            play={playTextType} // 추가: 화면 도달 시 시작
          />
       </h1>
        <p>
          IT Network는 성일정보고등학교와 함께 실무 중심의 기술 문화를 만들어온 동아리입니다. <br />
          개발과 인프라에 관심 있는 학생들이 모여 Docker, Spring Boot, AWS 등 실제 현업에서 사용되는 기술을 직접 전수하며 함께 성장해 왔습니다. <br />
          체계적인 학습 흐름과 책임감 있는 동아리 문화, 그리고 세대를 잇는 지식 공유는 IT Network의 가장 큰 자산입니다.
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
      <section id="board" className="board" style={{textAlign:"left"}}>
        <div className="board-intro">
          <h2>게시판 운영</h2>
          <p>최근 게시글을 빠르게 확인하고, 더 많은 글은 다음 페이지에서 살펴보세요.</p>
        </div>
        <BoardListSection
          title="최근 게시글"
          className="home-board-list"
          size={3}
          hidePagination
        />
        <div className="board-cta">
          <Link className="board-link" to="/board">
            전체 게시판 보기
          </Link>
        </div>
      </section>

      {/* Member Section */}
      <section id="member" className="member">
        <h1 style={{ marginBottom: "15px" }}>현재 부원</h1>
        <div className="profile-grid">
          <ProfileCard 
            imageUrl="https://github.com/kornet79.png"
            name="김규민"
            title="단장"
            affiliations={"JIN INFRA"}
            tagline="단장입니다"
            github={"https://github.com/kornet79/kornet79"}
          />

          <ProfileCard 
            imageUrl="https://github.com/dbsrjs.png"
            name="김민후"
            title="부단장"
            affiliations={"IIST"}
            tagline="부단장입니다"
            github={"https://github.com/kornet79/kornet79"}
          />

          <ProfileCard 
            imageUrl="https://github.com/jaehun221.png"
            name="이재훈"
            title="부원"
            affiliations={"FuriosaAI"}
            tagline="부원입니다"
            github={"https://github.com/jaehun221"}
          />

          <ProfileCard 
            imageUrl="https://github.com/gaengji.png"
            name="강지원"
            title="부원"
            affiliations={"안승우 집"}
            tagline="저는 3학년 11반 입니다."
            github={"https://github.com/gaengji"}
          />

          <ProfileCard 
            imageUrl="https://github.com/kimyangmin.png"
            name="박영민"
            title="부원"
            affiliations={"킹스정보통신"}
            tagline="Content-Type: application/json"
            github={"https://github.com/kimyangmin"}
          />

          <ProfileCard 
            imageUrl="https://github.com/kkob728.png"
            name="김서준"
            title="부원"
            affiliations={"비도제"}
            tagline="부원입니다"
            github={"https://github.com/kkob728"}
          />

          <ProfileCard 
            imageUrl="https://github.com/guthib.png"
            name="김태웅"
            title="부원"
            affiliations={"비도제"}
            tagline="부원입니다"
            github={"https://github.com/guthib"}
          />
        </div>
        
      </section>

      <section id="faq" className="faq">
        <h2>FAQ</h2>
        
      </section>

      {/* Recruit Section */}
      <section id="recruit" className="recruit">
        <h2>신입 부원 모집</h2>
        {recruit ? (
          <>
            <p>
              현재 동아리 모집 중입니다! <br />
              <span style={{fontSize:"2rem"}}>단장 김규민</span> {recruitPhone}로 연락하세요.
            </p>
            {/* <button className="cta">지원하기</button> */}
          </>
        ) : (
          <p>지금은 모집 기간이 아닙니다. 다음 모집 공고를 기다려 주세요.</p>
        )}
      </section>
    </main>
  );
}
