package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.Computing;

import controller.MainController;

/**
 * Dialog shown when new graph option activated
 * 
 * @author Axel Schumacher
 */
@SuppressWarnings("serial")
public class OpenGraphDialog extends JDialog implements Observer
{
	/**
	 * Open Random Graph options panel
	 */
	class OpenRandomGraphOptionsPanel extends JPanel
	{
		/**
		 * M choose text field
		 */
		private JTextField mChooseField = new JTextField("0");

		/**
		 * M choose label
		 */
		private JLabel mChooseLabel = new JLabel("Edges' number:");

		/**
		 * N choose text field
		 */
		private JTextField nChooseField = new JTextField("0");

		/**
		 * N choose label
		 */
		private JLabel nChooseLabel = new JLabel("Vertices' number:");

		/**
		 * Constructor
		 */
		public OpenRandomGraphOptionsPanel(MainController c)
		{
			controller = c;
			SelectTextFocusListener selectTextFocusListener = new SelectTextFocusListener();
			nChooseField.setPreferredSize(new Dimension(50, 30));
			nChooseField.addKeyListener(controller.getOpenGraphController().getIntegerTextFieldKeyListener());
			nChooseField.addFocusListener(selectTextFocusListener);
			mChooseField.setPreferredSize(new Dimension(50, 30));
			mChooseField.addKeyListener(controller.getOpenGraphController().getIntegerTextFieldKeyListener());
			mChooseField.addFocusListener(selectTextFocusListener);
			setLayout(new GridLayout(2, 2, 5, 5));
			add(nChooseLabel);
			add(nChooseField);
			add(mChooseLabel);
			add(mChooseField);
		}
	}

	/**
	 * Cancel Button
	 */
	private JButton cancelButton = new JButton("Cancel");

	/**
	 * Controller
	 */
	private MainController controller;

	/**
	 * Name text field
	 */
	private JTextField nameField = new JTextField();

	/**
	 * Label
	 */
	private JLabel nameLabel = new JLabel("Graph's name: ");

	/**
	 * OK Button
	 */
	private JButton okButton = new JButton("Create");

	/**
	 * Panel containing random graph options
	 */
	private OpenRandomGraphOptionsPanel openRandomGraphOptionsPanel;

	/**
	 * Basic constructor
	 */
	public OpenGraphDialog(MainController c)
	{
		controller = c;
		controller.getModel().addObserver(this);
		setTitle("Open a Graph");
		setLocationRelativeTo(controller.getMainWindow());
		setLayout(new FlowLayout());
		setSize(new Dimension(200, 200));
		add(nameLabel);
		nameField.setPreferredSize(new Dimension(150, 30));
		openRandomGraphOptionsPanel = new OpenRandomGraphOptionsPanel(controller);
		add(nameField);
		add(openRandomGraphOptionsPanel);
		add(okButton);
		add(cancelButton);
		okButton.addActionListener(controller.getOpenGraphController().getOpenGraphDialogOkButtonActionListener());
		cancelButton.addActionListener(controller.getOpenGraphController().getOpenGraphDialogCancelButtonActionListener());
	}

	/**
	 * @return the cancelButton
	 */
	public JButton getCancelButton()
	{
		return cancelButton;
	}

	/**
	 * @return entered m for next graph
	 */
	public int getEnteredM()
	{
		Integer m = Computing
				.toInteger(openRandomGraphOptionsPanel.mChooseField.getText());
		return m;
	}

	/**
	 * @return entered n for next graph
	 */
	public int getEnteredN()
	{
		Integer n = Computing
				.toInteger(openRandomGraphOptionsPanel.nChooseField.getText());
		return n;
	}

	/**
	 * @return entered name for next graph
	 */
	public String getEnteredName()
	{
		return nameField.getText();
	}

	public JTextField getMChooseField()
	{
		return openRandomGraphOptionsPanel.mChooseField;
	}

	/**
	 * @return the nameField
	 */
	public JTextField getNameField()
	{
		return nameField;
	}

	public JTextField getNChooseField()
	{
		return openRandomGraphOptionsPanel.nChooseField;
	}

	/**
	 * @return the okButton
	 */
	public JButton getOKButton()
	{
		return okButton;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		setVisible(controller.getModel().isOpenGraphDialogShown());
	}
}
