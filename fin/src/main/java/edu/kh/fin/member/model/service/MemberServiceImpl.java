package edu.kh.fin.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.fin.member.model.dao.MemberDAO;
import edu.kh.fin.member.model.vo.Member;

@Service // Service 레이어, 비즈니스 로직을 가진 클래스임을 명시 + Bean 등록
public class MemberServiceImpl implements MemberService{
	
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
		//   이를 비교할 수 있는 별도 메소드를 같이 제공
		
		
		//String temp = "$2a$10$WmZT9Q5V0YPEmceHdQr41.7F89HYrOhcwRgSnnHMrr/HfyVPhuWxi";
		//System.out.println(encoder.matches(member.getMemberPw(), temp));
											// 평문   			// 암호화
		
		// 로그인 DAO 호출
		Member loginMember = dao.login(member.getMemberId());
		
		System.out.println(loginMember);
		// 조회 성공 시 Member 객체, 실패 시 null
		
		// DB에 일치하는 아이디를 가진 회원이 있고
		// 입력 받은 비밀번호와 암호화된 비밀번호가 같을 때 ==> 로그인 성공
		if(loginMember != null 
				&& encoder.matches(member.getMemberPw(), loginMember.getMemberPw())) {
			
			// DB에서 조회한 비밀번호 삭제
			loginMember.setMemberPw(null);
			
		}else { // 로그인 실패
			
			loginMember = null;
		}
		
		return loginMember;
	}

}

