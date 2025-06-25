package com.example.prj2.member.service;

import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepo;

    /*
        회원가입 처리
     */
    public void create(MemberFormDto inputData) {
        // DTO to Entity
        Member member = new Member();
        member.setId(inputData.getId());
        member.setPassword(inputData.getPassword());
        member.setNickname(inputData.getNickname());
        member.setInfo(inputData.getInfo());
        // JPA Save
        memberRepo.save(member);
    }

    /*
        회원목록 조회
     */
    public List<MemberListInfo> list() {
        return memberRepo.findAllByOrderByCreatedAtDesc();
    }

    /*
        회원정보 조회
     */
    public MemberDetailDto detail(String id) {
        // Entity to Dto
        MemberDetailDto dto = new MemberDetailDto();
        memberRepo.findById(id).ifPresent(member -> {
            dto.setId(member.getId());
            dto.setNickname(member.getNickname());
            dto.setInfo(member.getInfo());
            dto.setCreatedAt(member.getCreatedAt());

        });

        return dto;
    }
}
