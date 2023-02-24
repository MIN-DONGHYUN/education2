
show user;

--- **** 회원 테이블 생성하기 **** ---
select *
from user_tables
where table_name = 'TBL_MEMBER';

select * from tab;

create table tbl_member
(userseq      number         not null    -- 회원번호(우리는 회원번호를 모른다)
,userid       varchar2(30)   not null    -- 회원아이디 (후보키)
,passwd       varchar2(30)   not null    -- 회원비밀번호
,name         Nvarchar2(20)  not null    -- 회원명
,mobile       varchar2(20)               -- 연락처
,point        number(10) default 0       -- 포인트
,registerday  date default sysdate       -- 가입일자
,status       number(1) default 1        -- status 컬럼의 값이 1 이면 가입된 상태, 0 이면 탈퇴 (가입되어있는 상태에서 아이디와 비번을 보겠다.)
,constraint  PK_tbl_member_userseq primary key(userseq)  -- 회원번호 primary key 로 사용한다.
,constraint  UQ_tbl_member_userid unique(userid)         -- 후보키(유니크 키)
,constraint  CK_tbl_member_status check( status in (0,1) )
);
-- Table TBL_MEMBER이(가) 생성되었습니다.

-- 회원번호가 자동적으로 증가되는 시퀀스 생성하기 
create sequence userseq
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence USERSEQ이(가) 생성되었습니다.

--상태확인
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

--상태확인
select *
from tbl_member
order by userseq desc;

-- 확인해보기 아이디, 비번
select userseq, name, mobile, point, to_char(registerday,'yyyy-mm-dd') AS REGISTERDAY
from tbl_member
where status = 1 and userid = 'leess' and passwd = '1234';

-- 확인해보기 아이디, 비번
select userseq, name, mobile, point, to_char(registerday,'yyyy-mm-dd') AS REGISTERDAY
from tbl_member
where status = 1 and userid = 'leess' and passwd = 'sadasd';




-- 확인해보기 아이디, 비번( 해킹일때 SQL Injection(SQL 주입))
select userseq, name, mobile, point, to_char(registerday,'yyyy-mm-dd') AS REGISTERDAY
from tbl_member
where status = 1 and userid = '' OR 1=1 -- ' and passwd = 'sadasdsdsdsd';
-- 1=1 은 무조건 참이다. 1=2는 거짓 
--  '--' 은 주석문 처리해서 그냥 앞에것만 나오게 했음 

/*
    1	엄정화	010-5245-2562	0	2023-02-23
    2	김정화	010-8245-8562	0	2023-02-23
    3	박정화	010-9245-7562	0	2023-02-23
    4	손정화	010-3245-4562	0	2023-02-23
    5	이순신	010-1234-5678	0	2023-02-23
*/


-- 확인해보기 아이디, 비번( 해킹일때 SQL Injection(SQL 주입))
select userseq, name, mobile, point, to_char(registerday,'yyyy-mm-dd') AS REGISTERDAY
from tbl_member
where status = 1 and userid = 'leess' and passwd = '' OR 1=1 --';


-- 엄정화를 업데이트
update tbl_member set userid = ''' OR 1=1 --'    -- ''가 두개여야만 ' 한개가 데이터로 나온다.
where userseq = 1;

commit;
-- 커밋 완료


--상태확인 (엄정화의 아이디가 변경됨)
select *
from tbl_member
order by userseq desc;


-- 결국 엄정화 아이디를 문자로 보기 때문에 엄정화만 나옴 
select userseq, name, mobile, point,to_char(registerday, 'yyyy-mm-dd ') AS registerday  
from tbl_member  
where status = 1 and userid = ''' OR 1=1 --' and passwd = 'qWer1234$'; 
-- 1	엄정화	010-5245-2562	0	2023-02-23 


-- 엄정화를 업데이트 (원상복구 하기)
update tbl_member set userid = 'eomjh'    -- ''가 두개여야만 ' 한개가 데이터로 나온다.
where userseq = 1;

commit;

-- 현재 상태 확인 
select *
from tbl_member
order by userseq desc;



-- 위치 홀더를 사용하지 않을때 해킹 위험 있음 이름으로 조회 7번 부분을 이해하기 위해 해봄
select userid, name, mobile, point,to_char(registerday, 'yyyy-mm-dd ') AS registerday  
from tbl_member  
where status = 1 and name like '%'
UNION 
SELECT USERID, NULL, NULL, NULL, PASSWD 
FROM TBL_MEMBER --


-- 이렇게도 뚫린다.
select userid, name, mobile, point,to_char(registerday, 'yyyy-mm-dd hh24:mi:ss') AS registerday 
from tbl_member
where status = 1 and name like '%'|| ' 
UNION SELECT USERID, NULL, NULL, NULL, PASSWD FROM TBL_MEMBER -- order by userseq desc;


