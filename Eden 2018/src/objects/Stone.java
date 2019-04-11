package objects;

import java.awt.Color;
import java.awt.Graphics;

import game.Obstacle;

public class Stone extends Obstacle{

	public Color c;
	
	public Stone(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.c = Color.LIGHT_GRAY;
	}

	@Override
	public void draw(Graphics g) {
//		g.setColor(c);
//		g.fillRect(x + Globals.insetX, y + Globals.insetY, width, height);
//		g.setColor(Color.BLACK);
//		g.drawRect(x + Globals.insetX, y + Globals.insetY, width, height);
	}

	@Override
	public void update(float tslf) {
		c = Color.LIGHT_GRAY;
	}
}