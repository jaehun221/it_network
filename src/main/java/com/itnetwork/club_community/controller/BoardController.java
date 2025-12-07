package com.itnetwork.club_community.controller;

import com.itnetwork.club_community.dto.BoardWriteRequest;
import com.itnetwork.club_community.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // 사용자 요청 처리
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

    @GetMapping("/board/write-form") // 글쓰기 폼 요청
    public String writeForm() {
        return "boardWrite";
    }

    @PostMapping("/board/write") // 게시글 저장 요청
    public String write(BoardWriteRequest request) {
        boardService.save(request);
        return "redirect:/board/list";
    }
}