show user;
-- USER이(가) "JDBC_USER"입니다.

select *
from tbl_member
order by userseq desc;
/*
5	leess	1234	이순신	010-1234-5678	0	23/02/23	0
4	sonjh	qWer1234$	손정화	010-3245-4562	0	23/02/23	1
3	parkjh	qWer1234$	박정화	010-9245-7562	0	23/02/23	1
2	kimjh	qWer1234$	김정화	010-8245-8562	0	23/02/23	1
1	eomjh	qWer1234$	엄정화	010-5245-2562	0	23/02/23	1
*/

update tbl_member set status = 1;
-- 5개 행 이(가) 업데이트되었습니다.

update tbl_member set status = 0
where userid = 'sonjh';
-- 1 행 이(가) 업데이트되었습니다.    sonjh 는 탈퇴처리라고 처리하기 위해 update 한다.

commit;
-- 커밋 완료.

-- 형태확인 
desc tbl_member;
/*
    이름          널?       유형            
    ----------- -------- ------------- 
    USERSEQ     NOT NULL NUMBER        
    USERID      NOT NULL VARCHAR2(30)  
    PASSWD      NOT NULL VARCHAR2(30)  
    NAME        NOT NULL NVARCHAR2(20) 
    MOBILE               VARCHAR2(20)  
    POINT                NUMBER(10)    
    REGISTERDAY          DATE          
    STATUS               NUMBER(1)     

*/


----- **** 게시판 테이블 생성하기 **** -----
create table tbl_board
(boardno       number         not null     --- 글번호
,fk_userid     varchar2(30)   not null     --- 작성자 아이디
,subject       Nvarchar2(100) not null     --- 글제목
,contents     Nvarchar2(200) not null     --- 글 내용  -- contents로 변경해야함 
,writeday      date default sysdate not null  --- 작성일자 
,viewcount     number default 0 not null         --- 조회수 
,boardpasswd   varchar2(20)     not null         --- 글암호 
,constraint PK_tbl_board_boardno primary key (boardno)
,constraint FK_tbl_board_fk_userid foreign key (fk_userid) references tbl_member (userid)
);
-- Table TBL_BOARD이(가) 생성되었습니다.


-- 시퀀스 생성 
create sequence seq_board
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_BOARD이(가) 생성되었습니다.

-- 현재 테이블 형태 확인 
desc tbl_board;

-- 현재 상황 (글쓰기 내용)
select *
from tbl_board
order by boardno desc;



---- **** 댓글 테이블 생성하기 **** ----
create table tbl_comment
(commentno        number               not null   -- 댓글번호
,fk_boardno       number               not null   -- 원글의 글번호
,fk_userid        varchar2(30)         not null   -- 작성자 아이디 
,contents         Nvarchar2(100)       not null   -- 댓글 내용
,writeday         date default sysdate not null   -- 작성일자 
,constraint       Pk_tbl_comment_commentno primary key (commentno) 
,constraint       Fk_tbl_comment_fk_boardno foreign key(fk_boardno) references tbl_board(boardno) on delete cascade
,constraint       Fk_tbl_comment_fk_userid  foreign key(fk_userid) references tbl_member(userid)
);
-- Table TBL_COMMENT이(가) 생성되었습니다.


-- 시퀀스 생성 
create sequence seq_comment
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_COMMENT이(가) 생성되었습니다.

-- 현재 댓글 상황 
select *
from tbl_comment;


--------------------------------------------------------------------------------------
/*
    Transaction(트렌잭션) 처리 실습을 위해서 
    tbl_member 테이블의 Point 컬럼의 값은 최대 30을 넘지 못하도록 check 재약울 걸도록 하겠습니다
*/
--------------------------------------------------------------------------------------

-- 체크제약을 걸어주자 
alter table tbl_member
add constraint ck_tbl_member_point check( point between 0 and 30);
-- Table TBL_MEMBER이(가) 변경되었습니다.


-- 체크제약이 잘되는지 확인해 보기 
update tbl_member set point = 40
where userid = 'leess';
-- 오류 보고 - ORA-02290: 체크 제약조건(JDBC_USER.CK_TBL_MEMBER_POINT)이 위배되었습니다


insert into tbl_comment(commentno, fk_boardno , fk_userid , contents)
values(seq_comment.nextval, 2, 'eomjh' , '즐거운 하루라는 글이 너무 좋아요 ' );
--1 행 이(가) 삽입되었습니다.

insert into tbl_comment(commentno, fk_boardno , fk_userid , contents)
values(seq_comment.nextval, 2, 'eomjh' , '글 잘보고 갑니다' );
-- --1 행 이(가) 삽입되었습니다.
commit;



select *
from tbl_board
order by boardno desc; sdsd


                    --  [퀴즈]
            -->>>>> 이렇게 만들어 보자 <<<<< -- 
--  글번호    글제목   작성자명    작성일자     조회수 
/*
5	저는 가수 엄정화입니다 반갑습니다!!![0] 	         엄정화	23/02/27	0	
3	세번째 글쓰기 입니당~[0]                         이순신	23/02/27	0	
2	두번째 글쓰기 입니닷[2]                          이순신  23/02/27	0	
1	첫번째 글입니다 ㅎㅎㅎ[0]	                     이순신  23/02/27	    0	
*/

select A.boardno AS 글번호
     , A.subject || '[' || nvl(CNT.COMMENTCNT, 0) || ']' AS 글제목
     , A.NAME AS 작성자명
     , A. writeday AS 작성일자
     , A. viewcount AS 조회수 
from 
(
    select B.boardno, B.subject, M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday, B.viewcount
    from tbl_member M join tbl_board B
    ON M.userid = B.fk_userid
) A
LEFT JOIN
(
    select fk_boardno, count(*) AS COMMENTCNT 
    from tbl_comment 
    group by fk_boardno
) CNT
ON A.boardno = CNT.fk_boardno
ORDER BY boardno desc;





----------------------------------------------------------------------------
select A.boardno 
     , A.subject
     , A.NAME
     , A. writeday 
     , A. viewcount 
     , nvl(CNT.COMMENTCNT, 0) AS COMMENTCNT
from 
(
    select B.boardno, B.subject, M.name, to_char(B.writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday, B.viewcount
    from tbl_member M join tbl_board B
    ON M.userid = B.fk_userid
) A
LEFT JOIN
(
    select fk_boardno, count(*) AS COMMENTCNT 
    from tbl_comment 
    group by fk_boardno
) CNT
ON A.boardno = CNT.fk_boardno
ORDER BY boardno desc;


