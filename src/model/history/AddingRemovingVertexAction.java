package model.history;

import model.GraphModel;
import model.Vertex;


/**
 * Vertex adding/Deleting action
 */
public abstract class AddingRemovingVertexAction extends VertexAction
{
	private GraphModel graphConcerned;
	
	public AddingRemovingVertexAction(Vertex vertexConcerned, GraphModel graphConcerned) 
	{
		super(vertexConcerned);
		setGraphConcerned(graphConcerned);
	}

	public void setGraphConcerned(GraphModel graphConcerned)
	{
		this.graphConcerned = graphConcerned;
	}

	public GraphModel getGraphConcerned()
	{
		return graphConcerned;
	}
}