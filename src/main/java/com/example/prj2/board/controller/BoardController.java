package com.example.prj2.board.controller;

import com.example.prj2.board.dto.BoardDetailDto;
import com.example.prj2.board.dto.BoardFormDto;
import com.example.prj2.board.dto.BoardListInfo;
import com.example.prj2.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String writeForm() {
        return "board/write";
    }

    /*
        게시글 등록 / 처리 / POST
     */
    @PostMapping("write")
    public String writeProc(BoardFormDto inputData) {
        boardServ.write(inputData);

        return "redirect:/board/write";
    }

    /*
        게시글 목록 / 화면 / GET
     */
    @GetMapping("list")
    public String listView(Model model,
                           @RequestParam(defaultValue = "1") Integer page) {
        Map<String, Object> resultMap = boardServ.getList(page);

        // TODO : Searching

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
    public String updateProc(BoardDetailDto inputData, RedirectAttributes rttr) {
        boardServ.update(inputData);
        rttr.addAttribute("seq", inputData.getSeq());

        return "redirect:/board/detail";
    }

    /*
        게시글 삭제 / 처리 / POST
     */
    @PostMapping("delete")
    public String deleteProc(Integer seq) {
        boardServ.delete(seq);


        return "redirect:/board/list";
    }

}
