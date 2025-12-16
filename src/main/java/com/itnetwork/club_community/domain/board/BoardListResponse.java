package com.itnetwork.club_community.domain.board;

import java.util.List;

public class BoardListResponse {

    private final List<BoardDto> items;
    private final long total;
    private final int page;
    private final int size;

    public BoardListResponse(List<BoardDto> items, long total, int page, int size) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
    }

    public List<BoardDto> getItems() {
        return items;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
