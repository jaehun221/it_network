package com.itnetwork.club_community.domain.board;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public BoardListResponse getBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<Board> items = boardService.findPage(page, size);
        long total = boardService.count();
        List<BoardDto> dtoItems = items.stream()
                .map(BoardDto::from)
                .collect(Collectors.toList());
        return new BoardListResponse(dtoItems, total, page, size);
    }

    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable Long id) {
        return BoardDto.from(boardService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BoardDto> createBoard(@RequestBody @Valid BoardCreateRequest request) {
        Board board = boardService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BoardDto.from(board));
    }
}
