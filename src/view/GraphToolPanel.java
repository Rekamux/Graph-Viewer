package view;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Constants;

import model.GraphModel;
import model.Model;
import model.Model.ColorOption;
import controller.MainController;

/**
 * Panel of the graph tool
 * 
 * @author Ax
 * 
 */
@SuppressWarnings("serial")
public class GraphToolPanel extends JPanel implements Observer
{
	/**
	 * Controller
	 */
	private MainController controller;

	/**
	 * Tool Presentation label
	 */
	private JLabel presentationLabel = new JLabel("Graph properties");

	/**
	 * Name label
	 */
	private JLabel nameLabel = new JLabel("Graph name: ");

	/**
	 * Name field
	 */
	private JTextField nameField = new JTextField();

	/**
	 * Buttons dimension
	 */
	private Dimension buttonsDimension = new Dimension(30, 30);

	/**
	 * Background color choose label
	 */
	private JLabel backgroundColorLabel = new JLabel("Background color: ");

	/**
	 * Background color choose button
	 */
	private JButton backgroundColorButton = new JButton();

	/**
	 * Vertices color choose label
	 */
	private JLabel verticesColorLabel = new JLabel("Vertex color: ");

	/**
	 * Vertices color choose button
	 */
	private JButton verticesColorButton = new JButton();

	/**
	 * Edges color choose label
	 */
	private JLabel edgesColorLabel = new JLabel("Edge color: ");

	/**
	 * Edges color choose button
	 */
	private JButton edgesColorButton = new JButton();

	/**
	 * Fixed color choose label
	 */
	private JLabel fixedColorLabel = new JLabel("Fixed vertex color: ");

	/**
	 * Fixed color choose button
	 */
	private JButton fixedColorButton = new JButton();

	/**
	 * Selected color choose label
	 */
	private JLabel selectedColorLabel = new JLabel("Selected vertex color: ");

	/**
	 * Selected color choose button
	 */
	private JButton selectedColorButton = new JButton();

	/**
	 * Stamp color choose label
	 */
	private JLabel stampColorLabel = new JLabel("Label color: ");

	/**
	 * Stamp color choose button
	 */
	private JButton stampColorButton = new JButton();

	/**
	 * Resize button
	 */
	private JButton shakeButton = new JButton(
			"Shake graph (moves all vertices)");

	/**
	 * Component resize button
	 */
	private JButton componentShakeButton = new JButton(
			"Group vertices by components");

	/**
	 * Component resize button
	 */
	private JButton complementButton = new JButton("Graph complement");

	/**
	 * Move check box
	 */
	private JCheckBox moveCheckBox = new JCheckBox("Search for a better position");

	/**
	 * Oriented check box
	 */
	private JCheckBox orientedCheckBox = new JCheckBox("This Graph is directed");

	/**
	 * Constructor
	 */
	public GraphToolPanel(MainController c)
	{
		setPreferredSize(Constants.getToolPanelDimension());
		controller = c;
		controller.getModel().addObserver(this);
		presentationLabel.setFont(new Font("Arial", Font.CENTER_BASELINE
				| Font.BOLD, 20));
		add(presentationLabel);

		JPanel n = new JPanel();
		n.add(nameLabel);
		nameField.setPreferredSize(new Dimension(100, 30));
		nameField.addKeyListener(controller.getGraphController().getTypeGraphNameKeyListener());
		n.add(nameField);
		add(n);

		for (ColorOption option : ColorOption.values())
		{
			getColorButton(option).addActionListener(
					controller.getProgramController()
							.getChangeColorActionListener());
			getColorButton(option).setPreferredSize(buttonsDimension);
		}

		JPanel bg = new JPanel();
		bg.add(backgroundColorLabel);
		bg.add(backgroundColorButton);
		add(bg);

		JPanel v = new JPanel();
		v.add(verticesColorLabel);
		v.add(verticesColorButton);
		add(v);

		JPanel e = new JPanel();
		e.add(edgesColorLabel);
		e.add(edgesColorButton);
		add(e);

		JPanel f = new JPanel();
		f.add(fixedColorLabel);
		f.add(fixedColorButton);
		add(f);

		JPanel s = new JPanel();
		s.add(selectedColorLabel);
		s.add(selectedColorButton);
		add(s);

		JPanel st = new JPanel();
		st.add(stampColorLabel);
		st.add(stampColorButton);
		add(st);

		shakeButton.addActionListener(controller.getGraphController()
				.getShakeActionListener());
		add(shakeButton);
		componentShakeButton.addActionListener(controller.getGraphController()
				.getComponentShakeActionListener());
		add(componentShakeButton);
		complementButton.addActionListener(controller.getGraphController()
				.getComplementActionListener());
		add(complementButton);
		moveCheckBox.addActionListener(controller.getGraphController()
				.getAllowMoveActionListener());
		add(moveCheckBox);
		orientedCheckBox.addActionListener(controller.getGraphController()
				.getOrientedActionListener());
		add(orientedCheckBox);
	}

	/**
	 * ToString
	 */
	public String toString()
	{
		return "Graph";
	}

	/**
	 * @param option
	 * @return concerned button
	 */
	public JButton getColorButton(ColorOption option)
	{
		switch (option)
		{
		case BACKGROUND:
			return backgroundColorButton;

		case EDGES:
			return edgesColorButton;

		case FIXED:
			return fixedColorButton;

		case SELECTED:
			return selectedColorButton;

		case VERTICES:
			return verticesColorButton;
			
		case LABEL:
			return stampColorButton;
		}
		return null;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		Model model = controller.getModel();
		GraphModel graph = model.getCurrentGraph();
		if (graph == null)
			return;
		nameField.setText(model.getCurrentGraph().getGraphsName());
		backgroundColorButton.setBackground(model.getBackgroundColor());
		backgroundColorButton.setBackground(model.getBackgroundColor());
		verticesColorButton.setBackground(model.getVerticesColor());
		edgesColorButton.setBackground(model.getEdgesColor());
		fixedColorButton.setBackground(model.getFixedColor());
		selectedColorButton.setBackground(model.getSelectedColor());
		stampColorButton.setBackground(model.getLabelColor());
		moveCheckBox.setSelected(graph.isAllowedToMove());
		orientedCheckBox.setSelected(graph.isOriented());
		nameField.setText(graph.getGraphsName());
	}
}
