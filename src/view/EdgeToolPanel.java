package view;

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Constants;

import controller.MainController;

@SuppressWarnings("serial")
public class EdgeToolPanel extends JPanel implements Observer
{
	/**
	 * Controller
	 */
	private MainController controller;
	
	/**
	 * Title
	 */
	private JLabel presentationLabel = new JLabel("Create/Delete edge tool");

	/**
	 * Description
	 */
	private JLabel description = new JLabel("<html>Link two vertices:" + "<ul>"
			+ "<li>" + "if they are neighbors,<br />"
			+ "the edge will be deleted" + "</li>" + "<li>" + "if not,<br />"
			+ "they will be linked" + "</li>" + "</ul>" + "</html>");

	/**
	 * Constructor
	 */
	public EdgeToolPanel(MainController c)
	{
		controller = c;
		controller.getModel().addObserver(this);
		setPreferredSize(Constants.getToolPanelDimension());
		presentationLabel.setFont(new Font("Arial", Font.CENTER_BASELINE
				| Font.BOLD, 20));
		add(presentationLabel);
		add(description);
	}

	/**
	 * ToString
	 */
	public String toString()
	{
		return "Modify Edge";
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{}
}
