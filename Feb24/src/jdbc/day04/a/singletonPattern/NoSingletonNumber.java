package jdbc.day04.a.singletonPattern;

public class NoSingletonNumber {

	private int cnt = 0;    // 인스턴스 변수 
	
	// === 기본 생성자는 생략되어있다. === //
	//public NosingletonNumber() {} 이 생략되어짐.
	
	
	public int getNextNumber() { // 인스턴스 메소드
		return ++cnt;   // 인스턴스 변수  원래 0이지만 이것이 실행되면 1로 return 
						// 메소드마다 cnt가 다르다.
	}
	
}
