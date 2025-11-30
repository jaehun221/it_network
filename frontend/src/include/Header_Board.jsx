export default function Header_Border() {
    return (
        <header id="header" className={headerClass}>
            <h1><Link to="/board-home">IT Network - Board</Link></h1>
                <nav>
                    <ul>
                        <li><Link to="/">홈</Link></li>
                        <li><Link to="/about">소개</Link></li>
                        <li><Link to="/projects">활동</Link></li>
                        <li><Link to="/recruit">지원</Link></li>
                    </ul>
                </nav>
        </header>
  );
}