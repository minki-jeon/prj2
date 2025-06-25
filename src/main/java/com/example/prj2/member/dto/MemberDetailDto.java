package com.example.prj2.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDetailDto {
    private String id;
    private String nickname;
    private String info;
    private LocalDateTime createdAt;
}
