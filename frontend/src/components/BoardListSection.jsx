import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import "../css/board_list.css";

const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9999";

const buildApiUrl = (path) => (path.startsWith("http") ? path : `${API_BASE_URL}${path}`);

export default function BoardListSection({
  title,
  className,
  hidePagination = false,
  size = 10,
  refreshSignal,
}) {
  const [items, setItems] = useState([]);
  const [page, setPage] = useState(0);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;
    setLoading(true);
    setError("");

    fetch(buildApiUrl(`/boards?page=${page}&size=${size}`))
      .then((res) => {
        if (!res.ok) {
          throw new Error("게시글 목록을 불러오는 데 실패했습니다.");
        }
        return res.json();
      })
      .then((data) => {
        if (!active) return;
        setItems(data.items ?? []);
        setTotal(data.total ?? 0);
      })
      .catch((err) => {
        if (active) {
          setError(err.message ?? "알 수 없는 오류가 발생했습니다.");
        }
      })
      .finally(() => {
        if (active) {
          setLoading(false);
        }
      });

    return () => {
      active = false;
    };
  }, [page, size, refreshSignal]);

  const totalPages = useMemo(() => Math.max(1, Math.ceil(total / size)), [total, size]);

  useEffect(() => {
    if (page >= totalPages) {
      setPage(Math.max(0, totalPages - 1));
    }
  }, [page, totalPages]);

  const resetPageIfEmpty = items.length === 0 && total > 0 && page > 0;
  useEffect(() => {
    if (resetPageIfEmpty) {
      setPage(Math.max(0, totalPages - 1));
    }
  }, [resetPageIfEmpty, totalPages]);

  const containerClass = ["board-list-widget", className].filter(Boolean).join(" ");
  const canPrevious = page > 0;
  const canNext = page + 1 < totalPages;

  return (
    <div className={containerClass}>
      {title && (
        <div className="board-list-heading">
          <h3>{title}</h3>
        </div>
      )}

      {error && <p className="board-error">{error}</p>}

      <ul className="board-list">
        {loading && (
          <li className="board-empty">불러오는 중입니다…</li>
        )}
        {!loading && items.length === 0 && (
          <li className="board-empty">게시글이 없습니다.</li>
        )}
        {items.map((item) => (
          <li key={item.id}>
            <Link className="board-item" to={`/board/${item.id}`}>
              <span className="board-item-id">#{item.id}</span>
              <span className="board-item-title">{item.title}</span>
              <span className="board-item-date">
                {new Date(item.createdAt).toLocaleString("ko-KR", {
                  dateStyle: "medium",
                  timeStyle: "short",
                })}
              </span>
            </Link>
          </li>
        ))}
      </ul>

      {!hidePagination && (
        <div className="board-pagination">
          <button onClick={() => setPage((p) => Math.max(0, p - 1))} disabled={!canPrevious}>
            이전
          </button>
          <span>
            {page + 1} / {totalPages}
          </span>
          <button onClick={() => setPage((p) => (canNext ? p + 1 : p))} disabled={!canNext}>
            다음
          </button>
        </div>
      )}
    </div>
  );
}
