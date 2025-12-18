import { useAuth } from "../context/AuthContext";
import { useMemo } from "react";

export default function Profile() {
    const { isAuthenticated, isLoading } = useAuth();
    
    const userInfo = useMemo(() => {
        const saved = localStorage.getItem("userInfo");
        return saved ? JSON.parse(saved) : null;
    }, []);

    if (isLoading) {
        return <h2>로딩 중...</h2>;
    }

    const isReady = isAuthenticated;

    return (
        <>
            {/* 제목: 인증 여부에 따라 변경 */}
            {isReady ? (
                <h1>
                    환영합니다
                    {userInfo?.user_nm ? `, ${userInfo.user_nm}님` : ""}
                </h1>
            ) : (
                <h1>환영하노</h1>
            )}

            {/* 본문: 미인증 안내 */}
            {!isReady && (
                <p>로그인이 필요합니다. 게시판 로그인 후 다시 확인하세요.</p>
            )}

            {/* 본문: 인증됨 + userInfo 존재 시 상세 정보 */}
            {isReady ? (
                userInfo ? (
                    <div className="user-info-box">
                        <h2>로그인 정보</h2>
                        <p><strong>ID:</strong> {userInfo.user_id}</p>
                        <p><strong>이름:</strong> {userInfo.user_nm}</p>
                        <p><strong>이메일:</strong> {userInfo.email}</p>
                    </div>
                ) : (
                    <p className="user-info-box">회원 정보가 없습니다.</p>
                )
            ) : null}
        </>
    );
}
