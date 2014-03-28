package states;

import app.Mediator;
import drawings.*;

public class CircleState extends State {
	//TODO 2.2 create a new circle on mouseDown

	CircleState(Mediator med) {
		super(med);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void mouseDown(int x, int y) {
		// TODO Auto-generated method stub
		
		med.addDrawing(new Circle(x, y));
	}
}