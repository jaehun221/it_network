import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer id="footer">
      <div className="footer-inner">
        <div className="footer-brand">
          <Link to="/" className="footer-logo">IT Network</Link>
          <p className="footer-email">contact@it-network.com</p>
          <hr style={{width:"40vh", marginTop:"5px", border:"1px solid #999"}}/>
          <p className="footer-copy">&copy; {new Date().getFullYear()} IT Network All Rights Reserved.</p>
        </div>
        <div className="footer-sns">
          <a href="https://github.com" target="_blank" rel="noreferrer">GitHub</a>
          <a href="https://www.instagram.com" target="_blank" rel="noreferrer">Instagram</a>
          <a href="https://www.youtube.com" target="_blank" rel="noreferrer">YouTube</a>
        </div>
      </div>
    </footer>
  );
}
