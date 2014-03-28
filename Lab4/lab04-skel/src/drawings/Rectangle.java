package drawings;

import java.awt.Graphics;


public class Rectangle extends Drawing {

	public Rectangle(int xpt, int ypt) {
		x = xpt;
		y = ypt;
		w = 40;
		h = 30;
		saveAsRect();
	}

	public void draw(Graphics g) {
		g.drawRect(x, y, w, h);
	}
}