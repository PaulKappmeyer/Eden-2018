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
	float knockback = 2.25f;
	int health = 200;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 1f;
	boolean hitAnimation = false;
	
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
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, Globals.size, Globals.size);
		if(hitAnimation) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, Globals.size, Globals.size);
				g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, Globals.size, Globals.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
			}
		}
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
		
		if(!followplayer) {
			float distx = enemycenterx - playercenterx;
			float disty = enemycentery - playercentery;
			float distanceToPlayer = (float) Math.hypot(distx, disty);
			if(distanceToPlayer < triggerdistance) {
				followplayer = true;
			}
		}
		
		if(followplayer) {
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
		
		if(hitAnimation) {
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				hitAnimation = false;
			}
		}
	}
	
	/**
	 * 
	 * @param b The bullet which hit the enemy
	 * {@link #applyKnockback(float angle)}
	 */
	public void getHitByBullet(Bullet b) {
		applyKnockback(b.angle);
		hitAnimation = true;
		health -= 50;
	}
	
	/**
	 * This function applies knock-back to an enemy when it got hit by an bullet
	 * @param angle The angle at which the bullet is flying and hit the enemy
	 * {@link #knockback}
	 */
	public void applyKnockback(float angle) {
		this.x += (float) Math.sin(Math.toRadians(angle)) * knockback;
		this.y += (float) Math.cos(Math.toRadians(angle)) * knockback;
	}
}
