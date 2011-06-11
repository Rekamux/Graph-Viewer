package model.history;

/**
 * Provides an reversible action interface
 * 
 * @author Axel Schumacher
 */
public abstract interface Action
{
	public void doAction();

	public void undoAction();
}
