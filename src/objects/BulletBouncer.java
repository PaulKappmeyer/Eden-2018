/*
 * 
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;

import game.Obstacle;

/**
 * 
 * @author Paul
 *
 */
public class BulletBouncer extends Obstacle{
	
	public float power = 400;
	public float time = 0.2f;
	
	public BulletBouncer(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect((int)x, (int)y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect((int)x, (int)y, width, height);
	}
	
	public void update(float tslf) {
	}
}
