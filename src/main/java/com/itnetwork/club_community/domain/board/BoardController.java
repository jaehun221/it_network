package com.itnetwork.club_community.domain.board;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Map;
import java.util.List;

@Controller //사용자 요청 처리
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService; // Service 연결
    }

    @GetMapping("/board/list") // 목록 조회 요청
    public String getBoardList(Model model) {
        model.addAttribute("list", boardService.findAll());
        return "boardList";
    }

    @GetMapping("/board/write-form") //글쓰기 폼 요청
    public String writeForm() {
        return "boardWrite";
    }

    @PostMapping("/board/write") // 게시글 저장 요청
    public String write(BoardWriteRequest request) {
        boardService.save(request);
        return "redirect:/board/list";
    }

    // 게시글 목록 JSON API (페이지네이션, 최근순)
    @GetMapping("/api/boards")
    @ResponseBody
    public Map<String, Object> getBoards(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<Board> items = boardService.findPage(page, size);
        long total = boardService.count();
        return Map.of(
            "items", items,
            "total", total,
            "page", page,
            "size", size
        );
    }
}
