// 라이브러리
import { BrowserRouter as Router, Routes, Route, useLocation } from "react-router-dom";

// 페이지
import Home from "./pages/Home";
import NotFound from "./pages/NotFound";
import About from "./pages/About";
import Board_Home from "./pages/Board_home";
// HNF
import Header from "./include/Header";
import Header_Board from "./include/Header_board";

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
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/board" element={<Board_Home />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
}

export default App;
