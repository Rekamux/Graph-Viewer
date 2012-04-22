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
		getGraphConcerned().addVertexWithHistory(getVertexConcerned());
	}

	public void undoAction()
	{
		getGraphConcerned().removeVertexWithHistory(getVertexConcerned());
	}
}