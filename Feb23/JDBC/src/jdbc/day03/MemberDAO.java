package jdbc.day03;

import java.sql.*;
import java.util.Map;

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
			
			if(conn != null) {
				conn.close();
				conn = null;
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
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");
			
			String sql ="insert into tbl_member(userseq, userid, passwd, name, mobile)"
					+ "values(userseq.nextval, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUserid());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getMobile());
			
			result = pstmt.executeUpdate();
		
		}catch(ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
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
		
		
		return result;
	}//end of public int memberRegister

	// 로그인 처리(select) 메소드
	@Override
	public MemberDTO login(Map<String, String> paraMap) {
		
		MemberDTO member = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");
			
			String sql = " select userseq, name, mobile, point,to_char(registerday, 'yyyy-mm-dd ') AS registerday "+
						 " from tbl_member "+
						 " where status = 1 and userid = ? and passwd = ? ";
					
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paraMap.get("userid"));
			pstmt.setString(2, paraMap.get("passwd"));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member = new MemberDTO();
				
				member.setUserseq(rs.getInt("USERSEQ"));
				member.setName(rs.getString("NAME"));
				member.setMobile(rs.getString("MOBILE"));
				member.setPoint(rs.getInt("POINT"));
				member.setRegisterday(rs.getString("REGISTERDAY"));
				
			}
		
		}catch(ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
		
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		
		
		
		return member;
	}//end of public MemberDTO login(Map<String, String> paraMap)


}
