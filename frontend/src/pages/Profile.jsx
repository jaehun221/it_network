import { useAuth } from "../context/AuthContext";
import { useMemo } from "react";
import { Link } from "react-router-dom";
import "../css/profile.css";

const highlightStats = [
  { label: "참여 프로젝트", value: "12+" },
  { label: "서로를 위한 코드 리뷰", value: "48회" },
  { label: "지원 커뮤니티", value: "Discord • Notion" },
];

export default function Profile() {
  const { isAuthenticated, isLoading } = useAuth();

  const userInfo = useMemo(() => {
    const saved = localStorage.getItem("userInfo");
    return saved ? JSON.parse(saved) : null;
  }, []);

  if (isLoading) {
    return <h2 className="profile-loading">로딩 중...</h2>;
  }

  const isReady = isAuthenticated;

  const avatarInitials = userInfo?.user_nm
    ? userInfo.user_nm
        .split(" ")
        .map((part) => part[0])
        .join("")
        .slice(0, 2)
        .toUpperCase()
    : "IT";

  return (
    <div className="profile-page">
      <section className="profile-hero">
        <div className="profile-hero__content">
          <p className="profile-hero__eyebrow">MY SPACE</p>
          <h1>
            {isReady
              ? `환영합니다, ${userInfo?.user_nm || "IT Network"}님`
              : "PROFILE · LOGIN REQUIRED"}
          </h1>
          <p>
            동아리 내에서의 활동 현황과 참여 기회를 한눈에 확인하며 다음
            도전을 설계하세요.
          </p>
        </div>
      </section>

      <section className="profile-panel">
        <div className="profile-card">
          <div className="profile-card__header">
            <div className="profile-badge">{avatarInitials}</div>
            <div>
              <h2>{userInfo?.user_nm || "Guest"}</h2>
              <p>{isReady ? userInfo?.email : "board@it-network.com"}</p>
            </div>
          </div>

          <div className="profile-card__body">
            {isReady ? (
              <>
                <div className="profile-info">
                  <div>
                    <h3>계정 정보</h3>
                    <p>
                      <strong>ID:</strong> {userInfo?.user_id}
                    </p>
                    <p>
                      <strong>이름:</strong> {userInfo?.user_nm}
                    </p>
                    <p>
                      <strong>이메일:</strong> {userInfo?.email}
                    </p>
                  </div>
                  <div className="profile-actions">
                    <Link to="/board">게시판 바로가기</Link>
                    <Link to="/board/profile" className="ghost">
                      정보 수정
                    </Link>
                  </div>
                </div>
                <div className="profile-stats">
                  {highlightStats.map((stat) => (
                    <article key={stat.label}>
                      <p>{stat.value}</p>
                      <span>{stat.label}</span>
                    </article>
                  ))}
                </div>
              </>
            ) : (
              <div className="profile-locked">
                <p>로그인이 필요합니다.</p>
                <Link to="/board/login" className="primary">
                  로그인 하러 가기
                </Link>
              </div>
            )}
          </div>
        </div>
      </section>
    </div>
  );
}
