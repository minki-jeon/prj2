package com.example.prj2.member.controller;

import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
