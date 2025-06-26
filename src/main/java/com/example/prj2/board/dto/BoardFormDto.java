package com.example.prj2.board.dto;

import com.example.prj2.member.dto.MemberFormDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardFormDto {
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
    private MemberFormDto member;

}
