// INTERFACE 를 사용하면 목차가 쭉 나오기때문에 데이터에 뭐가 있는지 나온다. 

package jdbc.day04.b;

import java.util.Scanner;

public class Member_main {

	public static void main(String[] args) {
		
		InterMemberCtrl mctrl = new MemberCtrl();  // 다형성
		
		Scanner sc = new Scanner(System.in);  // 키보드 입력을 받기위해 
		
		mctrl.menu_Start(sc);				//컨트롤에 있는 메뉴 시타트를 실행
		
		sc.close();
		System.out.println("\n~~~~~~~ 프로그램 종료~~~~~~~~");
		

	}

}

