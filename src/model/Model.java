package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Main model containing graphs list and view parameters
 * 
 * @author Ax
 * 
 */
public class Model extends Observable
{
	
	public Model()
	{
		setChanged();
	}
	/**
	 * Mouse action mode
	 */
	public static enum MouseActionMode
	{
		MOUSE_ADDS_VERTICES, MOUSE_CHANGES_EDGES, MOUSE_DELETES_VERTICES, MOUSE_MOVES_VERTICES, MOUSE_SELECTS_VERTICES
	}

	/**
	 * Tools
	 * 
	 * @author Ax
	 * 
	 */
	public static enum Tool
	{
		VERTEX, EDGE, GRAPH
	}

	/**
	 * Color options of the model
	 * 
	 * @author Ax
	 * 
	 */
	public static enum ColorOption
	{
		BACKGROUND, VERTICES, FIXED, EDGES, SELECTED
	}

	/**
	 * Changes the selected color
	 */
	public void setColor(ColorOption option, Color color)
	{
		switch (option)
		{
		case BACKGROUND:
			backgroundColor = color;
			break;

		case EDGES:
			edgesColor = color;
			break;

		case FIXED:
			fixedColor = color;
			break;

		case SELECTED:
			selectedColor = color;
			break;

		case VERTICES:
			verticesColor = color;
			break;
		}
		setChanged();
	}

	/**
	 * Returns the concerned color
	 */
	public Color getColor(ColorOption option)
	{
		switch (option)
		{
		case BACKGROUND:
			return backgroundColor;

		case EDGES:
			return edgesColor;

		case FIXED:
			return fixedColor;

		case SELECTED:
			return selectedColor;

		case VERTICES:
			return verticesColor;
		}
		return null;
	}

	/**
	 * @param option
	 * @return the name of the field containing the color
	 */
	public String getColorSectionName(ColorOption option)
	{
		switch (option)
		{
		case BACKGROUND:
			return "background";

		case EDGES:
			return "edges";

		case FIXED:
			return "fixed vertices and edges";

		case SELECTED:
			return "selected vertex";

		case VERTICES:
			return "vertices";
		}
		return null;
	}

	/**
	 * Background color
	 */
	private Color backgroundColor = Color.white;

	/**
	 * Current File in all the program
	 */
	private File currentFile = new File(".");

	/**
	 * Current selected graph index
	 */
	private int currentGraphIndex = -1;

	/**
	 * Current tool
	 */
	private Tool currentTool = Tool.GRAPH;

	/**
	 * Dragging hot point
	 */
	private Point draggingHotPoint = null;

	/**
	 * Mouse starting position when drawing a rectangle
	 */
	private Point drawingRectanglePosition = null;

	/**
	 * Edges color
	 */
	private Color edgesColor = Color.black;

	/**
	 * Fixed color
	 */
	private Color fixedColor = Color.black;

	/**
	 * Former moved vertex position
	 */
	private Point formerPosition;

	/**
	 * Graphs list
	 */
	private ArrayList<GraphModel> graphs = new ArrayList<GraphModel>();

	/**
	 * Mouse starting position when linking two vertices
	 */
	private Point linkingPosition = null;

	/**
	 * Mouse action mode
	 */
	private MouseActionMode mouseActionMode = MouseActionMode.MOUSE_MOVES_VERTICES;

	/**
	 * Next created vertex name
	 */
	private String nextVertexName = "a";
	
	/** Is next created vertex a label */
	private boolean nextVertexLabel = false;

	/**
	 * Is open graph dialog shown
	 */
	private boolean openGraphDialogShown = false;

	/**
	 * Selected color
	 */
	private Color selectedColor = Color.black;

	/**
	 * Selected vertices
	 */
	private int selectedVertexIndex = -1;

	/**
	 * Selection rectangle
	 */
	private Rectangle selectionRectangle = null;

	/**
	 * Stamp color
	 */
	private Color stampColor = Color.black;

	/**
	 * Vertex diameter multiplier : used when attempting to select a vertex
	 */
	private int vertexDiameterMultiplier = 2;

	/**
	 * Which Vertex is dragged
	 */
	private Vertex vertexDragged = null;;

	/**
	 * Vertices color
	 */
	private Color verticesColor = Color.black;

