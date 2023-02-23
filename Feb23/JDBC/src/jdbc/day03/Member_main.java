// INTERFACE 를 사용하면 목차가 쭉 나오기때문에 데이터에 뭐가 있는지 나온다. 

package jdbc.day03;

import java.util.Scanner;

public class Member_main {

	public static void main(String[] args) {
		
		InterMemberCtrl mctrl = new MemberCtrl();
		
		Scanner sc = new Scanner(System.in);
		
		mctrl.menu_Start(sc);
		
		sc.close();
		System.out.println("\n~~~~~~~ 프로그램 종료~~~~~~~~");
		

	}

}

