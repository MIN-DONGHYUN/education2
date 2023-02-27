// 게시판에 관련되어진 DAO

package jdbc.day05.board;

import java.util.List;

public interface InterBoardDAO { 

	int write(BoardDTO bdto);  // 게시판 글쓰기 (Transaction 처리 : tbl_board 테이블에 insert + tbl_member 테이블의 point 컬럼에 10씩 증가(update)

	List<BoardDTO> boardList(); // 글목록 보기 
	
	
	
}
