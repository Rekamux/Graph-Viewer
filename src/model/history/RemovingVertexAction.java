package model.history;

import model.GraphModel;
import model.Vertex;

/**
 * Removing vertex action
 */
public class RemovingVertexAction extends AddingRemovingVertexAction
{
	public RemovingVertexAction(Vertex vertexConcerned,
			GraphModel graphConcerned)
	{
		super(vertexConcerned, graphConcerned);
	}

	public void doAction() 
	{
		getGraphConcerned().removeVertex(getVertexConcerned());
	}

	public void undoAction() 
	{
		getGraphConcerned().addVertex(getVertexConcerned());
	}
}