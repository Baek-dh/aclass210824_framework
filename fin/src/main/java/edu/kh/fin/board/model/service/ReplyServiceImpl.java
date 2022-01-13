package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.fin.board.model.dao.ReplyDAO;
import edu.kh.fin.board.model.vo.Reply;
import edu.kh.fin.common.Util;

@Service // 비즈니스 로직 처리하는 Serivce임을 알려줌 + Bean 등록
public class ReplyServiceImpl implements ReplyService{
	
	// Bean으로 등록된 객체 중 같은 타입 또는 상속 관계 객체를
	// 자동으로 찾아 의존성 주입(DI) 하라고 지정하는 어노테이션
	@Autowired 
	private ReplyDAO dao;

	// 댓글 목록 조회
	@Override
	public List<Reply> selectList(int boardNo) {
		return dao.selectList(boardNo);
	}

	// 댓글 삽입
	@Override
	public int insertReply(Reply reply) {
		
		// XSS, 개행문자 처리
		reply.setReplyContent(  Util.XSS(reply.getReplyContent())  );
		reply.setReplyContent(  Util.changeNewLine(reply.getReplyContent())  );
		
		return dao.insertReply(reply);
	}
	
	
	
	
	
	
	
	
	
}
