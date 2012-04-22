package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import model.GraphModel;
import model.Model.MouseActionMode;
import utils.Constants;
import controller.MainController;

@SuppressWarnings("serial")
public class VertexToolPanel extends JPanel implements Observer
{
	/**
	 * Controller
	 */
	private MainController controller;

	/**
	 * Delete selection mode
	 */
	private JRadioButton deleteRadioButton = new JRadioButton(
			"Delete selected vertex");

	/**
	 * Create vertex mode
	 */
	private JRadioButton createRadioButton = new JRadioButton(
			"Create new vertex");
	
	/**
	 * Select vertex mode
	 */
	private JRadioButton selectRadioButton = new JRadioButton("Select a vertex");
	
	/** Is label mode */
	private JCheckBox labelCheckBox = new JCheckBox("Is a label");

	/**
	 * Radio button group
	 */
	private ButtonGroup modeGroup = new ButtonGroup();

	/**
	 * Name position angle slide bar
	 */
	private JSlider nameAngleSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);

	/**
	 * Name position distance slider
	 */
	private JSlider nameDistanceSlider = new JSlider(JSlider.HORIZONTAL, 0, 100,
			5);

	/**
	 * Name Text Field
	 */
	private JTextField nameField = new JTextField();

	/**
	 * Name label
	 */
	private JLabel nameLabel = new JLabel("Vertex name: ");

	/**
	 * Title
	 */
	private JLabel presentationLabel = new JLabel("Vertex selection tool");

	/**
	 * Constructor
	 */
	public VertexToolPanel(MainController c)
	{
		controller = c;
		setPreferredSize(Constants.getToolPanelDimension());
		controller.getModel().addObserver(this);
		presentationLabel.setFont(new Font("Arial", Font.CENTER_BASELINE
				| Font.BOLD, 20));
		GridLayout l = new GridLayout();
		setLayout(l);
		int line = 0;
		add(presentationLabel, 0, line);
		line++;

		add(nameLabel, 0, line);
		line++;
		nameField.setPreferredSize(new Dimension(150, 30));
		nameField.addKeyListener(controller.getGraphController()
				.getTypeVertexNameKeyListener());
		add(nameField, 0, line);
		line++;

		add(new JLabel("Mouse action mode :"), 0, line);
		line++;

		createRadioButton.addActionListener(controller.getProgramController()
				.getSelectCreateVerticesActionListener());
		deleteRadioButton.addActionListener(controller.getProgramController()
				.getSelectDeleteVerticesActionListener());
		selectRadioButton.addActionListener(controller.getProgramController().getSelectSelectVerticesActionListener());
		modeGroup.add(createRadioButton);
		modeGroup.add(deleteRadioButton);
		modeGroup.add(selectRadioButton);
		
		add(selectRadioButton, 0, line);
		line++;

		add(createRadioButton, 0, line);
		line++;

		add(deleteRadioButton, 0, line);
		line++;
		
		labelCheckBox.addActionListener(controller.getGraphController().getVertexLabelActionListener());
		add(labelCheckBox, 0, line);
		line++;

		JLabel namePositionLabel = new JLabel("Name position:");
		add(namePositionLabel, 0, line);
		line++;
		nameAngleSlider.setMajorTickSpacing(180);
		nameAngleSlider.setMinorTickSpacing(60);
		nameAngleSlider.setPaintTicks(true);
		nameAngleSlider.setPaintLabels(true);
		nameAngleSlider.setToolTipText("Name angle");
		nameAngleSlider.addChangeListener(controller.getGraphController()
				.getVertexNameThetaPositionChangeListener());
		add(nameAngleSlider, 0, line);
		line++;
		nameDistanceSlider.setMajorTickSpacing(50);
		nameDistanceSlider.setMinorTickSpacing(10);
		nameDistanceSlider.setPaintTicks(true);
		nameDistanceSlider.setPaintLabels(true);
		nameDistanceSlider.setToolTipText("Name's distance");
		nameDistanceSlider.addChangeListener(controller.getGraphController()
				.getVertexNameRoPositionChangeListener());
		add(nameDistanceSlider, 0, line);
		line++;

		l.setRows(line);
	}

	/**
	 * @return the nameField
	 */
	public JTextField getNameField()
	{
		return nameField;
	}

	/**
	 * ToString
	 */
	public String toString()
	{
		return "Select Vertex";
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		GraphModel graph = controller.getModel().getCurrentGraph();
		if (graph == null)
			return;
		int vertexIndex = controller.getModel().getCurrentVertexIndex();
		switch (controller.getModel().getMouseActionMode())
		{
		case MOUSE_ADDS_VERTICES:
			nameField.setText(controller.getModel().getNextVertexName());
			break;

		case MOUSE_SELECTS_VERTICES:
			nameField.setText(vertexIndex != -1 ? graph.getVertex(vertexIndex)
					.getName() : "Select a vertex");
			break;
			
		case MOUSE_DELETES_VERTICES:
			nameField.setText("Delete vertex");
			break;
		}
		MouseActionMode m = controller.getModel().getMouseActionMode();
		deleteRadioButton
				.setSelected(m == MouseActionMode.MOUSE_DELETES_VERTICES);
		createRadioButton.setSelected(m == MouseActionMode.MOUSE_ADDS_VERTICES);
		selectRadioButton.setSelected(m == MouseActionMode.MOUSE_SELECTS_VERTICES);
		nameAngleSlider.setEnabled(m == MouseActionMode.MOUSE_SELECTS_VERTICES);
		nameDistanceSlider.setEnabled(m == MouseActionMode.MOUSE_SELECTS_VERTICES);
		nameAngleSlider.setValue(vertexIndex != -1 ? graph.getVertex(
				vertexIndex).getNameAngle() : 0);
		nameDistanceSlider.setValue(vertexIndex != -1 ? graph.getVertex(
				vertexIndex).getNameDistance() : 0);
		labelCheckBox.setSelected(vertexIndex != -1 ? graph.getVertex(vertexIndex).isLabel():controller.getModel().isNextVertexLabel());
	}
}
