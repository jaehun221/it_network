import '../css/home.css'


export default function Home() {
  return (
    <main id="home">
      {/* Hero Section */}
      <section className="hero">
        <h1>IT Network</h1>
        <p>기술을 통해 함께 성장하는 IT 동아리</p>
        {/* <button className="cta">가입하기</button> */}
      </section>

      {/* About Section */}
      <section className="about">
        <h2>동아리 소개</h2>
        <p>
          IT Network는 개발, 보안, 네트워크, 디자인 등 다양한 분야에 관심 있는 학생들이 모여
          프로젝트를 하고 지식을 공유하는 동아리입니다.
        </p>
      </section>

      {/* Projects Section */}
      <section className="projects">
        <h2>주요 활동</h2>
        <div className="project-list">
          <div className="project-card">프로젝트 1</div>
          <div className="project-card">프로젝트 2</div>
          <div className="project-card">프로젝트 3</div>
        </div>
      </section>

      {/* Recruit Section */}
      <section className="recruit">
        <h2>신입 부원 모집</h2>
        <p>신입 기수 모집 기간입니다. 지금 바로 지원하세요!</p>
        <button className="cta">지원하기</button>
      </section>
    </main>
  );
}
