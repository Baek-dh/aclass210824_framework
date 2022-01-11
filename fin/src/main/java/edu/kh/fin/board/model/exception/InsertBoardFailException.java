package edu.kh.fin.board.model.exception;

// 사용자 정의 예외
public class InsertBoardFailException extends RuntimeException{

	public InsertBoardFailException() {
		super("게시글 삽입 과정에 문제 발생");
	}
	
	public InsertBoardFailException(String message) {
		super(message);
	}
}
