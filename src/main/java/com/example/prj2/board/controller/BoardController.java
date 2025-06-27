package com.example.prj2.board.controller;

import com.example.prj2.board.dto.BoardDetailDto;
import com.example.prj2.board.dto.BoardFormDto;
import com.example.prj2.board.dto.BoardListInfo;
import com.example.prj2.board.service.BoardService;
import com.example.prj2.member.dto.MemberDetailDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

    private final BoardService boardServ;

    /*
        게시글 등록 / 입력 폼 / GET
     */
    @GetMapping("write")
    public String writeForm(HttpSession session, RedirectAttributes rttr,
                            @SessionAttribute(name = "accessUser", required = false) MemberDetailDto user
    ) {
        if (user == null) {
            rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", "로그인 후 글을 작성해주세요."));
            return "redirect:/member/login";
        }
        return "board/write";
    }

    /*
        게시글 등록 / 처리 / POST
     */
    @PostMapping("write")
    public String writeProc(BoardFormDto inputData, RedirectAttributes rttr,
                            @SessionAttribute(name = "accessUser", required = false) MemberDetailDto user
    ) {
        if (user != null) {
            boardServ.write(inputData, user);
            return "redirect:/board/list";
        } else {
            rttr.addFlashAttribute("alert", Map.of("code", "warning", "message", "로그인 후 글을 작성해주세요."));
            return "redirect:/member/login";
        }

    }

    /*
        게시글 목록 / 화면 / GET
     */
    @GetMapping("list")
    public String listView(Model model,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "") String keyword) {
        Map<String, Object> resultMap = boardServ.getList(page, keyword);

        model.addAllAttributes(resultMap);
        return "board/list";
    }

    /*
        게시글 상세 조회 / 화면 / GET
     */
    @GetMapping("detail")
    public String detailView(Model model, Integer seq) {
        BoardDetailDto result = boardServ.getDetail(seq);
        model.addAttribute("board", result);

        return "board/detail";
    }

    /*
        게시글 수정 / 입력 폼 / GET
     */
    @GetMapping("update")
    public String updateForm(Model model, Integer seq) {
        BoardDetailDto result = boardServ.getDetail(seq);
        model.addAttribute("board", result);
        return "board/update";
    }

    /*
        게시글 수정 / 처리 / POST
     */
    @PostMapping("update")
    public String updateProc(BoardDetailDto inputData, RedirectAttributes rttr,
                             @SessionAttribute(name = "accessUser", required = false) MemberDetailDto user
    ) {
        boolean result = boardServ.update(inputData, user);

        if (result) {
            rttr.addFlashAttribute("alert", Map.of("code", "success", "message", inputData.getSeq() + "번 게시물이 수정되었습니다."));
        } else {
            rttr.addFlashAttribute("alert", Map.of("code", "danger", "message", inputData.getSeq() + "번 게시물이 수정되지 않았습니다."));
        }

        rttr.addAttribute("seq", inputData.getSeq());

        return "redirect:/board/detail";
    }

    /*
        게시글 삭제 / 처리 / POST
     */
    @PostMapping("delete")
    public String deleteProc(Integer seq, RedirectAttributes rttr,
                             @SessionAttribute(name = "accessUser", required = false) MemberDetailDto user
    ) {
        boolean result = boardServ.delete(seq, user);

        if (result) {
            rttr.addFlashAttribute("alert",
                    Map.of("code", "danger", "message", seq + "번 게시물이 삭제 되었습니다."));

            return "redirect:/board/list";
        } else {
            rttr.addFlashAttribute("alert",
                    Map.of("code", "danger", "message", seq + "번 게시물이 삭제되지 않았습니다."));
            rttr.addAttribute("id", seq);
            return "redirect:/board/detail";
        }

    }

}
