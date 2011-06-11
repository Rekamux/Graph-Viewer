package utils;

import java.util.ArrayList;

/**
 * Stack
 * 
 * @author Axel Schumacher
 *
 * @param <A>
 * 	stacked elements type
 */
public class Stack<A>
{
	/**
	 * Pile
	 */
	private ArrayList<A> stack;
	
	/**
	 * Constructeur
	 */
	public Stack()
	{
		stack = new ArrayList<A>();
	}
	
	/**
	 * Stacks an element
	 */
	public void push_front(A a)
	{
		if (a == null)
			System.err.println("In Stack.push_front: pushing a null element!");
		else
			stack.add(a);
	}
	
	/**
	 * Pops last pushed element
	 * 
	 */
	public A pop_front()
	{
		int index = stack.size()-1;
		if (index < 0)
			System.err.println("In Stack.pop_front: poping an empty stack!");
		A retour = stack.get(index);
		stack.remove(index);
		return retour;
	}
	
	/**
	 * Retourne si la pile est vide
	 * 
	 * @return
	 * 	Si la pile est vide
	 */
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}
}
