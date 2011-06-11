package view;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

/**
 * Listener for JTextField selecting text when gaining focus
 * @author Ax
 *
 */
public class SelectTextFocusListener implements FocusListener
{
	@Override
	public void focusGained(FocusEvent e)
	{
		JTextField src = (JTextField) e.getSource();
		src.setSelectionStart(0);
		src.setSelectionEnd(src.getText().length());
	}

	@Override
	public void focusLost(FocusEvent e)
	{
	}
}
