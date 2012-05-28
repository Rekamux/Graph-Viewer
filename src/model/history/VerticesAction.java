package model.history;

import java.util.ArrayList;

import model.Vertex;

/**
 * Vertices action
 */
public abstract class VerticesAction implements Action
{
	private ArrayList<Vertex> verticesConcerned;
	
	public VerticesAction(ArrayList<Vertex> verticesConcerned)
	{
		setVerticesConcerned(verticesConcerned);
	}

	public void setVerticesConcerned(ArrayList<Vertex> verticesConcerned)
	{
		this.verticesConcerned = verticesConcerned;
	}

	public ArrayList<Vertex> getVerticesConcerned() 
	{
		return verticesConcerned;
	}
}