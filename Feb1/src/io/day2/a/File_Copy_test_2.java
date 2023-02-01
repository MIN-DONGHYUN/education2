package io.day2.a;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class File_Copy_test_2 {
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
        필터스트림(보조스트림)을 이용해서 파일로 부터 입력받고, 
        입력받은 그 내용을 파일에 출력해본다., 즉 파일 복사하기 
        
     >>> 데이터소스 : 파일(FileinputStream --> 노드스트림)
                  + 필터스트림(보조스트림)으로 BufferedInputStream 을 사용함.
     
     >>> 데이터목적지 : 파일(FileOutputStream --> 노드스트림)
                   + 필터스트림(보조스트림)으로  BufferedOutputStream 을 사용함.                                                 
       
  */   
	
	
	public static void main(String[] args) {
		
		try
		{
			Scanner sc = new Scanner(System.in);
			
			System.out.print(">> 복사할 원본파일명(절대경로)을 입력 => ");
			
			String src_fileName = sc.nextLine(); 		   // 복사할 파일명 입력
		    //C:/NCS/iotest_data/원본/_eclipse-jee-2022-12-R-win32-x86_64.zip
		    
			
		    System.out.print(">> 목적 파일명(절대경로)을 입력 => ");
		    
		    String target_fileName = sc.nextLine();;        // 저장할 파일 이름 지정
		    //C:/NCS\iotest_data/복사본/_eclipse-jee-2022-12-R-win32-x86_64
		    
		    FileInputStream fist = new FileInputStream(src_fileName);
		    //FileInputStream 생성 : 접속점이 파일인 것으로 특정 파일에 빨대를 꽃아 파일의 내용물을 1byte로 받아옴  
		    
		    BufferedInputStream bist = new BufferedInputStream(fist,1024*1024);	//1MB
		    // 노드 스트림인 fist에 필터 스트림(보조 스트림)을 장착함 
		    
		    FileOutputStream fost = new FileOutputStream(target_fileName);
		    // FileOutputStream 생성 : 접속점이 파일인 것으로 특정 파일에 빨대를 꽃아 파일의 내용물을 1byte로 받아옴  
		    
		    BufferedOutputStream bost = new BufferedOutputStream(fost,1024*1024);	//1MB
			// 노드 스트림인 fost 에 필터스트림(보조 스트림)을 장착함
		    // bost의 버퍼의 크기는 1024*1024 byte로 했다.
		    
		    byte[] dataArr = new byte[1024*1024];
		    
		    int inputLength = 0;
		    int totalByte = 0;
		    int sharpCount = 0; 	// 한개당 1MB로 표시 
		    
		    while( (inputLength =  bist.read(dataArr)) != -1)
		    {
		    	
		    	// 파일에 출력하기 
		    	bost.write(dataArr, 0, inputLength);
		    	bost.flush();
		    	
		    	totalByte += inputLength; 	// totalByte에 누적 
		    	
		    	if(inputLength == 1024*1024)	// 1MB일때마다 
		    	{
		    		System.out.print("#");
		    		sharpCount++; 		// #이 몇개인지 찍어보겠다. # 1개당 1MB
		    	}
		    	
		    	if(sharpCount % 50 == 0)		//#이 50개가 나오면 줄바꿈
		    	{
		    		System.out.print("\n");
		    	}
		    }// end of while
		    bost.close();
		    fost.close();
		    
		    bist.close();
		    fist.close();
		    
		    System.out.println(">> " + totalByte + " Byte 씀 <<");
		    
		    sc.close();
		    
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
