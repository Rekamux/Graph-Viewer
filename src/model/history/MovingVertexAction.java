package model.history;

import java.awt.Point;

import model.Vertex;

/**
 * Vertex moving action
 */
public class MovingVertexAction extends VertexAction
{
	private Point origin;
	private Point arrival;
	public MovingVertexAction(Vertex vertexConcerned, Point origin, Point arrival) 
	{
		super(vertexConcerned);
		setOrigin(origin);
		setArrival(arrival);
	}

	public void doAction() 
	{
		getVertexConcerned().setXPosition((int) arrival.getX());
		getVertexConcerned().setYPosition((int) arrival.getY());
	}

	public void undoAction() 
	{
		getVertexConcerned().setXPosition((int) origin.getX());
		getVertexConcerned().setYPosition((int) origin.getY());
	}

	public void setOrigin(Point origin) 
	{
		this.origin = origin;
	}

	public Point getOrigin() 
	{
		return origin;
	}

	public void setArrival(Point arrival) 
	{
		this.arrival = arrival;
	}

	public Point getArrival()
	{
		return arrival;
	}
}
