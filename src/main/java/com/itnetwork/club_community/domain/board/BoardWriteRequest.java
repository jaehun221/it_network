package com.itnetwork.club_community.domain.board;

// 폼 데이터를 담아 옮기는 객체
public class BoardWriteRequest {

    private String title;
    private String content;

    // Getter/Setter
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
}