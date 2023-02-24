package jdbc.day04.b;

import java.util.*;

import jdbc.day04.b.dbconnection.MyDBConnection;

public class MemberCtrl implements InterMemberCtrl{
	// field, attribute, property, 속성
		InterMemberDAO mdao = new MemberDAO();
		
		// method, operation, 기능
		
		// == 시작메뉴를 보여주는 메소드 구현(생성)하기 ==
		@Override
		public void menu_Start(Scanner sc) {
			
			MemberDTO member = null;
			
			String s_Choice = "";
			
			do {       							 // 3번이라면 탈출
				String loginName = "";
				String login_logout = "로그인";
				String menu_add = "";
				
				if(member != null) {
					loginName = "["+ member.getName() +"로그인중..]";
					login_logout = "로그아웃";
					menu_add = "4.나의정보보기   5.회원탈퇴하기    6.모든회원조회    7.회원명으로조회\n";
				}
				
				System.out.println("\n>>> ----- 시작메뉴 "+loginName+" ----- <<<\n"
								  +"1. 회원가입	2."+login_logout+"		3. 프로그램종료\n"
								  + menu_add 
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
					
					MyDBConnection.closeConnection();  //MyDBConnection 에 객체 자원 반납하기 메소드 실행 
					
					break;
					
				case "4": // 나의정보보기
					 if(member != null)
					 {
						 // System.out.println(member.toString());
						 // 또는
						 System.out.println(member);
						 break;
					 }
					
				case "5": // 회원탈퇴하기
					if(member != null)
					 {	 
						String YN = "";
						 do
						 {
							 System.out.print(" >>> 정말로 탈퇴하시겠습니까?[Y/N] : ");
							 YN = sc.nextLine();
							 
							 if("y".equalsIgnoreCase(YN))   // 회원 탈퇴하기
							 {
								 //행을 업데이트 하기 위해 
								 int n = mdao.deleteMember(member.getUserseq()); // member는 로그인 하기 .getuserid는 null 값이 있으므로 사용하면 안됨  
								 
								 if(n == 1)
								 {
									 System.out.println(">> 회원탈퇴 처리가 성공되었습니다. << \n");
									 member = null;     // 로그인 중인것을 빼서 로그아웃으로 변경하기 위해 member를 null로 넣어준다. 처음 do쪽으로 갈때 
									 					// member 가 null이므로 타이틀 찍히는 것도 다르다.
								 }
							 }
							 else if("n".equalsIgnoreCase(YN))   // 회원 탈퇴 취소하기 
							 {
								 System.out.println(" >> 회원 탈퇴를 취소하셨습니다!! << \n");
							 }
							 else			// y,n이 아닌 다른 문자가 들어올때
							 {
								 System.out.println(">> Y 또는 N 만 입력하세요 !! << \n");
								 
							 }
						 } while(!("y".equalsIgnoreCase(YN) || "n".equalsIgnoreCase(YN)));
						 
						 
						 break;
					 }
					
				case "6": // 모든회원조회 
					if(member != null)
					 {
						 showAllMember();
						 break;
					 }

				
				case "7": // 회원명으로조회
					if(member != null)
					 {
						 searchMemberByName(sc);
						 break;
					 }
				 
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
		
		/// *** 모든회원조회를 해주는 메소드  *** ///
		private void showAllMember() {
			
			// mdao는 memberDAO를 뜻한다. 
			List<MemberDTO> memberList = mdao.showAllMember();   // 몇개의 회원정보가 있는지 모르기 때문에 list를 사용한다.
			
			if(memberList != null)   // 데이터베이스에 누구라도 있으면
			{
				StringBuilder sb = new StringBuilder();
				
				/*
				// 일반 for문 
				//memberDAO에서 해온것을 말함
				for(int i = 0; i < memberList.size(); i++)  // memberList에 담긴 데이터 만큼 반복
				{
					
					// 리스트에서 값을 꺼내서 StringBuilder에 저장 (1번 돌아갈때마다 한명의 정보 나옴)
					sb.append(memberList.get(i).getUserid() + "\t" + // 이 자체가 memberDTO이다.
					memberList.get(i).getName() + "\t" +
					memberList.get(i).getMobile() + "\t" +
					memberList.get(i).getPoint() + "\t" +
					memberList.get(i).getRegisterday() + "\n");
					
				}//end of for
				*/
				// 또는 
				// 확장 for문 
				for( MemberDTO member : memberList)
				{
					
					sb.append(member.getUserid() + "\t" + 
					member.getName() + "\t" + 
					member.getMobile() + "\t" + 
					member.getPoint() + "\t" + 
					member.getRegisterday() + "\n"); 
					
				}// end of for
				
				
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("아이디     성명    연락처           포인트     가입일자");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				
				System.out.println(sb.toString());
				
			}
			else					// 데이터 베이스에 누구라도 없으면
			{
				System.out.println(">> 가입중인 회원이 아무도 없습니다 << \n");
			}
		}
		
		//*** 회원명으로조회를 해주는 메소드  *** ///
		private void searchMemberByName(Scanner sc) {
			
			System.out.print("▷ 조회할 성명 : ");
			String searchName = sc.nextLine();
			
			// mdao는 memberDAO를 뜻한다. 
			List<MemberDTO> memberList = mdao.searchMemberByName(searchName);   // 몇개의 회원정보가 있는지 모르기 때문에 list를 사용한다.
			
			if(memberList != null)   // 데이터베이스에 누구라도 있으면
			{
				StringBuilder sb = new StringBuilder();
				
				/*
				// 일반 for문 
				//memberDAO에서 해온것을 말함
				for(int i = 0; i < memberList.size(); i++)  // memberList에 담긴 데이터 만큼 반복
				{
					
					// 리스트에서 값을 꺼내서 StringBuilder에 저장 (1번 돌아갈때마다 한명의 정보 나옴)
					sb.append(memberList.get(i).getUserid() + "\t" + // 이 자체가 memberDTO이다.
					memberList.get(i).getName() + "\t" +
					memberList.get(i).getMobile() + "\t" +
					memberList.get(i).getPoint() + "\t" +
					memberList.get(i).getRegisterday() + "\n");
					
				}//end of for
				*/
				// 또는 
				// 확장 for문 
				for( MemberDTO member : memberList)
				{
					
					sb.append(member.getUserid() + "\t" + 
					member.getName() + "\t" + 
					member.getMobile() + "\t" + 
					member.getPoint() + "\t" + 
					member.getRegisterday() + "\n"); 
					
				}// end of for
				
				
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("아이디     성명    연락처           포인트     가입일자");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				
				System.out.println(sb.toString());
				
			}
			else					// 데이터 베이스에 누구라도 없으면
			{
				System.out.println(">> 조회하려는 이름을 가진 회원이 없습니다 << \n");
			}
			
		}

		
}
