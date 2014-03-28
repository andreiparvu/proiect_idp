package states;

import app.Mediator;
import drawings.Rectangle;


public class RectangleState extends State {

	//TODO 2.3 create a new Rectangle on mouseDown
	RectangleState(Mediator med) {
		super(med);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseDown(int x, int y) {
		// TODO Auto-generated method stub
		
		med.addDrawing(new Rectangle(x, y));
	} 
}