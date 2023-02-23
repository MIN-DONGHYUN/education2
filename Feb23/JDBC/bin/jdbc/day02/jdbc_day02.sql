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

--------------------------------------------------------------------------------------------
--2/23 추가
----------------------------------------------------------------------------------------------
select *
from tbl_student
where addr like '%' || '서울' || '%';


create or replace procedure pcd_student_select_many
(p_addr    IN    tbl_student.addr%type
,o_data    OUT   SYS_REFCURSOR  --커서 타입
)
is
begin
    open o_data for  -- select 문을 해줄때 for문 뒤에 결과물이 c_date에 담긴다.
    select S.stno, S.name, S.tel, S.addr, to_char(S.registerdate, 'yyyy-mm-dd hh24:mi:ss') AS registerdate, 
           C.classname, C.teachername 
    from 
    (select *
     from tbl_student
     where addr like '%'|| p_addr ||'%'
    ) S JOIN tbl_class C 
    ON S.fk_classno = C.classno;

end pcd_student_select_many;
-- Procedure PCD_STUDENT_SELECT_MANY이(가) 컴파일되었습니다.


-------------------------------------------------------------------------------------------
create table tbl_member_test
(userid        varchar2(20)
,passwd        varchar2(20) not null
,name          Nvarchar2(10) not null
,constraint    PK_tbl_member_test_userid primary key(userid)
);
--Table TBL_MEMBER_TEST이(가) 생성되었습니다.


/*
    === tbl_member_test01 테이블에 insert 할 수 있는 요일명과 시간을 제한해 두겠습니다. ===
        
    tbl_member_test01 테이블에 insert 할 수 있는 요일명은 월,화,수,목,금 만 가능하며
    또한 월,화,수,목,금 중에 오전 11시 부터 오후 5시 이전까지만(오후 5시 정각은 안돼요) insert 가 가능하도록 하고자 한다.
    만약에 insert 가 불가한 요일명(토,일)이거나 불가한 시간대에 insert 를 시도하면 
    '영업시간(월~금 11:00 ~ 16:59:59 까지) 아니므로 입력불가함!!' 이라는 오류메시지가 뜨도록 한다. 
    */

create or replace procedure pcd_tbl_member_test_insert
     (p_userid      IN tbl_member_test.userid%type
     ,p_passwd      IN tbl_member_test.passwd%type
     ,p_name        IN tbl_member_test.name%type)
     is
        v_length                 number(2);
        v_ch                    varchar2(2);
        v_flag_alphabet         number(1) :=0;
        v_flag_number           number(1) :=0;
        v_flag_special          number(1) :=0;
        
        error_passwd            EXCEPTION;
        error_dayTime           EXCEPTION;
     begin
        
         
         --입력(insert)이 불가한 요일명과 시간대를 알아봅니다. --
              
         if(to_char(sysdate, 'd') in ('1','7') OR
            to_char(sysdate, 'hh24') < '10' OR to_char(sysdate, 'hh24') > '17' ) then raise error_dayTime; -- to_char(sysdate, 'd')==> '1'(일), '2'(월), '3'(화), '4'(수), '5'(목), '6'(금), '7'(토)
         
         else
          
             v_length := length (p_passwd);
             
             if(v_length < 5 or v_length >10) then 
                raise error_passwd;
             else
                  for i in 1..v_length loop
                      v_ch :=  substr(p_passwd, i, 1);
                      
                      if( (v_ch between 'A' and 'Z') or (v_ch between 'a' and 'z') ) then --영문자
                            v_flag_alphabet := 1;
                            
                      elsif (v_ch between '0' and '9' )then
                            v_flag_number := 1;
                            
                      else -- 특수문자 이라면      
                            v_flag_special := 1;
                      end if;    
                   end loop;       
                   
                
                   if( v_flag_alphabet * v_flag_number * v_flag_special) = 1 then
                        insert into tbl_member_test(userid, passwd, name) values (p_userid, p_passwd, p_name);
                    else
                        raise error_passwd;
                    end if;   
                end if;
            end if;
            
          EXCEPTION
           WHEN error_passwd THEN
                RAISE_APPLICATION_ERROR(-20003, '>> 암호는 최소 5글자 이상 10글자 이하 이면서 영문자, 숫자, 특수기호가 혼합되어져야만 합니다. <<');
                --  -20001 은 오류번호로써, 사용자가 정의해주는 EXCEPTION 에 대해서는 오류번호를 -20001 부터 -20999 까지만 사용하도록 오라클에서 비워두었다. 
          WHEN error_dayTime THEN
                RAISE_APPLICATION_ERROR(-20004, '영업시간(월~금 11:00 ~ 16:59:59 까지) 아니므로 입력불가함!!');      
                         
     end pcd_tbl_member_test_insert;
     --Procedure PCD_TBL_MEMBER_TEST_INSERT이(가) 컴파일되었습니다.
     
     select * 
     from tbl_member_test;
     -- 아무것도 없음
     -- 자바에 있으면 결과가 나온다.
     
      
     -- 퀴즈에서 데이터값 입력이 나옴
     select * 
     from tbl_student
     order by stno asc;
