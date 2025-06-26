package com.example.prj2.board.service;

import com.example.prj2.board.dto.BoardDetailDto;
import com.example.prj2.board.dto.BoardFormDto;
import com.example.prj2.board.dto.BoardListInfo;
import com.example.prj2.board.entity.Board;
import com.example.prj2.board.repository.BoardRepository;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepo;
    private final MemberRepository memberRepo;

    /*
        게시물 등록 처리
     */
    public void write(BoardFormDto inputData) {
        Board board = new Board();
        board.setTitle(inputData.getTitle());
        board.setContent(inputData.getContent());
        board.setWriter(inputData.getWriter());

        // TODO : getSession.id
        Member member = memberRepo.findById(inputData.getWriter()).get();
        board.setId(member);

        boardRepo.save(board);
    }


    public List<BoardListInfo> getList() {
        List<BoardListInfo> resultList = boardRepo.findAllByOrderBySeqDesc();

        return resultList;
    }

    public BoardDetailDto getDetail(Integer seq) {
        Board board = boardRepo.findById(seq).get();
        BoardDetailDto dto = new BoardDetailDto();
        dto.setSeq(board.getSeq());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setWriter(board.getWriter());
        dto.setCreatedAt(board.getCreatedAt());
        dto.setId(board.getId().toString());

        return dto;
    }
}
