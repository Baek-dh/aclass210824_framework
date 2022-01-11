package edu.kh.fin.board.model.exception;

// 사용자 정의 예외
public class UpdateBoardFailException extends RuntimeException{

	public UpdateBoardFailException() {
		super("게시글 수정 과정에 문제 발생");
	}
	
	public UpdateBoardFailException(String message) {
		super(message);
	}
}