	/**
	 * Creates a vertex at indicated position
	 * 
	 * @param point
	 *            position
	 */
	public void addVertex(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		int x = (int) point.getX();
		int y = (int) point.getY();
		g.addVertex(nextVertexName, x, y, nextVertexLabel);
		nextVertexName = Vertex.indexToString(g.getN());
		setChanged();
	};

	/**
	 * Creates or deletes an edge if a starting vertex is defined and if point
	 * contains an other vertex
	 * 
	 * @param point
	 *            end position
	 */
	public void changeEdge(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		int endVertex = g.whichVertex(point, vertexDiameterMultiplier);
		if (selectedVertexIndex != -1 && endVertex != -1
				&& selectedVertexIndex != endVertex)
			if (g.areNeighbors(selectedVertexIndex, endVertex))
				g.removeEdge(selectedVertexIndex, endVertex);
			else
				g.addEdge(selectedVertexIndex, endVertex);
		setChanged();
	}

	/**
	 * Change vertex fixation included in point
	 */
	public void changeVertexFixation(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		int i = g.whichVertex(point, vertexDiameterMultiplier);
		if (i == -1)
			return;
		Vertex v = g.getVertex(i);
		v.setFixed(!v.isFixed());
		setChanged();
	}

	/**
	 * Inverts vertices fixation in drawn rectangle
	 */
	public void changeVerticesFixationInRectangle()
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		if (selectionRectangle == null)
			return;
		for (int i = 0; i < g.getN(); i++)
		{
			Vertex v = g.getVertex(i);
			if (selectionRectangle.contains(g.getVertex(i).getPoint()))
				v.setFixed(!v.isFixed());
		}
	}

	/**
	 * Adds a graph to the model
	 */
	public void createGraph(GraphModel graph)
	{
		graphs.add(graph);
		setCurrentGraphIndex(getGraphsCount() - 1);
		setChanged();
	}

	/**
	 * Deletes last vertex in rectangle drawn
	 */
	public void deleteVertexInRectangle()
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		if (selectionRectangle == null)
			return;
		for (int i = g.getN() - 1; i >= 0; i--)
			if (selectionRectangle.contains(g.getVertex(i).getPoint()))
				g.removeVertex(i);
	}

	/**
	 * Tries to delete a vertex included in point
	 */
	public void deleteVertice(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		int i = g.whichVertex(point, vertexDiameterMultiplier);
		if (i != -1)
			g.removeVertex(i);
		else
			selectedVertexIndex = -1;
		setChanged();
	}

	/**
	 * Do action
	 */
	public void doAgain()
	{
		GraphModel g = getCurrentGraph();
		if (g != null)
			g.doAgain();
	}

	/**
	 * Drags selected vertex onto the given point
	 * 
	 * @param point
	 *            point
	 */
	public void dragVertex(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		if (vertexDragged == null)
			return;
		vertexDragged.setFixed(true);
		int x = (int) point.getX();
		int y = (int) point.getY();
		if (x >= 0 && x < g.getWidth())
			vertexDragged.setXPosition((int) (x - draggingHotPoint.getX()));
		if (y >= 0 && y < g.getHeight())
			vertexDragged.setYPosition((int) (y - draggingHotPoint.getY()));
		setChanged();
	}

	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	/**
	 * @return the currentFile
	 */
	public File getCurrentFile()
	{
		return currentFile;
	}

	/**
	 * @return the current graph
	 */
	public GraphModel getCurrentGraph()
	{
		if (currentGraphIndex < 0)
			return null;
		return graphs.get(currentGraphIndex);
	}

	/**
	 * @return the currentGraphIndex
	 */
	public int getCurrentGraphIndex()
	{
		return currentGraphIndex;
	}

	/**
	 * @return the currentTool
	 */
	public Tool getCurrentTool()
	{
		return currentTool;
	}

	/**
	 * @return the currentVertex
	 */
	public int getCurrentVertexIndex()
	{
		return selectedVertexIndex;
	}

	/**
	 * @return the draggingHotPoint
	 */
	public Point getDraggingHotPoint()
	{
		return draggingHotPoint;
	}

	/**
	 * @return the drawingRectPosition
	 */
	public Point getDrawingRectPosition()
	{
		return drawingRectanglePosition;
	}

	/**
	 * @return the edgesColor
	 */
	public Color getEdgesColor()
	{
		return edgesColor;
	}

	/**
	 * @return the fixedColor
	 */
	public Color getFixedColor()
	{
		return fixedColor;
	}

	/**
	 * @return the formerPosition
	 */
	public Point getFormerPosition()
	{
		return formerPosition;
	}

	/**
	 * returns the graph
	 * 
	 * @param graphIndex
	 *            index
	 * @return the graph indexed by graphIndex
	 */
	public GraphModel getGraph(int graphIndex)
	{
		return graphs.get(graphIndex);
	}

	/**
	 * @return the total graphs count
	 */
	public int getGraphsCount()
	{
		return graphs.size();
	}

	/**
	 * @return the linkingPosition
	 */
	public Point getLinkingPosition()
	{
		return linkingPosition;
	}

	/**
	 * @return the mouseActionMode
	 */
	public MouseActionMode getMouseActionMode()
	{
		return mouseActionMode;
	}

	/**
	 * @return the next graph's default name
	 */
	public String getNextGraphName()
	{
		return "graph_" + getGraphsCount();
	}

	/**
	 * @return the nextVertexName
	 */
	public String getNextVertexName()
	{
		return nextVertexName;
	}

	/**
	 * @return the selectedColor
	 */
	public Color getSelectedColor()
	{
		return selectedColor;
	}

	/**
	 * @return the selectedVertex
	 */
	public int getSelectedVertex()
	{
		return selectedVertexIndex;
	}

	/**
	 * @return the selectedVertexIndex
	 */
	public int getSelectedVertexIndex()
	{
		return selectedVertexIndex;
	}

	/**
	 * @return the selectionRect
	 */
	public Rectangle getSelectionRect()
	{
		return selectionRectangle;
	}

	/**
	 * @return the stampColor
	 */
	public Color getStampColor()
	{
		return stampColor;
	}

	/**
	 * @return the vertexDragged
	 */
	public Vertex getVertexDragged()
	{
		return vertexDragged;
	}

	/**
	 * @return the verticesColor
	 */
	public Color getVerticesColor()
	{
		return verticesColor;
	}

	/**
	 * Initiates all geometric points
	 */
	public void initPoints()
	{
		drawingRectanglePosition = null;
		vertexDragged = null;
		draggingHotPoint = null;
		linkingPosition = null;
		selectionRectangle = null;
		formerPosition = null;
		setChanged();
	}

	/**
	 * @return the openGraphDialogShown
	 */
	public boolean isOpenGraphDialogShown()
	{
		return openGraphDialogShown;
	}

	@Override
	public void notifyObservers()
	{
		super.notifyObservers();
		for (GraphModel graph : graphs)
		{
			graph.notifyObservers();
		}
	}

	/**
	 * Selects a vertex in drawn rectangle
	 */
	public void selectInRectangle()
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		if (selectionRectangle == null)
			return;
		for (int i = 0; i < g.getN(); i++)
			if (selectionRectangle.contains(g.getVertex(i).getPoint()))
			{
				selectedVertexIndex = i;
				return;
			}
		setChanged();
	}

	/**
	 * Selects vertex included in point
	 */
	public void selectVertex(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		if (vertexDragged != null)
			vertexDragged = null;
		int i = g.whichVertex(point, vertexDiameterMultiplier);
		selectedVertexIndex = i;
		setChanged();
	}

	/**
	 * @param currentFile
	 *            the currentFile to set
	 */
	public void setCurrentFile(File currentFile)
	{
		this.currentFile = currentFile;
		setChanged();
	}

	/**
	 * @param currentGraphIndex
	 *            the currentGraphIndex to set
	 */
	public void setCurrentGraphIndex(int currentGraphIndex)
	{
		this.currentGraphIndex = currentGraphIndex;
		selectedVertexIndex = -1;
		setChanged();
	}

	/**
	 * @param currentTool
	 *            the currentTool to set
	 */
	public void setCurrentTool(Tool currentTool)
	{
		this.currentTool = currentTool;
		switch (currentTool)
		{
		case GRAPH:
			setMouseActionMode(MouseActionMode.MOUSE_MOVES_VERTICES);
			break;

		case VERTEX:
			setMouseActionMode(MouseActionMode.MOUSE_ADDS_VERTICES);
			break;

		case EDGE:
			setMouseActionMode(MouseActionMode.MOUSE_CHANGES_EDGES);
			break;
		}
		setChanged();
	}

	/**
	 * @param currentVertex
	 *            the currentVertex to set
	 */
	public void setCurrentVertex(int currentVertex)
	{
		this.selectedVertexIndex = currentVertex;
		setChanged();
	}

	/**
	 * @param drawingRectanglePosition
	 *            the drawingRectanglePosition to set
	 */
	public void setDrawingRectanglePosition(Point drawingRectanglePosition)
	{
		this.drawingRectanglePosition = drawingRectanglePosition;
		setChanged();
	}

	/**
	 * @param linkingPosition
	 *            the linkingPosition to set
	 */
	public void setLinkingPosition(Point linkingPosition)
	{
		this.linkingPosition = linkingPosition;
		setChanged();
	}

	/**
	 * Changes mouse action mode
	 */
	public void setMouseActionMode(MouseActionMode i)
	{
		mouseActionMode = i;
		initPoints();
		selectedVertexIndex = -1;
		setChanged();
	}

	/**
	 * @param nextVertexName
	 *            the nextVertexName to set
	 */
	public void setNextVertexName(String nextVertexName)
	{
		this.nextVertexName = nextVertexName;
		setChanged();
	}

	/**
	 * @param openGraphDialogShown
	 *            the openGraphDialogShown to set
	 */
	public void setOpenGraphDialogShown(boolean openGraphDialogShown)
	{
		this.openGraphDialogShown = openGraphDialogShown;
		setChanged();
	}

	/**
	 * @param selectedVertexIndex
	 *            the selectedVertexIndex to set
	 */
	public void setSelectedVertexIndex(int selectedVertexIndex)
	{
		this.selectedVertexIndex = selectedVertexIndex;
		setChanged();
	}

	/**
	 * Tries to start dragging a vertex or to draw a rectangle
	 */
	public void startDraggingVertex(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		int i = g.whichVertex(point, vertexDiameterMultiplier);
		if (i == -1)
			drawingRectanglePosition = point;
		else
		{
			vertexDragged = g.getVertex(i);
			int x = (int) point.getX() - vertexDragged.getXPosition();
			int y = (int) point.getY() - vertexDragged.getYPosition();
			draggingHotPoint = new Point(x, y);
			formerPosition = new Point(vertexDragged.getXPosition(),
					vertexDragged.getYPosition());
		}
		setChanged();
	}

	/**
	 * Undo action
	 */
	public void undo()
	{
		GraphModel g = getCurrentGraph();
		if (g != null)
			g.undo();
	}

	/**
	 * Updates the current selectionRectangle if defined according to given
	 * point
	 * 
	 * @param point
	 *            point
	 */
	public void updateSelectionRectangle(Point point)
	{
		GraphModel g = getCurrentGraph();
		if (g == null)
			return;
		if (drawingRectanglePosition == null)
			return;
		int xC = (int) point.getX();
		int yC = (int) point.getY();
		int xDRP = (int) drawingRectanglePosition.getX();
		int yDRP = (int) drawingRectanglePosition.getY();
		Point start = null;
		int width = 0;
		int height = 0;
		if (xC <= xDRP && yC <= yDRP)
		{
			start = point;
			width = xDRP - xC;
			height = yDRP - yC;
		} else if (xC <= xDRP)
		{
			start = new Point(xC, yDRP);
			width = xDRP - xC;
			height = yC - yDRP;
		} else if (yC <= yDRP)
		{
			start = new Point(xDRP, yC);
			width = xC - xDRP;
			height = yDRP - yC;
		} else
		{
			start = drawingRectanglePosition;
			width = xC - xDRP;
			height = yC - yDRP;
		}
		selectionRectangle = new Rectangle(start, new Dimension(width, height));
		setChanged();
	}

	/**
	 * Removes a graph
	 * 
	 * @param i
	 */
	public void removeGraph(int i)
	{
		graphs.remove(i);
		if (currentGraphIndex >= i)
			currentGraphIndex--;
		setChanged();
	}

	/**
	 * Resize all graphs to given dimensions
	 */
	public void resizeGraphs(Dimension dimension)
	{
		for (GraphModel graph : graphs)
		{
			graph.setHeight((int) dimension.getHeight());
			graph.setWidth((int) dimension.getWidth());
		}
	}

	public boolean isNextVertexLabel() {
		return nextVertexLabel;
	}

	public void setNextVertexLabel(boolean nextVertexLabel) {
		this.nextVertexLabel = nextVertexLabel;
	}
}
