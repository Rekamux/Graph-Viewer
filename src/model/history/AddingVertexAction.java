package model.history;

import model.GraphModel;
import model.Vertex;

/**
 * Adding vertex action
 */
public class AddingVertexAction extends AddingRemovingVertexAction
{
	public AddingVertexAction(Vertex vertexConcerned,
			GraphModel graphConcerned)
	{
		super(vertexConcerned, graphConcerned);
	}

	public void doAction() 
	{
		getGraphConcerned().addVertex(getVertexConcerned());
	}

	public void undoAction()
	{
		getGraphConcerned().removeVertex(getVertexConcerned());
	}
}