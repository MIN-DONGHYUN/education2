//11 시에 만 입력이 가능하기 11시 이외에는 오류가 나게 하여 exception을 연습해 봅시다.

package jdbc.day02;

import java.sql.*;
import java.util.Scanner;

/*
 == 먼저 jdbc_day02.sql 파일을 열어서 tbl_class 테이블 및 tbl_student 테이블을 생성한다. 
 == 그리고 아래와 같이 오라클에서 프로시저를 생성해야 한다. ==

>>>> Stored Procedure 란? <<<<<
	Query 문을 하나의 파일형태로 만들거나 데이터베이스에 저장해 놓고 함수처럼 호출해서 사용하는 것임.
	Stored Procedure 를 사용하면 연속되는 query 문에 대해서 매우 빠른 성능을 보이며, 
	코드의 독립성과 함께 보안적인 장점도 가지게 된다.

/*
   === tbl_member_test 테이블에 insert 할 수 있는 요일명과 시간을 제한해 두겠습니다. ===
        
   tbl_member_test 테이블에 insert 할 수 있는 요일명은 월,화,수,목,금 만 가능하며
   또한 월,화,수,목,금 중에 오전 11시 부터 오후 5시 이전까지만(오후 5시 정각은 안돼요) insert 가 가능하도록 하고자 한다.
   만약에 insert 가 불가한 요일명(토,일)이거나 불가한 시간대에 insert 를 시도하면 
   '영업시간(월~금 11:00 ~ 16:59:59 까지) 아니므로 입력불가함!!' 이라는 오류메시지가 뜨도록 한다. 


create or replace procedure pcd_tbl_member_test_insert
(p_userid   IN   tbl_member_test.userid%type
,p_passwd   IN   tbl_member_test.passwd%type
,p_name     IN   tbl_member_test.name%type
)
is
    v_length  number(2);
    error_dayTime  exception;
    error_passwd   exception;
    v_ch      varchar2(1);
    v_flag_alphabet number(1) := 0;
    v_flag_number   number(1) := 0; 
    v_flag_special  number(1) := 0;
begin
   
    -- 입력(insert)이 불가한 요일명과 시간대를 알아봅니다. --
    if( to_char(sysdate, 'd') in('1','7') OR  -- to_char(sysdate, 'd') ==> '1'(일), '2'(월), '3'(화), '4'(수), '5'(목), '6'(금), '7'(토) 
        to_char(sysdate, 'hh24') < '11' OR to_char(sysdate, 'hh24') > '16' ) then raise error_dayTime; 
   
    else  -- 암호를 검사하겠다.
   
        v_length := length(p_passwd);
        
        if(v_length < 5 or v_length > 10) then 
              raise error_passwd;
        else 
              for i in 1..v_length loop
                  v_ch := substr(p_passwd, i, 1);
              
                  if( (v_ch between 'A' and 'Z') OR (v_ch between 'a' and 'z') ) then -- 영문자 이라면 
                        v_flag_alphabet := 1;
                        
                  elsif(v_ch between '0' and '9') then -- 숫자 이라면  
                        v_flag_number := 1;
                        
                  else -- 특수문자 이라면
                        v_flag_special := 1;  
                  end if;
              end loop;
              
              if(v_flag_alphabet * v_flag_number * v_flag_special = 1) then 
                  insert into tbl_member_test(userid, passwd, name) values(p_userid, p_passwd, p_name);
              else
                  raise error_passwd;
              end if;
              
        end if;
    
    end if;
    
    exception
        when error_dayTime then
             raise_application_error(-20004, '영업시간(월~금 11:00 ~ 16:59:59 까지) 아니므로 입력불가함!!');
        when error_passwd then
             raise_application_error(-20003, '암호는 최소 5글자 이상 10글자 이하 이면서 영문자, 숫자, 특수기호가 혼합되어져야만 합니다.');                 
   
end pcd_tbl_member_test_insert;
-- Procedure PCD_TBL_MEMBER_TEST_INSERT이(가) 컴파일되었습니다.
  
*/


