package game;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;

public class Healthbar {

	Enemy owner;
	
	public Healthbar(Enemy e) {
		this.owner = e;
	}
	
	public void draw(Graphics g) {
		if(owner.isInHitAnimation) {
			g.setColor(Color.GREEN);
			g.fillRect((int)owner.x + Globals.insetX, (int)owner.y - 10 + Globals.insetY, (int)(owner.size * (owner.health / owner.MAX_HEALTH)), 5);
			g.setColor(Color.BLACK);
			g.drawRect((int)owner.x + Globals.insetX, (int)owner.y - 10 + Globals.insetY, owner.size, 5);
		}
	}
	
	public void update(float tslf) {
		
	}
	
}
