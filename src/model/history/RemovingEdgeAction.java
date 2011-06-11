package model.history;

import model.GraphModel;

/**
 * Edge Removing action
 */ 
public class RemovingEdgeAction extends EdgeAction
{
	public RemovingEdgeAction(int side1, int side2, GraphModel concernedGraph)
	{
		super(side1, side2, concernedGraph);
	}

	public void doAction()
	{
		getConcernedGraph().removeEdge(getSide1(), getSide2());
	}

	public void undoAction()
	{
		getConcernedGraph().addEdge(getSide1(), getSide2());
	}
}