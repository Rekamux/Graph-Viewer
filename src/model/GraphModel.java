package model;

import java.awt.Point;
import java.util.ArrayList;

import model.history.AddingEdgeAction;
import model.history.AddingVertexAction;
import model.history.HistoryModel;
import model.history.MovingVertexAction;
import model.history.RemovingEdgeAction;
import model.history.RemovingVertexAction;

/**
 * A graph main model
 * 
 * @author Axel Schumacher
 * 
 */
public class GraphModel extends GeometricGraph
{
	private static final long serialVersionUID = 1L;

	/**
	 * Graph's name
	 */
	private String graphsName = "graph";

	/**
	 * Graph's history
	 */
	private transient HistoryModel history = new HistoryModel();

	/**
	 * History lock
	 */
	private transient boolean historyAskedIt = false;

	/**
	 * Constructs an empty graph
	 */
	public GraphModel(String name, Model model)
	{
		super(model);
		this.graphsName = name;
	}

	/**
	 * Contructor using main fields
	 * 
	 * @param name
	 * @param width
	 * @param height
	 * @param n
	 * @param m
	 * @param model
	 */
	public GraphModel(String name, int width, int height, int n, int m,
			Model model)
	{
		super(width, height, n, m, model);
		this.graphsName = name;
	}

	/**
	 * Constructor with no vertices
	 * 
	 * @param name
	 * @param width
	 * @param height
	 * @param model
	 */
	public GraphModel(String name, int width, int height, Model model)
	{
		super(width, height, model);
		this.graphsName = name;
	}

	/**
	 * Constructor using adjacency matrix
	 * 
	 * @param name
	 * @param width
	 * @param height
	 * @param matrix
	 * @param m
	 */
	public GraphModel(String name, int width, int height,
			ArrayList<ArrayList<Boolean>> matrix, Model m)
	{
		super(width, height, matrix, m);
		this.graphsName = name;
	}

	/**
	 * Constructor using adjacency matrix and vertices list
	 * 
	 * @param name
	 * @param width
	 * @param height
	 * @param matrix
	 * @param verticesList
	 * @param m
	 */
	public GraphModel(String name, int width, int height,
			ArrayList<ArrayList<Boolean>> matrix,
			ArrayList<Vertex> verticesList, Model m)
	{
		super(width, height, matrix, verticesList, m);
		this.graphsName = name;
	}

	/**
	 * Returns vertices list
	 * 
	 * @return
	 */
	public String verticesListToString()
	{
		String result = "";
		for (int i = 0; i < getN(); i++)
			result += getVertex(i).getName() + " ";
		for (int i = 0; i < getN(); i++)
			for (int j = 0; j < getN(); j++)
				if (areNeighbors(i, j))
					result += getVertex(i).getName() + ","
							+ getVertex(j).getName() + " ";
		return result;
	}

	/**
	 * Undo action
	 */
	public void undo()
	{
		if (history.isUndoPossible())
		{
			historyAskedIt = true;
			history.undoLastAction();
			historyAskedIt = false;
		}
		setChanged();
	}

	/**
	 * Do action
	 */
	public void doAgain()
	{
		if (history.isDoNextPossible())
		{
			historyAskedIt = true;
			history.doNextAction();
			historyAskedIt = false;
		}
		setChanged();
	}

	/**
	 * @return the graphsName
	 */
	public String getGraphsName()
	{
		return graphsName;
	}

	/**
	 * @param graphsName
	 *            the graphsName to set
	 */
	public void setGraphsName(String graphsName)
	{
		this.graphsName = graphsName;
		setChanged();
	}

	/**
	 * Modifies a vertex's name
	 * 
	 * @param vertexIndex
	 * @param text
	 */
	public void setVertexName(int vertexIndex, String text)
	{
		getVertex(vertexIndex).setName(text);
		setChanged();
	}

	@Override
	public void notifyObservers()
	{
		super.notifyObservers();
	}

	/**
	 * Changes vertex' name distance
	 * 
	 * @param vertexIndex
	 * @param value
	 */
	public void setVertexNameDistance(int vertexIndex, int value)
	{
		getVertex(vertexIndex).setNameDistance(value);
		setChanged();
	}

	/**
	 * Changes vertex' name angle
	 * 
	 * @param vertexIndex
	 * @param value
	 */
	public void getVertexNameAngle(int vertexIndex, int value)
	{
		getVertex(vertexIndex).setNameAngle(value);
		setChanged();
	}

	/**
	 * Checks if model is null and sets given in this case Checks if history is
	 * null and initiates it in this case
	 */
	public void linkModelAndCheck(Model model)
	{
		if (this.model == null)
			this.model = model;
		if (history == null)
			history = new HistoryModel();
		setChanged();
	}

	@Override
	public void addVertex(Vertex vertex)
	{
		if (!historyAskedIt)
		{
			AddingVertexAction action = new AddingVertexAction(vertex, this);
			history.addAction(action);
		}
		super.addVertex(vertex);
	}

	@Override
	public void moveVertex(int i, int x, int y)
	{
		if (!historyAskedIt)
		{
			Vertex v = getVertex(i);
			MovingVertexAction action = new MovingVertexAction(v, v.getPoint(),
					new Point(x, y));
			history.addAction(action);
		}
		super.moveVertex(i, x, y);
	}

	@Override
	public void removeVertex(int i)
	{
		if (!historyAskedIt)
		{
			Vertex v = getVertex(i);
			RemovingVertexAction a = new RemovingVertexAction(v, this);
			history.addAction(a);
		}
		super.removeVertex(i);
	}

	@Override
	public void removeVertex(Vertex v)
	{
		if (!historyAskedIt)
		{
			RemovingVertexAction a = new RemovingVertexAction(v, this);
			history.addAction(a);
		}
		super.removeVertex(v);
	}

	@Override
	public void setXPosition(int i, int position)
	{
		if (!historyAskedIt)
		{
			Vertex v = getVertex(i);
			MovingVertexAction a = new MovingVertexAction(v, v.getPoint(),
					new Point(position, (int) v.getPoint().getY()));
			history.addAction(a);
		}
		super.setXPosition(i, position);
	}

	@Override
	public void setYPosition(int i, int position)
	{
		if (!historyAskedIt)
		{
			Vertex v = getVertex(i);
			MovingVertexAction a = new MovingVertexAction(v, v.getPoint(),
					new Point((int) v.getPoint().getX(), position));
			history.addAction(a);
		}
		super.setYPosition(i, position);
	}

	@Override
	public boolean addEdge(int i, int j)
	{
		if (!historyAskedIt)
		{
			AddingEdgeAction a = new AddingEdgeAction(i, j, this);
			history.addAction(a);
		}
		return super.addEdge(i, j);
	}

	@Override
	public boolean removeEdge(int i, int j)
	{
		if (!historyAskedIt)
		{
			RemovingEdgeAction a = new RemovingEdgeAction(i, j, this);
			history.addAction(a);
		}
		return super.removeEdge(i, j);
	}

	/**
	 * @return is undo possible
	 */
	public boolean isUndoPossible()
	{
		return history.isUndoPossible();
	}

	/**
	 * @return is do undid possible
	 */
	public boolean isDoNextPossible()
	{
		return history.isDoNextPossible();
	}

	/**
	 * Change vertex label state
	 * @param vertexIndex vertex to change
	 * @param selected is a label
	 */
	public void setVertexLabel(int vertexIndex, boolean selected) {
		getVertex(vertexIndex).setLabel(selected);
		System.out.println("Label of "+vertexIndex+": "+selected);
	}
}
