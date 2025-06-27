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
            System.out.println("member.getId() = " + member.getId());
            System.out.println("user.getId() = " + user.getId());
            if (member.getId().equals(user.getId())) {
                model.addAttribute("member", member);

                return "member/detail";
            }
        }
        rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", " 권한이 없습니다."));

        return "redirect:/board/list";
    }

    /*
        회원 수정 / 입력 폼 / GET
     */
    @GetMapping("update")
    public String updateForm(Model model, String id) {
        MemberDetailDto member = memberServ.getDetail(id);
        model.addAttribute("member", member);

        return "member/update";
    }

    /*
        회원 수정 / 처리 / POST
     */
    @PostMapping("update")
    public String updateProc(MemberFormDto inputData, RedirectAttributes rttr) {
        memberServ.update(inputData);

        rttr.addAttribute("id", inputData.getId());
        return "redirect:/member/detail";
    }

    /*
        암호 변경 / 처리 / POST
     */
    @PostMapping("changePw")
    public String changePassword(String id, String oldPassword, String newPassword,
                                 RedirectAttributes rttr) {

        memberServ.changePassword(id, oldPassword, newPassword);
        rttr.addAttribute("id", id);
        return "redirect:/member/detail";
    }

    /*
        회원 탈퇴 / 처리 / POST
     */
    @PostMapping("delete")
    public String deleteProc(String id, String password) {
        memberServ.delete(id, password);

        return "redirect:/board/list";
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
    public String logout(HttpSession session) {
        session.invalidate();

        return "redirect:/board/list";
    }

}
