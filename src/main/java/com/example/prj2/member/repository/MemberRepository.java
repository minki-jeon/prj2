package com.example.prj2.member.repository;

import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    List<MemberListInfo> findAllByOrderByCreatedAtDesc();
}