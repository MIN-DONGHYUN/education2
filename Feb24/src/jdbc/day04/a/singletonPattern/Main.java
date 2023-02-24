package jdbc.day04.a.singletonPattern;

public class Main {

	public void a_method() {  
	      NoSingletonNumber aObj = new NoSingletonNumber(); // 객체생성
	      
	      System.out.println("aObj =>" + aObj);
	      System.out.println("a_method() 메소드에서 cnt 값 호출 : " + aObj.getNextNumber() +"\n");  
	   }
	   
	   public void b_method() {
	      NoSingletonNumber bObj = new NoSingletonNumber(); // 객체생성
	      
	      System.out.println("bObj =>" + bObj);
	      System.out.println("b_method() 메소드에서 cnt 값 호출 : " + bObj.getNextNumber()+"\n");  
	   }
	   
	   public void c_method() {
	      NoSingletonNumber cObj = new NoSingletonNumber(); // 객체생성
	      
	      System.out.println("cObj =>" + cObj);
	      System.out.println("c_method() 메소드에서 cnt 값 호출 : " + cObj.getNextNumber()+"\n");  
	   }
	//////////////////////////////////////////////////////////////////
	   
	   public void d_method() {  // 집접 객체 못만든다.
		      // SingletonNumber dObj = new SingletonNumber(); 
		      // 객체생성이 불가하다.
		      // 왜냐하면 SingletonNumber 생성자의 접근제한자를 private 으로 해두었기 때문이다.
		      
		      SingletonNumber dObj = SingletonNumber.getInstance(); // static 메소드 호출(접근제한자는 public 임)
		      
		      System.out.println("dObj =>" + dObj);
		      System.out.println("d_method() 메소드에서 cnt 값 호출 : " + dObj.getNextNumber()+"\n");  
		   }
		   
		   
		   public void e_method() {
		      // SingletonNumber eObj = new SingletonNumber(); 
		      // 객체생성이 불가하다.
		      // 왜냐하면 SingletonNumber 생성자의 접근제한자를 private 으로 해두었기 때문이다.
		      
		      SingletonNumber eObj = SingletonNumber.getInstance(); // static 메소드 호출(접근제한자는 public 임)
		      
		      System.out.println("eObj =>" + eObj);
		      System.out.println("e_method() 메소드에서 cnt 값 호출 : " + eObj.getNextNumber()+"\n");  
		   }
		   
		   public void f_method() {
		      // SingletonNumber fObj = new SingletonNumber(); 
		      // 객체생성이 불가하다.
		      // 왜냐하면 SingletonNumber 생성자의 접근제한자를 private 으로 해두었기 때문이다.
		      
		      SingletonNumber fObj = SingletonNumber.getInstance(); // static 메소드 호출(접근제한자는 public 임)
		      
		      System.out.println("fObj =>" + fObj);
		      System.out.println("f_method() 메소드에서 cnt 값 호출 : " + fObj.getNextNumber()+"\n");  
		   }
	   
		   // d,e,f 에 올라가는 메모리 주소가 모두 동일해진다.
		   
	   
	//////////////////////////////////////////////////////////////////
	public static void main(String[] args) {

		Main ma = new Main();		// 이것을 만들어야 Main에 있는 메소드 호출 가능하다
		
		ma.a_method();		// a 메소드 호출 --> 결과 1
		/*
		 	aObj =>jdbc.day04.a.singletonPattern.NoSingletonNumber@15db9742
			a_method() 메소드에서 cnt 값 호출 : 1 
		*/
		
		ma.b_method();		// b 메소드 호출 --> 결과 1
		/*
	 		bObj =>jdbc.day04.a.singletonPattern.NoSingletonNumber@6d06d69c
			b_method() 메소드에서 cnt 값 호출 : 1
		 */
		ma.c_method();		// c 메소드 호출 --> 결과 1
		/*
	 		cObj =>jdbc.day04.a.singletonPattern.NoSingletonNumber@7852e922
			c_method() 메소드에서 cnt 값 호출 : 1
		 */
		
		// --> 이것들은 new를 매번 하기 때문에 메모리 주소 값이 다르다.
		
		
	///////////////////////////////////////////////////////////////
		
		ma.d_method();		// d 메소드 호출 --> 결과 1 
		/*
		 	dObj =>jdbc.day04.a.singletonPattern.SingletonNumber@4e25154f
			d_method() 메소드에서 cnt 값 호출 : 1
		*/
		ma.e_method();		// e 메소드 호출 --> 결과 2
		/*
	 		eObj =>jdbc.day04.a.singletonPattern.SingletonNumber@4e25154f
			e_method() 메소드에서 cnt 값 호출 : 2
		*/
		ma.f_method();		// f 메소드 호출 --> 결과 3
		/*
	 		fObj =>jdbc.day04.a.singletonPattern.SingletonNumber@4e25154f
			f_method() 메소드에서 cnt 값 호출 : 3
		*/
		
		// --> 메모리 주소가 같다.
	} // end of main

}
