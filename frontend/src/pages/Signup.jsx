import React, { useState } from "react";

export default function Signup() {
    const [userId, setUserId] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);

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
            const res = await fetch("http://localhost:9999/auth/signup", {
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
        <>
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>아이디 (user_id)</label>
                    <br />
                    <input
                        type="text"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>이메일 (email)</label>
                    <br />
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>이름 (user_nm)</label>
                    <br />
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>비밀번호 (user_pw)</label>
                    <br />
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div style={{ marginTop: 10 }}>
                    <button type="submit" disabled={loading}>
                        {loading ? "가입중..." : "회원가입"}
                    </button>
                </div>
            </form>
        </>
    );
}