public class Procedure_insert_exception_CallableStatement_03 {

	public static void main(String[] args) {
		
		Connection conn = null;
		// Connection conn 은 데이터베이스 서버와 연결을 맺어주는 자바의 객체이다.
		
		CallableStatement cstmt = null;
		// CallableStatement cstmt 은 Connection conn(연결한 DB 서버)에 존재하는 Procedure 를 호출해주는 객체(우편배달부)이다. 
		
		String userid = "";
		
		try {
			// >>> 1. 오라클 드라이버 로딩 <<<
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
			// >>> 2. 어떤 오라클 서버에 연결을 할래? <<<
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:xe", "JDBC_USER", "gclass");
			
			
			// >>> 3. Connection conn 객체를 사용하여 prepareCall() 메소드를 호출함으로써
			//        CallableStatement cstmt 객체를 생성한다.
			//        즉, 우편배달부(택배기사) 객체 만들기
			
			cstmt = conn.prepareCall("{call pcd_tbl_member_test_insert(?,?,?)}");
			/*
			   오라클 서버에 생성한 프로시저 pcd_tbl_member_test_insert 의 
			   매개변수 갯수가 3개 이므로 ? 를 3개 준다.
			   
			   프로시저의 IN mode 로 되어진 파라미터에 값을 넣어줄때는 
			   cstmt.setXXX() 메소드를 사용한다. 
			*/
			
			Scanner sc = new Scanner(System.in);
			System.out.print("▷ 아이디 : "); 
			userid = sc.nextLine();
			
			System.out.print("▷ 비밀번호 : "); 
			String passwd = sc.nextLine();
			
			System.out.print("▷ 성명 : "); 
			String name = sc.nextLine();
			
			
			cstmt.setString(1, userid); // 숫자 1 은 프로시저 파라미터중 첫번째 파라미터인 IN 모드의 ? 를 말한다.
			cstmt.setString(2, passwd); // 숫자 2 은 프로시저 파라미터중 두번째 파라미터인 IN 모드의 ? 를 말한다.
			cstmt.setString(3, name);   // 숫자 3 은 프로시저 파라미터중 세번째 파라미터인 IN 모드의 ? 를 말한다.
			
						
			// >>> 4. CallableStatement cstmt 객체를 사용하여 오라클의 프로시저 실행하기 <<<
			int n = cstmt.executeUpdate();  // 오라클 서버에게 해당 프로시저를 실행해라는 것이다.  
		 // 프로시저의 실행은 cstmt.executeUpdate(); 또는 cstmt.execute(); 이다. 
			
			if(n == 1) {
				System.out.println(">>> 데이터 입력 성공!! <<<");
			}
			
			sc.close();
			
		} catch (ClassNotFoundException e) {
			System.out.println(">>> ojdbc8.jar 파일이 없습니다. <<<");
		} catch (SQLException e) {
		   
			 if(e.getErrorCode() == 20003 || e.getErrorCode() == 20004) { 
				 System.out.println(e.getMessage()); 
		     }
			 else if(e.getErrorCode() == 1) {
				 System.out.println(">>> 아이디 "+userid+" 은 현재 사용중이므로 다른 아이디로 입력하세요!! <<<"); 
			 } 
			 else {
				 e.printStackTrace(); 
			 }
			
		} finally {
			// >>> 5. 사용하였던 자원을 반납하기 <<< 
			// 반납의 순서는 생성순서의 역순으로 한다.
			
			try {
				  if(cstmt != null) {
					  cstmt.close();
					  cstmt = null;
				  }
				  
				  if(conn != null) {
				      conn.close();
				      conn = null;
				  }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		System.out.println("~~~~ 프로그램 종료 ~~~~");

	}// end of main()-------------------------------------

}
