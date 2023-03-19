package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired  //  의존성주입(DI)
    private BoardRepository boardRepository;


    // 게시글 작성하기 CREATE
    public void write(Board board, MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID(); // UUID 랜덤생성

        String fileName = uuid + "_" + file.getOriginalFilename(); // uuid 붙여 파일 이름 생성

        File saveFile = new File(projectPath, fileName);
        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);

        boardRepository.save(board);

    }


    // 게시글 리스트 전부 가져오기  READ
    public Page<Board> boardList(Pageable pageable) {
        // List<Board> 객체에서 Page 객체로 변경하고 Pageable 함수를 사용하여 간단하게 페이징을 할 수 있다.

        return boardRepository.findAll(pageable);
        // DB의 전체 데이터 가져와서 출력 Board 타입이 담김.

    }


    // 검색기능 구현 !
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {

        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }



    // 특정 게시글 불러오기
    public Board boardview(Integer id) {

        return boardRepository.findById(id).get();
        // id(PK)값으로 특정 게시글을 찾아와 반환 !
        // findBy로 가져온 값은 Optional 형태이기 때문에 .get() 함수로 언래핑 작업을 해줘야 합니다.
    }



    // 특정 게시글 삭제
    public void boardDelete(Integer id) {

        boardRepository.deleteById(id);
    }

}
