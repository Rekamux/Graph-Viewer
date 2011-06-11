package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import model.GeometricGraph;
import model.Vertex;

/**
 * Thread modifying a graph in order to search a better position
 * 
 * @author Ax
 */
public class SearchBetterPosition extends Thread
{
	/**
	 * Concerned graph
	 */
	private GeometricGraph graph;

	/**
	 * Geometric distances matrix
	 */
	private ArrayList<ArrayList<Double>> poundsMatrix = new ArrayList<ArrayList<Double>>();

	/**
	 * Default constructor
	 * 
	 * @param g
	 */
	public SearchBetterPosition(GeometricGraph g)
	{
		graph = g;
	}

	@Override
	public void run()
	{
		move();
	}

	/**
	 * Updates geometric distance between a Vertex and all the others in the
	 * pounds matrix
	 */
	private void computePounds(int i)
	{
		int n = graph.getN();
		for (int j = 0; j < n; j++)
			poundsMatrix.get(i).set(j, computePounds(i, j));
	}

	/**
	 * Computes geometric distance between 2 vertices indexes
	 */
	private double computePounds(int i, int j)
	{
		Vertex a = graph.getVertex(i);
		Vertex b = graph.getVertex(j);
		return Point.distance(a.getPoint().getX(), a.getPoint().getY(), b
				.getPoint().getX(), b.getPoint().getY());
	}

	/**
	 * Computes the geometric distances matrix
	 */
	private void computePoundsMatrix()
	{
		int n = graph.getN();
		poundsMatrix = new ArrayList<ArrayList<Double>>();

		for (int i = 0; i < n; i++)
		{
			poundsMatrix.add(new ArrayList<Double>());
			for (int j = 0; j < n; j++)
			{
				poundsMatrix.get(i).add(computePounds(i, j));
			}
		}
	}

	/**
	 * Computes geometric distance sum for a given Vertex
	 */
	private double computeVerticesPoundsSum(int i)
	{
		double sum = 0;
		int n = graph.getN();
		double height = graph.getHeight();
		double width = graph.getWidth();
		for (int j = 0; j < n; j++)
		{
			sum += 1 / poundsMatrix.get(i).get(j);
			if (graph.areNeighbors(i, j))
				sum -= poundsMatrix.get(i).get(j);
		}
		Vertex v = graph.getVertex(i);
		Point pV = v.getPoint();
		int z = 25;
		for (int j = 0; j < z; j++)
		{
			double sidePounds1 = Point.distance(pV.getX(), pV.getY(), 0, j
					* height / z);
			double sidePounds2 = Point.distance(pV.getX(), pV.getY(), width,
					(j + 1) * height / z);
			double sidePounds3 = Point.distance(pV.getX(), pV.getY(), (j + 1)
					* width / z, 0);
			double sidePounds4 = Point.distance(pV.getX(), pV.getY(), j * width
					/ z, height);
			sum += (sidePounds1 + sidePounds2 + sidePounds3 + sidePounds4) / z;
		}
		return sum;
	}

	/**
	 * Move
	 */
	private void move()
	{
		int n = graph.getN();

		if (n != poundsMatrix.size())
			computePoundsMatrix();

		while (true)
		{
			for (int i = 0; i < n; i++)
				moveVertexIfFurther(i);
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
			}
			graph.notifyObservers();
		}
	}

	/**
	 * Moves the given Vertex following randomly one of the eight cardinals
	 * directions returns the move
	 */
	private void moveVertexIfFurther(int i)
	{
		Vertex v = graph.getVertex(i);
		int diameter = graph.getDiameter();
		int width = graph.getWidth();
		int height = graph.getHeight();
		if (v.isFixed())
			return;
		double previousSum = computeVerticesPoundsSum(i);
		int previousX = v.getXPosition();
		int previousY = v.getYPosition();
		Random r = new Random(System.currentTimeMillis());

		int amplitude = 1;
		int xMove = r.nextInt(amplitude * 2 + 1) - amplitude;
		int yMove = r.nextInt(amplitude * 2 + 1) - amplitude;

		if (xMove == 0 && yMove == 0)
			return;

		int newX = previousX + xMove;
		int newY = previousY + yMove;
		if (newX < 2 * diameter)
			newX = 2 * diameter;
		if (newX > width - 2 * diameter)
			newX = width - 2 * diameter;
		if (newY < 2 * diameter)
			newY = 2 * diameter;
		if (newY > height - 2 * diameter)
			newY = height - 2 * diameter;
		v.setXPosition(newX);
		v.setYPosition(newY);
		computePounds(i);
		double newSum = computeVerticesPoundsSum(i);

		if (newSum < previousSum)
		{
			graph.moveVertex(i, newX, newY);
			graph.notifyObservers();
		} else
		{
			v.setXPosition(previousX);
			v.setYPosition(previousY);
			computePounds(i);
		}
	}
}
