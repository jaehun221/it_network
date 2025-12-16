import '../css/board_home.css'
import { useEffect, useState } from 'react';

export default function Board_Home() {
    const [items, setItems] = useState([]);
    const [page, setPage] = useState(0);
    const size = 10;
    const [total, setTotal] = useState(0);

    useEffect(() => {
        const fetchData = async () => {
            const res = await fetch(`/api/boards?page=${page}&size=${size}`);
            const data = await res.json();
            setItems(data.items ?? []);
            setTotal(data.total ?? 0);
        };
        fetchData();
    }, [page]);

    const totalPages = Math.ceil(total / size);

    return (
        <>
            <h1>최근 게시글</h1>
            <ul>
                {items.map((item) => (
                    <li key={item.id}>
                        <span className="board-id">#{item.id}</span>{' '}
                        <span className="board-title">{item.title}</span>
                    </li>
                ))}
                {items.length === 0 && <li>게시글이 없습니다.</li>}
            </ul>
            <div className="pagination">
                <button
                    onClick={() => setPage((p) => Math.max(0, p - 1))}
                    disabled={page === 0}
                >
                    이전
                </button>
                <span>
                    {page + 1} / {Math.max(1, totalPages)}
                </span>
                <button
                    onClick={() => setPage((p) => (p + 1 < totalPages ? p + 1 : p))}
                    disabled={page + 1 >= totalPages}
                >
                    다음
                </button>
            </div>
        </>
    )
}