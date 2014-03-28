package buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import app.Command;
import app.Mediator;


public class RectangleButton extends JToggleButton implements Command {
	private static final long serialVersionUID = 8275443566456861598L;
	Mediator med;

	public RectangleButton(ActionListener act, Mediator md) {
		super(" ");
		setSize(new Dimension(35, 35));
		setBorderPainted(true);
		setMargin(new Insets(5, 12, 5, 12));
		setToolTipText("Draw rectangle");
		addActionListener(act);
		med = md;
		med.registerRectangleButton(this);
	}

	public Dimension getPreferredSize() {
		return new Dimension(35, 35);
	}

	public void paint(Graphics g) {
		super.paint(g);
		int h = getHeight();
		int w = getWidth();
		g.setColor(Color.black);
		g.drawRect(4, 4, w - 8, h - 8);
	}

	public void execute() {
		//TODO 1.3 redirect the execution of this command to the Mediator
		med.startRectangle();
	}

}
