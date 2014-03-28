package buttons;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import app.Command;
import app.Mediator;

public class UndoButton extends JButton implements Command {
	private static final long serialVersionUID = 1965872208694244126L;
	Mediator med;

	public UndoButton(ActionListener act, Mediator md) {
		super("Undo");
		setMargin(new Insets(5, 12, 5, 12));
		setToolTipText("Undo");
		addActionListener(act);
		med = md;
	}

	public void execute() {
		//TODO 1.3 redirect the execution of this command to the Mediator
		med.undo();
	}
}