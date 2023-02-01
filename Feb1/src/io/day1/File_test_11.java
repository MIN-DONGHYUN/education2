package io.day1;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class File_test_11 {
	
	/*
		>>> file 클래스 <<<
		자바에서 file 클래스의 객체라 함은 파일 및 폴더(디렉토리)를 다 포함한다.

	*/ 

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

		System.out.print("탐색기에 존재하는 파일명을 입력하세요 : ");
		String file_name = sc.nextLine();
		// C:\NCS\iotest_data\원본\Ms.Kong.jpg
		
		
		//~~~~~~~~~~~~~ 파일명만 나타내보자~~~~~~~~~~~~~~~~~~~ //
		File file_1 = new File(file_name);
		
		System.out.println("파일명만 : " + file_1.getName());
		// file_1.getName() 은 파일명만 알려주는 것이다.
		// 파일명만 : Ms.Kong.jpg
		
		
		//파일 크기 를 변수에 저장 
		long file_1_size = file_1.length();
		
		System.out.println("파일크기 : " + file_1_size + "Byte");
		// 파일크기 : 1011712Byte
		
		
		
		//~~~~~~~~~~~~~~~~파일명과 경로를 나타내보자 ~~~~~~~~~~~~~~~~~~~~//
		String absolutePath = file_1.getAbsolutePath();
		System.out.println("1. 경로명을 포함한 파일명 : " + absolutePath);
		
		// 위에 것과 같다.
		String path_file_name = file_1.getPath();
		System.out.println("2. 경로명을 포함한 파일명 : " +path_file_name);
		
		/*
			1. 경로명을 포함한 파일명 : C:\NCS\iotest_data\원본\Ms.Kong.jpg
			2. 경로명을 포함한 파일명 : C:\NCS\iotest_data\원본\Ms.Kong.jpg
		*/
		
		
		// [퀴즈]
		//~~~~~~~~~~~~~~~~~~~ 경로명만 나타내어 보자 ~~~~~~~~~~~~~~~~~~~~//
		/*
		       !!!!!!!!!!!!!!!!!!내가 풀어본 풀이!!!!!!!!!!!!!!!!!!!!!!!!!
		String path_name = file_1.getAbsolutePath();
		int last = path_name.lastIndexOf("\\");
		path_name = path_name.substring(0,last+1);
		*/   
		
		// 강사님 풀이 
		String path_name = path_file_name.substring(0, path_file_name.indexOf(file_1.getName()));
		System.out.println("3. 경로명만 : " + path_name);
		
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

		
		
		// ~~~~~~~~~~~~~~~~~~~~~~~폴더를 생성해보자 ~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		System.out.println(">>> 디렉토리(폴더) 생성하기 <<<");
		
		//MyDir 폴더가 없다면 생성을 해주겠다.   dir은 파일 객체이다.
		File dir = new File("C:/NCS/iotest_data/MyDir");
		
		if( !dir.exists())   // 해당 폴더(디렉토리)가 존재하지 않으면 실행한다.
		{
			// 폴더를 만들자  
			boolean bool = dir.mkdir();	// 해당 폴더(디렉토리)를 생성해라 !! 리턴타입은 boolean
			
			// 삼항 연산자 사용
			String result = bool?"폴더(디렉토리)생성 성공":"폴더(디렉토리)생성 실패";
			
			System.out.println("C:/NCS/iotest_data/MyDir" + result);
		}
		
		// File dir 이 폴더(디렉토리)인지 알아보기 
		if(dir.isDirectory())   // 리턴 타입은 boolean 이며 이것이 폴더이냐고 묻는 것이다.
		{
			System.out.println("C:/NCS/iotest_data/MyDir 은 폴더(디렉토리)입니다.");
			// C:/NCS/iotest_data/MyDir 은 폴더(디렉토리)입니다.
		}
		
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~파일 생성하기 ~~~~~~~~~~~~~~~~~~~~~//
		System.out.println(">>> 파일 생성하기 <<<");
		
		//MyDir 폴더가 없다면 생성을 해주겠다.   dir은 파일 객체이다.
		File file_2 = new File("C:/NCS/iotest_data/MyDir/테스트1.txt");
		
		if( !file_2.exists())   // 해당 파일이 존재하지 않으면 실행한다.
		{
			// 파일을 만들자
			try {
				boolean bool = file_2.createNewFile(); //해당 파일을 생성해라 !! 리턴타입은 boolean
				// 삼항 연산자 사용
				String result = bool?"파일 생성 성공":"파일생성 실패";
				
				System.out.println("C:/NCS/iotest_data/MyDir/테스트1.txt" + result);
			} 
			catch (IOException e) {
				e.printStackTrace();
			}	
			
		}
		
		
		// File file_2 이 파일인지 알아보기 
		if(file_2.isFile())   // 리턴 타입은 boolean 이며 이것이 파일이냐고 묻는 것이다.
		{
			System.out.println("C:/NCS/iotest_data/MyDir/테스트1.txt 은 파일입니다.");
			// C:/NCS/iotest_data/MyDir/테스트1.txt 은 파일입니다.
		}
		
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~파일 삭제하기 ~~~~~~~~~~~~~~~~~~~~~//
		System.out.println(">>> 파일 삭제하기 <<<");
		
		boolean bool = file_2.delete(); 		//파일을 삭제해주는 명령어 리턴은 boolean
		
		String result = bool?"파일 삭제 성공":"파일 삭제 실패";
		
		System.out.println("C:/NCS/iotest_data/MyDir/테스트1.txt" + result);
		// C:/NCS/iotest_data/MyDir/테스트1.txt파일 삭제 성공
		
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~텅 빈 폴더 삭제하기 ~~~~~~~~~~~~~~~~~~~~~//
		System.out.println(">>> 텅 빈 폴더(디렉토리) 삭제하기 <<<");
		
		if(dir.exists()) {   // dir이 존재하는가
			bool = dir.delete();    // 텅빈 폴더(디렉토리) 삭제하기
			result = bool?"폴더(디렉토리) 삭제 성공":"폴더(디렉토리) 삭제 실패";
			
			System.out.println("C:/NCS/iotest_data/MyDir" + result);
		}
		
		
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		
		
		
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~내용물이 들어있는 폴더 삭제하기 ~~~~~~~~~~~~~~~~~~~~~//
		System.out.println(">>> 내용물이 들어있는 폴더(디렉토리) 삭제하기 <<<");
		// 먼저 아래의 실습을 하기 위해서는 C:\NCS\iotest_data\myData 라는 폴더를 만들고 
		// myData 폴더 속에 아무런 파일이나 몇개를 넣어준 후 실습을 하도록 한다.
		
		File myData_dir = new File("C:/NCS/iotest_data/myData");
		
		/*   !!!!!!!!!!!!!!!실패 버전!!!!!!!!!!!!!!!!!!
		if( !dir.exists())   // 해당 폴더(디렉토리)가 존재하지 않으면 실행한다.
		{
			// 폴더를 삭제하자   
			bool = myData_dir.delete();	// 파일이 들어있는 폴더(디렉토리)를 삭제해라 !! 리턴타입은 boolean
			
			// 삼항 연산자 사용
			result = bool?"폴더(디렉토리)삭제 성공":"폴더(디렉토리)삭제 실패";
			
			System.out.println("C:/NCS/iotest_data/myData" + result);
		}
		*/
		
		// 1. 내용물이 들어있는 폴더(디렉토리)내에 존재하는 내용물을 파악한다.
		File [] file_arr = myData_dir.listFiles(); 		// myData_dir에 있는 것을 확인한다(파일인지, 폴더인지 등등)
		
		for(int i=0; i<file_arr.length; i++)			// 배열만큼 반복
		{
			
			System.out.println(file_arr[i].getPath());  // 경로와 파일명을 알려준다.				
		}
		/*
			C:\NCS\iotest_data\myData\Korea.txt
			C:\NCS\iotest_data\myData\Ms.Kong.jpg
			C:\NCS\iotest_data\myData\result.txt
			C:\NCS\iotest_data\myData\result_2.txt
			C:\NCS\iotest_data\myData\resume_download_4089_2.docx
		*/	
		System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		for(int i=0; i<file_arr.length; i++)			// 배열만큼 반복
		{
			
			file_arr[i].delete(); 		// 모든 배열에 있는 파일들을 삭제한다.				
		}
		
		if( !dir.exists())   // 해당 폴더(디렉토리)가 존재하지 않으면 실행한다.
		{
			// 폴더를 삭제하자   
			bool = myData_dir.delete();	// 파일이 들어있는 폴더(디렉토리)를 삭제해라 !! 리턴타입은 boolean
			
			// 삼항 연산자 사용
			result = bool?"폴더(디렉토리)삭제 성공":"폴더(디렉토리)삭제 실패";
			
			System.out.println("C:/NCS/iotest_data/myData" + result);
			//C:/NCS/iotest_data/myData폴더(디렉토리)삭제 성공
		}
		
		
		
		sc.close();
	}

}
