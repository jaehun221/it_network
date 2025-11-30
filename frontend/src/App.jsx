// 라이브러리
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// 페이지
import Home from "./pages/Home";
import NotFound from "./pages/NotFound";
import About from "./pages/About";
import Board_Home from "./pages/Board_home";
// HNF
import Header from "./include/Header";

// CSS
import './App.css'

function App() {
  return(
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/board-home" element={<Board_Home/>} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  )
}

export default App
