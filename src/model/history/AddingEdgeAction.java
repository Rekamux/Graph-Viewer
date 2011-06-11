package model.history;

import model.GraphModel;

/**
 * Edge Adding action
 */
public class AddingEdgeAction extends EdgeAction
{
	public AddingEdgeAction(int side1, int side2,
			GraphModel concernedGraph) 
	{
		super(side1, side2, concernedGraph);
	}

	public void doAction()
	{
		getConcernedGraph().addEdge(getSide1(), getSide2());
	}

	public void undoAction()
	{
		getConcernedGraph().removeEdge(getSide1(), getSide2());
	}
}
