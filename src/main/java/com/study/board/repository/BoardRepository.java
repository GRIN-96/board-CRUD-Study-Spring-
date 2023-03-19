package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> { // 엔티티타입과 pk 타입 순서대로입력

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    // 자동 쿼리 생성. 컨테이너에 추가.
}
