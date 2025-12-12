// 라이브러리
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";

// 페이지
import Home from "./pages/Home";
import NotFound from "./pages/NotFound";
import Board_Home from "./pages/Board_Home";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile";

// 헤더
import Header from "./include/Header";
import Header_Board from "./include/Header_Board";

// CSS
import './App.css'

function App() {
  return (
    <Router>
      <Layout />
    </Router>
  );
}

function Layout() {
  const location = useLocation();

  // 현재 URL 이 "/board" 로 시작하면 true
  const isBoard = location.pathname.startsWith("/board");

  return (
    <>
      {isBoard ? <Header_Board /> : <Header />}
      
      <Routes>
        {/* 기본 페이지 */}
        <Route path="/" element={<Home />} />

        {/* 게시판 페이지 */}
        <Route path="/board" element={<Board_Home />} />
        <Route path="/board/login" element={<Login />} />
        <Route path="/board/signup" element={<Signup />} />
        <Route path="/board/profile" element={<Profile />} />

        {/* 경로 없음 */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
}

export default App;
