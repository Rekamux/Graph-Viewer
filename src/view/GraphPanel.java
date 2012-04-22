package view;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.GraphModel;
import model.Model;
import model.Model.MouseActionMode;
import model.Vertex;
import controller.MainController;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel implements Observer {
	/**
	 * Main controller
	 */
	private MainController controller;

	/**
	 * Observed graph
	 */
	private GraphModel graph;

	/**
	 * @param model
	 */
	public GraphPanel(MainController controller, GraphModel graph) {
		this.controller = controller;
		this.graph = graph;
		controller.getModel().addObserver(this);
		graph.addObserver(this);
		addMouseListener(controller.getGraphController()
				.getGraphPanelMouseListener());
		addMouseMotionListener(controller.getGraphController()
				.getGraphPanelMotionListener());
		addComponentListener(controller.getProgramController()
				.getResizeComponentListener());
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		drawGraph(controller, graph, g, getSize());
	}

	/**
	 * Draws a graph
	 * 
	 * @param controller
	 * @param graph
	 * @param g
	 * @param d
	 */
	public static void drawGraph(MainController controller, GraphModel graph,
			Graphics g, Dimension d) {
		Model model = controller.getModel();
		int n = graph.getN();
		g.setColor(model.getBackgroundColor());
		g.fillRect(0, 0, (int) d.getWidth(), (int) d.getHeight());
		graph.drawGraph(g);
		g.setColor(model.getEdgesColor());
		MouseActionMode mouseActionMode = model.getMouseActionMode();
		int selectedVertex = model.getSelectedVertex();
		Point linkingPosition = model.getLinkingPosition();
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (graph.areNeighbors(i, j))
					graph.drawEdge(g, i, j);
		if (mouseActionMode == MouseActionMode.MOUSE_CHANGES_EDGES
				&& selectedVertex != -1 && linkingPosition != null) {
			Vertex v = graph.getVertex(selectedVertex);
			if (!v.isLabel()) {
				g.setColor(model.getSelectedColor());
				graph.drawEdge(g, v.getXPosition(), v.getYPosition(),
						(int) linkingPosition.getX(),
						(int) linkingPosition.getY(), false);
			}
		}
		if (selectedVertex != -1) {
			g.setColor(model.getSelectedColor());
			graph.getVertex(selectedVertex).draw(g);
		}
		Rectangle selectionRect = model.getSelectionRect();
		if (selectionRect != null) {
			if (mouseActionMode == MouseActionMode.MOUSE_MOVES_VERTICES)
				g.setColor(model.getFixedColor());
			else if (mouseActionMode == MouseActionMode.MOUSE_SELECTS_VERTICES)
				g.setColor(model.getSelectedColor());
			Graphics2D g2d = (Graphics2D) g;
			float dashArray[] = { 1, 2 };
			BasicStroke bs = new BasicStroke((float) (1),
					BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
					(float) (10), dashArray, (float) (0));
			g2d.setStroke(bs);
			g2d.drawRect((int) (selectionRect.getMinX()),
					(int) (selectionRect.getMinY()),
					(int) (selectionRect.getWidth()),
					(int) (selectionRect.getHeight()));
		}
	}
}
