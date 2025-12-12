import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../css/login.css";

export default function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const { login, userInfo } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!email || !password) {
            alert("이메일과 비밀번호를 입력해주세요.");
            return;
        }

        try {
            setLoading(true);
            await login(email, password);            
            navigate("/board", { replace: true });
        } catch (err) {
            console.error(err);
            alert("로그인 중 오류가 발생했습니다: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <div className="login-card">
                <div className="login-asset">
                    {/* 왼쪽 섹션: 에셋 들어갈 영역 */}
                </div>
                <div className="login-form-section">
                    <h2>로그인</h2>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>이메일</label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label>비밀번호</label>
                            <input
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div className="form-actions">
                            <button type="submit" disabled={loading}>
                                {loading ? "로그인중..." : "로그인"}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}
