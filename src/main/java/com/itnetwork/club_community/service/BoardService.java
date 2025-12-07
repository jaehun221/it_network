package com.itnetwork.club_community.service;

import com.itnetwork.club_community.domain.Board;
import com.itnetwork.club_community.dto.BoardWriteRequest;
import com.itnetwork.club_community.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
}