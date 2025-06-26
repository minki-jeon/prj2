package com.example.prj2.board.controller;

import com.example.prj2.board.dto.BoardFormDto;
import com.example.prj2.board.dto.BoardListInfo;
import com.example.prj2.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

        return "board/write";
    }

    /*
        게시글 목록 / 화면 / GET
     */
    @GetMapping("list")
    public String listView(Model model) {
        List<BoardListInfo> resultList = boardServ.getList();

        // TODO : Paging
        // TODO : Searching

        model.addAttribute("boardList", resultList);
        return "board/list";
    }

    /*
        게시글 상세 조회 / 화면 / GET
     */
    @GetMapping("detail")
    public String detailView(Model model, Integer seq) {

        // TODO

        return "board/detail";
    }

}
