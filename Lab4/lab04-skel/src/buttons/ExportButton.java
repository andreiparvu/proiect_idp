package buttons;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import app.Command;
import app.Mediator;


public class ExportButton extends JButton implements Command {
	private static final long serialVersionUID = 1307159571616823541L;
	Mediator med;

	public ExportButton(ActionListener act, Mediator md) {
		super("Export image");

		setMargin(new Insets(5, 12, 5, 12));	
		setToolTipText("Export image");
		addActionListener(act);
		med = md;
		
	}

	public void execute() {
		med.export();
	}
}
