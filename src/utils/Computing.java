package utils;

import java.awt.Point;

public abstract class Computing
{
	/**
	 * Computes geometric distance between two points
	 */
	public static float geometricDistance(Point a, Point b)
	{
		int xI = (int) a.getX();
		int yI = (int) a.getY();
		int xJ = (int) b.getX();
		int yJ = (int) b.getY();
		int difX = xI - xJ;
		int difY = yI - yJ;
		float distance = (float) Math.sqrt(difX * difX + difY * difY);
		return distance;
	}

	/**
	 * Provides a string containing '0' or '1' according to b
	 * 
	 * @param b
	 * @return
	 */
	public static String booleanToBinary(boolean b)
	{
		if (b)
			return "1";
		else
			return "0";
	}

	/**
	 * Checks if a string is a 0 or a 1 and returns concerned boolean, null if
	 * not valid
	 */
	public static Boolean toBoolean(String s)
	{
		try
		{
			int i = Integer.parseInt(s);
			if (i != 0 && i != 1)
				return null;
			return (i == 1);
		} catch (NumberFormatException e)
		{
			return null;
		}
	}

	/**
	 * Returns true if given char is numeric, false else
	 */
	public static Integer toInteger(String text)
	{
		Integer i = null;
		try
		{
			i = Integer.parseInt(String.valueOf(text));
		} catch (NumberFormatException e)
		{
			return null;
		}
		return i;
	}
}
