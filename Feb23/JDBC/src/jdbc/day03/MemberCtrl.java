package jdbc.day03;

import java.util.*;

public class MemberCtrl implements InterMemberCtrl{
	// field, attribute, property, 속성
		InterMemberDAO mdao = new MemberDAO();
		
		// method, operation, 기능
		
		// == 시작메뉴를 보여주는 메소드 구현(생성)하기 ==
		@Override
		public void menu_Start(Scanner sc) {
			
			MemberDTO member = null;
			
			String s_Choice = "";
			
			do {
				String loginName = "";
				String login_logout = "로그인";
				
				if(member != null) {
					loginName = "["+ member.getName() +"로그인중..]";
					login_logout = "로그아웃";
				}
				
				System.out.println("\n>>> ----- 시작메뉴 "+loginName+" ----- <<<\n"
								  +"1. 회원가입	2."+login_logout+"		3. 프로그램종료\n"
								  
						
						
						
								  +"---------------------------------------------------------------------\n");
				System.out.print("▷ 메뉴번호 선택 : ");
				s_Choice = sc.nextLine();
				
				
				switch (s_Choice) {
				case "1": // 회원가입
					memberRegister(sc);
					break;
					
				case "2": // 로그인 OR 로그아웃
					if("로그인".equals(login_logout)){// 로그인 시도하기
						member = login(sc);
					}
					else{// 로그아웃 하기
						member = null;
						System.out.println(">>> 로그아웃 되었습니다. <<<\n");
					}
					break;
				case "3": // 프로그램 종료
					
					break;
		
				default:
					System.out.println(">>> 메뉴에 없는 번호 입니다. 다시 선택하세요!! <<<\n");
					break;
				}//end of switch
				
			}while(!("3".equals(s_Choice)));
			
		}// end of public void menu_Strat(Scanner sc)
		
		// *** 회원가입 해주는 메소드 *** //
		private void memberRegister(Scanner sc) {
			
			System.out.println("\n >>> ----- 회원가입 ----- <<<");
			
			System.out.print("1. 아이디 : ");
			String userid = sc.nextLine();
			
			System.out.print("2. 비밀번호 : ");
			String passwd = sc.nextLine();
			
			System.out.print("3. 회원명 : ");
			String name = sc.nextLine();
			
			System.out.print("4. 연락처(휴대폰) : ");
			String mobile = sc.nextLine();
			
			MemberDTO member = new MemberDTO();
			member.setUserid(userid);
			member.setPasswd(passwd);
			member.setName(name);
			member.setMobile(mobile);
			
			int n = mdao.memberRegister(member);
			
			if(n==1) {
				System.out.println("\n >>>> 회원가입을 축하드립니다.<<<<");
			}
			else {
				System.out.println("\n >>>> 회원가입을 실패 되었습니다.<<<<");
			}
			
		}// end of private void memberRegister(Scanner sc)
		
		
		// *** 로그인 해주는 메소드 *** ///
		private MemberDTO login(Scanner sc) {
			
			
			MemberDTO member = null;
			
			System.out.println("\n >>> --- 로그인 --- <<<");
			
			System.out.print("1. 아이디 : ");
			String userid = sc.nextLine();
			
			System.out.print("2. 비밀번호 : ");
			String passwd = sc.nextLine();
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", userid);
			paraMap.put("passwd", passwd);
			
			member = mdao.login(paraMap);
			
			if(member != null) {
				System.out.println("\n>>> 로그인 성공 !!! <<<");
			}
			else {
				System.out.println("\n>>> 로그인 실패 ... <<<");
			}
			
			return member;
			
		}
}
