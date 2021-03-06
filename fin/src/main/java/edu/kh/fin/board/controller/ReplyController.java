package edu.kh.fin.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import edu.kh.fin.board.model.service.ReplyService;
import edu.kh.fin.board.model.vo.Reply;

@RestController // 요청 시 값만 반환되는 컨트롤러
				// @Controller + @ResponseBody
@RequestMapping("/reply/*")
public class ReplyController {

	@Autowired
	private ReplyService service;
	
	
	// @ResponseBody : 컨트롤러 메소드 반환 값이 주소/경로가 아닌 값 자체임을 알려줌
	//					-> 비동기 통신(ajax)에 주로 사용
	
	
	// 댓글 목록 조회
	@RequestMapping(value="select", method=RequestMethod.GET)
	public String selectList(int boardNo) {
							// ajax코드 -> data 속성에 작성된 key값
		
		List<Reply> rList = service.selectList(boardNo);
		
		// 조회된 rList를 JSON 형식으로 변경
		// -> 직접 변경, JSONSimple 라이브러리, GSON 라이브러리(당첨)
		
		// Gson.toJson(object) : object를 JSON형태로 변환
		
		return new Gson().toJson(rList);
	}
	
	// 댓글 삽입 
	@RequestMapping(value="insert", method=RequestMethod.POST)
	public int insertReply(Reply reply/*커맨드 객체*/) {
		return service.insertReply(reply);
	}
	
	
	// 댓글 수정 
	@RequestMapping(value="update", method=RequestMethod.POST)
	public int updateReply(Reply reply/*커맨드 객체*/) {
							//replyNo, replyContent
		return service.updateReply(reply);
	}
	
	
	
	// 댓글 삭제 
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public int deleteReply(int replyNo) {
		return service.deleteReply(replyNo);
	}
	
	
}





