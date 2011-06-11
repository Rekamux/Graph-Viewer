package view;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import model.GraphModel;
import model.Model.Tool;
import controller.MainController;

/**
 * Provides a tool bar
 * 
 * @author Ax
 * 
 */
@SuppressWarnings("serial")
public class ProgramWindowToolBar extends JToolBar implements Observer
{
	/**
	 * Controller
	 */
	private MainController controller;

	/**
	 * Graph's properties tool
	 */
	private JButton graphButton = new JButton(new ImageIcon("images/graph.png"));

	/**
	 * Create an edge tool
	 */
	private JButton createEdgeButton = new JButton(new ImageIcon(
			"images/edge.png"));

	/**
	 * Create a vertex tool
	 */
	private JButton vertexButton = new JButton(new ImageIcon(
			"images/vertex.png"));

	/**
	 * All buttons tab
	 */
	private JButton buttonsTab[] =
	{ graphButton, createEdgeButton, vertexButton};

	/**
	 * Undo button
	 */
	private JButton undoButton = new JButton(new ImageIcon("images/undo.png"));

	/**
	 * Redo button
	 */
	private JButton doUndidButton = new JButton(
			new ImageIcon("images/redo.png"));

	/**
	 * Constructor
	 */
	public ProgramWindowToolBar(MainController c)
	{
		controller = c;
		controller.getModel().addObserver(this);
		graphButton.setToolTipText("Graph's properties");
		graphButton.addActionListener(controller.getProgramController().getSelectGraphToolActionListener());
		createEdgeButton.setToolTipText("Edge tools");
		createEdgeButton.addActionListener(controller.getProgramController().getSelectEdgesToolActionListener());
		vertexButton.setToolTipText("Vertex tools");
		vertexButton.addActionListener(controller.getProgramController().getSelectVertexToolActionListener());
		for (int i = 0; i < buttonsTab.length; i++)
		{
			buttonsTab[i].setBackground(new Color(220, 220, 220));
			buttonsTab[i].setFocusable(false);
			add(buttonsTab[i]);
		}
		addSeparator();
		undoButton.setFocusable(false);
		undoButton.addActionListener(controller.getProgramController().getUndoActionListener());
		undoButton.setToolTipText("Undo last action");
		doUndidButton.setFocusable(false);
		doUndidButton.addActionListener(controller.getProgramController().getDoUndidActionListener());
		doUndidButton.setToolTipText("Do again undid action");
		add(undoButton);
		add(doUndidButton);
	}

	/**
	 * Changes selected button
	 */
	private void selectTool(Tool tool)
	{
		for (int i = 0; i < buttonsTab.length; i++)
			buttonsTab[i].setBackground(new Color(220, 220, 220));
		buttonsTab[getToolIndex(tool)].setBackground(Color.red);
	}

	private int getToolIndex(Tool tool)
	{
		switch (tool)
		{
		case GRAPH:
			return 0;
		case EDGE:
			return 1;
		case VERTEX:
			return 2;
		}
		return 0;
	}

	public JButton getUndoButton()
	{
		return undoButton;
	}

	public JButton getRedoButton()
	{
		return doUndidButton;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		Tool tool = controller.getModel().getCurrentTool();
		selectTool(tool);
		GraphModel graph = controller.getModel().getCurrentGraph();
		boolean graphNotValid = graph == null;
		boolean undoNotEnable = graphNotValid || !graph.isUndoPossible();
		boolean doUndidNotEnable = graphNotValid || !graph.isDoNextPossible();
		undoButton.setEnabled(!undoNotEnable);
		doUndidButton.setEnabled(!doUndidNotEnable);
	}

}
