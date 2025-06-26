package com.example.prj2.board.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDetailDto {
    private Integer seq;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private String id;
}
