package com.example.prj2.board.service;

import com.example.prj2.board.dto.BoardDetailDto;
import com.example.prj2.board.dto.BoardFormDto;
import com.example.prj2.board.dto.BoardListInfo;
import com.example.prj2.board.entity.Board;
import com.example.prj2.board.repository.BoardRepository;
import com.example.prj2.member.dto.MemberDetailDto;
import com.example.prj2.member.entity.Member;
import com.example.prj2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepo;
    private final MemberRepository memberRepo;

    /*
        게시물 등록 처리
     */
    public void write(BoardFormDto inputData, MemberDetailDto user) {
        Board board = new Board();
        board.setTitle(inputData.getTitle());
        board.setContent(inputData.getContent());

        Member member = memberRepo.findById(user.getId()).get();
        board.setId(member);
        board.setWriter(member.getNickname());

        boardRepo.save(board);
    }

    /*
        게시글 목록 조회
     */
    public Map<String, Object> getList(Integer page, String keyword) {
        int pageSize = 10;

        Page<BoardListInfo> boardPage = null;

        if (keyword == null || keyword.isBlank()) {
            boardPage = boardRepo.findAllBy(
                    PageRequest.of(page - 1, pageSize, Sort.by("seq").descending())
            );
        } else {
            boardPage = boardRepo.findSearchByKeyword(
                    "%" + keyword + "%",
                    PageRequest.of(page - 1, pageSize, Sort.by("seq").descending())
            );
        }

        List<BoardListInfo> boardList = boardPage.getContent();

        Integer totalPage = boardPage.getTotalPages();

        Integer rightPageNo = ((page - 1) / 10 + 1) * 10;
        Integer leftPageNo = rightPageNo - 9;
        rightPageNo = Math.min(rightPageNo, totalPage);

        Map<String, Object> resultMap = Map.of(
                "boardList", boardList,
                "totalPageCount", totalPage,
                "totalElements", boardPage.getTotalElements(),
                "rightPageNumber", rightPageNo,
                "leftPageNumber", leftPageNo,
                "currentPage", page);

        return resultMap;
    }

    /*
        게시글 상세 조회
     */
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

    /*
        게시글 수정 처리
     */
    public boolean update(BoardDetailDto inputData, MemberDetailDto user) {
        if (user != null) {
//            Member member = memberRepo.findById(inputData.getWriter()).get();
            Board board = boardRepo.findById(inputData.getSeq()).get();
            if (board.getId().getId().equals(user.getId())) {
                board.setTitle(inputData.getTitle());
                board.setContent(inputData.getContent());
//                board.setId(member);
                boardRepo.save(board);

                return true;
            }
        }
        return false;
    }

    /*
        게시글 삭제 처리
     */
    public boolean delete(Integer seq, MemberDetailDto user) {
        if (user != null) {
            Member member = boardRepo.findById(seq).get().getId();
            if (member.getId().equals(user.getId())) {
                boardRepo.deleteById(seq);
                return true;
            }
        }
        return false;
    }
}
