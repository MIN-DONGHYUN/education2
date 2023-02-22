package jdbc.day01;

import java.sql.*;

public class DDL_create_PreparedStatement_06 {

	public static void main(String[] args) {
		
		
		Connection conn = null;
		// Connection conn 은 데이터베이스 서버와 연결을 맺어주는 자바의 객체이다.
		
		PreparedStatement pstmt = null;
		// PreparedStatement pstmt 은 
		// Connection conn(연결한 DB 서버)에 전송할 SQL문(편지)을 전송(전달)을 해주는 객체(우편배달부)이다.
		
		ResultSet rs = null; 
		// ResultSet rs 은 select 되어진 결과물이 저장되어지는 곳. 
		
		try {
			
			// >>> 1. 오라클 드라이버 로딩 <<< // 
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// >>> 2. 어떤 오라클 서버에 연결을 할래? <<< //
			
			
			
			//oracle_user을 하나 생성 
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");
			// 127.0.0.1 을 "loop back address" 라고 부른다. 자기자신의 IP를 말하는 것이다.
			
			

			// >>> 3. SQL문(편지)을 작성한다. <<< //
			
			String sql_1 = " select * "
					+ " from user_tables "
					+ " where table_name = 'TBL_EXAM_TEST' ";
			// SQL문 뒤에 ; 를 넣으면 오류이다.!!!!!!!
			// SQL 문을 작성할 때 1줄 마다 잎, 뒤로 공백을 꼭 주도록 한다.
			
			
			// TBL_EXAM_TEST 가 존재하면 삭제 (부모 테이블이여도 삭제 가능하게 cascade constraints 사용)
			// (foreign 키 자식 부터 지우고 부모도 지움
			String sql_2 = " drop table TBL_EXAM_TEST cascade constraints purge ";
			
			//테이블 생성 
			String sql_3 = " create table TBL_EXAM_TEST "
						 + " (no      number(4) "
					     + " ,name   Nvarchar2(10) "
						 + " ,msg    Nvarchar2(200) "
					     + " ) ";
			
			// 시퀀스 있다면 삭제
			String sql_4 = " select * "
					     + " from user_sequences "
				         + " where sequence_name = 'SEQ_EXAM_TEST' ";
			
			String sql_5 = " drop sequence SEQ_EXAM_TEST ";
			
			// 시퀀스 생성 
			String sql_6 = " create sequence seq_exam_test "
					+ " start with 1 "
					+ " increment by 1 "
					+ " nomaxvalue "
					+ " nominvalue "
					+ " nocycle "
					+ " nocache ";
			
			
			String sql_7 = " insert into tbl_exam_test(no, name, msg) "
					     + " values(SEQ_EXAM_TEST.nextval, '이순신', '안녕하세요? 이순신 입니다') ";
			
			String sql_8 = " select * "
				         + " from tbl_exam_test "
				         + " order by no desc ";
			
			
			// >>> 4. 연결한 오라클 서버(conn)에 SQL문(편지)을 전달할 객체 PreparedStatement 객체(우편배달부) 생성하기 <<< //
			pstmt = conn.prepareStatement(sql_1);
			
			
			
			// >>> 5. PreparedStatement 객체(우편배달부)는 작성된 SQL문(편지)을 오라클 서버에 보내서 실행이 되도록 해야 한다. <<< //
			
			rs = pstmt.executeQuery();
			// SQL문이 DQL문(select) 이므로 ,executeQuery(); 이다
			// pstmt.executeQuery(); 을 실행하면 select 되어진 결과물을 가져오는데 그 타입은 ResultSet 으로 가져온다.
			
			int n;
			if(rs.next()) 
			{
				// 'TBL_EXAM_TEST' 테이블이 존재하는 경우
				
				// 'TBL_EXAM_TEST' 테이블을 drop 한다.
				pstmt.close();
				pstmt = conn.prepareStatement(sql_2);
				
				n = pstmt.executeUpdate();
				/*  
				 	.executeUpdate(); 은 SQL문이 DML문(insert, update, delete, merge) 이거나 
		            SQL문이 DDL문(create, drop, alter, truncate) 일 경우에 사용된다. 
		
					SQL문이 DML문이라면 return 되어지는 값은 적용되어진 행의 개수를 리턴시켜준다.
					예를 들어, insert into ... 하면 1 개행이 입력되므로 리턴값은 1 이 나온다. 
					   update ... 할 경우에 update 할 대상의 행의 개수가 5 이라면 리턴값은 5 가 나온다. 
					   delete ... 할 경우에 delete 되어질 대상의 행의 개수가 3 이라면 리턴값은 3 이 나온다.
					
					SQL문이 DDL문(create, alter 등등)이라면 return 되어지는 값은 무조건 0 이 리턴된다. 
				*/
				
				System.out.println("롹인용 drop table : " + n);
				// 롹인용 drop table : 0
				
			}
			
			pstmt = conn.prepareStatement(sql_3);   // 테이블을 만드는 것 
			n = pstmt.executeUpdate();
			System.out.println("롹인용 create table : " + n);
			// 롹인용 create table : 0
			
			
			pstmt = conn.prepareStatement(sql_4);   // 시퀀스가 있는지 없는지 검사 
			rs.close();
			rs = pstmt.executeQuery();
			
			if(rs.next()) 
			{
				// 'SEQ_EXAM_TEST' 시퀀스가 존재하는 경우
				
				// 'SEQ_EXAM_TEST' 시퀀스를 drop
				
				pstmt = conn.prepareStatement(sql_5);   // 시퀀스 drop (DDL문)
				n = pstmt.executeUpdate();
				System.out.println("롹인용 drop sequence : " + n);
				// 롹인용 drop sequence table : 0
			}
			
			pstmt = conn.prepareStatement(sql_6);   // 시퀀스을 만드는 것 
			n = pstmt.executeUpdate();
			System.out.println("롹인용 create sequence : " + n);
			// 롹인용 create sequence table : 0
			
			
			pstmt = conn.prepareStatement(sql_7);   // 시퀀스을 insert
			n = pstmt.executeUpdate();
			System.out.println("롹인용 insert into tbl_exam_test : " + n);
			// 롹인용 insert into tbl_exam_test : 1
			
			
			pstmt = conn.prepareStatement(sql_8);   // 결과물 
			rs = pstmt.executeQuery();
			
			StringBuilder sb = new StringBuilder();
			int cnt = 0;
			
			while(rs.next()) 
			{
				cnt++;
				
				if(cnt==1)
				{
					sb.append("-----------------------------------\n");
					sb.append("일련번호\t성명\t글내용\n");
					sb.append("-----------------------------------\n");
					
				}
				sb.append( rs.getInt("NO") + "\t" + rs.getString("NAME") + "\t" + rs.getString("MSG") + "\n" );  //NO는 컬럼이름으로 대문자 사용 
			} // end of while
			
			if(cnt > 0) 
			{
				System.out.println(sb.toString());
			}
			else
			{
				System.out.println(" >> 입력된 데이터가 없습니다. << ");
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
		

		finally {

			
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
		
		
		System.out.println("~~~~~~ 프로그램 종료 ~~~~~~~");

	}// end of main

}
