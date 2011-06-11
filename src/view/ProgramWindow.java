package view;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import controller.MainController;

/**
 * Main Program's Window
 * 
 * @author Axel Schumacher
 */
@SuppressWarnings("serial")
public class ProgramWindow extends JFrame implements Observer
{
	private MainController controller;

	/**
	 * Tabbed pane containing all open graphs
	 */
	private GraphTabbedPane tab;

	/**
	 * Panel containing current tool option
	 */
	private ToolPanel toolPanel;

	/**
	 * Opening graph dialog
	 */
	private OpenGraphDialog openGraphDialog;

	/**
	 * Menu Bar
	 */
	private ProgramWindowMenuBar menuBar;

	/**
	 * Tool Bar
	 */
	private ProgramWindowToolBar toolBar;

	/**
	 * Basic constructor
	 */
	public ProgramWindow(MainController c)
	{
		controller = c;
		controller.getModel().addObserver(this);
		tab = new GraphTabbedPane(controller);
		menuBar = new ProgramWindowMenuBar(controller);
		toolBar = new ProgramWindowToolBar(controller);
		openGraphDialog = new OpenGraphDialog(controller);
		toolPanel = new ToolPanel(controller);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setResizable(true);
		setTitle("Graph Viewer");
		setLayout(new BorderLayout());
		add(tab, BorderLayout.CENTER);
		add(toolPanel, BorderLayout.EAST);
		toolPanel.setVisible(false);
		setJMenuBar(menuBar);
		add(toolBar, BorderLayout.NORTH);
		addComponentListener(controller.getProgramController().getResizeComponentListener());
	}

	public OpenGraphDialog getOpenGraphDialog()
	{
		return openGraphDialog;
	}

	public ToolPanel getToolPanel()
	{
		return toolPanel;
	}

	public ProgramWindowMenuBar getWindowMenuBar()
	{
		return menuBar;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		repaint();
	}

	/**
	 * @return the tab
	 */
	public GraphTabbedPane getTab()
	{
		return tab;
	}

	/**
	 * @return the toolBar
	 */
	public ProgramWindowToolBar getToolBar()
	{
		return toolBar;
	}
}
