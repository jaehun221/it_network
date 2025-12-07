import { useAuth } from "../context/AuthContext";

export default function Profile() {
    const { isAuthenticated, userInfo, isLoading } = useAuth();

    if (isLoading) {
        return <h2>로딩 중...</h2>;
    }

    return (
        <>
            <h1>환영하노</h1>

            {!isAuthenticated && (
                <p>로그인하지 않은 사용자입니다.</p>
            )}

            {isAuthenticated && userInfo && (
                <div className="user-info-box">
                    <h2>로그인 정보</h2>
                    <p><strong>ID:</strong> {userInfo.user_id}</p>
                    <p><strong>이름:</strong> {userInfo.user_nm}</p>
                    <p><strong>이메일:</strong> {userInfo.email}</p>
                    
                </div>
            )}
        </>
    );
}
