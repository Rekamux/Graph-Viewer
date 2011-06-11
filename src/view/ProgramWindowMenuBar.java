package view;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import model.GraphModel;

import controller.MainController;

/**
 * Menu Bar
 * 
 * @author Axel Schumacher
 */
@SuppressWarnings("serial")
public class ProgramWindowMenuBar extends JMenuBar implements Observer
{
	/**
	 * Main controller
	 */
	private MainController controller;

	/**
	 * Graph menu
	 */
	private JMenu graphMenu = new JMenu("Graph");

	/**
	 * Open graph item in Graph menu
	 */
	private JMenu openGraphMenu = new JMenu("Open graph");

	/**
	 * Open serialized graph
	 */
	private JMenuItem openSerializedGraphMenuItem = new JMenuItem(
			"Open Graph Viewer file");

	/**
	 * Open adjacency matrix
	 */
	private JMenuItem openAdjacencyMatrixMenuItem = new JMenuItem(
			"Open adjacency matrix file");

	/**
	 * Open edges list
	 */
	private JMenuItem openEdgesListMenuItem = new JMenuItem(
			"Open edges list file");

	/**
	 * Create Graph item in Graph Menu
	 */
	private JMenu createGraphMenu = new JMenu("Create Graph");

	/**
	 * Create empty graph
	 */
	private JMenuItem createEmptyGraphMenuItem = new JMenuItem(
			"Create an empty graph");

	/**
	 * Create custom graph
	 */
	private JMenuItem createCustomGraphMenuItem = new JMenuItem(
			"Create a custom graph");

	/**
	 * Save graph menu
	 */
	private JMenu saveGraphMenu = new JMenu("Save Graph");

	/**
	 * Save graph
	 */
	private JMenuItem saveGraphMenuItem = new JMenuItem("Save Graph");

	/**
	 * Save graph's adjacency matrix
	 */
	private JMenuItem saveGraphsAdjacencyMatrixMenuItem = new JMenuItem(
			"Save Graph adjacency matrix");

	/**
	 * Save graph's edges list
	 */
	private JMenuItem saveGraphsEdgesListMenuItem = new JMenuItem(
			"Save Graph edge list");

	/**
	 * Save Graph's image
	 */
	private JMenuItem saveGraphJPGMenuItem = new JMenuItem(
			"Save Graph JPG image");

	/**
	 * Save Graph's image
	 */
	private JMenuItem saveGraphPNGMenuItem= new JMenuItem(
			"Save Graph PNG image");
	/**
	 * Save Graph's PS format
	 */
	private JMenuItem saveGraphsPSMenuItem = new JMenuItem("Save Graph PS");

	/**
	 * Tools menu
	 */
	private JMenu toolsMenu = new JMenu("Tools");

	/**
	 * Graph's properties tool
	 */
	private JMenuItem graphPropertiesToolMenuItem = new JMenuItem(
			"Graph's properties");

	/**
	 * Edge tool
	 */
	private JMenuItem edgeToolMenuItem = new JMenuItem("Edge Tool");

	/**
	 * Vertex tool
	 */
	private JMenuItem vertexToolMenuItem = new JMenuItem(
			"Vertex tool");

	/**
	 * History menu
	 */
	private JMenu historyMenu = new JMenu("History");

	/**
	 * Backward action
	 */
	private JMenuItem undoMenuItem = new JMenuItem("Undo");

	/**
	 * Foreward action
	 */
	private JMenuItem redoMenuItem = new JMenuItem("Redo");

