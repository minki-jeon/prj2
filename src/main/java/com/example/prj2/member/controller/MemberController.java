package com.example.prj2.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {

    /*
        회원가입 FORM / GET
     */
    @GetMapping("signup")
    public String signupForm() {
        return "member/signup";
    }

    /*
        회원가입 Process / POST
     */
    @PostMapping("signup")
    public String signupProc() {


        return "redirect:/member/signup";
    }
}
