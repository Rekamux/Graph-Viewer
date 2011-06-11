package model.history;

import utils.Stack;

/**
 * History manager
 * 
 * @author Axel Schumacher
 * 
 */
public class HistoryModel
{
	/**
	 * Past actions stack
	 */
	private Stack<Action> pastActions = new Stack<Action>();;

	/**
	 * Fore Actions Stack
	 */
	private Stack<Action> foreActions = new Stack<Action>();

	/**
	 * Adds an action
	 */
	public void addAction(Action a)
	{
		pastActions.push_front(a);
		foreActions = new Stack<Action>();
	}

	/**
	 * Undoes last action (moves it in foreaction stack)
	 */
	public void undoLastAction()
	{
		Action lastAction = pastActions.pop_front();
		lastAction.undoAction();
		foreActions.push_front(lastAction);
	}

	/**
	 * Does again last action cancelled
	 */
	public void doNextAction()
	{
		Action nextAction = foreActions.pop_front();
		nextAction.doAction();
		pastActions.push_front(nextAction);
	}

	/**
	 * @return true if undo is possible
	 */
	public boolean isUndoPossible()
	{
		return !pastActions.isEmpty();
	}

	/**
	 * @return true if do next is possible
	 */
	public boolean isDoNextPossible()
	{
		return !foreActions.isEmpty();
	}
}
