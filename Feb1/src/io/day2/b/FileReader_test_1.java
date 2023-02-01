package io.day2.b;

import io.Util.FileManager;

public class FileReader_test_1 {

	public static void main(String[] args) {
		//읽어 들일 파일명은 명령프롬프트에서 파라미터(인자)로 주겠다.
		
		/*
		String str1 = args[0];
		String str2 = args[1];
		
		System.out.println("str1 => " + str1);
		System.out.println("str2 => " + str2);
		*/
		
		//"문자로만 이루어진 특정 파일명"에 해당하는 내용을 읽어다가 화면에 출력하고자 한다.
		String str = FileManager.reading(args[0]); 		// io.Util에 있는 것을 호출 (FileManager에서 reading이라는 메소드 만들것) 
		//args[0] 에 C:\NCS\iotest_data/Korea.txt 로 할것이다.
		
		System.out.println(str);
		
	}

}
/*
	실행은 명령프롬프트에서 아래와 같이 한다.
 
	C:\NCS\workspace(java)\IO\bin>java io.day2.b.FileReader_test_1 안녕하세요 Hello
	str1 => 안녕하세요
	str2 => Hello
	
	실행은 명령프롬프트에서 아래와 같이 한다.
	C:\NCS\workspace(java)\IO\bin>java io.day2.b.FileReader_test_1 C:\NCS\iotest_data/Korea.txt
	동해물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세
	무궁화 삼천리 화려강산 대한사람 대한으로 길이 보전하세
	
	
	C:\NCS\iotest_data/Korea.txt 파일은 저장시 인코딩을 ANSI로 해야 한다.

*/