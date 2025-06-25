package com.example.prj2.member.controller;

import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    private final MemberService memberServ;

    /*
        회원가입 양식 / GET
     */
    @GetMapping("signup")
    public String signupForm() {
        return "member/signup";
    }

    /*
        회원가입 처리 / POST
     */
    @PostMapping("signup")
    public String signupProc(MemberFormDto inputData) {
        memberServ.create(inputData);


        return "redirect:/member/signup";
    }

    /*
        회원 목록 / GET
     */
    @GetMapping("list")
    public String list(Model model) {
        List<MemberListInfo> resultList = memberServ.list();
        System.out.println(resultList);
        model.addAttribute("memberList", resultList);
        return "member/list";
    }
}
