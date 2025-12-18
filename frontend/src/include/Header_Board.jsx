import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext"; // 추가

export default function Header_Board() {
  const { isAuthenticated, logout } = useAuth(); // 추가
  const navigate = useNavigate(); // 추가

  const handleLogout = async () => {
    try {
      await logout();
      navigate("/board"); // 로그아웃 후 홈으로 이동
    } catch (e) {
      // 간단히 무시하거나 필요시 오류 처리
      console.error(e);
    }
  };

  return (
    <>
      <header id="header" className="solid header_board">
        <div className="header-inner">
          <h1>
            <Link to="/board">
              <img src="/logo.png" alt="IT Network logo" className="header-logo" />
            </Link>
          </h1>
          <nav>
            <ul>
              <li><Link to="/board">홈</Link></li>
              <li><Link to="/">동아리</Link></li>

              {/* 인증 상태에 따라 메뉴 분기 */}
              {!isAuthenticated ? (
                <>
                  <li><Link to="/board/signup">회원가입</Link></li>
                  <li><Link to="/board/login">로그인</Link></li>
                </>
              ) : (
                <>
                  <li><Link to="/board/profile">마이페이지</Link></li>
                  <li>
                    <button
                      onClick={handleLogout}
                      style={{
                        background: "none",
                        border: "none",
                        padding: 0,
                        fontWeight: "bold",
                        fontSize: "18px",
                        cursor: "pointer",
                        color: "inherit",
                      }}
                      aria-label="로그아웃"
                    >
                      로그아웃
                    </button>
                  </li>
                </>
              )}
            </ul>
          </nav>
        </div>
      </header>
      <div className="header_board-spacer" aria-hidden="true" />
    </>
  );
}
