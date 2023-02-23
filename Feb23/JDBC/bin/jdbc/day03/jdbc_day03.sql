
show user;

--- **** 회원 테이블 생성하기 **** ---
select *
from user_tables
where table_name = 'TBL_MEMBER';

select * from tab;

create table tbl_member
(userseq      number         not null    -- 회원번호
,userid       varchar2(30)   not null    -- 회원아이디
,passwd       varchar2(30)   not null    -- 회원비밀번호
,name         Nvarchar2(20)  not null    -- 회원명
,mobile       varchar2(20)               -- 연락처
,point        number(10) default 0       -- 포인트
,registerday  date default sysdate       -- 가입일자
,status       number(1) default 1        -- status 컬럼의 값이 1 이면 가입된 상태, 0 이면 탈퇴
,constraint  PK_tbl_member_userseq primary key(userseq)
,constraint  UQ_tbl_member_userid unique(userid)
,constraint  CK_tbl_member_status check( status in (0,1) )
);
-- Table TBL_MEMBER이(가) 생성되었습니다.

create sequence userseq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence USERSEQ이(가) 생성되었습니다.

select *
from tbl_member
order by userseq desc;


insert into tbl_member(userseq, userid, passwd, name, mobile)
values(userseq.nextval, 'eomjh', 'qWer1234$', '엄정화', '010-5245-2562');

insert into tbl_member(userseq, userid, passwd, name, mobile)
values(userseq.nextval, 'kimjh', 'qWer1234$', '김정화', '010-8245-8562');

insert into tbl_member(userseq, userid, passwd, name, mobile)
values(userseq.nextval, 'parkjh', 'qWer1234$', '박정화', '010-9245-7562');

insert into tbl_member(userseq, userid, passwd, name, mobile)
values(userseq.nextval, 'sonjh', 'qWer1234$', '손정화', '010-3245-4562');

commit;

select *
from tbl_member
order by userseq desc;


select userseq, name, mobile, point, to_char(registerday,'yyyy-mm-dd') AS REGISTERDAY
from tbl_member
where status = 1 and userid = 'leess' and passwd = '1234';




