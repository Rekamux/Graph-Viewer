package model.history;

import model.Vertex;

/**
 * Vertex' action
 */
public abstract class VertexAction implements Action
{
	private Vertex vertexConcerned;
	
	public VertexAction(Vertex vertexConcerned)
	{
		setVertexConcerned(vertexConcerned);
	}

	public void setVertexConcerned(Vertex vertexConcerned)
	{
		this.vertexConcerned = vertexConcerned;
	}

	public Vertex getVertexConcerned() 
	{
		return vertexConcerned;
	}
}