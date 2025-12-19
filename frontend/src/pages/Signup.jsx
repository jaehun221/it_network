import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../css/signup.css";
import { useAuth } from "../context/AuthContext";

const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9999/api";

export default function Signup() {
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 간단한 유효성 검사
        if (!userId || !password || !name || !email) {
            alert("모든 필드를 입력해주세요.");
            return;
        }

        const payload = {
            user_id: userId,
            user_pw: password,
            user_nm: name,
            email: email,
        };

        try {
            setLoading(true);
            const res = await fetch(`${API_BASE_URL}/auth/signup`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });

            if (!res.ok) {
                const text = await res.text();
                throw new Error(text || "회원가입 실패");
            }

            // 성공 처리
            alert("회원가입 성공");
            await login(email, password);
            navigate("/board", { replace: true });
            // 폼 초기화
            setUserId("");
            setPassword("");
            setName("");
            setEmail("");
        } catch (err) {
            console.error(err);
            alert("회원가입 중 오류가 발생했습니다: " + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="signup-page">
            <div className="signup-card">
                <div className="signup-asset">
                    {/* 에셋 들어갈 영역 */}
                </div>
                <div className="signup-form-section">
                    <h2>회원가입</h2>
                    <p className="signup-form-subtitle">
                        계정을 생성하여 게시글을 작성해보세요.
                    </p>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>아이디</label>
                            <input
                                type="text"
                                value={userId}
                                onChange={(e) => setUserId(e.target.value)}
                                required
                            />
                        </div>
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
                            <label>이름</label>
                            <input
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
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
                                {loading ? "가입중..." : "회원가입"}
                            </button>
                        </div>
                    </form>
                    <div className="signup-form-footnote">
                        이미 계정이 있다면 <Link to="/board/login">로그인</Link>하세요.
                    </div>
                </div>
            </div>
        </div>
    );
}
