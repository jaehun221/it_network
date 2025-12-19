import { useAuth } from "../context/AuthContext";
import { useMemo } from "react";
import { Link } from "react-router-dom";
import "../css/profile.css";

export default function Profile() {
  const { isAuthenticated, isLoading } = useAuth();

  const userInfo = useMemo(() => {
    const saved = localStorage.getItem("userInfo");
    return saved ? JSON.parse(saved) : null;
  }, []);

  if (isLoading) {
    return <div className="profile-container"><p className="profile-loading">로딩 중...</p></div>;
  }

  const avatarInitials = userInfo?.user_nm
    ? userInfo.user_nm.slice(0, 2).toUpperCase()
    : "??";

  return (
    <div className="profile-container">
      <h1 className="profile-title">프로필</h1>

      {isAuthenticated ? (
        <div className="profile-card">
          <div className="profile-avatar">{avatarInitials}</div>
          <div className="profile-details">
            <p className="profile-name">{userInfo?.user_nm || "이름 없음"}</p>
            <p className="profile-email">{userInfo?.email || "이메일 없음"}</p>
          </div>
          <div className="profile-info-list">
            <div className="profile-info-item">
              <span className="info-label">ID</span>
              <span className="info-value">{userInfo?.user_id || "-"}</span>
            </div>
            <div className="profile-info-item">
              <span className="info-label">이름</span>
              <span className="info-value">{userInfo?.user_nm || "-"}</span>
            </div>
            <div className="profile-info-item">
              <span className="info-label">이메일</span>
              <span className="info-value">{userInfo?.email || "-"}</span>
            </div>
          </div>
          <div className="profile-actions">
            <Link to="/board" className="profile-btn">게시판</Link>
            <Link to="/board/profile" className="profile-btn profile-btn--outline">정보 수정</Link>
          </div>
        </div>
      ) : (
        <div className="profile-card profile-card--empty">
          <p>로그인이 필요합니다.</p>
          <Link to="/board/login" className="profile-btn">로그인</Link>
        </div>
      )}
      <p style={{marginTop:"20px", color:"#b0b0b0ff"}}>정보 수정 기능은 아직 구현되지 않았습니다.</p>
    </div>
  );
}
