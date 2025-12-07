package com.itnetwork.club_community.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity //DB 테이블과 연결
public class Board {

    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 증가
    private Long id;

    private String title;
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now(); // 작성 시간 기록

    protected Board() {}
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter들 (데이터 접근용)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}