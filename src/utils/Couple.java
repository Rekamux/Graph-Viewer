package utils;

public class Couple<A>
{
	private A first;
	private A second;
	
	public Couple(A i, A j)
	{
		first = i;
		second = j;
	}

	public A getFirst()
	{
		return first;
	}

	public void setFirst(A first)
	{
		this.first = first;
	}

	public A getSecond() 
	{
		return second;
	}

	public void setSecond(A second) 
	{
		this.second = second;
	}
	
}