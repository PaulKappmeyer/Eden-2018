/*
 * 
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;

import game.Globals;

/**
 * 
 * @author Paul
 *
 */
public class LaserBarrier {

	float x;
	float y;
	int width;
	int height;
	
	boolean isActive;
	
	boolean isClosed;
	
	public LaserBarrier(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, width, height);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, width, height);
		
		g.fillRect((int)(x - width/2 + Globals.insetX), (int)(y - width/2 + Globals.insetY), width*2, width*2);
	}
	
}
