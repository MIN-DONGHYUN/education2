package io.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {

	//reading 메소드 생성하기 
	public static String reading(String file_name) {
		
		//해당 파일에 노드 연결 생성(빨대꽃기)
		try 
		{
			FileReader fr = new FileReader(file_name);
			StringBuilder sb = new StringBuilder();		// 스트링 빌더
			
			
			do
			{
				int data = fr.read();	// 파일로 부터 글자(char)1ro(2byte)씩 읽어들임.  리턴 타입은 int 
										// 파일로 부터 읽어들일 글자(char)가 없으면 -1을 리턴시켜준다.
				if(data != -1)
				{
					sb.append((char)data);		// 스트링빌더에 계속 누적시킨다. int형이므로 캐스팅으로 문자로 받지 
				}
				else			// 문자가 다끝난후 -1이 들어온다면 
				{
					break;   //do-while문 탈출 
				}
				
			} while(true);
			//end of do-while문
			fr.close();			// 빨대 제거
			return sb.toString();			// 스트링 빌더를 String 타입으로 변경후 리턴 시켜준다.
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return file_name +  " 이라는 파일은 없습니다.!!";
		}
		catch (IOException e) {
			e.printStackTrace();
			return file_name + " 이 손상되었습니다.!!";
		}

	}// end of public static String reading(String file_name) {

	

}
