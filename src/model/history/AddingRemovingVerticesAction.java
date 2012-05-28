package model.history;

import java.util.ArrayList;

import model.GraphModel;
import model.Vertex;


/**
 * Vertices adding/Deleting action
 */
public abstract class AddingRemovingVerticesAction extends VerticesAction
{
	private GraphModel graphConcerned;
	
	public AddingRemovingVerticesAction(ArrayList<Vertex> verticesConcerned, GraphModel graphConcerned) 
	{
		super(verticesConcerned);
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