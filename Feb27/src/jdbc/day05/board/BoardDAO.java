package jdbc.day05.board;

import java.sql.*;
import java.util.*;

public class BoardDAO implements InterBoardDAO {// 실제 구현하는 곳 

	
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
	
	
	// 게시판 글쓰기 (Transaction 처리 : tbl_board 테이블에 insert + tbl_member 테이블의 point 컬럼에 10씩 증가(update) 메소드 구현하기
	@Override
	public int write(BoardDTO bdto) {   
		
		int result = 0;
		
		try
		{
			conn = MyDBConnection.getConn();			//오라클과 자바를 연결 
			
			
			// 이때만 수동 커밋으로 전환하기 
			// !!!!!!!!! Transaction 처리를 위해서 수동 커밋(commit)으로 전환시킨다 !!!!!!!!
			conn.setAutoCommit(false);
			
			
			String sql = " insert into tbl_board(boardno, fk_userid, subject , contents , boardpasswd) "
					   + " values(seq_board.nextval, ?, ?, ?, ?) ";			// 4개의 위치 홀더 사용
			
			pstmt = conn.prepareStatement(sql);  // 우편배달부    
			
			// 매핑하기 
			pstmt.setString(1, bdto.getFk_userid() );
			pstmt.setString(2, bdto.getSubject() );
			pstmt.setString(3, bdto.getContents() );
			pstmt.setString(4, bdto.getBoardpasswd() );
			
			int n1 = pstmt.executeUpdate();   // DML문이므로 update를 사용
			
			if(n1 == 1)		// 결과가 있나 
			{
				sql = " update tbl_member set point = point + 10 "
					+ " where userid = ? ";
				
				pstmt = conn.prepareStatement(sql); // 우편배달부
				
				pstmt.setString(1, bdto.getFk_userid());
				
				int n2 = pstmt.executeUpdate();   // DML문이므로 update를 사용
				
				if(n2 == 1)    // 한 개 행만 바뀔꺼니까 1이 나올때
				{   
					conn.commit();     // 커밋을 해준다.
					result = 1;        // 결과가 있으므로 1을 저장한다.
				}
				
			}
			
		} 
		catch(SQLException e)   // 오류가 발생하면 무조건 rollback 을 해줘야 한다.
		{
			// e.printStackTrace();   // 개발 도중에만 사용하자 
			if(e.getErrorCode() == 2290)   // 2290번의 에러일때
			{
				System.out.println(">> 아이디 " + bdto.getFk_userid() + " 포인트는 30을 초과할 수 없기 때문에 오류가 발생하였습니다.<< \n");
			}
			else
			{
				// e.printStackTrace();   // 개발 도중에만 사용하자 
			}
			
			try
			{
				conn.rollback();   // 롤백을 해준다.
			}
			catch(SQLException e1)
			{
				
			}
			
			result = -1;
				
		}
		
		finally  // 자원 반납하기 
		{
			// 다시 자동 commit으로 복원시킨다. (수동 커밋이 필요한 곳만 제외하고 나머지를 위해서 자동 commit으로 변경)
			try 
			{
				conn.setAutoCommit(true); // auto 커밋으로 복원시킨다.!!!!
			} 
			catch (SQLException e) 
			{
				
				//e.printStackTrace();
			}
			
			close();  // 자원반납하기 
		}
		
		return result;
	}  // end of public int write(BoardDTO bdto) { 


	// 글목록보기 메소드 구현하기 
	@Override
	public List<BoardDTO> boardList() {
		
		List<BoardDTO> boardList = new ArrayList<>();  // ArrayList 사용한다. 값 저장 ==> select 되어진것을 BoardList에 담을 것이다.
														// boardList를 ArratyList로 만듬
		
		try {
			conn = MyDBConnection.getConn();  // 스테틱 되어진 것을 불러온다. 연결할 오라클을 받아옴 
			

			String sql = "select A.boardno \n"+
			"     , A.subject\n"+
			"     , A.NAME\n"+
			"     , A. writeday \n"+
			"     , A. viewcount \n"+
			"     , nvl(CNT.COMMENTCNT, 0) AS COMMENTCNT\n"+
			"from \n"+
			"(\n"+
			"    select B.boardno, B.subject, M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday, B.viewcount\n"+
			"    from tbl_member M join tbl_board B\n"+
			"    ON M.userid = B.fk_userid\n"+
			") A\n"+
			"LEFT JOIN\n"+
			"(\n"+
			"    select fk_boardno, count(*) AS COMMENTCNT \n"+
			"    from tbl_comment \n"+
			"    group by fk_boardno\n"+
			") CNT\n"+
			"ON A.boardno = CNT.fk_boardno\n"+
			"ORDER BY boardno desc";

					
			
			pstmt = conn.prepareStatement(sql);
			
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				BoardDTO bdto = new BoardDTO();    // BoardDTO의 boardList 에 넣어주기 위해 선언??  //반복문 안에 계속 selete 해주기 위해  
				
				// 보드 DTO에 있는 것들
				bdto.setBoardno(rs.getInt("BOARDNO"));   //값을 받아오기위해 
				bdto.setSubject(rs.getString("SUBJECT"));
				
				// 맴버 DTO에 있는 것들 
				MemberDTO member = new MemberDTO(); 
				member.setName(rs.getString("NAME"));		// 이름을 가져옴 
				//member.setPoint(rs.getString("POINT"));   // 포인트가 있으면 사용 
				
				bdto.setMember(member);   // selete에 보드맴버를 선언해왔기 때문에 가능하다.
				
				// 다시 보드 DTO에 있는것들 
				bdto.setWriteday(rs.getString("WRITEDAY"));   // 작성일자를 가져옴
				bdto.setViewcount(rs.getInt("VIEWCOUNT"));    // 조회수를 가져옴
				bdto.setCommentcnt(rs.getInt("COMMENTCNT"));  // 댓글 개수를 가져옴
				
				
				
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				// 지금까지 받아온 값을 LIST에 담는다
				boardList.add(bdto);   //이렇게 단독으로 null이 초기 값이기 때문에 안된다. 
										  //그렇기에 List<BoardDTO> boardList = new ArrayList<>(); 를 써줘야 한다.
				
			} // end of while 
		
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // 자원반납
		}
		
		
		return boardList;
	} // end of public List<BoardDTO> boardList() {

}
