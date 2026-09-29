
abstract class Operation
{
	// Addition, Subtraction, Multiplication, Division
	public abstract int op(int a, int b);
}

class AdditionOperation extends Operation
{
	@Override
	public int op(int a, int b)
	{
		int c = a + b;
		return c;
	}
}

class SubtractionOperation extends Operation
{
	@Override
	public int op(int a, int b) {
		int c = a - b;
		return c;
	}
}


public class Example5
{
	public static void main(String[] args)
	{
		Operation additionOperation = new Operation()
		{
			public int op(int a, int b)
			{
				int c = a+b;
				return c;
			}
		};

		int result = additionOperation.op(12,14);
		System.out.println("The Result Of Addition Is " + result);


		Operation multiplicationOperation = new Operation()
		{
			public int op(int a, int b)
			{
				int c = a * b;
				return c;
			}
		};

		int result1 = multiplicationOperation.op(3,4);
		System.out.println("Result Of Multiplication : " + result1);

	}
}
