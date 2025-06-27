package com.example.prj2.member.service;

import com.example.prj2.board.entity.Board;
import com.example.prj2.board.repository.BoardRepository;
import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
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
    private final BoardRepository boardRepo;

    /*
        회원가입 처리
     */
    public void create(MemberFormDto inputData) {
        Optional<Member> db = memberRepo.findById(inputData.getId());
        if (db.isEmpty()) {
            Optional<Member> byNickname = memberRepo.findByNickname(inputData.getNickname());
            if (byNickname.isEmpty()) {
                Member member = new Member();
                member.setId(inputData.getId());
                member.setPassword(inputData.getPassword());
                member.setNickname(inputData.getNickname());
                member.setInfo(inputData.getInfo());

                memberRepo.save(member);
            } else {
                // 닉네임 중복
                throw new DuplicateKeyException(inputData.getNickname() + "는 이미 존재하는 닉네임입니다.");
            }
        } else {
            // 아이디 중복
            throw new DuplicateKeyException(inputData.getId() + "는 이미 존재하는 아이디입니다.");
        }
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
    public boolean update(MemberFormDto inputData, MemberDetailDto user, HttpSession session) {
        if (user != null) {
            // 수정(update) 대상 데이터 조회 후, save(update)
            Member member = memberRepo.findById(inputData.getId()).get();
            if (member.getId().equals(user.getId())) {
                if (member.getPassword().equals(inputData.getPassword())) {
                    // 수정 정보 업데이트
                    //                member.setId(inputData.getId());
                    member.setNickname(inputData.getNickname());
                    member.setInfo(inputData.getInfo());
                    memberRepo.save(member);

                    // 게시글 작성자 변경된 닉네임으로 update
                    boardRepo.updateWriterFromMemberById(member.getId());

                    // 세션 set
                    addUserToSession(session, member);
                    return true;
                }
            }
        }

        return false;
    }

    /*
        세션 추가 - 사용자 정보
     */
    private static void addUserToSession(HttpSession session, Member member) {
        MemberDetailDto dto = new MemberDetailDto();
        dto.setId(member.getId());
        dto.setNickname(member.getNickname());
        dto.setInfo(member.getInfo());
        dto.setCreatedAt(member.getCreatedAt());
        session.setAttribute("accessUser", dto);
    }

    /*
        비밀번호 변경 처리
     */
    public boolean changePassword(String id, String oldPassword, String newPassword) {
        // 대상 데이터 조회, 기존 암호 일치 확인 후, save(update)
        Member member = memberRepo.findById(id).get();
        String dbPassword = member.getPassword();
        if (dbPassword.equals(oldPassword)) {
            member.setPassword(newPassword);
            memberRepo.save(member);
            return true;
        } else {
            return false;
        }
    }

    /*
        회원 탈퇴 처리
     */
    public boolean delete(String id, String password, MemberDetailDto user) {
        if (user != null) {
            // 대상 데이터 조회, 기존 암호 일치 확인 후, delete
            Member member = memberRepo.findById(id).get();
            if (member.getId().equals(user.getId())) {
                String dbPassword = member.getPassword();
                if (dbPassword.equals(password)) {
                    // 삭제 대상 회원의 작성한 게시글 삭제
                    boardRepo.deleteBoardById(member);

                    // 회원 삭제
                    memberRepo.deleteById(id);

                    return true;
                }
            }
        }

        return false;
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
                addUserToSession(session, member);
                return true;
            }
        }

        return false;
    }
}
