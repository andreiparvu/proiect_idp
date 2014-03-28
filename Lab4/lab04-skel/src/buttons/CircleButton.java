package buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import app.Command;
import app.Mediator;

public class CircleButton extends JToggleButton implements Command {
	private static final long serialVersionUID = -1934805925856773261L;
	Mediator med;

	public CircleButton(ActionListener act, Mediator md) {
		super(" ");
		setSize(new Dimension(35, 35));
		setBorderPainted(true);
		setMargin(new Insets(5, 12, 5, 12));
		setToolTipText("Draw circle");
		addActionListener(act);
		med = md;
		med.registerCircleButton(this);
	}

	public Dimension getPreferredSize() {
		return new Dimension(35, 35);
	}

	public void paint(Graphics g) {
		super.paint(g);
		int h = getHeight();
		g.setColor(Color.black);
		g.drawArc(2, 2, h - 4, h - 4, 0, 360);
	}

	public void execute() {
		//TODO 1.3 redirect the execution of this command to the Mediator
		med.startCircle();
	}

}