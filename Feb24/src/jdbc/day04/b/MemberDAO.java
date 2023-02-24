//DAO는 오라클 문을 처리해 주는 클래스이다.

package jdbc.day04.b;

import java.sql.*;
import java.util.*;

import jdbc.day04.b.dbconnection.MyDBConnection;

// DAO(Data Access Object) ==> 데이터베이스에 연결하여 SQL구문(DDL, DML, DQL)을 실행해주는 객체


public class MemberDAO implements InterMemberDAO {
	
	// field, attribute, property, 속성
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	
	// method, operation, 기능
	
	// === 자원반납을 해주는 메소드 === //
	private void close() {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	// === 회원가입(insert) 메소드 구현(생성)하기 ===
	@Override
	public int memberRegister(MemberDTO member) {
		
		int result = 0;
		
		try {
			conn = MyDBConnection.getConn();  // 스테틱 되어진 것을 불러온다. 연결할 오라클을 받아옴 
			
			String sql ="insert into tbl_member(userseq, userid, passwd, name, mobile)"
					+ "values(userseq.nextval, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUserid());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getMobile());
			
			result = pstmt.executeUpdate();
		
		}catch(SQLIntegrityConstraintViolationException e) {
			
			if(e.getErrorCode() == 1) {
				System.out.println(">> 아이디가 중복되었습니다. 새로운 아이디를 입력하세요!! <<");
			}
			else {
				System.out.println("에러메시지 : "+ e.getMessage());
			}
		}catch(SQLException e) {
			e.printStackTrace();    // 개발도중에만 필요하고 개발 완료하면 없애야 한다.
		} finally {
			close(); // 자원반납
		}
		
		
		return result;
	}//end of public int memberRegister

	// 로그인 처리(select) 메소드
	@Override
	public MemberDTO login(Map<String, String> paraMap) {
		
		MemberDTO member = null;
		
		try {
			conn = MyDBConnection.getConn();  // 스테틱 되어진 것을 불러온다. 연결할 오라클을 받아옴 
			
			/*
			// == 아래처험 데이터에 입력될 값을 변수로 처리해주면 SQL Injection(SQL 주입) 공격에 당해 해킹된다!!!! //
			String sql = " select userseq, name, mobile, point,to_char(registerday, 'yyyy-mm-dd ') AS registerday "+
					 " from tbl_member "+
					 " where status = 1 and userid = '"+ paraMap.get("userid") +"' and passwd = '"+ paraMap.get("passwd") +"' ";
			
			pstmt = conn.prepareStatement(sql);
			*/
			
			
			
			 	//  SQL Injection(SQL 주입) 공격에 당하지 않으려면 데이터에 입력될 값은 위치홀더(?)로 처리해주면 된다 !!!!!!
			
			// 위치 홀더를 쓰면 우리가 입력한 데이터 값이 나오지 않는다 해킹을 막을 수 있다. (실제 아이디로 받기 때문에 ' OR 1=1 -- 을 데이터로 받아서 로그인 실패를 하게된다.) 
			String sql = " select userseq, name, mobile, point,to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday "+
						 " from tbl_member "+
						 " where status = 1 and userid = ? and passwd = ? ";
			// 위치 홀더에 ? 만 넣어야지 '?' 를 사용하면 안된다!
			
					
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("passwd"));
			
			
			System.out.println("확인용 SQL : " + sql);
			// 확인용 SQL :  select userseq, name, mobile, point,to_char(registerday, 'yyyy-mm-dd ') AS registerday  from tbl_member  where status = 1 and userid = 'leess' and passwd = '1234'
			// 출력시 내가 입력한 아이디와 비밀번호가 그 대로 나오므로 위치 홀더를 사용해야 보안상 유리하다.
			
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new MemberDTO();
				
				member.setUserseq(rs.getInt("USERSEQ"));
				member.setName(rs.getString("NAME"));
				member.setMobile(rs.getString("MOBILE"));
				member.setPoint(rs.getInt("POINT"));
				member.setRegisterday(rs.getString("REGISTERDAY"));
				
			}
		
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		
		
		return member;
	}//end of public MemberDTO login(Map<String, String> paraMap)


