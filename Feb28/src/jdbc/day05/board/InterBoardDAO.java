// 게시판에 관련되어진 DAO

package jdbc.day05.board;

import java.util.*;

public interface InterBoardDAO { 

	int write(BoardDTO bdto);  // 게시판 글쓰기 (Transaction 처리 : tbl_board 테이블에 insert + tbl_member 테이블의 point 컬럼에 10씩 증가(update)

	List<BoardDTO> boardList(); // 글목록 보기 

	BoardDTO viewContents(String boardno);   // 글내용보기 

	void updateViewContents(String boardno);  // 조회수 1 증가 시키기 

	//*** 원글에 대한 댓글을 가져오는것을 말한다 (특정 게시글 글 번호에 대한 tbl_comments 테이블과 tbl_member 테이블을 JOIN 해서 보여준다. *** //
	List<CommentDTO> commentList(String boardno);

	
	int writeComment(CommentDTO cmtdto, boolean bFlag); // 댓글쓰기 (Transaction 처리 : tbl_board 테이블에 insert + tbl_member 테이블의 point 컬럼에 5씩 증가 update
														 // 사용자가 다른 사용자가 쓴 원글에 댓글쓰기를 하면 포인트 점수를 5점씩 증가하도록 하겠습니다.
	// bFlag의 값이 TRUE 이면 원글은 로그인한 사용자가 아닌 다른 사용자가 쓴 글임.
	// bFlag의 값이 False 이면 원글릉 로그인한 사용자가 쓴 글임 

	int updateBoard(Map<String, String> paraMap);   // 글 수정하기 

	
	int deleteBoard(String boardno);  // 글 삭제하기 


	
	
	
	
}
