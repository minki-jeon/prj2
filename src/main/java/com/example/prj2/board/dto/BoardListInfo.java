package com.example.prj2.board.dto;

import java.time.LocalDateTime;

public interface BoardListInfo {
    Integer getSeq();

    String getTitle();

    String getWriter();

    LocalDateTime getCreatedAt();
}
