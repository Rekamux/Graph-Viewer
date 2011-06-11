package controller;

import model.Model;
import view.ProgramWindow;

/**
 * Controller watching out a model
 * @author Ax
 */
public class MainController
{
	/**
	 * Model to watch out
	 */
	private Model model;

	/**
	 * Program main window
	 */
	private ProgramWindow mainWindow;

	/**
	 * Graph controller
	 */
	private GraphController graphController; 
	
	/**
	 * Program controller
	 */
	private ProgramController programController;
	
	/**
	 * Open graph dialog controller
	 */
	private ProgramController openGraphController;
	
	/**
	 * Default constructor
	 */
	public MainController()
	{
		graphController = new GraphController(this); 
		programController = new ProgramController(this);
		openGraphController = new ProgramController(this);
		model = new Model();
		mainWindow = new ProgramWindow(this);
		model.notifyObservers();
	}

	/**
	 * @return the graphController
	 */
	public GraphController getGraphController()
	{
		return graphController;
	}

	/**
	 * @return the openGraphController
	 */
	public ProgramController getOpenGraphController()
	{
		return openGraphController;
	}

	/**
	 * @return the model
	 */
	public Model getModel()
	{
		return model;
	}

	/**
	 * @return the mainWindow
	 */
	public ProgramWindow getMainWindow()
	{
		return mainWindow;
	}

	/**
	 * @return the programController
	 */
	public ProgramController getProgramController()
	{
		return programController;
	}
	
	/**
	 * Shows the program
	 */
	public void showProgram()
	{
		mainWindow.setVisible(true);
	}
}
