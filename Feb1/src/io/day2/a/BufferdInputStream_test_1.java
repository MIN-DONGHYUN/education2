package io.day2.a;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BufferdInputStream_test_1 {
/*
    >>>>> BufferedInputStream 와 BufferedOutputStream <<<<<
    -- 1 byte 기반 스트림.
    -- 필터스트림(다른말로 보조스트림 이라고 부른다.)
    -- 단독으로 사용할 수 없고, 반드시 노드스트림에 장착되어 사용되는 것이다.
            마치 수영장에서 오리발처럼 보조기구로 사용한다.
            이것을 사용하면 오리발처럼 속도가 향상되므로 많이 사용한다.
            
    -- 읽어올 데이터를 매번 1 byte 마다 읽고,쓰고 한다라면 입.출력에 너무 많은 시간이 소요된다.
            그래서 쓰는 작업없이 메모리 버퍼에 데이터를 한꺼번에 쭉~~ 읽기만 하여 모아두면
            그만큼 쓰는 작업이 없으므로 읽기 속도는 빨라질 것이다.
            그리고 나서 메모리 버퍼에 읽어서 모아두었던 내용을 한방에 쓰기를 하면 매번 1byte 씩 쓰는 것보다
            속도가 빨라진다.
            
     BufferedInputStream 와  BufferedOutputStream 의 기본 버퍼크기는 512 byte 이다.
     
     [예제]
        필터스트림(보조스트림)을 이용해서 키보드로 부터 입력받고, 
        입력받은 그 내용을 모니터(콘솔화면)에 출력하고,
        또한 동시에 파일에도 출력해본다.
        
     >>> 데이터소스 : 키보드(System.in --> 노드스트림)
                  + 필터스트림(보조스트림)으로 BufferedInputStream 을 사용함.
     
     >>> 데이터목적지 : 모니터(System.out --> 노드스트림)
                   + 필터스트림(보조스트림)으로  BufferedOutputStream 을 사용함.
                   
                                      파일(FileOutputStream --> 노드스트림)
                   + 필터스트림(보조스트림)으로  BufferedOutputStream 을 사용함.                                                 
       
  */   
	
	
	public static void main(String[] args) {
		
		try 
		{
			// 입력노드스트림 ==> 키보드(System.in) 
			// 입력노드스트림에 필터스트림(보조스트림)인 BufferedInputStream 을 장착한다.
			//BufferedInputStream bist = new BufferedInputStream(System.in); 
			BufferedInputStream bist =new BufferedInputStream(System.in, 1024);		//1024는 1kb
			// 노드스트림인 System.in(키보드)에 필터스트림(보조스트림)을 장착함.
			// bist 의 버퍼크기는 1024Byte로 설정함 기본은 512 byte



			// 출력노드스트림 ==> 모니터(System.out) 
			// 출력노드스트림에 필터스트림(보조스트림)인 BufferedOutputStream 을 장착한다.
			BufferedOutputStream bost_1 = new BufferedOutputStream(System.out, 1024);
			// 출력노드스트림인 System.out(모니터) 에 필터스트림(보조스트림)을 장착함
			// bost_1 의 버퍼크기는 1024Byte로 설정함 기본은 512 byte



			// 출력노드스트림 ==> 파일(FileOutputStream) 
			// 출력노드스트림에 필터스트림(보조스트림)인 BufferedOutputStream 을 장착한다.
			String file_name = "C:\\NCS\\iotest_data\\나의소개.txt";
			
			FileOutputStream fost = new FileOutputStream(file_name);
			
			BufferedOutputStream bost_2 = new BufferedOutputStream(fost, 1024);
			// 출력노드스트림인 fost(파일) 에 필터스트림(보조스트림)을 장착함
			// bost_2 의 버퍼크기는 1024Byte로 설정함 기본은 512 byte
			
			byte[] dataArr = new byte[512];		//배열 설정
			
			int inputLength = 0;				// 실제 읽어온 크기
			int totalByte = 0;					// 몇번 반복
			
			while( (inputLength = bist.read(dataArr)) != -1 ) 		// 입력을 원래는 System.in.read로 했지만 지금은 bist로 하자 
			{
				// 모니터에 출력하기
				bost_1.write(dataArr, 0, inputLength);
				bost_1.flush();
				
				// 파일에 출력하기 
				bost_2.write(dataArr, 0, inputLength);
				bost_2.flush();
				
				totalByte += inputLength;
			}// end of while
			
			
			
			
			// !!!!!!!!닫을 때는 보조스트림(필터스트림)부터 먼저 닫고, 그 다음에 노드스트림을 닫는다.!!!!!!!!!!//
			bost_2.close();
			fost.close();
			
			bost_1.close(); // 모니터는 굳이 할 필요는 없음 
			bist.close();   // 키보드 또한 굳이 할 핗요는 없음 
			
			System.out.println(">> totalByte : " + totalByte + "byte 씀 <<");
			
		} 
		catch (FileNotFoundException e) {
			System.out.println(">> 파일이 존재하지 않습니다. << ");
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