	// == 회원탈퇴(update) 메소드 구현(생성)하기 
	@Override
	public int deleteMember(int userseq) {
		//DML메소드
		
		int n = 0;
		
		try {
			conn = MyDBConnection.getConn();  // 스테틱 되어진 것을 불러온다. 연결할 오라클을 받아옴 
			
			// update 내용 적어주기 오라클 문법으로 
			String sql =" update tbl_member set status = 0 "
					+ " where userseq = ? ";
			
			// 매핑 시키자!!!
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userseq);
			
			// 실행해라 라는 말이다 DML이므로 Update를 써줘야 한다.
			n = pstmt.executeUpdate();
		
			// 오류일때 
		}catch(SQLIntegrityConstraintViolationException e) {
			
			if(e.getErrorCode() == 1) {
				System.out.println(">> 아이디가 중복되었습니다. 새로운 아이디를 입력하세요!! <<");
			}
			else {
				System.out.println("에러메시지 : "+ e.getMessage());
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		
		return n;
	}


	// 모든회원조회(select) 메소드 구현(생성)하기 ==
	@Override
	public List<MemberDTO> showAllMember() {
		
		// 초기값 설정 
		List<MemberDTO> memberList = null;
		
		try {
			conn = MyDBConnection.getConn();  // 스테틱 되어진 것을 불러온다. 연결할 오라클을 받아옴 
			
			// select 내용 적어주기 오라클 문법으로 
			String sql = " select userid, name, mobile, point,to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday "+
						 " from tbl_member "+
						 " where status = 1 "+
						 " order by userseq desc ";   // 최근 가입한 순서부터 보이자 
						 
			
					
			// 매핑시키기 
			pstmt = conn.prepareStatement(sql);
			
			// SQL문이 DQL문(select) 이므로 ,executeQuery(); 이다
			// pstmt.executeQuery(); 을 실행하면 select 되어진 결과물을 가져오는데 그 타입은 ResultSet 으로 가져온다.
			rs = pstmt.executeQuery();
			
			
			int cnt = 0;
			
			// selete 되어질 한 행, 한행을 새롭게 만든다 
			while(rs.next()) {
				
				cnt++;
				
				if(cnt == 1)  // 한번만 실행하기 위해 조건문을 만든다.
				{
					memberList = new ArrayList<>();	// memberList를 ArratyList로 만듬
				}
				
				
				MemberDTO member = new MemberDTO();  //반복문 안에 계속 selete 해주기 위해 
				
				member.setUserid(rs.getString("USERID"));
				member.setName(rs.getString("NAME"));
				member.setMobile(rs.getString("MOBILE"));
				member.setPoint(rs.getInt("POINT"));
				member.setRegisterday(rs.getString("REGISTERDAY"));
				
				// 지금까지 받아온 값을 LIST에 담는다
				memberList.add(member);   //이렇게 단독으로 null이 초기 값이기 때문에 안된다. 
										  //그렇기에 memberList = new ArrayList<>(); 를 써줘야 한다.
			}
		
		// 오류 잡아주기 
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		return memberList;
	}
	

	// 회원명으로조회(select) 메소드 구현(생성)하기 ==
	@Override
	public List<MemberDTO> searchMemberByName(String searchName) {  //searchName에 값을 매핑시키는것을 해줘야만 함 
		
		
		// 초기값 설정 
		List<MemberDTO> memberList2 = null;
		
		try {
			conn = MyDBConnection.getConn();  // 스테틱 되어진 것을 불러온다. 연결할 오라클을 받아옴 
			
			/*
			// 위치 홀더를 사용하지 않으면 보안에 취약하다.
			// select 내용 적어주기 오라클 문법으로 
			String sql = " select userid, name, mobile, point,to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday "+
						 " from tbl_member "+
						 " where status = 1 and name like '%'|| '"+searchName+"' || '%' "+
						 " order by userseq desc ";   // 최근 가입한 순서부터 보이자 
					 
			
					
			// 매핑시키기 
			pstmt = conn.prepareStatement(sql);
			*/
			
			
			// 위치 홀더를 사용하면 보안에 좋다.
			// select 내용 적어주기 오라클 문법으로 
			String sql = " select userid, name, mobile, point,to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday "+
						 " from tbl_member "+
						 " where status = 1 and name like '%' || ? || '%' "+
						 " order by userseq desc ";   // 최근 가입한 순서부터 보이자 
						 
			
					
			// 매핑시키기 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchName);
			
			
			System.out.println(">> 확인용 SQL : " + sql);
			
			
			// SQL문이 DQL문(select) 이므로 ,executeQuery(); 이다
			// pstmt.executeQuery(); 을 실행하면 select 되어진 결과물을 가져오는데 그 타입은 ResultSet 으로 가져온다.
			rs = pstmt.executeQuery();
			
			
			int cnt = 0;
			
			// selete 되어질 한 행, 한행을 새롭게 만든다 
			while(rs.next()) {
				
				cnt++;
				
				if(cnt == 1)  // 한번만 실행하기 위해 조건문을 만든다.
				{
					memberList2 = new ArrayList<>();	// memberList를 ArratyList로 만듬
				}
				
				
				MemberDTO member2 = new MemberDTO();  //반복문 안에 계속 selete 해주기 위해 
				
				member2.setUserid(rs.getString("USERID"));
				member2.setName(rs.getString("NAME"));
				member2.setMobile(rs.getString("MOBILE"));
				member2.setPoint(rs.getInt("POINT"));
				member2.setRegisterday(rs.getString("REGISTERDAY"));
				
				// 지금까지 받아온 값을 LIST에 담는다
				memberList2.add(member2);   //이렇게 단독으로 null이 초기 값이기 때문에 안된다. 
										  //그렇기에 memberList = new ArrayList<>(); 를 써줘야 한다.
			}
		
		// 오류 잡아주기 
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		return memberList2;
	}


}
