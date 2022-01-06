package edu.kh.fin.member.model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.fin.member.model.dao.MemberDAO;
import edu.kh.fin.member.model.vo.Member;

@Service // Service 레이어, 비즈니스 로직을 가진 클래스임을 명시 + Bean 등록
public class MemberServiceImpl implements MemberService {

	@Autowired // bean으로 등록된 MemberDAO 객체 의존성 주입(DI)
	private MemberDAO dao;

	@Autowired
	private BCryptPasswordEncoder encoder;
	// Bean으로 등록된 BCryptPasswordEncoder 객체를 의존성 주입(DI)

	// 로그인
	@Override
	public Member login(Member member) {
		System.out.println("서비스 bean 등록 및 DI 성공");
		System.out.println(member.getMemberId());
		System.out.println(member.getMemberPw());

		// 암호화된 비밀번호
		String encPw = encoder.encode(member.getMemberPw());
		// 평문(암호화X 비밀번호)

		System.out.println("암호화된 비밀번호 : " + encPw);

		// BCrypt 암호화 원리
		// - 평문에 추가적인 문자열을 임의로 붙여(salt)서 암호화를 진행
		// - 암호화된 결과가 계속 다르지만
		// 이를 비교할 수 있는 별도 메소드를 같이 제공

		// String temp = "$2a$10$WmZT9Q5V0YPEmceHdQr41.7F89HYrOhcwRgSnnHMrr/HfyVPhuWxi";
		// System.out.println(encoder.matches(member.getMemberPw(), temp));
		// 평문 // 암호화

		// 로그인 DAO 호출
		Member loginMember = dao.login(member.getMemberId());

		System.out.println(loginMember);
		// 조회 성공 시 Member 객체, 실패 시 null

		// DB에 일치하는 아이디를 가진 회원이 있고
		// 입력 받은 비밀번호와 암호화된 비밀번호가 같을 때 ==> 로그인 성공
		if (loginMember != null && encoder.matches(member.getMemberPw(), loginMember.getMemberPw())) {

			// DB에서 조회한 비밀번호 삭제
			loginMember.setMemberPw(null);

		} else { // 로그인 실패

			loginMember = null;
		}

		return loginMember;
	}

	// 아이디 중복 검사
	@Override
	public int idDupCheck(String inputId) {
		return dao.idDupCheck(inputId);
	}

	// 이메일 중복 검사
	@Override
	public int emailDupCheck(String inputEmail) {
		return dao.emailDupCheck(inputEmail);
	}

	// 회원 가입
	// rollbackFor 속성 : 어떤 예외 발생 시 롤백을 수행할 지 지정
	// @Transactional(rollbackFor = Exception.class)
	@Transactional
	@Override
	public int signUp(Member member) {

		// 비밀번호 암호화
		String encPw = encoder.encode(member.getMemberPw());

		// 암호화된 비밀번호를 member에 다시 set
		member.setMemberPw(encPw);

		return dao.signUp(member);

		// 트랜잭션 처리 @Transactional

		/*
		 * 스프링에서 트랜잭션을 처리하는 방법
		 * 
		 * 1. 코드 기반 처리 방법 (기존 commit, rollback)
		 * 
		 * 2. 선언적 트랜잭션 처리 방법 1) <tx:advice> XML 방식 2) @Transactional 어노테이션 방식 조건 1. 트랜잭션
		 * 매니저가 Bean으로 등록 되어 있어야 함 조건 2. @Transactional 어노테이션을 인식하기 위한
		 * <tx:annotaion-driven/> 태그가 존재해야 함.
		 * 
		 * @Transactional 어노테이션은 rollback을 위한 어노테이션 왜 커밋은 안하지? 커넥션 반환 시 아무런 트랜잭션 처리가
		 * 되어있지 않다면 자동 commit
		 * 
		 * @Transactional은 RuntimeException이 발생했을 때 Rollback을 수행함.
		 */
	}

	// 회원 정보 수정
	// @Transactional // 트랜잭션 처리는 여러 DML 수행 시 사용한다.
	@Override
	public int updateMember(Member member) {
		dao.updateMember(member);
		return dao.updateMember(member);
	}

	// 회원 정보 수정
	@Override
	public int updatePw(Map<String, String> map) {

		// 2. 회원 번호를 이용해서 DB에 저장된 비밀번호를 조회
		String savePw = dao.selectSavePw(map.get("memberNo"));

		// 3. DB 저장된 비밀번호와 입력된 현재 비밀번호 비교( matches() 사용 )
		int result = 0;
		if (encoder.matches(map.get("currentPw"), savePw)) { // 일치할 때
			// 4. 일치하면 새 비밀번호를 암호화 -> 비밀번호 변경 DAO 호출

			String encPw = encoder.encode(map.get("newPw")); // 새 비밀번호 암호화
			map.put("newPw", encPw); // map에 key가"newPw"인 요소의 value를 encPw로 변경

			result = dao.updatePw(map); // 비밀번호 변경 DAO 호출
		}

		// 5. 일치하지 않으면 Controller로 0 반환

		return result;
	}

	
	// 회원 탈퇴
	@Override
	public int secession(int memberNo, String currentPw) {
		
		String savePw = dao.selectSavePw(memberNo+"");

		int result = 0;

		if (encoder.matches(currentPw, savePw)) { 

			result = dao.secession(memberNo); 
		}

		return result;
	}

	
	
	
	
}
