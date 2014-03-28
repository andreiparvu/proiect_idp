package app;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import states.StateManager;
import worker.ExportTask;
import buttons.CircleButton;
import buttons.RectangleButton;
import drawings.Drawing;

public class Mediator {
	Vector<Drawing> drawings;

	RectangleButton rectangleButton;
	CircleButton circleButton;
	JPanel canvas;

	StateManager stateMgr;

	public Mediator() {
		drawings = new Vector<Drawing>();
		stateMgr = new StateManager(this);
	}

	public void startRectangle() {
		// TODO 2.6
		circleButton.setSelected(false);
		stateMgr.setRectangleState();
	}

	public void startCircle() {
		// TODO 2.6
		rectangleButton.setSelected(false);
		stateMgr.setCircleState();
	}

	public Vector<Drawing> getDrawings() {
		return drawings;
	}

	public void addDrawing(Drawing d) {
		drawings.addElement(d);
//		System.out.println("da");
	}

	public void registerRectangleButton(RectangleButton rb) {
		rectangleButton = rb;
	}

	public void registerCircleButton(CircleButton cb) {
		circleButton = cb;
	}

	public void registerCanvas(JPanel p) {
		canvas = p;
	}

	public void mouseDown(int x, int y) {
		//TODO 2.5
		stateMgr.mouseDown(x, y);
		repaint();
	}

	public void mouseUp(int x, int y) {
		//TODO 2.5
		stateMgr.mouseUp(x, y);
	}

	public void clear() {
		drawings = new Vector<Drawing>();
		repaint();
	}

	private void repaint() {
		canvas.repaint();
	}

	public void reDraw(Graphics g) {
		g.setColor(Color.black);
		for (int i = 0; i < drawings.size(); i++) {
			Drawing v = (Drawing) drawings.elementAt(i);
			v.draw(g);
		}
	}

	public void undo() {
		//TODO 1.4
		
		drawings.remove(drawings.size() - 1);
		drawings.remove(drawings.size() - 1);
		repaint();
	}

	public void export() {
		//Just simulating file export - nothing is actually exported
		//TODO 3.1
	}
	
}
