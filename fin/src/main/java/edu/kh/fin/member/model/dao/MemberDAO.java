package edu.kh.fin.member.model.dao;

import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.fin.member.model.vo.Member;

@Repository // Persistance layer, 영속성을 가지는 속성(파일, DB)을 제어하는 클래스 + Bean 등록
public class MemberDAO {

	@Autowired // bean으로 등록된 SqlSessionTemplate 객체 의존성 주입(DI)
	private SqlSessionTemplate sqlSession; // Mybatis를 이용한 DB연결, SQL 수행
	
	
	/** 로그인
	 * @param memberId
	 * @return loginMember
	 */
	public Member login(String memberId) {
		
		// sqlSession.selectOne("매퍼명.태그id" , 전달할 파라미터 )
		// -> 특정 매퍼에 있는 id가 일치하고 1행만 반환하는 select 문을 수행
		//    + select문에서 파라미터 사용
		Member loginMember = sqlSession.selectOne("memberMapper.login", memberId);
		return loginMember;
	}


	/** 아이디 중복 검사
	 * @param inputId
	 * @return result
	 */
	public int idDupCheck(String inputId) {
		return sqlSession.selectOne("memberMapper.idDupCheck", inputId);
	}


	/** 이메일 중복 검사
	 * @param inputEmail
	 * @return result
	 */
	public int emailDupCheck(String inputEmail) {
		return sqlSession.selectOne("memberMapper.emailDupCheck", inputEmail);
	}


	/** 회원 가입
	 * @param member
	 * @return result
	 */
	public int signUp(Member member) {
		return sqlSession.insert("memberMapper.signUp", member);
	}


	/** 회원 정보 수정
	 * @param member
	 * @return result
	 */
	public int updateMember(Member member) {
		return sqlSession.update("memberMapper.updateMember", member);
	}


	
	/** 현재 DB에 저장된 암호화된 비밀번호 조회
	 * @param memberNo
	 * @return savePw
	 */
	public String selectSavePw(String memberNo) {
		return sqlSession.selectOne("memberMapper.selectSavePw", memberNo);
	}


	/** 비밀번호 변경
	 * @param map
	 * @return result
	 */
	public int updatePw(Map<String, String> map) {
		return sqlSession.update("memberMapper.updatePw", map);
	}
	
	
	/** 회원 탈퇴
	 * @param memberNo
	 * @return result
	 */
	public int secession(int memberNo) {
		return sqlSession.update("memberMapper.secession", memberNo);
	}
	
	
	
	
	
}
