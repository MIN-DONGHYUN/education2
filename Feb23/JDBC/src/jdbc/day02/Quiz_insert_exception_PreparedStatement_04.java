//tbl_student 에 insert 하는 것을 하기 

package jdbc.day02;

import java.sql.*;
import java.util.Scanner;

/*
	▶ 학번 : 9003
	▶ 성명 : 홍길동
	▶ 연락처 : 010-2345-5234
	▶ 주 소 : 서울시 마포구 월드컵북로 21
	▶ 학급번호 : 3
	
	>>> 학번 9003 은 이미 사용중이므로 다른 학번을 입력하세요!! <<<
	
	
	▶ 학번 : 9006
	▶ 성명 : 홍길동
	▶ 연락처 : 010-2345-5234
	▶ 주 소 : 서울시 마포구 월드컵북로 21
	▶ 학급번호 : 20
	
	>>> 학급번호 20 은 존재하지 않습니다. <<<
	>>> 사용가능한 학급번호는 1,2,3 입니다. <<<
	
	
	▶ 학번 : 9006
	▶ 성명 : 홍길동
	▶ 연락처 : 010-2345-5234
	▶ 주 소 : 서울시 마포구 월드컵북로 21
	▶ 학급번호 : 3
	
	>>> 데이터 입력성공 !! <<<

*/

public class Quiz_insert_exception_PreparedStatement_04 {

	public static void main(String[] args) {
		
		
		Connection conn = null;   // Connection conn 은 데이터베이스 서버와 연결을 맺어주는 자바의 객체이다.
		PreparedStatement pstmt = null;
		// PreparedStatement cstmt 은 
		// Connection conn(연결한 DB 서버)에 존재하는 procedure 를 호출해주는 객체(우편배달부)이다.
		ResultSet rs = null;
		// ResultSet rs 은 select 되어진 결과물이 저장되어지는 곳.
		
		Scanner sc = new Scanner(System.in);
		
		String stno = "";
		String fk_classno = "";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 오러클 드라이브 로딩하는 것이다 
			
			// 연결해야할 DB (오라클 서버)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");
			
			System.out.print("▶ 학번 : ");
			stno = sc.nextLine();
			
			System.out.print("▶ 성명 : ");
			String name = sc.nextLine();
			
			System.out.print("▶ 연락처 : ");
			String tel = sc.nextLine();
			
			System.out.print("▶ 주소 : ");
			String addr = sc.nextLine();
			
			System.out.print("▶ 학급번호 : ");
			fk_classno = sc.nextLine();
			
			// insert가 되야하므로 쓴다. (학번, 이름, 전화번호, 주소, 반 위치)
			String sql = " insert into tbl_student(stno, name, tel, addr, fk_classno) "
					   + " values(to_number(?), ?, ?, ?, ?) ";
			// 또는     + " values(?, ?, ?, ?, ?) ";
			
			// 매핑 시키자!!!
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, stno);
			pstmt.setString(2, name);
			pstmt.setString(3, tel);
			pstmt.setString(4, addr);
			pstmt.setString(5, fk_classno);
			
			int n = pstmt.executeUpdate(); // 실행해라 라는 말이다 DML이므로 Update를 써줘야 한다.
			
			//n이 1이면 정상적으로 들어간 것이다.
			if(n == 1) {
				System.out.println(">>> 데이터 입력성공 !! <<<");
			}
			
		// 파일이 없을 경우에는 	
		} catch (ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
			
			// 에러가 나올때
		} catch (SQLException e) {
			
			// 프라이빗에 위배 되었다면 1번이면 (학번이 같다면 )
			if(e.getErrorCode() == 1) {
				System.out.println(">>> 학번 "+stno+" 은 이미 사용중이므로 다른 학번을 입력하세요!! <<<");
			}
			
			// 에러코드 2291번 이라면 foreign key 위배 (학급이 없는 학급번호를 넣었다면)
			else if(e.getErrorCode() == 2291) {
				System.out.println(">>> 학급번호 "+fk_classno+" 은 존재하지 않습니다. <<<");
				
				try {
					// 오라클에서 알고 싶은 것을 써서 DB에서 얻어온다.
					String sql = " select classno "
							   + " from tbl_class"
							   + " order by classno asc ";
					
					pstmt.close();
					pstmt = conn.prepareStatement(sql);  // 새로운 sql문 을 pstmt에 넣어준다.
					rs = pstmt.executeQuery();     // 실행해라 
					
					StringBuilder sb = new StringBuilder();  //스트링 빌더 사용한다.
					while(rs.next()) {                       // 리턴 타입은 boolean 그 다음 행이 있으면 true 없으면 false
						int classno = rs.getInt("CLASSNO");  // 컬럼 이름은 CLASSNO
						sb.append(classno+", "); 			 //스트링빌더에 담는다.
					}// end of while------------------------
					
					String str_classno = sb.toString();      // 스트링 빌더를 String으로 변환
					System.out.println(">>> 사용가능한 학급번호는 "+ str_classno.substring(0, str_classno.length()-2) +" 입니다. <<<"); 
					 // 마지막에는 ,가 필요 없으므로 subString 을 사용해서 str_classno 문자열의 길이 에서 2개를 빼면 공백 , ','를 빼므로 정상적으로 나온다.
					
					
					// 또다른 에러가 생길수 있으므로 에러 처리를 해준다.
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
				
			}
			
			else {
				e.printStackTrace();
			}
			
		} finally {
			
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		sc.close();
		
		System.out.println("~~~~ 프로그램 종료 ~~~~");

	}// end of main()---------------------------------------

}
