package states;
import app.Mediator;


public class StateManager {
	private State currentState;
	private Mediator med;
	
	public StateManager(Mediator med) {
		this.med = med;
	}

	public void setRectangleState() {
		//TODO 2.4
		//set current state as rectangle state
		
		currentState = new RectangleState(med);
	}

	public void setCircleState() {
		//TODO 2.4
		//set current state as circle state
		
		currentState = new CircleState(med);
	}

	public void mouseDown(int x, int y) {
		//TODO 2.4
		//perform mouseDown on the currentState of the manager
		
		currentState.mouseDown(x, y);
	}

	public void mouseUp(int x, int y) {
		//TODO 2.4
		//perform mouseUp on the currentState of the manager
		
		currentState.mouseDown(x, y);
	}

}