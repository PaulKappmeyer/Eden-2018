package game;

import java.awt.Color;
import java.awt.Graphics;

public class Stone {

	public int x;
	public int y;
	public int width;
	public int height;

	public Stone(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x + Globals.insetX, y + Globals.insetY, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x + Globals.insetX, y + Globals.insetY, width, height);
	}

	public void update(float tslf) {
		//TODO: new Collision system
	}
}