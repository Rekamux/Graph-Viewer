package model.history;

import model.GraphModel;


/**
 * Edges Action
 */
public abstract class EdgeAction implements Action
{
	private int side1;
	private int side2;
	private GraphModel concernedGraph;
	
	public EdgeAction(int side1, int side2,
			GraphModel concernedGraph)
	{
		setSide1(side1);
		setSide2(side2);
		setConcernedGraph(concernedGraph);
	}
	public void setSide1(int side1) 
	{
		this.side1 = side1;
	}
	public int getSide1()
	{
		return side1;
	}
	public void setSide2(int side2)
	{
		this.side2 = side2;
	}
	public int getSide2() 
	{
		return side2;
	}
	public void setConcernedGraph(GraphModel concernedGraph)
	{
		this.concernedGraph = concernedGraph;
	}
	public GraphModel getConcernedGraph()
	{
		return concernedGraph;
	}
}
