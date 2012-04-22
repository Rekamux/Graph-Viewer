package view;

import java.util.Observable;
import java.util.Observer;

import utils.Constants;

import model.Model.Tool;

import controller.MainController;

/**
 * Contain a function init(StackPanel toolPanel, ProgramWindow w) that creates
 * all toolPanels and add them in toolPanel
 * 
 * @author Axel Schumacher
 */
@SuppressWarnings("serial")
public class ToolPanel extends StackPanel implements Observer
{

	/**
	 * Main controller
	 */
	private MainController controller;

	/**
	 * tool panels
	 */
	private GraphToolPanel graphToolPanel;
	private EdgeToolPanel edgeToolPanel;
	private VertexToolPanel vertexToolPanel;

	/**
	 * Constructor
	 * 
	 * @param toolPanel
	 * @param w
	 */
	public ToolPanel(MainController c)
	{
		controller = c;
		setPreferredSize(Constants.getToolPanelDimension());
		controller.getModel().addObserver(this);
		graphToolPanel = new GraphToolPanel(controller);
		edgeToolPanel = new EdgeToolPanel(controller);
		vertexToolPanel = new VertexToolPanel(controller);
		add(graphToolPanel);
		add(edgeToolPanel);
		add(vertexToolPanel);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		Tool currentTool = controller.getModel().getCurrentTool();
		switch (currentTool)
		{
		case GRAPH:
			setSelectedIndex(0);
			break;

		case EDGE:
			setSelectedIndex(1);
			break;

		case VERTEX:
			setSelectedIndex(2);
			break;
		}
		if (!isVisible() && controller.getModel().getCurrentGraph() != null)
			setVisible(true);

	}

	public String getNextVertexName()
	{
		return vertexToolPanel.getNameField().getText();
	}

	/**
	 * @return the graphToolPanel
	 */
	public GraphToolPanel getGraphToolPanel()
	{
		return graphToolPanel;
	}

	/**
	 * @return the edgeToolPanel
	 */
	public EdgeToolPanel getEdgeToolPanel()
	{
		return edgeToolPanel;
	}

	/**
	 * @return the selectVertexToolPanel
	 */
	public VertexToolPanel getVertexToolPanel()
	{
		return vertexToolPanel;
	}
}
