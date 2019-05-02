/*
 * 
 */
package game;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;

/**
 * 
 * @author Paul
 *
 */
public class Healthbar {

	Enemy owner;
	
	public Healthbar(Enemy e) {
		this.owner = e;
	}
	
	public void draw(Graphics g) {
		if(owner.isInHitAnimation) {
			g.setColor(Color.GREEN);
			g.fillRect((int)owner.x, (int)owner.y - 10, (int)(owner.size * (owner.health / owner.MAX_HEALTH)), 5);
			g.setColor(Color.BLACK);
			g.drawRect((int)owner.x, (int)owner.y - 10, owner.size, 5);
		}
	}
	
	public void update(float tslf) {
		
	}
	
}
