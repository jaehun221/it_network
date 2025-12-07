import { Link } from "react-router-dom";

export default function Header_Board() {
  return (
    <header id="header" className="solid">
      <div className="header-inner">
        <h1>
          <Link to="/board">IT Network - Board</Link>
        </h1>
        <nav>
          <ul>
            <li><Link to="/">홈</Link></li>
            <li><Link to="/recruit">지원</Link></li>
            <li><Link to="/board/signup">회원가입</Link></li>
            <li><Link to="/board/login">로그인</Link></li>
          </ul>
        </nav>
      </div>
    </header>
  );
}
