import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../css/board_detail.css";

const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9999/api";
const buildApiUrl = (path) => (path.startsWith("http") ? path : `${API_BASE_URL}${path}`);

export default function BoardDetailPage() {
  const { id } = useParams();
  const [board, setBoard] = useState(null);
  const [boardError, setBoardError] = useState("");
  const [loadingBoard, setLoadingBoard] = useState(true);

  const [comments, setComments] = useState([]);
  const [loadingComments, setLoadingComments] = useState(true);
  const [commentsError, setCommentsError] = useState("");

  const [commentContent, setCommentContent] = useState("");
  const [formError, setFormError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const { fetchWithAuth, isAuthenticated, status } = useAuth();

  useEffect(() => {
    const controller = new AbortController();
    setBoardError("");
    setLoadingBoard(true);

    fetchWithAuth(buildApiUrl(`/boards/${id}`), { signal: controller.signal })
      .then((res) => {
        if (!res.ok) {
          throw new Error("게시글을 찾을 수 없습니다.");
        }
        return res.json();
      })
      .then((data) => setBoard(data))
      .catch((err) => {
        if (err.name !== "AbortError") {
          setBoardError(err.message);
        }
      })
      .finally(() => setLoadingBoard(false));

    return () => controller.abort();
  }, [id, fetchWithAuth]);

  useEffect(() => {
    const controller = new AbortController();
    setCommentsError("");
    setLoadingComments(true);

    fetchWithAuth(buildApiUrl(`/comments?postId=${id}`), { signal: controller.signal })
      .then((res) => {
        if (!res.ok) {
          throw new Error("댓글을 불러오는 중 오류가 발생했습니다.");
        }
        return res.json();
      })
      .then((data) => setComments(data))
      .catch((err) => {
        if (err.name !== "AbortError") {
          setCommentsError(err.message);
        }
      })
      .finally(() => setLoadingComments(false));

    return () => controller.abort();
  }, [id, fetchWithAuth]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!commentContent.trim()) {
      setFormError("댓글을 입력해 주세요.");
      return;
    }

    setFormError("");
    setSubmitting(true);

    try {
      const res = await fetchWithAuth(buildApiUrl(`/comments`), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          postId: Number(id),
          content: commentContent.trim(),
        }),
      });

      if (!res.ok) {
        throw new Error("댓글 등록에 실패했습니다.");
      }

      setCommentContent("");
      // 댓글 목록을 갱신 (인증 헤더 포함)
      const refresh = await fetchWithAuth(buildApiUrl(`/comments?postId=${id}`));
      if (!refresh.ok) {
        throw new Error("댓글을 다시 불러오는 데 실패했습니다.");
      }
      const refreshed = await refresh.json();
      setComments(refreshed);
    } catch (err) {
      setFormError(err.message || "오류가 발생했습니다.");
    } finally {
      setSubmitting(false);
    }
  };

  const formatDate = (value) => {
    if (!value) return "-";
    return new Date(value).toLocaleString("ko-KR", {
      dateStyle: "medium",
      timeStyle: "short",
    });
  };

  return (
    <main className="board-detail-page">
      <Link className="board-detail-back-link" to="/board">
        ← 전체 게시판으로 돌아가기
      </Link>

      {boardError && <p className="comment-error">{boardError}</p>}

      {loadingBoard && !boardError && <p>게시글을 불러오는 중입니다…</p>}

      {board && (
        <>
          <article className="board-detail-header">
            <h1>{board.title}</h1>
            <div className="board-detail-meta">
              <span>ID #{board.id}</span>
              <span>{formatDate(board.createdAt)}</span>
            </div>
          </article>

          <section className="board-detail-body">{board.content}</section>
        </>
      )}

      <section className="comment-section">
        <h2>댓글 ({comments.length})</h2>

        {isAuthenticated ? (
          <form className="comment-form" onSubmit={handleSubmit}>
            <textarea
              value={commentContent}
              onChange={(event) => setCommentContent(event.target.value)}
              placeholder="댓글을 입력하세요."
              aria-label="댓글 내용"
            />
            <button type="submit" disabled={submitting}>
              {submitting ? "등록 중…" : "댓글 작성"}
            </button>
            {formError && <p className="comment-error">{formError}</p>}
          </form>
        ) : (
          status === "loading" ? (
            <p className="comment-error">로그인 상태를 확인하는 중입니다…</p>
          ) : (
            <p className="comment-error">로그인 후 댓글을 작성할 수 있습니다.</p>
          )
        )}

        {commentsError && <p className="comment-error">{commentsError}</p>}

        {loadingComments && !commentsError && <p>댓글을 불러오는 중입니다…</p>}

        {!loadingComments && comments.length === 0 && (
          <p className="comment-error" style={{ color: "rgba(255,255,255,0.7)" }}>
            아직 댓글이 없습니다.
          </p>
        )}

        <ul className="comment-list">
          {comments.map((comment) => (
            <li key={comment.id} className="comment-item">
              <div className="comment-meta">
                <span>{comment.writerName || comment.writerId || `#${comment.writerUid}`}</span>
                <span>{formatDate(comment.regDate)}</span>
              </div>
              <p className="comment-body">{comment.content}</p>
            </li>
          ))}
        </ul>
      </section>
    </main>
  );
}
