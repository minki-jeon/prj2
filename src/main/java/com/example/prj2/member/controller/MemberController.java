package com.example.prj2.member.controller;

import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    private final MemberService memberServ;

    /*
        회원가입 / 입력 폼 / GET
     */
    @GetMapping("signup")
    public String signupForm() {
        return "member/signup";
    }

    /*
        회원가입 / 처리 / POST
     */
    @PostMapping("signup")
    public String signupProc(MemberFormDto inputData, RedirectAttributes rttr) {
        try {
            memberServ.create(inputData);
            rttr.addFlashAttribute("alert", Map.of("code", "success", "message", "회원가입이 완료되었습니다."));

            return "redirect:/board/list";
        } catch (DuplicateKeyException e) {
            rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", e.getMessage()));

            rttr.addFlashAttribute("member", inputData);

            return "redirect:/member/signup";
        }
    }

    /*
        회원 목록 / 화면 / GET
     */
    @GetMapping("list")
    public String listView(Model model,
                           @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> resultMap = memberServ.getList(page);

        model.addAllAttributes(resultMap);
        return "member/list";
    }

    /*
        회원 상세 / 화면 / GET
     */
    @GetMapping("detail")
    public String detailView(Model model, String id,
                             @SessionAttribute(value = "accessUser", required = false) MemberDetailDto user,
                             RedirectAttributes rttr) {

        if (user != null) {
            MemberDetailDto member = memberServ.getDetail(id);
            if (member.getId().equals(user.getId())) {
                model.addAttribute("member", member);

                return "member/detail";
            }
        }
        rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", " 권한이 없습니다."));

        return "redirect:/member/list";
    }

    /*
        회원 수정 / 입력 폼 / GET
     */
    @GetMapping("update")
    public String updateForm(Model model, String id,
                             @SessionAttribute(value = "accessUser", required = false) MemberDetailDto user,
                             RedirectAttributes rttr) {
        if (user != null) {
            MemberDetailDto member = memberServ.getDetail(id);
            if (member.getId().equals(user.getId())) {
                model.addAttribute("member", member);
                return "member/update";
            }
        }
        rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", " 권한이 없습니다."));

        return "redirect:/member/list";
    }

    /*
        회원 수정 / 처리 / POST
     */
    @PostMapping("update")
    public String updateProc(MemberFormDto inputData,
                             RedirectAttributes rttr,
                             HttpSession session,
                             @SessionAttribute(value = "accessUser", required = false) MemberDetailDto user
    ) {
        boolean result = memberServ.update(inputData, user, session);
        rttr.addAttribute("id", inputData.getId());
        if (result) {
            rttr.addFlashAttribute("alert", Map.of("code", "success", "message", "회원 정보가 변경되었습니다."));

            return "redirect:/member/detail";
        } else {
            rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", "암호가 일치하지 않습니다."));

            return "redirect:/member/update";
        }
    }

    /*
        암호 변경 / 처리 / POST
     */
    @PostMapping("changePw")
    public String changePassword(String id, String oldPassword, String newPassword,
                                 RedirectAttributes rttr,
                                 @SessionAttribute(value = "accessUser", required = false) MemberDetailDto user
    ) {
        if (user != null && user.getId().equals(id)) {
            boolean result = memberServ.changePassword(id, oldPassword, newPassword);

            if (result) {
                rttr.addFlashAttribute("alert", Map.of("code", "success", "message", "암호가 변경되었습니다."));
            } else {
                rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", "암호가 일치하지 않습니다."));
            }
        }
        rttr.addAttribute("id", id);
        return "redirect:/member/detail";
    }

    /*
        회원 탈퇴 / 처리 / POST
     */
    @PostMapping("delete")
    public String deleteProc(String id, String password,
                             RedirectAttributes rttr,
                             HttpSession session,
                             @SessionAttribute(value = "accessUser", required = false) MemberDetailDto user
    ) {
        boolean result = memberServ.delete(id, password, user);
        if (result) {
            rttr.addFlashAttribute("alert", Map.of("code", "success", "message", id + " 님 탈퇴가 완료되었습니다."));

            // 탈퇴 후 로그아웃 처리
            session.invalidate();
            return "redirect:/board/list";
        } else {
            rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", "암호가 일치하지 않습니다."));
            rttr.addAttribute("id", id);
            return "redirect:/board/list";
        }
    }

    /*
        회원 로그인 / 입력 폼 / GET
     */
    @GetMapping("login")
    public String loginForm() {
        return "member/login";
    }

    /*
        회원 로그인 / 처리 / POST
     */
    @PostMapping("login")
    public String loginProc(String id, String password, HttpSession session, RedirectAttributes rttr) {
        boolean access = memberServ.login(id, password, session);

        if (access) {
            rttr.addFlashAttribute("alert", Map.of("code", "success", "message", "로그인되었습니다."));

            return "redirect:/board/list";
        } else {
            rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", "아이디/패스워드가 일치하지 않습니다."));
            rttr.addFlashAttribute("id", id);

            return "redirect:/member/login";
        }
    }

    /*
        회원 로그아웃 / 처리
     */
    @RequestMapping("logout")
    public String logout(HttpSession session, RedirectAttributes rttr) {
        session.invalidate();

        rttr.addFlashAttribute("alert", Map.of("code", "success", "message", "로그아웃되었습니다."));

        return "redirect:/board/list";
    }

}
