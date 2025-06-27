package com.example.prj2.member.repository;

import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Page<MemberListInfo> findAllBy(PageRequest id);

    Optional<Member> findByNickname(String nickname);
}