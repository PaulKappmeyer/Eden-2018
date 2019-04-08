package game;

import java.awt.Color;
import java.awt.Graphics;

public class BulletBouncer extends Obstacle{
	
	float power = 400;
	float time = 0.2f;
	
	public BulletBouncer(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, width, height);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, width, height);
	}
	
	public void update(float tslf) {
	}
}