	/**
	 * About menu
	 */
	private JMenu aboutMenu = new JMenu("?");

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            Program Window
	 */
	public ProgramWindowMenuBar(MainController c)
	{
		controller = c;
		controller.getModel().addObserver(this);

		add(graphMenu);

		graphMenu.add(openGraphMenu);
		openGraphMenu.add(openSerializedGraphMenuItem);
		openSerializedGraphMenuItem.addActionListener(controller.getProgramController().getOpenGraphFromSerializedFileActionListener());
		openGraphMenu.add(openAdjacencyMatrixMenuItem);
		openAdjacencyMatrixMenuItem.addActionListener(controller.getProgramController().getOpenGraphFromAdjacencyMatrixFileActionListener());
		openGraphMenu.add(openEdgesListMenuItem);
		openEdgesListMenuItem.addActionListener(controller.getProgramController().getOpenGraphFromEdgesListFileActionListener());

		graphMenu.add(createGraphMenu);
		createGraphMenu.add(createEmptyGraphMenuItem);
		createEmptyGraphMenuItem.addActionListener(controller.getProgramController().getCreateEmptyGraphActionListener());
		createGraphMenu.add(createCustomGraphMenuItem);
		createCustomGraphMenuItem.addActionListener(controller.getProgramController().getCreateCustomGraphActionListener());

		graphMenu.add(saveGraphMenu);
		saveGraphMenu.add("Openable ressources");
		saveGraphMenu.getItem(saveGraphMenu.getItemCount() - 1).setEnabled(
				false);
		saveGraphMenu.add(saveGraphMenuItem);
		saveGraphMenuItem.addActionListener(controller.getProgramController().getSaveGraphActionListener());
		saveGraphMenu.add(saveGraphsAdjacencyMatrixMenuItem);
		saveGraphsAdjacencyMatrixMenuItem.addActionListener(controller.getProgramController().getSaveAdjacencyMatrixActionListener());
		saveGraphMenu.add(saveGraphsEdgesListMenuItem);
		saveGraphsEdgesListMenuItem.addActionListener(controller.getProgramController().getSaveEdgesListActionListener());
		saveGraphMenu.addSeparator();
		saveGraphMenu.add("Non-openable ressources");
		saveGraphMenu.getItem(saveGraphMenu.getItemCount() - 1).setEnabled(
				false);
		saveGraphMenu.add(saveGraphJPGMenuItem);
		saveGraphJPGMenuItem.addActionListener(controller.getProgramController().getSaveGraphJPGImageActionListener());
		saveGraphMenu.add(saveGraphPNGMenuItem);
		saveGraphPNGMenuItem.addActionListener(controller.getProgramController().getSaveGraphPNGImageActionListener());
		saveGraphMenu.add(saveGraphsPSMenuItem);
		saveGraphsPSMenuItem.addActionListener(controller.getProgramController().getSavePSActionListener());

		add(toolsMenu);
		toolsMenu.add(graphPropertiesToolMenuItem);
		graphPropertiesToolMenuItem.addActionListener(controller.getProgramController().getSelectGraphToolActionListener());
		toolsMenu.add(edgeToolMenuItem);
		edgeToolMenuItem.addActionListener(controller.getProgramController().getSelectEdgesToolActionListener());
		toolsMenu.add(vertexToolMenuItem);
		vertexToolMenuItem.addActionListener(controller.getProgramController().getSelectVertexToolActionListener());

		add(historyMenu);
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK));
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				KeyEvent.CTRL_MASK));
		openGraphMenu.setMnemonic('o');
		graphPropertiesToolMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
		edgeToolMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
		vertexToolMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		createEmptyGraphMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		historyMenu.add(undoMenuItem);
		undoMenuItem.addActionListener(controller.getProgramController().getUndoActionListener());
		historyMenu.add(redoMenuItem);
		redoMenuItem.addActionListener(controller.getProgramController().getDoUndidActionListener());

		add(aboutMenu);
	}

	/**
	 * @return the graphMenu
	 */
	public JMenu getGraphMenu()
	{
		return graphMenu;
	}

	/**
	 * @return the openGraphMenu
	 */
	public JMenu getOpenGraphMenu()
	{
		return openGraphMenu;
	}

	/**
	 * @return the openSerializedGraphMenuItem
	 */
	public JMenuItem getOpenSerializedGraphMenuItem()
	{
		return openSerializedGraphMenuItem;
	}

	/**
	 * @return the openAdjacencyMatrixMenuItem
	 */
	public JMenuItem getOpenAdjacencyMatrixMenuItem()
	{
		return openAdjacencyMatrixMenuItem;
	}

	/**
	 * @return the openEdgesListMenItem
	 */
	public JMenuItem getOpenEdgesListMenItem()
	{
		return openEdgesListMenuItem;
	}

	/**
	 * @return the createGraphMenu
	 */
	public JMenu getCreateGraphMenu()
	{
		return createGraphMenu;
	}

	/**
	 * @return the createEmptyGraphMenuItem
	 */
	public JMenuItem getCreateEmptyGraphMenuItem()
	{
		return createEmptyGraphMenuItem;
	}

	/**
	 * @return the createCustomGraphMenuItem
	 */
	public JMenuItem getCreateCustomGraphMenuItem()
	{
		return createCustomGraphMenuItem;
	}

	/**
	 * @return the saveGraphMenu
	 */
	public JMenu getSaveGraphMenu()
	{
		return saveGraphMenu;
	}

	/**
	 * @return the saveGraphMenuItem
	 */
	public JMenuItem getSaveGraphMenuItem()
	{
		return saveGraphMenuItem;
	}

	/**
	 * @return the saveGraphsAdjacencyMatrixMenuItem
	 */
	public JMenuItem getSaveGraphsAdjacencyMatrixMenuItem()
	{
		return saveGraphsAdjacencyMatrixMenuItem;
	}

	/**
	 * @return the saveGraphsEdgesListMenuItem
	 */
	public JMenuItem getSaveGraphsEdgesListMenuItem()
	{
		return saveGraphsEdgesListMenuItem;
	}

	/**
	 * @return the saveGraphsImageMenuItem
	 */
	public JMenuItem getSaveGraphsImageMenuItem()
	{
		return saveGraphJPGMenuItem;
	}

	/**
	 * @return the saveGraphsPSMenuItem
	 */
	public JMenuItem getSaveGraphsPSMenuItem()
	{
		return saveGraphsPSMenuItem;
	}

	/**
	 * @return the toolsMenu
	 */
	public JMenu getToolsMenu()
	{
		return toolsMenu;
	}

	/**
	 * @return the graphPropertiesToolMenuItem
	 */
	public JMenuItem getGraphPropertiesToolMenuItem()
	{
		return graphPropertiesToolMenuItem;
	}

	/**
	 * @return the edgeToolMenuItem
	 */
	public JMenuItem getEdgeToolMenuItem()
	{
		return edgeToolMenuItem;
	}

	/**
	 * @return the createVertexToolMenuItem
	 */
	public JMenuItem getVertexToolMenuItem()
	{
		return vertexToolMenuItem;
	}

	/**
	 * @return the historyMenu
	 */
	public JMenu getHistoryMenu()
	{
		return historyMenu;
	}

	/**
	 * @return the aboutMenu
	 */
	public JMenu getAboutMenu()
	{
		return aboutMenu;
	}

	public JMenuItem getUndoMenuItem()
	{
		return undoMenuItem;
	}

	public JMenuItem getRedoMenuItem()
	{
		return redoMenuItem;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		GraphModel graph = controller.getModel().getCurrentGraph();
		boolean graphValid = graph != null;
		boolean undoNotEnable = !graphValid || !graph.isUndoPossible();
		boolean doNextNotEnable = !graphValid || !graph.isDoNextPossible();
		saveGraphMenu.setEnabled(graphValid);
		undoMenuItem.setEnabled(!undoNotEnable);
		redoMenuItem.setEnabled(!doNextNotEnable);
	}

}
