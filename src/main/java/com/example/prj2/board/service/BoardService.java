package com.example.prj2.board.service;

import com.example.prj2.board.dto.BoardFormDto;
import com.example.prj2.board.entity.Board;
import com.example.prj2.board.repository.BoardRepository;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
