------ day02 01번 -----

show user;
-- USER이(가) "JDBC_USER"입니다.

-- 1) 학급테이블 생성
create table tbl_class
(classno        number(3)
,classname      varchar2(100)
,teachername    varchar2(20)
,constraint PK_tbl_class_classno primary key(classno)
);

create sequence seq_classno
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_class(classno, classname, teachername) 
values(seq_classno.nextval, '자바웹프로그래밍A', '김샘'); 

insert into tbl_class(classno, classname, teachername) 
values(seq_classno.nextval, '자바웹프로그래밍B', '이샘');

insert into tbl_class(classno, classname, teachername) 
values(seq_classno.nextval, '자바웹프로그래밍C', '서샘');

commit;

select *
from tbl_class;
/*
    1	자바웹프로그래밍A	김샘
    2	자바웹프로그래밍B	이샘
    3	자바웹프로그래밍C	서샘
*/

-- 2) 학생테이블 생성 
create table tbl_student
(stno           number(8)               -- 학번
,name           varchar2(20) not null   -- 학생명
,tel            varchar2(15) not null   -- 연락처
,addr           varchar2(100)           -- 주소
,registerdate   date default sysdate    -- 입학일자
,fk_classno     number(3) not null      -- 학급번호
,constraint PK_tbl_student_stno primary key(stno)
,constraint FK_tbl_student_classno foreign key(fk_classno) references tbl_class(classno)
);    

-- 학번에 사용할 시퀀스 생성
create sequence seq_stno
start with 9001
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into tbl_student(stno, name, tel, addr, registerdate, fk_classno)
values(seq_stno.nextval, '이순신', '02-234-5678', '서울시 강남구 역삼동', default, 1);

insert into tbl_student(stno, name, tel, addr, registerdate, fk_classno)
values(seq_stno.nextval, '김유신', '031-345-8876', '경기도 군포시', default, 2);

insert into tbl_student(stno, name, tel, addr, registerdate, fk_classno)
values(seq_stno.nextval, '안중근', '02-567-1234', '서울시 강서구 화곡동', default, 2);

insert into tbl_student(stno, name, tel, addr, registerdate, fk_classno)
values(seq_stno.nextval, '엄정화', '032-777-7878', '인천시 송도구', default, 3);

insert into tbl_student(stno, name, tel, addr, registerdate, fk_classno)
values(seq_stno.nextval, '박순신', '02-888-9999', '서울시 마포구 서교동', default, 3);

commit;

select *
from tbl_student;
/*
    9001	이순신	02-234-5678	서울시 강남구 역삼동	23/02/22	1
    9002	김유신	031-345-8876	경기도 군포시	23/02/22	2
    9003	안중근	02-567-1234	서울시 강서구 화곡동	23/02/22	2
    9004	엄정화	032-777-7878	인천시 송도구	23/02/22	3
    9005	박순신	02-888-9999	서울시 마포구 서교동	23/02/22	3
*/

/*
>>>> Stored Procedure 란? <<<<<
   Query 문을 하나의 파일형태로 만들거나 데이터베이스에 저장해 놓고 함수처럼 호출해서 사용하는 것임.
   Stored Procedure 를 사용하면 연속되는 query 문에 대해서 매우 빠른 성능을 보이며, 
   코드의 독립성과 함께 보안적인 장점도 가지게 된다.
*/


-- 학생 한명만 해보자 
create or replace procedure pcd_student_select_one
(p_stno           IN   tbl_student.stno%type     --- OUT 모드를 사용한다.
,o_name           OUT  tbl_student.name%type     --- OUT 모드를 사용한다.
,o_tel            OUT  tbl_student.tel%type     --- OUT 모드를 사용한다.
,o_addr           OUT  tbl_student.addr%type     --- OUT 모드를 사용한다.
,o_registerdate   OUT  varchar2
,o_classname      OUT  tbl_class.classname%type
,o_teachername    OUT  tbl_class.teachername%type
)
is
   v_cnt  number(1);  
begin
      select count(*) INTO v_cnt 
      from tbl_student 
      where stno = p_stno;
      
      if v_cnt = 0 then
         o_name := null;
         o_tel := null;
         o_addr := null;
         o_registerdate := null;
         o_classname := null;
         o_teachername := null;
      else
         select S.name, S.tel, S.addr, to_char(S.registerdate, 'yyyy-mm-dd hh24:mi:ss'),
                C.classname, C.teachername 
                INTO 
                o_name, o_tel, o_addr, o_registerdate, o_classname, o_teachername 
         from 
         (
          select *
          from tbl_student
          where stno = p_stno
         ) S JOIN tbl_class C 
         on S.fk_classno = C.classno;
      end if;

end pcd_student_select_one;
-- Procedure PCD_STUDENT_SELECT_ONE이(가) 컴파일되었습니다.

--------------------------------------------------------------------------------------------
show user;

select *
from HR.employees;
-- grant를 하게 되면 실행이 잘 된다 -> 107개 행 
-- revote 가 되면 실행이 안되고 오류가 발생한다.

select *
from HR.departments;
-- 안나옴 

-- 나온다.
select *
from HR.view_employees_a;


