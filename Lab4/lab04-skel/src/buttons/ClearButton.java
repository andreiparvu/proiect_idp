package buttons;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import app.Command;
import app.Mediator;

public class ClearButton extends JButton implements Command {
	private static final long serialVersionUID = -4613211576146845021L;
	Mediator med;

	public ClearButton(ActionListener act, Mediator md) {
		super("Clear");

		setMargin(new Insets(5, 12, 5, 12));	
		setToolTipText("Clear");
		addActionListener(act);
		med = md;
		
	}

	public void execute() {
		//TODO 1.3 redirect the execution of this command to the Mediator
		med.clear();
	}
}