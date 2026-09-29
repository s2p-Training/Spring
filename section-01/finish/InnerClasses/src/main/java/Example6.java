abstract class MyAbstractClass
{
	int x = 10;

	public void method1()
	{
		System.out.println("Concrete Method1 Of MyAbstract Class");
	}

	public abstract void method2();
	public abstract void method3();

}

public class Example6
{
	public static void main(String[] args)
	{
		MyAbstractClass instance = new MyAbstractClass()
		{
			public void method2()
			{
				System.out.println("Concrete Method2 Of Anonymous Class");
			}

			public void method3()
			{
				System.out.println("Concrete Method3 Of Anonymous Class");
			}
		};

		System.out.println(instance.x);
		instance.method1();
		instance.method2();  // Anonymous Class
		instance.method3();  // Anonymous Class
	}
}
