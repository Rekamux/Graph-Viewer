package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

/**
 * Represents a graph under adjacency matrix representation
 * 
 * @author Axel Schumacher
 */
public class MathematicGraph extends Observable implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Returns true if i and j are neighbors
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public static boolean areComponentNeighbors(int[] components, int i, int j)
	{
		return components[i] == components[j];
	}

	/**
	 * Vertices adjacency matrix
	 */
	private ArrayList<ArrayList<Boolean>> adjacencyMatrix = new ArrayList<ArrayList<Boolean>>();

	/**
	 * Main model
	 */
	protected transient Model model;

	/**
	 * Vertices' number
	 */
	private int n;

	/**
	 * Is an non-oriented graph
	 */
	private boolean nonOriented = true;

	/**
	 * Matrix constructor using ArrayList tab Warning! There is no control!
	 * 
	 * @param matrix
	 *            Given adjacency matrix
	 */
	public MathematicGraph(ArrayList<ArrayList<Boolean>> matrix, Model m)
	{
		model = m;
		n = matrix.size();
		init(n);
		copyMatrix(matrix);
	}

	/**
	 * Matrix constructor using multidimensional tab Warning! There is no
	 * control!
	 * 
	 * @param matrix
	 *            Given adjacency matrix
	 */
	public MathematicGraph(boolean matrix[][], Model m)
	{
		model = m;
		n = matrix.length;
		init(n);
		copyMatrix(matrix);
	}

	/**
	 * Random constructor generating n vertices and m edges
	 * 
	 * @param n
	 *            Vertices' number
	 * @param m
	 *            Edges' number
	 */
	public MathematicGraph(int n, int m, Model model)
	{
		this.model = model;
		init(n);
		generateRandomlyVertices(m);
	}

	/**
	 * Constructor generating n vertices and no edge
	 * 
	 * @param n
	 *            Vertices' number
	 */
	public MathematicGraph(int n, Model m)
	{
		model = m;
		init(n);
	}

	/**
	 * Copy constructor
	 * 
	 * @param other
	 *            graph to copy
	 * @param m
	 *            main model
	 */
	public MathematicGraph(MathematicGraph other, Model m)
	{
		this(m);
		n = other.n;
		init(n);
		copyMatrix(other.adjacencyMatrix);
		nonOriented = other.nonOriented;
	}

	/**
	 * Basic constructor
	 */
	public MathematicGraph(Model m)
	{
		model = m;
	}

	/**
	 * Adds an edge between two vertices
	 * 
	 * @param i
	 *            First extremity vertex
	 * @param j
	 *            Second extremity vertex
	 * @return true if the edge doesn't already exist, false else
	 */
	public boolean addEdge(int i, int j)
	{
		if (areNeighbors(i, j))
			return false;
		adjacencyMatrix.get(i).set(j, true);
		if (isNonOriented())
			adjacencyMatrix.get(j).set(i, true);
		setChanged();
		return true;
	}

	/**
	 * Adds a vertex with no neighborhood
	 */
	public void addVertex()
	{
		for (int i = 0; i < n; i++)
			adjacencyMatrix.get(i).add(false);
		n++;
		ArrayList<Boolean> neighbors = new ArrayList<Boolean>();
		for (int i = 0; i < n; i++)
			neighbors.add(false);
		adjacencyMatrix.add(neighbors);
		setChanged();
	}

	/**
	 * Returns Adjacency matring under a String
	 */
	public String adjacencyMatrixToString()
	{
		String result = "";
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
				result += (adjacencyMatrix.get(i).get(j)?1:0) + " ";
			result += "\n";
		}
		return result;
	}

	/**
	 * Returns true if given vertices' numbers are neighbors
	 * 
	 * @param i
	 *            First vertex
	 * @param j
	 *            Second vertex
	 * @return true if i and j are neighbors, false else
	 */
	public boolean areNeighbors(int i, int j)
	{
		boolean result = adjacencyMatrix.get(i).get(j);
		if (isNonOriented())
			result = result && adjacencyMatrix.get(j).get(i);
		return result;
	}

	/**
	 * Computes the components
	 */
	public int[] computeComponent()
	{
		int[] components = new int[n];
		for (int i = 0; i < n; i++)
			components[i] = i;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (areNeighbors(i, j) && components[i] != components[j])
				{
					int aux = components[j];
					for (int k = 0; k < n; k++)
						if (components[k] == aux)
							components[k] = components[i];
				}
		return components;
	}

	/**
	 * Copy a matrix into the adjacency matrix
	 * 
	 * @param matrix
	 *            Adjacency matrix to copy
	 */
	private void copyMatrix(ArrayList<ArrayList<Boolean>> matrix)
	{
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				adjacencyMatrix.get(i).set(j, matrix.get(i).get(j));
		setChanged();
	}

	/**
	 * Copy a matrix into the adjacency matrix
	 * 
	 * @param matrix
	 *            Adjacency matrix to copy
	 */
	private void copyMatrix(boolean matrix[][])
	{
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				adjacencyMatrix.get(i).set(j, matrix[i][j]);
		setChanged();
	}

	/**
	 * Generates randomly m vertices
	 */
	private void generateRandomlyVertices(int m)
	{
		Random R = new Random(System.currentTimeMillis());
		for (int i = 0; i < m; i++)
		{
			boolean found = false;
			while (!found)
			{
				int x = R.nextInt(n);
				int y = R.nextInt(n);
				if (x != y)
				{
					boolean areNeighbors = false;
					if (adjacencyMatrix.get(x).get(y))
						areNeighbors = true;
					else if (isNonOriented())
						if (adjacencyMatrix.get(y).get(x))
							areNeighbors = true;
					if (!areNeighbors)
					{
						adjacencyMatrix.get(x).set(y, true);
						if (isNonOriented())
							adjacencyMatrix.get(y).set(x, true);
						found = true;
					}
				}
			}
		}
		setChanged();
	}

	/**
	 * @return the adjacencyMatrix
	 */
	public ArrayList<ArrayList<Boolean>> getAdjacencyMatrix()
	{
		return adjacencyMatrix;
	}

	/**
	 * @return n
	 */
	public int getN()
	{
		return n;
	}

	/**
	 * Initiates graph using n vertices
	 * 
	 * @param n
	 */
	private void init(int n)
	{
		this.n = n;
		for (int i = 0; i < n; i++)
		{
			adjacencyMatrix.add(new ArrayList<Boolean>());
			for (int j = 0; j < n; j++)
				adjacencyMatrix.get(i).add(false);
		}
	}

	/**
	 * @return true if graph is not oriented
	 */
	public boolean isNonOriented()
	{
		return nonOriented;
	}

	/**
	 * @return true if graph is oriented
	 */
	public boolean isOriented()
	{
		return !nonOriented;
	}

	/**
	 * Removes an edge between two vertices
	 * 
	 * @param i
	 *            First extremity vertex
	 * @param j
	 *            Second extremity vertex
	 * @return true if the edge did exist, false else
	 */
	public boolean removeEdge(int i, int j)
	{
		if (!areNeighbors(i, j))
			return false;
		adjacencyMatrix.get(i).set(j, false);
		setChanged();
		return true;
	}

	/**
	 * Removes a vertex
	 * 
	 * @param i
	 *            Vertex to remove
	 */
	protected void removeVertex(int i)
	{
		adjacencyMatrix.remove(i);
		n--;
		for (int j = 0; j < n; j++)
			adjacencyMatrix.get(j).remove(i);
		setChanged();
	}

	/**
	 * @param the
	 *            oriented to set
	 */
	public void setOriented(boolean oriented)
	{
		this.nonOriented = !oriented;
		if (!oriented)
			for (int x = 0; x < n; x++)
				for (int y = 0; y < n; y++)
					if (adjacencyMatrix.get(x).get(y))
						adjacencyMatrix.get(y).set(x, true);
		setChanged();
	}

	/**
	 * Computes graph's complement
	 */
	public void toComplement()
	{
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				if (i != j)
					adjacencyMatrix.get(i).set(j,
							!adjacencyMatrix.get(i).get(j));
			}
		}
		setChanged();
	}
}
