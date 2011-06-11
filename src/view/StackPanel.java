package view;

import java.awt.CardLayout;

import javax.swing.JPanel;

/**
 * Provides a stack of panels where only one panel is visible at a time
 * 
 * @author Axel Schumacher
 */
public class StackPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Panel shown index
	 */
	private int index = -1;
	
	/**
	 * Card Layout
	 */
	private CardLayout layout = new CardLayout();
	
	/**
	 * Basic constructor
	 */
	public StackPanel()
	{
		super();
		setLayout(layout);
	}
	
	/**
	 * Constructor from a JPanel tab
	 */
	public StackPanel(JPanel tabs[])
	{
		super();
		setLayout(layout);
		for (int i=0; i<tabs.length; i++)
			add(tabs[i], Integer.toString(i));
	}
	
	/**
	 * Adds a panel
	 */
	public void add(JPanel pan)
	{
		add(pan, Integer.toString(getComponentCount()));
	}
	
	/**
	 * Removes a panel
	 */
	public void remove(int i)
	{
		super.remove(i);
		if (i == index)
		{
			index--;
			changePanel();
		}
	}
	
	/**
	 * Changes panel shown
	 */
	public void setSelectedIndex(int i)
	{
		if (i>=getComponentCount() || i<0)
		{
			System.err.println("In StackPanel.setSelectedIndex: index out of bounds!");
			return;
		}
		index = i;
		changePanel();
	}
	
	/**
	 * Changes panel shown
	 */
	private void changePanel()
	{
		if (index != -1)
			layout.show(this, Integer.toString(index));
	}
}
