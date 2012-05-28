package model.history;

import java.util.ArrayList;

import model.GraphModel;
import model.Vertex;

/**
 * Removing vertices action
 */
public class RemovingVerticesAction extends AddingRemovingVerticesAction
{
	public RemovingVerticesAction(ArrayList<Vertex> verticesConcerned,
			GraphModel graphConcerned)
	{
		super(verticesConcerned, graphConcerned);
	}

	public void doAction() 
	{
		getGraphConcerned().removeVertices(getVerticesConcerned());
	}

	public void undoAction() 
	{
		getGraphConcerned().addVertices(getVerticesConcerned());
	}
}