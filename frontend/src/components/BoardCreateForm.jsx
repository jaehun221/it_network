import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import "../css/board_list.css";

const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9999";

export default function BoardCreateForm({ onSuccess, onCancel }) {
  const { fetchWithAuth } = useAuth();
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");

    if (!title.trim() || !content.trim()) {
      setError("제목과 내용을 모두 입력해주세요.");
      return;
    }

    setSubmitting(true);
    try {
      const response = await fetchWithAuth({API_BASE_URL} + "/api/boards", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          title: title.trim(),
          content: content.trim(),
        }),
      });

      if (!response.ok) {
        const message = (await response.json().catch(() => null))?.message;
        throw new Error(message || "게시글 작성에 실패했습니다.");
      }

      setTitle("");
      setContent("");
      onSuccess?.();
    } catch (err) {
      setError(err.message || "알 수 없는 오류가 발생했습니다.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <form className="board-create-form" onSubmit={handleSubmit}>
      <div className="board-create-field">
        <label htmlFor="board-title">제목</label>
        <input
          id="board-title"
          type="text"
          value={title}
          onChange={(event) => setTitle(event.target.value)}
          placeholder="게시글 제목"
          maxLength={120}
        />
      </div>
      <div className="board-create-field">
        <label htmlFor="board-content">내용</label>
        <textarea
          id="board-content"
          value={content}
          onChange={(event) => setContent(event.target.value)}
          placeholder="내용을 입력하세요."
          rows={6}
        />
      </div>
      <div className="board-create-actions">
        <button type="button" onClick={onCancel} className="board-create-cancel">
          취소
        </button>
        <button type="submit" disabled={submitting}>
          {submitting ? "등록 중…" : "게시글 등록"}
        </button>
      </div>
      {error && <p className="board-create-error">{error}</p>}
    </form>
  );
}
