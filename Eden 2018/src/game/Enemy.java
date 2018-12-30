package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Enemy {

	float x,y;
	int triggerdistance = 300;
	boolean followplayer;
	int walkspeed = 50;
	
	public Enemy(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * This function draws the enemy as a red rectangle to the screen
	 * @param g The graphics object to draw
	 */
	public void show(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, Globals.size, Globals.size);
	}
	
	/**
	 * This function updates the enemy; if player is in range follow him
	 * @param tslf
	 */
	public void update(float tslf) {
		float halfsize = Globals.size/2;
		float playercenterx = Globals.player.x + halfsize;
		float playercentery = Globals.player.y + halfsize;
		float enemycenterx = this.x + halfsize;
		float enemycentery = this.y + halfsize;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;
		float distanceToPlayer = (float) Math.hypot(distx, disty);
		if(distanceToPlayer < triggerdistance) {
			followplayer = true;
		}
		
		if(followplayer) {
//			float angle = (float) ((float) Math.atan(distx / disty));
//			float velx = (float) Math.sin(angle);
//			float vely = (float) Math.cos(angle);
//			System.out.println(angle + "  " + velx + "   " + vely);
//			this.x += velx * walkspeed * tslf;
//			this.y += vely * walkspeed * tslf;
			if(enemycenterx < playercenterx) {
				this.x += walkspeed * tslf;
			}
			if(enemycenterx > playercenterx) {
				this.x -= walkspeed * tslf;
			}
			if(enemycentery < playercentery) {
				this.y += walkspeed * tslf;
			}
			if(enemycentery > playercentery) {
				this.y -= walkspeed * tslf;
			}
		}
	}
}
