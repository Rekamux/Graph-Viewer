package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.GraphModel;
import controller.MainController;

/**
 * Provides a Tabbed Pane class with a distinction between set selected index
 * from actions and from model
 * 
 * @author Ax
 * 
 */
@SuppressWarnings("serial")
public class GraphTabbedPane extends JTabbedPane implements Observer
{
	/**
	 * Controller
	 */
	private MainController controller;

	/**
	 * lock against infinite notifications true if allowing them
	 */
	private boolean notificationLock = true;

	/**
	 * Default constructor
	 * 
	 * @param c
	 */
	public GraphTabbedPane(MainController c)
	{
		controller = c;
		controller.getModel().addObserver(this);
	}

	@Override
	public Component add(String title, Component component)
	{
		Component component2 = super.add(component);
		final JButton closeButton = new JButton(new ImageIcon(
				"images/closeNotActivated.png"));
		closeButton.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
			}

			@Override
			public void mousePressed(MouseEvent arg0)
			{
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
				closeButton.setIcon(new ImageIcon(
						"images/closeNotActivated.png"));
			}

			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				closeButton.setIcon(new ImageIcon("images/close.png"));
			}

			@Override
			public void mouseClicked(MouseEvent arg0)
			{
			}
		});
		closeButton.setBackground(new Color(220, 220, 220));
		closeButton.setFocusable(false);
		closeButton.setPreferredSize(new Dimension(25, 25));
		closeButton.setContentAreaFilled(false);
		closeButton.setBorderPainted(false);
		closeButton.setActionCommand("" + getTabCount());
		closeButton.addActionListener(controller.getProgramController()
				.getCloseGraphActionListener());
		closeButton.setToolTipText("Close this graph");
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		setTabComponentAt(getTabCount() - 1, panel);
		JLabel label = new JLabel(title);
		panel.add(label);
		panel.add(closeButton);
		return component2;
	}
	
	@Override
	public void setTitleAt(int index, String title)
	{
		JPanel panel = (JPanel) getTabComponentAt(index);
		JLabel label = (JLabel) panel.getComponent(0);
		label.setText(title);
	}

	@Override
	public void setSelectedIndex(int index)
	{
		super.setSelectedIndex(index);
		if (notificationLock)
			controller.getProgramController().changeCurrentGraphIndex(index);
	};

	/**
	 * Changes selected index without notify observers
	 * 
	 * @param index
	 */
	public void setSelectedIndexWithoutNotify(int index)
	{
		super.setSelectedIndex(index);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		int modelGraphsCount = controller.getModel().getGraphsCount();
		int viewGraphsCount = getTabCount();
		if (modelGraphsCount != viewGraphsCount)
		{
			removeAll();
			for (int i = 0; i < modelGraphsCount; i++)
			{
				GraphModel graph = controller.getModel().getGraph(i);
				notificationLock = false;
				add(graph.getGraphsName(), new GraphPanel(controller, graph));
				notificationLock = true;
			}
		}
		for (int i = 0; i < modelGraphsCount; i++)
			setTitleAt(i, controller.getModel().getGraph(i).getGraphsName());
		int graphIndex = controller.getModel().getCurrentGraphIndex();
		if (graphIndex != -1)
			setSelectedIndexWithoutNotify(graphIndex);
	}
}
