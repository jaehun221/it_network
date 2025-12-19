import "../css/home.css";
import "../css/board_page.css";
import { useEffect, useRef, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import ProfileCard from "../include/ProfileCard";
import BoardListSection from "../components/BoardListSection";
import TextType from "../lib/TextType";
import mhpi from '../assets/mhpi.png'
import LogoLoop from "../lib/LogoLoop";
import { SiReact, SiDocker, SiSpringboot, SiGit } from 'react-icons/si';

const techLogos = [
  { node: <SiDocker />, title: "Docker", href: "https://www.docker.com" },
  { node: <SiSpringboot />, title: "Spring Boot", href: "https://spring.io/projects/spring-boot" },
  { node: <SiReact />, title: "React", href: "https://react.dev" },
  { node: <SiGit />, title: "Git", href: "https://git-scm.com" },
];

const faqItems = [
  {
    question: "동아리 회원은 어떤 활동을 하나요?",
    answer:
      "매주 정기모임과 프로젝트 스프린트를 진행하며, Docker, Spring, AWS 같은 실무 기술을 직접 적용하는 경험을 나눕니다. 외부 해커톤과 스터디도 병행하여 실력을 이어갑니다.",
  },
  {
    question: "지원할 때 준비물이 따로 있나요?",
    answer:
      "기본적으로 개발에 대한 관심과 간단한 자기소개만 있으면 되고, 제출물은 별도로 요구하지 않습니다. 단, 포트폴리오가 있다면 함께 첨부하면 더 좋습니다.",
  },
  {
    question: "초보자도 함께할 수 있나요?",
    answer:
      "물론입니다. 기초부터 순차적으로 알려주는 멘토링과 실습 세션이 준비되어 있고, 기존 부원이 함께 페어 프로그래밍을 해줘서 더 빠르게 성장할 수 있어요.",
  },
  {
    question: "모임은 어디에서 열리나요?",
    answer:
      "성일정보고등학교 내에서 진행되며, 프로젝트에 따라 온라인 협업(Discord, GitHub)을 병행합니다. 일정은 공지방을 통해 공유됩니다.",
  },
  {
    question: "활동 기록이나 포트폴리오는 어떻게 관리하나요?",
    answer:
      "Notion과 GitHub Pages를 활용해 활동 로그를 정리하며, 중요한 프로젝트는 외부 발표로 공유합니다. 원한다면 개인 포트폴리오 링크도 지원서에서 안내해주세요.",
  },
];

export default function Home() {
  const location = useLocation();
  const navigate = useNavigate();
  const aboutRef = useRef(null);
  const [playTextType, setPlayTextType] = useState(false);
  const [openFaq, setOpenFaq] = useState(null);

  // 모집 상태 변수와 연락처
  const recruit = true; // true로 변경하면 모집 중 문구가 표시됩니다.
  const recruitPhone = '(+82) 010-4045-0498';

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
        <h2 className="title" style={{fontSize:"2.5rem", marginBottom:"2.5rem"}}>활용 기술</h2>
        <div style={{width:"100%", height: '200px', position: 'relative', overflow: 'hidden'}}>
          {/* Vertical loop with deceleration on hover */}
          <LogoLoop
            logos={techLogos}
            speed={80}
            direction="right"
            logoHeight={40}
            gap={40}
            hoverSpeed={20}
            // fadeOut
          />
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
            github={"https://github.com/kornet79"}
          />

          <ProfileCard 
            imageElement={ <img src={mhpi} alt="김민후" className="profile-image"/> }
            name="김민후"
            title="부단장"
            affiliations={"IIST"}
            tagline="트릭컬 리바이브 지금 당장 다운로드"
            notion={"https://valuable-ankle-b82.notion.site/8831905b1a034d0cabdae96314127cd3"}
          />

          <ProfileCard 
            imageUrl="https://github.com/jaehun221.png"
            name="이재훈"
            title="부원"
            affiliations={"FuriosaAI"}
            tagline="여고생 입니다"
            github={"https://github.com/jaehun221"}
            githubIo={"https://jaehun221.github.io/"}
          />

          <ProfileCard 
            imageUrl="https://github.com/gaengji.png"
            name="강지원"
            title="부원"
            affiliations={"대양CIS"}
            tagline="너정말핵심을찔렀어"
            github={"https://github.com/gaengji"}
          />

          <ProfileCard 
            imageUrl="https://github.com/kimyangmin.png"
            name="박영민"
            title="모집 담당"
            affiliations={"킹스정보통신"}
            tagline="동아리 지원 많이 해주세요"
            github={"https://github.com/kimyangmin"}
            notion={"https://rainy-jacket-3d3.notion.site/213d205f76688018b7e3ccdc7a8034ca?source=copy_link"}
          />

          <ProfileCard 
            imageUrl="https://github.com/kkob728.png"
            name="김서준"
            title="부원"
            affiliations={"비도제"}
            tagline="롤할사람 구합니다"
            github={"https://github.com/kkob728"}
          />

          <ProfileCard 
            imageUrl="https://github.com/rokaing123.png"
            name="김태웅"
            title="부원"
            affiliations={"비도제"}
            tagline="김태웅 입니다"
            github={"https://github.com/rokaing123"}
          />
        </div>
        
      </section>

      <section id="faq" className="faq">
        <h2>FAQ</h2>
        <div className="faq-list">
          {faqItems.map((item, idx) => {
            const isOpen = openFaq === idx;
            return (
              <article className={`faq-item ${isOpen ? "open" : ""}`} key={item.question}>
                <button
                  className="faq-question"
                  type="button"
                  aria-expanded={isOpen}
                  onClick={() => setOpenFaq(isOpen ? null : idx)}
                >
                  <span>{item.question}</span>
                  <span className="faq-arrow">{isOpen ? "−" : "+"}</span>
                </button>
                <div className={`faq-answer ${isOpen ? "open" : ""}`}>
                  <p>{item.answer}</p>
                </div>
              </article>
            );
          })}
        </div>
      </section>

      {/* Recruit Section */}
      <section id="recruit" className="recruit">
        <h2>신입 부원 모집</h2>
        {recruit ? (
          <>
            <p className="recruit-lead">
              IT Network는 협업 중심의 실무형 커리큘럼과 동료 지원 문화를 기반으로 신입을 기다리고 있습니다.
              관심있는 친구는 어느 수준이든 환영하며, 성실한 태도와 호기심만 챙겨오세요.
            </p>
            <div className="recruit-columns">
              <div className="recruit-card">
                <h3>지원 자격</h3>
                <ul>
                  <li>개발 또는 인프라에 관심 있는 성일정보고등학교 재학생</li>
                  <li>매주 정기 모임(일요일 오후 8시 예정)에 온라인 참여 가능한 분</li>
                  <li>팀원과의 협업에서 책임감 있게 역할을 수행할 분</li>
                </ul>
              </div>
              <div className="recruit-card">
                <h3>활동 혜택</h3>
                <ul>
                  <li>ChatGPT Plus, Gemini AI Pro 계정 제공</li>
                  <li>기술 멘토링, 코드 리뷰, 배포/운영 경험 공유 세션</li>
                  <li>개인 학습을 위한 전공책·인프런 강의 지원</li>
                </ul>
              </div>
            </div>
            <div className="recruit-contact">
              <p>
                <strong>지원 방법</strong> : 아래 연락처로 간단한 자기소개(관심기술, 참여 목적)를 보내주세요.
              </p>
              <p className="recruit-phone">
                <span><span style={{fontWeight:"lighter", fontSize:"15px", color:"#e2e2e2ff", paddingRight:"5px"}}>모집 담당</span> 박영민</span>
                <span style={{color:"#e8e8e8ff"}}>{recruitPhone}</span>
              </p>
              {/* <p>
                <strong>IT Network</strong> 는 개발에 관심이 많은 학생분들을 환영합니다.
              </p> */}
            </div>
          </>
        ) : (
          <p>지금은 모집 기간이 아닙니다. 다음 모집 공고를 기다려 주세요.</p>
        )}
      </section>
    </main>
  );
}
