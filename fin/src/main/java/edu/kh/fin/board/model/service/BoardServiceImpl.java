package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.fin.board.model.dao.BoardDAO;
import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;

@Service // Service임을 알려줌 + Bean 등록
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardDAO dao;

	// 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
	@Override
	public Pagination getPagination(int cp) {
		
		// 1. 전체 게시글 수 count
		int listCount = dao.getListCount();
		
		// 2. 페이징 처리에 필요한 값 계산 + 반환
		return new Pagination(listCount, cp);
	}

	
	// 지정된 범위의 게시글 목록 조회
	@Override
	public List<Board> selectBoardList(Pagination pagination) {
		return dao.selectBoardList(pagination);
	}

	
	// 게시글 상세 조회
	@Override
	public Board selectBoard(int boardNo, int memberNo) {
		
		Board board = dao.selectBoard(boardNo);
		
		// 게시글 상세조회 성공 && 게시글 작성자 != 회원번호
		if(board != null && board.getMemberNo() != memberNo) {
			
			// 조회 수 증가
			int result = dao.increaseReadCount(boardNo);
			
			// 조회 수 증가 성공 시 
			// 미리 조회된 board의 readCount를 + 1 (DB 동기화)
			if(result > 0) {
				board.setReadCount(  board.getReadCount() +1  );
			}
		}
		
		return board;
	}


	// 카테고리 목록 조회
	@Override
	public List<Category> selectCategory() {
		return dao.selectCategory();
	}
	
	
	
	
	
	
	
	
	
	

}
