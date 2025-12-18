import { useEffect, useMemo, useState } from "react";
import "./TextType.css";

/**
 * TextType: typewriter effect
 * props:
 * - texts: string[] (여러 개면 순환)
 * - typingMs: number (타이핑 속도)
 * - deletingMs: number (삭제 속도)
 * - pauseMs: number (완성 후 멈춤)
 * - loop: boolean
 * - cursor: boolean
 */
export default function TextType({
  texts = ["Welcome!"],
  typingMs = 80,
  deletingMs = 40,
  pauseMs = 1200,
  loop = true,
  cursor = true,
  cursorChar = '_',
  className = "",
  play = true, // 추가: 화면 도달 시 시작 제어
}) {
  const list = useMemo(() => (Array.isArray(texts) ? texts : [String(texts)]), [texts]);

  const [index, setIndex] = useState(0);
  const [text, setText] = useState("");
  const [isDeleting, setIsDeleting] = useState(false);

  useEffect(() => {
    if (!play) return; // 추가: 아직 시작하지 않음
    const full = list[index] ?? "";
    let timer;

    // 완료 후 pause
    if (!isDeleting && text === full) {
      if (!loop && index === list.length - 1) return; // 마지막에서 정지
      timer = setTimeout(() => setIsDeleting(true), pauseMs);
      return () => clearTimeout(timer);
    }

    // 다 지웠으면 다음 문장
    if (isDeleting && text === "") {
      setIsDeleting(false);
      setIndex((prev) => (prev + 1) % list.length);
      return;
    }

    timer = setTimeout(() => {
      const next = isDeleting
        ? full.slice(0, Math.max(0, text.length - 1))
        : full.slice(0, text.length + 1);
      setText(next);
    }, isDeleting ? deletingMs : typingMs);

    return () => clearTimeout(timer);
  }, [text, isDeleting, index, list, typingMs, deletingMs, pauseMs, loop, play]); // play 의존성 추가

  return (
    <span className={`texttype ${className}`}>
      <span className="texttype__text">{text}</span>
      {cursor && <span className="texttype__cursor" aria-hidden="true">{cursorChar}</span>}
    </span>
  );
}
