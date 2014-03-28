package states;

import app.Mediator;

public abstract class State {
	Mediator med;
	State(Mediator med){
		this.med = med;
	}
	public abstract void mouseDown(int x, int y);

	public void mouseUp(int x, int y){
		
	}

}