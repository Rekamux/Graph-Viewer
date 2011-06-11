package controller;

/**
 * Abstract controller
 * 
 * @author Ax
 */
public abstract class AbstractController
{
	/**
	 * Main controller
	 */
	protected MainController controller;

	/**
	 * @param controller
	 */
	public AbstractController(MainController controller)
	{
		this.controller = controller;
	}
}
