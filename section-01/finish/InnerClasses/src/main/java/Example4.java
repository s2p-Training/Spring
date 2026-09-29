abstract class MyAbstract
{
	public abstract void display();

	public void method1()
	{
		System.out.println("Concrete Method Of Abstract Class");
	}
}

class MyImpl1 extends MyAbstract
{
	public void display()
	{
		System.out.println("Display Method Of MyImpl1 Class");
	}
}

class MyImpl2 extends MyAbstract
{
	@Override
	public void display()
	{
		System.out.println("Display Method Of Impl2 Class");
	}
}


public class Example4
{
	public static void main(String[] args) {
		MyAbstract instance = new MyAbstract(){
			public void display()
			{
				System.out.println("Anonymous Class Display Method");
			}
		};

		instance.display();

		MyAbstract instance1 = new MyImpl1();
		instance1.display();


	}
}
