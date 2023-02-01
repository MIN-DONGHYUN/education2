package io.day1;

import java.io.*;
import java.util.Scanner;

public class FileCopy_test_9 {


/*
    ※ Data Source(키보드, 파일, 내 컴퓨터) 
    : 데이터의 근원
    
    ※ Data Destination(모니터, 파일, 서버컴퓨터)  
    : 데이터가 최종적으로 도착하는 곳 
    
    
    Data Source o ====> 프로그램  ====>  o Data Destination
                ↑입력스트림              ↑출력스트림     
                InputStream           OutputStream

	// 키보드에서 입력받은 것을 파일에 기록(출력)하기로 한자.
	1. 데이터소스 : 키보드로 부터 읽어들임 (노드스트림 : FileinputStream)
	2. 데이터 목적지 : 결과물을 특정 파일에 출력 (노드스트림 : FileOutputStream)

*/
	
	
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println(">> C:\\NCS\\iotest_data\\원본\\Ms.Kong.jpg을 복사해 봅시다.<<");
	    System.out.print(">> 복사할 원본파일명(절대경로)을 입력 => ");
	    
	    String src_fileName = sc.nextLine(); 		   // 복사할 파일명 입력
	    //C:\\NCS\\iotest_data\\원본\\Ms.Kong.jpg
	    
	    System.out.print(">> 목적 파일명(절대경로)을 입력 => ");
	    
	    String target_fileName = sc.nextLine();;        // 저장할 파일 이름 지정
	    //C:\\NCS\\iotest_data\\복사본\\Ms.Kong.jpg
	    
	    byte[] dataArr = new byte[1024*992
	                              ]; 		// 1024 byte = 1kb
	    int inputLength = 0; 		// 실제로 읽어온 크기를 알아내기 위해 설정 
	    int totalByte = 0;  // byte 수 누적 용도 
	    int cnt = 0;  // 반복회수 
	    
	    try {
	    	FileInputStream fist = new FileInputStream(src_fileName);
	    	//FileInputStream 생성 : 접속점이 파일인 것으로 특정 파일에 빨대를 꽅아 파일의 내용물을 1byte 기반으로 내용물을 빨아들이는 입력노드 스트림이다.
	    	
	    	FileOutputStream fost = new FileOutputStream(target_fileName); // 이것으로 인해 경로가 맞으면은 Ms.kong가 생성이 된다.
	    	// FileOutputStream 생성 : 접속점이 파일인 것으로 특정 파일에 빨대를 꽅아 파일의 내용물을 1byte 기반으로 내용물을 써주는 출력노드 스트림이다.
	    	
	    	       // 읽어올 파일을 읽어온다. Ctrl + C 이면 반복문 종료
	    	while( (inputLength = fist.read(dataArr)) != -1  ) {
	    		// 읽어오기 
	    		fost.write(dataArr);
	    		fost.flush();
	    		
	    		totalByte += inputLength;
	    		cnt++;
	    		
	    	}
	    	
	    	System.out.println(target_fileName + "에 쓰기 완료 " + totalByte + "byte 복사됨");
	    	System.out.println(cnt + " 번 반복함");
	   
	    	
	    	fist.close();
	    	fost.close();
	    }
	    catch (FileNotFoundException e) {
			System.out.println(src_fileName + " 파일이 없습니다.");
		}  
	    catch (IOException e) {
			e.printStackTrace(); 	//오류 실행 
		}  
		
	    sc.close();
	}

}
