package edu.kh.fin.board.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.fin.board.model.service.BoardService;
import edu.kh.fin.board.model.service.BoardServiceImpl;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.common.Util;
import edu.kh.fin.member.model.vo.Member;

@Controller // 컨트롤러임을 알려줌 + Bean등록
@RequestMapping("/board/*") // /board로 시작하는 모든 요청을 받는 프론트 컨트롤러
@SessionAttributes({"loginMember"}) 
// 1. Model 속성 추가 시 key값 일치하는 값을 Session 영역으로 이동
// 2. Session에서 key가 일치하는 값을 얻어와 해당 컨트롤러 내에서 사용 가능하게 함
	// -> @ModelAttribut("loginMember")를 작성하여 얻어다 쓸 수 있다.
public class BoardController {
	
	@Autowired // Bean으로 등록된 객체 중 같은 타입 또는 상속 관계 객체를 자동으로 DI
	private BoardService service;
	

	// 게시글 목록 조회
	// -> 현재 페이지를 나타내는 파라미터 cp 전달 받기
	@RequestMapping("list")
	public String selectBoardList( 
			@RequestParam(value="cp", required=false, defaultValue="1") int cp,  Model model   ) {    
		
		// 1. 페이징 처리용 객체 Pagination 생성하기
		//    -> 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
		Pagination pagination = service.getPagination(cp);
		//System.out.println(pagination);
		
		// 2. 지정된 범위의 게시글 목록 조회
		List<Board> boardList = service.selectBoardList(pagination);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("boardList", boardList);
		
		return "board/boardList";
	}
	
	
	/* @PathVariable : URL 경로상에 있는 값을 파라미터로 사용할 수 있게하는 어노테이션
	 *
	 * PathVariable 언제 사용할까?
	 * - 자원을 식별하는 용도 값(파라미터)
	 * 
	 * QueryString은 언제 사용할까?
	 * - 필터링(검색, 정렬, 현재 페이지)
	 *
	 * */
	
	// 게시글 상세 조회
	@RequestMapping("view/{boardNo}")
	public String selectBoard(@PathVariable("boardNo") int boardNo,
			@RequestParam(value="cp", required=false, defaultValue="1") int cp,
			Model model, RedirectAttributes ra, HttpSession session
			/*@ModelAttribute("loginMember") Member loginMember*/) {
			
			// @SessionAttributes 
			// 1) Model.addAttribute(K,V) 수행 시 K가 일치하는 값을 Request -> Session scope로 이동
			// 2) Model.addAttribute(K,V) Session으로 이동한 값을 얻어와
			//		-> @ModelAttribute("K")에 전달
		
		
		int memberNo = 0;
		
		// session에 loginMember가 있을 경우
		if(session.getAttribute("loginMember") != null) {
			memberNo = ( (Member)session.getAttribute("loginMember") ).getMemberNo();
		}
		
		
		// 게시글 상세 조회 Service 호출
		Board board = service.selectBoard(boardNo, memberNo);
		
		String path = null;
		if(board != null) { // 조회 성공 시
			
			model.addAttribute("board", board);
			path = "board/boardView";
			
		}else { // 조회 실패 시
			
			Util.swalSetMessage("해당 글이 존재하지 않습니다.", null, "info", ra);
			path = "redirect:../list";
		}
		
		return path;
	}
	
	

	// 게시글 작성 화면 전환
	@RequestMapping(value="insert", method=RequestMethod.GET)
	public String boardInsert(Model model) {
		
		List<Category> category = service.selectCategory();
		model.addAttribute("category", category);
		
		return "board/boardInsert";
	}
	
	
	
	
	// 게시글 삽입
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public String boardInsert(Board board /*커맨드 객체*/, 
		  @RequestParam(value="images", required=false) List<MultipartFile> images/*업로드 파일*/,
		  @ModelAttribute("loginMember") Member loginMember /*세션 로그인 정보*/,
		  HttpSession session/*파일저장경로*/, RedirectAttributes ra) {
		
		/* MultipartFile이 제공하는 메소드
		 * - getOriginalFilename() : 파일 원본명 반환
		 * - getSize() : 파일 크기 반환
		 * - getInputStream() : 파일에 대한 입력 스트림
		 * - transferTo() : MultipartFile 객체는 메모리에 저장된 파일과 연결되어 있음
		 * 					해당 메소드 호출 시 연결된 메모리의 파일을 디스크(HDD,SSD)로 저장
		 * */
		
		// 1) 로그인 회원 번호를 board에 세팅
		board.setMemberNo( loginMember.getMemberNo() );
		
		// 2) 웹 접근 경로(webPath), 서버 저장 경로(serverPath)
		String webPath = "/resources/images/board/"; // (DB에 저장되는 경로)
		String serverPath = session.getServletContext().getRealPath(webPath);
		
		// 3) 게시글 삽입 Servic 호출
		int boardNo = service.insertBoard(board, images, webPath, serverPath);
		// -> Service 수행 후 삽입된 게시글 번호를 얻어올 예정
		
		
		String path = null;
		if(boardNo > 0) { // 삽입 성공
			Util.swalSetMessage("게시글 삽입 성공", null, "success", ra);
			path = "view/"+boardNo;
			
		}else { // 실패
			Util.swalSetMessage("게시글 삽입 실패", null, "error", ra);
			path = "insert";
		}
		
		return "redirect:"+path;
	}
	
	
	
	// 게시글 수정 화면 전환
	@RequestMapping(value="updateForm", method=RequestMethod.POST)
	public String updateForm(int boardNo, Model model) {
		
		// 카테고리 목록 조회
		List<Category> category = service.selectCategory();
		
		// 게시글 상세 조회
		Board board = service.selectBoard(boardNo);
		
		model.addAttribute("category", category);
		model.addAttribute("board", board);
		
		return "board/boardUpdate";
	}
	
	
	// 게시글 수정
	@RequestMapping(value="update", method=RequestMethod.POST)
	public String updateBoard(Board board/*커맨드 객체*/,
			@RequestParam(value="cp", required=false, defaultValue="1") int cp, 
			@RequestParam("deleteImages") String deleteImages,
			@RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra, HttpSession session) {
		
		//System.out.println("deleteImages  : " + deleteImages);
		// deleteImages 가 비어있을 경우 == "" (빈 문자열)
		// deleteImages 가 비어 있지 않을 경우 == "0,2" 문자열
		
		// 1) 웹 접근 경로(webPath), 서버 저장 경로(serverPath)
		String webPath = "/resources/images/board/"; // (DB에 저장되는 경로)
		String serverPath = session.getServletContext().getRealPath(webPath);
		
		// 2) 게시글 수정 Service 호출
		int result = service.updateBoard(board, images, webPath, serverPath, deleteImages);
		
		
		return null;
	}
	
	
	
	
}








