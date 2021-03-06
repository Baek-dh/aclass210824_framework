package edu.kh.fin.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.fin.board.model.vo.Board;
import edu.kh.fin.board.model.vo.Category;
import edu.kh.fin.board.model.vo.Pagination;
import edu.kh.fin.board.model.vo.Search;

// Service 인터페이스 왜 사용할까?
// 1. 규칙성
// 2. 유지보수
// 3. 결합도 다운
// 4. AOP 사용

/**
 * @author user1
 *
 */
public interface BoardService {

	/** 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
	 * @param cp
	 * @return pagination
	 */
	Pagination getPagination(int cp);

	/** 지정된 범위의 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 */
	List<Board> selectBoardList(Pagination pagination);

	/** 게시글 상세 조회
	 * @param boardNo
	 * @param memberNo
	 * @return board
	 */
	Board selectBoard(int boardNo, int memberNo);

	
	/** 카테고리 목록 조회
	 * @return category
	 */
	List<Category> selectCategory();

	/** 게시글 삽입 + 이미지 삽입
	 * @param board
	 * @param images
	 * @param webPath
	 * @param serverPath
	 * @return boardNo
	 */
	int insertBoard(Board board, List<MultipartFile> images, String webPath, String serverPath);

	
	/** 게시글 수정
	 * @param board
	 * @param images
	 * @param webPath
	 * @param serverPath
	 * @param deleteImages
	 * @return result
	 */
	int updateBoard(Board board, List<MultipartFile> images, String webPath, String serverPath, String deleteImages);

	/** 수정 화면 전환용 게시글 상세 조회
	 * @param boardNo
	 * @return board
	 */
	Board selectBoard(int boardNo);

	/** 게시글 삭제
	 * @param boardNo
	 * @return result
	 */
	int deleteBoard(int boardNo);

	/**  검색 조건에 맞는 전체 게시글 수 count + 페이징 처리에 필요한 값 계산
	 * @param cp
	 * @param search
	 * @return pagination
	 */
	Pagination getPagination(int cp, Search search);


	/** 검색 조건에 맞는 게시글 목록 조회
	 * @param pagination
	 * @param search
	 * @return boardList
	 */
	List<Board> selectBoardList(Pagination pagination, Search search);

	/** 이미지 파일명 목록 조회
	 * @return dbImgList
	 */
	List<String> selectImgList();

}





