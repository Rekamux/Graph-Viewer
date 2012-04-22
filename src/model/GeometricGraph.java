package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import utils.Computing;
import view.SearchBetterPosition;

/**
 * Represents a graph under plan representation
 * 
 * @author Axel Schumacher
 */
public class GeometricGraph extends MathematicGraph implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Default Vertex's diameter
	 */
	private int diameter = 10;

	/**
	 * Graph's height
	 */
	private int height = 50;

	/**
	 * Graph's width
	 */
	private int width = 50;

	/**
	 * Maximal distance
	 */
	private float maximalDistance = Math.min(width, height) / 5;

	/**
	 * Minimal neighbors distance
	 */
	private float minimalDistance = 10 * diameter;

	/**
	 * Search better position runnable
	 */
	private SearchBetterPosition searchBetterPositionThread = new SearchBetterPosition(
			this);

	/**
	 * Vertices list
	 */
	protected ArrayList<Vertex> verticesList = new ArrayList<Vertex>();

	/**
	 * Constructor using a vertices list
	 */
	public GeometricGraph(ArrayList<Vertex> verticesList, int width,
			int height, Model m) {
		super(m);
		this.width = width;
		this.height = height;
		int n = verticesList.size();
		copyVertices(verticesList);
		init(n, false);
	}

	/**
	 * Copy constructor
	 * 
	 * @param other
	 *            graph to copy
	 * @param m
	 *            model
	 */
	public GeometricGraph(GeometricGraph other, Model m) {
		super(other, m);
		copyVertices(other.verticesList);
		init(other.getN(), false);
	}

	/**
	 * Constructor using an adjacency matrix and a vertices list
	 */
	public GeometricGraph(int width, int height,
			ArrayList<ArrayList<Boolean>> matrix,
			ArrayList<Vertex> verticesList, Model m) {
		super(matrix, m);
		this.width = width;
		this.height = height;
		int n = matrix.size();
		copyVertices(verticesList);
		init(n, false);
	}

	/**
	 * Constructor using an adjacency matrix
	 */
	public GeometricGraph(int width, int height,
			ArrayList<ArrayList<Boolean>> matrix, Model m) {
		super(matrix, m);
		this.width = width;
		this.height = height;
		int n = matrix.size();
		init(n, true);
	}

	/**
	 * Constructor with dimensions, vertices' count and random edges count
	 * 
	 * @param width
	 *            Graph width
	 * @param height
	 *            Graph height
	 */
	public GeometricGraph(int width, int height, int n, int m, Model model) {
		super(n, m, model);
		this.width = width;
		this.height = height;
		init(n, true);
	}

	/**
	 * Constructor with dimensions and Vertices' number
	 * 
	 * @param width
	 *            Graph width
	 * @param height
	 *            Graph height
	 */
	public GeometricGraph(int width, int height, int n, Model m) {
		super(m);
		this.width = width;
		this.height = height;
		init(n, true);
	}

	/**
	 * Constructor with dimensions
	 * 
	 * @param width
	 *            Graph width
	 * @param height
	 *            Graph height
	 */
	public GeometricGraph(int width, int height, Model m) {
		super(m);
		this.width = width;
		this.height = height;
	}

	/**
	 * Basic constructor
	 */
	public GeometricGraph(Model m) {
		super(m);
	}

	@Override
	public void addVertex() {
		addVertex(new Vertex());
	}

	/**
	 * Add a Vertex
	 */
	public Vertex addVertex(String name, int x, int y, boolean label) {
		Vertex v = new Vertex(name, x, y, diameter, label);
		addVertex(v);
		setChanged();
		return v;
	}

	/**
	 * Adds a Vertex
	 */
	public void addVertex(Vertex vertex) {
		stopMove();
		super.addVertex();
		verticesList.add(vertex);
		setChanged();
	}

	/**
	 * Returns true if Vertices i and j are both fixed
	 */
	public boolean areBothFixed(int i, int j) {
		Vertex vI = verticesList.get(i);
		Vertex vJ = verticesList.get(j);
		return (vI.isFixed() && vJ.isFixed());
	}

	/**
	 * Computes pounds between two Vertices
	 */
	public float computePounds(int i, int j) {
		if (i == j)
			return 0;
		Vertex I = verticesList.get(i);
		Vertex J = verticesList.get(j);
		float distance = Computing
				.geometricDistance(I.getPoint(), J.getPoint());
		float pounds;
		if (distance == 0)
			pounds = Float.MAX_VALUE;
		else
			pounds = (float) (Math.exp(distance));
		if (distance > minimalDistance)
			if (areNeighbors(i, j))
				pounds = -10 * pounds;
			else if (distance > maximalDistance)
				pounds -= pounds;
		return pounds;
	}

	/**
	 * Deeply copies vertices
	 * 
	 * @param verticesList2
	 */
	private void copyVertices(ArrayList<Vertex> other) {
		verticesList = new ArrayList<Vertex>();
		for (int i = 0; i < getN(); i++) {
			Vertex v = new Vertex(other.get(i));
			verticesList.add(v);
		}
	}

	/**
	 * Draws an edge between two vertices
	 */
	public void drawEdge(Graphics g, int i, int j) {
		Vertex vI = getVertex(i);
		int xI = vI.getXPosition();
		int yI = vI.getYPosition();
		Vertex vJ = getVertex(j);
		int xJ = vJ.getXPosition();
		int yJ = vJ.getYPosition();
		boolean bothFixed = areBothFixed(i, j);
		drawEdge(g, xI, yI, xJ, yJ, bothFixed);
	}

	public void drawEdge(Graphics g, int xI, int yI, int xJ, int yJ,
			boolean bothFixed) {
		if (bothFixed)
			g.setColor(model.getFixedColor());
		// Array drawing
		if (isOriented()) {
			int triangle[] = getOrientedTriangle(xI, yI, xJ, yJ);
			int Xs[] = { triangle[0], triangle[2], triangle[4] };
			int Ys[] = { triangle[1], triangle[3], triangle[5] };
			g.fillPolygon(Xs, Ys, 3);
		}
		g.drawLine(xI, yI, xJ, yJ);
		g.setColor(model.getEdgesColor());
	}

	/**
	 * Draws the graph
	 */
	public void drawGraph(Graphics g) {
		for (int i = 0; i < getN(); i++)
			for (int j = 0; j < getN(); j++)
				if (areNeighbors(i, j))
					drawEdge(g, i, j);
		g.setColor(model.getVerticesColor());
		for (int i = 0; i < getN(); i++)
			if (i != model.getCurrentVertexIndex())
				drawVertex(i, g);

	}

	/**
	 * Searches a balanced configuration
	 */
	/*
	 * public boolean searchBalancedConfiguration(int limit, int
	 * inactivityTimeMax, int threshold, boolean repaint, int timeToSleep) { if
	 * (repaint && graph == null) {System.err.println(
	 * "In searchBalancedConfiguration: parent isn't defined! Cannot repaint!");
	 * return false; } int inactivityTime = 0; int i = 0; int n =
	 * verticesList.size(); boolean finished = false; ArrayList<Couple<Integer>>
	 * movesList = new ArrayList<Couple<Integer>>(); for (int j=0; j<n; j++)
	 * movesList.add(new Couple<Integer>(0, 0)); while (i < limit && !finished)
	 * { if (repaint) { graph.repaint(); try {Thread.sleep(timeToSleep);} catch
	 * (InterruptedException e) {} } if (inactivityTime<inactivityTimeMax) {
	 * inactivityTime ++; } else { inactivityTime = 0; boolean found = false;
	 * for (int j=0; j<n && !found; j++) { Couple<Integer> testCouple =
	 * movesList.get(j); if (testCouple.getFirst() > threshold ||
	 * testCouple.getSecond() > threshold) found = true; } if (!found) {
	 * finished = true; break; } else for (int j=0; j<n; j++) movesList.set(j,
	 * new Couple<Integer>(0, 0)); } for (int j=0; j<n; j++) { Couple<Integer>
	 * newCoupleJ = moveVertexIfFurther(j); Couple<Integer> formerCoupleJ =
	 * movesList.get(j); Couple<Integer> sumCouple = new
	 * Couple<Integer>(newCoupleJ.getFirst() + formerCoupleJ.getFirst(),
	 * newCoupleJ.getSecond() + formerCoupleJ.getSecond()); movesList.set(j,
	 * sumCouple); } i++; } if (finished) {
	 * System.out.println("Configuration found."); return true; } else {
	 * System.out.println("No configuration found."); return false; } }
	 */

	/**
	 * Draws the Vertex i
	 */
	private void drawVertex(int i, Graphics g) {
		Vertex v = verticesList.get(i);
		boolean fixed = v.isFixed();
		boolean label = v.isLabel();
		g.setColor(model.getVerticesColor());
		if (label) {
			g.setColor(model.getLabelColor());
		} else if (fixed) {
			g.setColor(model.getFixedColor());
		}
		v.draw(g);
		g.setColor(model.getVerticesColor());
	}

	public int getDiameter() {
		return diameter;
	}

	/**
	 * @return graphs dimension
	 */
	public Dimension getDimension() {
		return new Dimension(width, height);
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Returns three couples representing oriented arrow
	 */
	public int[] getOrientedTriangle(int xI, int yI, int xJ, int yJ) {
		// Middle position
		int xM = xI - (xI - xJ) / 2;
		int yM = yI - (yI - yJ) / 2;
		// Array points to link to the middle
		int xP1 = 0;
		int yP1 = 0;
		int xP2 = 0;
		int yP2 = 0;
		if (xI == xJ) {
			int start;
			if (yI <= yJ)
				start = yM - diameter;
			else
				start = yM + diameter;
			xP1 = xM + diameter;
			yP1 = start;
			xP2 = xM - diameter;
			yP2 = start;
		} else {
			double dist = Math.sqrt((xI - xJ) * (xI - xJ) + (yI - yJ)
					* (yI - yJ));
			// start point definition
			double xStart, yStart;
			if (xI <= xJ)
				xStart = xM - (double) ((xJ - xI) * diameter) / dist;
			else
				xStart = xM + (double) ((xI - xJ) * diameter) / dist;
			if (yI <= yJ)
				yStart = yM - (double) ((yJ - yI) * diameter) / dist;
			else
				yStart = yM + (double) ((yI - yJ) * diameter) / dist;
			// Let's solve y=ax+b
			double a = (double) (yJ - yI) / (double) (xJ - xI);
			// Normal vector definition
			double dN = Math.sqrt(a * a + 1);
			double xN = a / dN * (double) diameter;
			double yN = -1 / dN * (double) diameter;
			xP2 = (int) (xStart + xN);
			yP2 = (int) (yStart + yN);
			xP1 = (int) (xStart - xN);
			yP1 = (int) (yStart - yN);
		}
		int result[] = { xM, yM, xP1, yP1, xP2, yP2 };
		return result;
	}

	public Vertex getVertex(int i) {
		return verticesList.get(i);
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Initiates the graph
	 * 
	 * @param n
	 */
	private void init(int n, boolean createVerticesList) {
		for (int i = 0; i < n; i++) {
			if (createVerticesList)
				verticesList.add(new Vertex(Vertex.indexToString(i), 0, 0,
						diameter, false));
		}
		if (createVerticesList)
			moveAllVertices();
	}

	public boolean isAllowedToMove() {
		return searchBetterPositionThread != null;
	}

	/**
	 * Lets all unfixed vertices move
	 */
	public void letEmMove() {
		searchBetterPositionThread = new SearchBetterPosition(this);
		searchBetterPositionThread.start();
		setChanged();
	}

	/**
	 * Changes every Vertex position
	 */
	public void moveAllVertices() {
		Random R = new Random(System.currentTimeMillis());
		int n = verticesList.size();
		int rands[][] = new int[n][2];

		for (int i = 0; i < n; i++) {
			rands[i][0] = R.nextInt(width - 4 * diameter) + 2 * diameter;
			rands[i][1] = R.nextInt(height - 4 * diameter) + 2 * diameter;
		}

		for (int i = 0; i < n; i++)
			moveVertex(i, rands[i][0], rands[i][1]);
		setChanged();
	}

	/**
	 * Changes every Vertex position
	 */
	public void moveAllVerticesByComponent() {
		Random R = new Random(System.currentTimeMillis());
		int n = verticesList.size();
		int rands[][] = new int[n][2];

		for (int i = 0; i < n; i++) {
			rands[i][0] = R.nextInt(width - 4 * diameter) + 2 * diameter;
			rands[i][1] = R.nextInt(height - 4 * diameter) + 2 * diameter;
		}
		int[] components = computeComponent();

		for (int i = 0; i < n; i++)
			moveVertex(i, rands[components[i]][0], rands[components[i]][1]);
		setChanged();
	}

	/**
	 * Changes a Vertex's position
	 */
	public void moveVertex(int i, int x, int y) {
		verticesList.get(i).setXPosition(x);
		verticesList.get(i).setYPosition(y);
		setChanged();
	}

	@Override
	public void removeVertex(int i) {
		super.removeVertex(i);
		verticesList.remove(i);
		setChanged();
	}

	/**
	 * removes specified vertex
	 */
	public void removeVertex(Vertex v) {
		int i = verticesList.indexOf(v);
		removeVertex(i);
		setChanged();
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}

	public void setDiameter(int i, int diameter) {
		verticesList.get(i).setDiameter(diameter);
		setChanged();
	}

	public void setDimension(Dimension d) {
		setWidth((int) d.getWidth());
		setHeight((int) d.getHeight());
		setChanged();
	}

	public void setStateFixed(int i, boolean fixed) {
		verticesList.get(i).setFixed(fixed);
		setChanged();
	}

	public void setHeight(int height) {
		int n = verticesList.size();
		if (this.height > height)
			for (int i = 0; i < n; i++)
				if (getVertex(i).getYPosition() > height - diameter)
					getVertex(i).setYPosition(height - diameter);
		this.height = height;
	}

	public void setName(int i, String name) {
		verticesList.get(i).setName(name);
		setChanged();
	}

	public void setWidth(int width) {
		int n = verticesList.size();
		if (this.width > width)
			for (int i = 0; i < n; i++)
				if (getVertex(i).getXPosition() > width - diameter)
					getVertex(i).setXPosition(width - diameter);
		this.width = width;
	}

	public void setXPosition(int i, int position) {
		verticesList.get(i).setXPosition(position);
		setChanged();
	}

	public void setYPosition(int i, int position) {
		verticesList.get(i).setYPosition(position);
		setChanged();
	}

	public void removeVertices(ArrayList<Vertex> vertices) {
		for (Vertex v : vertices) {
			removeVertex(v);
		}
	}

	/**
	 * Stop move
	 */
	public void stopMove() {
		if (searchBetterPositionThread == null)
			return;
		searchBetterPositionThread.interrupt();
		System.out.println("interrupt");
		searchBetterPositionThread = null;
	}

	/**
	 * Checks if a click concerns a Vertex and returns its number if its exists,
	 * -1 else.
	 */
	public int whichVertex(Point p, int multiplier, Graphics g) {
		int n = verticesList.size();
		for (int i = 0; i < n; i++) {
			Vertex v = verticesList.get(i);
			if (v.getCircleRectangle(multiplier).contains(p))
				return i;
		}
		for (int i = 0; i < n; i++) {
			Vertex v = verticesList.get(i);
			if (v.getNameRectangle(g).contains(p))
				return i;
		}
		return -1;
	}

	public ArrayList<Vertex> getVerticesList() {
		return verticesList;
	}
}
