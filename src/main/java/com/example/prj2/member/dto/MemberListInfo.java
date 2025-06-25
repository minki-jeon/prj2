package com.example.prj2.member.dto;

import java.time.LocalDateTime;

public interface MemberListInfo {
    String getId();

    String getNickname();

    LocalDateTime getCreatedAt();
}
