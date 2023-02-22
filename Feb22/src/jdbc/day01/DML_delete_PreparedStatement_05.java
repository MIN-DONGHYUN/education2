package jdbc.day01;

import java.sql.*;
import java.util.Scanner;

public class DML_delete_PreparedStatement_05 {

	public static void main(String[] args) {
		
		
		Connection conn = null;
		// Connection conn 은 데이터베이스 서버와 연결을 맺어주는 자바의 객체이다.
		
		PreparedStatement pstmt = null;
		// PreparedStatement pstmt 은 
		// Connection conn(연결한 DB 서버)에 전송할 SQL문(편지)을 전송(전달)을 해주는 객체(우편배달부)이다.
		
		ResultSet rs = null; 
		// ResultSet rs 은 select 되어진 결과물이 저장되어지는 곳. 
		
		Scanner sc = new Scanner(System.in);
		
		
		try {
			
			// >>> 1. 오라클 드라이버 로딩 <<< // 
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. 어떤 오라클 서버에 연결을 할래? <<< //
			
			System.out.print("▷ 연결할 오라클 서버의 IP 주소 : ");
			String ip = sc.nextLine();
			
			//oracle_user을 하나 생성 
			conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ip+":1521:xe", "JDBC_USER", "gclass");
			

			// >>> 3. SQL문(편지)을 작성한다. <<< //
			
			String sql = " select no, name, msg, to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday "
					+ " from tbl_memo "
					+ " order by no desc ";
			// SQL문 뒤에 ; 를 넣으면 오류이다.!!!!!!!
			// SQL 문을 작성할 때 1줄 마다 잎, 뒤로 공백을 꼭 주도록 한다.
			

			// >>> 4. 연결한 오라클 서버(conn)에 SQL문(편지)을 전달할 객체 PreparedStatement 객체(우편배달부) 생성하기 <<< //
			pstmt = conn.prepareStatement(sql);
			
			
			// >>> 5. PreparedStatement 객체(우편배달부)는 작성된 SQL문(편지)을 오라클 서버에 보내서 실행이 되도록 해야 한다. <<< //
			
			rs = pstmt.executeQuery();
			// SQL문이 DQL문(select) 이므로 ,executeQuery(); 이다
			// pstmt.executeQuery(); 을 실행하면 select 되어진 결과물을 가져오는데 그 타입은 ResultSet 으로 가져온다.
			
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("글번호\t글쓴이\t글내용\t작성일자");
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
			
			StringBuilder sb = new StringBuilder();
			
			while(rs.next()) {  // 리턴 타입은 boolean 그 다음 행이 있으면 true 없으면 false
				/*
	               rs.next() 는 select 되어진 결과물에서 위치(행의 위치)를 다음으로 옮긴 후 
	               행이 존재하면 true 를 리턴해주고, 행이 없으면 false 를 리턴해주는 메소드이다.
	            */
				
				//int no = rs.getInt("NO"); // "NO" 은 select 해온 컬럼명이다. (대문자로 쓰지 않아도 됨)
				// 또는
				int no = rs.getInt(1);    // 1 은 select 해온 컬럼의 위치값으로서 1번째 컬럼을 가리키는 것이다.
						
				
				//String name = rs.getNString("NAME");	// "NAME" 은 select 해온 컬럼명이다. (대문자로 쓰지 않아도 됨)
				// 또는
				String name = rs.getNString(2);  // 2 는 select 해온 컬럼의 위치값으로서 2번째 컬럼을 가리키는 것이다.
				
				
				//String msg = rs.getNString("MSG");	// "MSG" 은 select 해온 컬럼명이다. (대문자로 쓰지 않아도 됨)
				// 또는
				String msg = rs.getNString(3);  // 3 는 select 해온 컬럼의 위치값으로서 3번째 컬럼을 가리키는 것이다.
				
				
				//String WRITEDAY = rs.getNString("WRITEDAY");	// "WRITEDAY" 은 select 해온 컬럼명이다. (대문자로 쓰지 않아도 됨)
				// 또는
				String WRITEDAY = rs.getNString(4);  // 4 는 select 해온 컬럼의 위치값으로서 4번째 컬럼을 가리키는 것이다.
				
				
				
				sb.append(no);
				sb.append("\t" + name);
				sb.append("\t" + msg);
				sb.append("\t" + WRITEDAY + "\n");
				
			} // end of while (rs.next())
			
			
			System.out.println(sb.toString());
			
		/////////////////////////////////////////////////////////////////////////////////
			
			System.out.print("▷ 삭제할 글 번호 : ");
			String no = sc.nextLine();
		
			sql = " select name, msg "
				+ " from tbl_memo "
			    + " where no = ? ";
			
			pstmt.close();   // 이전 pstmt를 닫아주고 다시 사용한다.
			pstmt = conn.prepareStatement(sql);   // 편지를 배달할 우편배달부
			pstmt.setString(1, no);
			
			rs.close();   // 이전 rs를 닫아주고 다시 사용한다.
			rs = pstmt.executeQuery();
			
			if(rs.next())      // 삭제해야할 글번호가 존재하는 경우
			{
				String name = rs.getString(1);   // name 컬럼을 말한다.
				String msg = rs.getString(2);   // msg 컬럼을 말한다.
				
				System.out.println("\n=== 삭제할 내용 ===");
				System.out.println("\n □ 글쓴이 : " + name);
				System.out.println(" □ 글내용 : " + msg);
				
				
				String yn = "";
				do 
				{
					System.out.print(" 정말로 삭제하시겠습니까?[Y/N] : ");
					yn = sc.nextLine();
					
					if("y".equalsIgnoreCase(yn))		// 삭제하겠다
					{
						sql = " delete tbl_memo "
								+ " where no = ? ";
						
						pstmt = conn.prepareStatement(sql);   // 편지를 배달할 우편배달부
						pstmt.setString(1, no);
						
						
						int n = pstmt.executeUpdate();
						
						if(n==1)
						{
							System.out.println(">> 글번호 " + no + "글은 삭제되었습니다. <<\n");
						}
						
						
					}
					else if("n".equalsIgnoreCase(yn))	// 삭제안하겠다
					{
						System.out.println(" >> 데이터 삭제 취소 !! << \n");
					}
					else				// y, n 입력 안함 
					{
						System.out.println(">> Y 또는 n 만 입력하세요!! << \n");
					}
				} while (!("y".equalsIgnoreCase(yn) || "n".equalsIgnoreCase(yn)));
				
			}
			else       			// 삭제해야할 글번호가 존재하지 않는 경우 
			{
				System.out.println(">>> 글 번호 "+ no + "은 존재하지 않습니다 <<<");
			}
		
		}
		
		catch (ClassNotFoundException e) 
		{			
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
		}
		catch (SQLException e) 
		{			
			e.printStackTrace();
		}
		
		// 반납하기 
		finally {
			// 6. 사용하였던 자원을 반납하기 
			// 반납의 순서는 생성순서의 역순으로 한다.
			
			try {
				if(rs != null)
				{
					rs.close();
					rs = null;
				}
				
				if(pstmt != null)
				{
					pstmt.close();   //null 이 존재하기에 null 이 아니면 정상 종료
					pstmt = null;
				}
				if(conn != null)
				{
					conn.close();   //null 이 존재하기에 null 이 아니면 정상 종료
					conn = null;
				}
				
				
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
		}
		
		
		
		sc.close();
		System.out.println("~~~~~~ 프로그램 종료 ~~~~~~~");

	}// end of main

}
