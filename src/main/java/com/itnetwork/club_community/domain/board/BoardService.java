package com.itnetwork.club_community.domain.board;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Comparator;

@Service //비즈니스 로직 담당
@Transactional(readOnly = true) // 기본 읽기 전용
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional // 저장 로직
    public void save(BoardWriteRequest request) {
        Board board = new Board(request.getTitle(), request.getContent());
        boardRepository.save(board);
    }

    public List<Board> findAll() { // 조회 로직
        return boardRepository.findAll();
    }

    // 최근 게시글 기준 페이지네이션 조회 (createdAt 내림차순)
    public List<Board> findPage(int page, int size) {
        List<Board> all = boardRepository.findAll();
        all.sort(Comparator.comparing(Board::getCreatedAt).reversed());
        int from = Math.max(0, page * size);
        int to = Math.min(all.size(), from + size);
        if (from >= all.size()) return List.of();
        return all.subList(from, to);
    }

    // 전체 게시글 수 반환
    public long count() {
        return boardRepository.findAll().size();
    }
}