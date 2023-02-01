// 키보드에서 입력받은 것을 파일로 저장시키기 // 1Byte씩 받아온다.
package io.day1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStream_test_7 {


/*
    ※ Data Source(키보드, 파일, 내 컴퓨터) 
    : 데이터의 근원
    
    ※ Data Destination(모니터, 파일, 서버컴퓨터)  
    : 데이터가 최종적으로 도착하는 곳 
    
    
    Data Source o ====> 프로그램  ====>  o Data Destination
                ↑입력스트림              ↑출력스트림     
                InputStream           OutputStream

	// 키보드에서 입력받은 것을 파일(C:\NCS\iotest_data\result.txt)에 기록(출력)하기로 하자
	1. 데이터소스 : 키보드로 부터 읽어들임 (노드스트림 : System.in)
	2. 데이터 목적지 : 결과물을 특정 파일에 출력 (노드스트림 : FileOutputStream)

*/
	
	
	public static void main(String[] args) {
	
		System.out.println(">> 내용을 입력하세요[입력하신 내용은 C:\\NCS\\iotest_data\\result.txt 에 저장됩니다.] <<");
		
		String fileName = "C:\\NCS\\iotest_data\\result.txt";   // 저장할 파일 이름 지정 
		
		boolean append = true;
		
		int input = 0;
		int totalByte = 0;  // byte수 누적 용도 
													//경로명,    
		try {
			FileOutputStream fost = new FileOutputStream(fileName, append); // 이것으로 인해 경로가 맞으면은 result.txt가 생성이 된다.
			/*
	            만약에  탐색기에서 
	         	C:/NCS/iotest_data/result.txt 파일이 없다라면
	            파일을 자동으로 생성해준다.
	            단, 탐색기에서 C:/NCS/iotest_data 폴더는 존재해야 한다.
	         
	            두번째 파라미터인 append 가 true 인 경우는
	            첫번째 파라미터인 해당파일에 이미 내용물이 적혀 있는 경우일때
	            기존내용물은 그대로 두고 기존내용뒤에 새로운 내용을 덧붙여 추가하겠다는 말이다. 
	            
	            두번째 파라미터인 append 가 false 인 경우는
	            첫번째 파라미터인 해당파일에 이미 내용물이 적혀 있는 경우일때
	            기존내용물은 싹 지우고 새로운 내용을 새롭게 처음부터 쓰겠다는 말이다.
	          
	            그런데 만약에 두번째 파라미터를 생략하면    
	            즉, FileOutputStream fost = new FileOutputStream(filename); 이라면
	            자동적으로 false 로 인식한다. 즉, filename 의 기존내용물은 싹 지우고 새로운 내용을 새롭게 처음부터 쓰겠다는 말이다.    
			 */
			
			// 1Byte씩 받아온다.
			while((input = System.in.read()) != -1)	// Ctrl + C 가 아니면은 실행 
			{
				fost.write(input);	// 출력하기위해 write, flush사용
				fost.flush();
				
				totalByte++;
				
			}// end of while
			
			fost.close(); 	// 메모리 누수 방지 
			
			System.out.println(fileName + " 에 쓰기 완료 !!" + totalByte + "byte 씀");
			System.out.println("반복회수" + totalByte + " 번 반복함");
			System.out.close();						// 더이상 안쓰기 때문에 닫는다. 
		} 
		catch (FileNotFoundException e) {
			System.out.println(fileName + " 파일이 없습니다.");
		}  
		catch (IOException e) {
			e.printStackTrace(); 	//오류 실행 
		}  
	}// end of main
}
/*
C:\NCS\workspace(java)\IO\bin>java io.day1.FileOutputStream_test_7
>> 내용을 입력하세요[입력하신 내용은 C:\NCS\iotest_data\result.txt 에 저장됩니다.] <<
2023년 겨울 이적시장(토트넘)in/out
in 패드로 포로 (RB), 단주마(LW)
out 브라이안 힐 (RW), 제드 스팬스(RB), 맷 도허티 (FA)
==> 브라이안 힐은 세비야로 갑니다
==> 제드 스팬스는 렌으로 갑니다
==> 맷 도허티는 AT마드리드로 갑니다

//Ctrl + C 를 엔터// 


C:\NCS\iotest_data\result.txt 에 쓰기 완료 !!230byte 씀
반복회수230 번 반복함


*/