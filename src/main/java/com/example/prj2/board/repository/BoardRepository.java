package com.example.prj2.board.repository;

import com.example.prj2.board.dto.BoardListInfo;
import com.example.prj2.board.entity.Board;
import com.example.prj2.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<BoardListInfo> findAllBy(PageRequest seq);

    @Query("""
            SELECT b FROM Board b
            WHERE b.title LIKE :keyword
                    OR b.content LIKE :keyword
                    OR b.id.id LIKE :keyword
                    OR b.id.nickname LIKE :keyword
            """)
    Page<BoardListInfo> findSearchByKeyword(String keyword, PageRequest seq);

    void deleteBoardById(Member id);

    @Modifying
    @Query(value = """
            UPDATE board b
                JOIN member m ON b.id = m.id
                SET b.writer = m.nickname
                WHERE b.id = :id
            """, nativeQuery = true)
    void updateWriterFromMemberById(@Param("id") String id);
}