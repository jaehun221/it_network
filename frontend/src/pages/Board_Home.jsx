import "../css/board_page.css";
import { useState } from "react";
import BoardListSection from "../components/BoardListSection";
import BoardCreateForm from "../components/BoardCreateForm";
import { useAuth } from "../context/AuthContext";

export default function Board_Home() {
  const { isAuthenticated, status } = useAuth();
  const [showForm, setShowForm] = useState(false);
  const [refreshSignal, setRefreshSignal] = useState(0);

  const handleNewBoard = () => {
    setRefreshSignal((prev) => prev + 1);
  };

  const toggleForm = () => {
    setShowForm((prev) => !prev);
  };

  const disableFormButton = !isAuthenticated && status !== "loading";

  return (
    <main className="board-page">
      <div className="board-page-header">
        <h1>자유게시판 - Beta</h1>
        <p>자유롭게 이야기를 나누는 공간입니다. 댓글로 활발하게 소통해 보세요.</p>
      </div>

      <div className="board-create-bar">
        <button
          className="board-create-trigger"
          onClick={toggleForm}
          disabled={disableFormButton}
        >
          {showForm ? "작성 취소" : "새 게시글 작성"}
        </button>
        {!isAuthenticated && status !== "loading" && (
          <p className="board-create-note">로그인해야 글을 작성할 수 있습니다.</p>
        )}
      </div>

      {showForm && isAuthenticated && (
        <BoardCreateForm
          onSuccess={() => {
            handleNewBoard();
            setShowForm(false);
          }}
          onCancel={() => setShowForm(false)}
        />
      )}

      <BoardListSection
        title="최근 게시글"
        description="최신 10개 글을 최신순으로 보여줍니다. 더 많은 글은 다음 페이지에서 확인하세요."
        refreshSignal={refreshSignal}
      />
    </main>
  );
}
