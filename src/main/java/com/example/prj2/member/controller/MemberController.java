package com.example.prj2.member.controller;

import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String signupProc(MemberFormDto inputData) {
        memberServ.create(inputData);


        return "redirect:/member/list";
    }

    /*
        회원 목록 / 화면 / GET
     */
    @GetMapping("list")
    public String listView(Model model) {
        List<MemberListInfo> resultList = memberServ.getList();
        System.out.println(resultList);
        model.addAttribute("memberList", resultList);
        return "member/list";
    }

    /*
        회원 상세 / 화면 / GET
     */
    @GetMapping("detail")
    public String detailView(Model model, String id) {
        MemberDetailDto member = memberServ.getDetail(id);
        model.addAttribute("member", member);

        return "member/detail";
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

        return "redirect:/member/list";
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
            return "redirect:/member/list";
        } else {
            rttr.addAttribute("id", id);
            return "redirect:/member/login";
        }

    }
}
