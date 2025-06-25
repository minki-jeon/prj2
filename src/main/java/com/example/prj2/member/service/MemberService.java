package com.example.prj2.member.service;

import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
