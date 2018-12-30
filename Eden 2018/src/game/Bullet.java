package game;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet {

	float x,y;
	int size = 4;
	float velX,velY;
	float speed = 350;
	
	public Bullet(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.velX = (float) Math.sin(Math.toRadians(angle));
		this.velY = (float) Math.cos(Math.toRadians(angle));
	}
	
	public void show(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
	}
	
	public void update(float tslf) {
		this.x += velX * speed * tslf;
		this.y += velY * speed * tslf;
	}
}
