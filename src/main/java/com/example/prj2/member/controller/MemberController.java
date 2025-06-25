package com.example.prj2.member.controller;

import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.dto.MemberFormDto;
import com.example.prj2.member.dto.MemberListInfo;
import com.example.prj2.member.service.MemberService;
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


        return "redirect:/member/list";
    }

    /*
        회원 목록 / GET
     */
    @GetMapping("list")
    public String list(Model model) {
        List<MemberListInfo> resultList = memberServ.getList();
        System.out.println(resultList);
        model.addAttribute("memberList", resultList);
        return "member/list";
    }

    /*
        회원 상세 / GET
     */
    @GetMapping("detail")
    public String detail(Model model, String id) {
        MemberDetailDto member = memberServ.getDetail(id);
        model.addAttribute("member", member);

        return "member/detail";
    }

    /*
        회원 수정 / GET
     */
    @GetMapping("update")
    public String updateForm(Model model, String id) {
        MemberDetailDto member = memberServ.getDetail(id);
        model.addAttribute("member", member);

        return "member/update";
    }

    /*
        회원 수정 처리 / POST
     */
    @PostMapping("update")
    public String updateProc(MemberFormDto inputData, RedirectAttributes rttr) {
        memberServ.update(inputData);

        rttr.addAttribute("id", inputData.getId());
        return "redirect:/member/detail";
    }

    /*
        암호 변경 처리 / POST
     */
    // TODO changePassword


}
