class Outer1
{
	int a = 10;
	static int b = 100;

	public void method1()
	{
		System.out.println("Non-Static Method Of Outer1 Class");
	}

	// Static Function
	public static void method2()
	{
		System.out.println("Static Method Of Outer1 Class");
	}


	// Class : Non-Static
	class NonStaticNestedClass
	{
		int x = 20;
		static int y = 200;
		static int b = 4000;


		public void display()
		{
			System.out.println(a);
			method1();
			System.out.println(b);  // 4000
			System.out.println(Outer1.b); // 100
		}

	}

}


public class Example1
{
	public static void main(String[] args) {
		Outer1 instance = new Outer1();
		System.out.println(instance.a);
		instance.method1();

		System.out.println(Outer1.b);
		Outer1.method2();

		Outer1 instance2 = new Outer1();
		Outer1.NonStaticNestedClass inner = instance2.new NonStaticNestedClass();
		inner.display();

		/**
		 * Non-Static Inner Class Can Access The Static And Non-Static Members Of Outer Class
		 */
	}
}
