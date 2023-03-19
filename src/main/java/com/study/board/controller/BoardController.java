package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {


    // 서비스 DI
    @Autowired
    private BoardService boardService;


    // 게시글 작성 페이지 생성
    @GetMapping("/board/write")  // localhost:8080/board/write
    public String boardWriteForm() {

        return "boardwrite";
    }

    // 게시글 작성 완료 후 페이지.
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {

//        System.out.println("제목 : " + title);
//        System.out.println("내용 : " + content);
//        System.out.println(board.getContent());
        boardService.write(board, file);



        model.addAttribute("message", "글 작성이 완료되었습니다.");

//        model.addAttribute("message", "글 작성이 실패하였습니다.");
        // 위 실패 조건을 사용할 경우 if문으로 조건을 넣어서 사용 가능합니다.

        // 위 작업 끝난 후 페이지 이동.
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }


    // db 내의 전체 데이터 가져와 출력하기. READ
    @GetMapping("/board/list")
    public String boardList(Model model,  // ui를 구현하는 model 객체 ( View )
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board> list= boardService.boardList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;   // 현재 페이지
        int startPage = Math.max(nowPage - 4, 1);  // 페이지 네이션에 보이는 시작 페이지
        int endPage = Math.min(nowPage + 5 , list.getTotalPages()); // 페이지 네이션 끝에 보이는 마지막 페이지

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list", boardService.boardList(pageable));
        // view에 데이터를 담아 출력할 때  model 객체를 이용하여 속성값을 담아 전달.

        return "boardlist";   // boardlist.html에 매핑되어 화면 출력 !
    }


    // 상세페이지 생성하기.
    @GetMapping("/board/view") // localhost:8080/board/view?id=1 형식으로 요청
    public String boardview(Model model, Integer id) {

        model.addAttribute("board", boardService.boardview(id));
        return "boardview";  // boardview.html에 매핑되어 화면 출력 !
    }


    // 해당 게시글 삭제 DELETE
    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        boardService.boardDelete(id);

        return "redirect:/board/list";
        // 삭제 작업이 끝난 후 페이지를 다시 리스트 페이지로 이동 !
    }


    // 해당 게시글 수정하기 UPDATE
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id,
                              Model model) {

        model.addAttribute("board", boardService.boardview(id));

        return "boardmodify";
    }


    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id,
                              Board board,
                              MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardview(id); // 특정 데이터 가져오기 ( 기존 내용 )
        boardTemp.setTitle(board.getTitle());  // 새로 입력한 내용 덮어 씌우기
        boardTemp.setContent(board.getContent());  // 새로 입력한 내용 덮어 씌우기

        boardService.write(boardTemp, file);  // 수정본으로 다시 저장 ! UPDATE

        return "redirect:/board/list";

    }


    // 파일 다운로드받기
//    @GetMapping("/board")


}
