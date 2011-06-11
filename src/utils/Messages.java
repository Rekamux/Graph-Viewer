package utils;

import javax.swing.JOptionPane;

import controller.MainController;

/**
 * Provides default JOptionPane messages
 * @author Ax
 *
 */
public abstract class Messages
{
	public static final String GRAPH_DOESNT_EXIST_SAVE = "You don't have any graph to save !";
	
	public static final String CANNOT_WRITE_FILE = "Error while writing this file.";
	
	public static final String CANNOT_READ_FILE = "Error while reading this file";
	
	public static final String EMPTY_FILE = "Selected file is empty !";
	
	public static void showGraphDoesntExistSaveError(MainController controller)
	{
		showErrorMessage(controller, GRAPH_DOESNT_EXIST_SAVE);
	}
	
	public static void showCannotWriteFileError(MainController controller)
	{
		showErrorMessage(controller, CANNOT_WRITE_FILE);
	}
	
	public static void showCannotReadFileError(MainController controller)
	{
		showErrorMessage(controller, CANNOT_READ_FILE);
	}
	
	public static void showEmptyFileError(MainController controller)
	{
		showErrorMessage(controller, EMPTY_FILE);
	}
	
	public static void showErrorMessage(MainController controller, String message)
	{
		JOptionPane.showMessageDialog(controller.getMainWindow(), message,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
}
