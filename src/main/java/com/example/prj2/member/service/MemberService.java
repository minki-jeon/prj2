package com.example.prj2.member.service;

import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepo;

    /*
        회원가입 처리
     */
    public void create(MemberFormDto inputData) {
        // TODO : ID중복체크 + 닉네임중복체크

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
    public Map<String, Object> getList(Integer page) {
        int pageSize = 10;

        Page<MemberListInfo> memberPage = memberRepo.findAllBy(
                PageRequest.of(page - 1, pageSize, Sort.by("id").descending())
        );
        List<MemberListInfo> memberList = memberPage.getContent();

        Integer totalPage = memberPage.getTotalPages();

        Integer rightPageNo = ((page - 1) / 10 + 1) * 10;
        Integer leftPageNo = rightPageNo - 9;
        rightPageNo = Math.min(rightPageNo, totalPage);

        Map<String, Object> resultMap = Map.of(
                "memberList", memberList,
                "totalPageCount", totalPage,
                "totalElements", memberPage.getTotalElements(),
                "rightPageNumber", rightPageNo,
                "leftPageNumber", leftPageNo,
                "currentPage", page);

        return resultMap;
    }

    /*
        회원정보 조회
     */
    public MemberDetailDto getDetail(String id) {
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

    /*
        회원정보 수정 처리
     */
    public void update(MemberFormDto inputData) {
        // 수정(update) 대상 데이터 조회 후, save(update)
        memberRepo.findById(inputData.getId()).ifPresent(member -> {
            // TODO : 암호가 일치하지 않을 때 처리

            member.setId(inputData.getId());
            member.setNickname(inputData.getNickname());
            member.setInfo(inputData.getInfo());
            memberRepo.save(member);
        });
    }

    /*
        비밀번호 변경 처리
     */
    public void changePassword(String id, String oldPassword, String newPassword) {
        // 대상 데이터 조회, 기존 암호 일치 확인 후, save(update)
        Member member = memberRepo.findById(id).get();
        String dbPassword = member.getPassword();
        if (dbPassword.equals(oldPassword)) {
            member.setPassword(newPassword);
            memberRepo.save(member);
        } else {
            // TODO : 기존 암호가 일치하지 않을 때 처리
        }
    }

    /*
        회원 탈퇴 처리
     */
    public void delete(String id, String password) {
        // 대상 데이터 조회, 기존 암호 일치 확인 후, delete
        Member member = memberRepo.findById(id).get();
        String dbPassword = member.getPassword();

        if (dbPassword.equals(password)) {
            memberRepo.deleteById(id);
        } else {
            // TODO : 기존 암호가 일치하지 않을 때 처리
        }

    }

    /*
        로그인 처리
     */
    public boolean login(String id, String password, HttpSession session) {

        Optional<Member> db = memberRepo.findById(id);
        if (db.isPresent()) {
            Member member = db.get();
            String dbPassword = member.getPassword();
            if (dbPassword.equals(password)) {
                // 로그인 사용자 정보 (DTO) 세션 등록
                MemberDetailDto dto = new MemberDetailDto();
                dto.setId(member.getId());
                dto.setNickname(member.getNickname());
                dto.setInfo(member.getInfo());
                dto.setCreatedAt(member.getCreatedAt());
                session.setAttribute("accessUser", dto);
                return true;
            }
        }

        return false;
    }
}
