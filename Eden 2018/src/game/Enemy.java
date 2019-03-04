package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Enemy {

	//TODO: Revise the variable mess
	float x,y;
	int size = 16;
	int triggerdistance = 300;
	boolean followplayer;
	int walkspeed = 40;
	float knockback = 6.25f;
	int health = 200;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 1f;
	boolean hitAnimation = false;
	float radius = 0;
	float radiusIncrease = 1600;
	float maxRadius = 50;
	boolean dieAnimation = false;
	
	boolean alive = true;
	
	/**
	 * Constructor; initializes the enemy
	 * @param x The x-position
	 * @param y The y-position
	 */
	public Enemy(float x, float y) {
		this.x = x;
		this.y = y;
		this.walkspeed = walkspeed + -10 + Globals.random.nextInt(20);
	}
	
	
	/**
	 * This function draws the enemy as a red rectangle to the screen
	 * @param g The graphics object to draw
	 */
	public void show(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		if(hitAnimation) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
			}
		}
		if(dieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}

	/**
	 * This function updates the enemy; if player is in range follow him
	 * @param tslf
	 */
	public void update(float tslf) {
		float halfsize = this.size/2;
		float playercenterx = Globals.player.x + halfsize;
		float playercentery = Globals.player.y + halfsize;
		float enemycenterx = this.x + halfsize;
		float enemycentery = this.y + halfsize;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;
		
		if(!followplayer) {
			float distanceToPlayer = distx * distx + disty * disty;
			if(distanceToPlayer < triggerdistance * triggerdistance) {
				followplayer = true;
			}
		}
		
		if(followplayer) {
			float angle = (float) Math.atan(distx / disty);
			angle = (float) Math.toDegrees(angle);
			if(playercentery > enemycentery) angle =  -90 - (90-angle);
			if(playercenterx < enemycenterx && playercentery < enemycentery) angle = -270 - (90-angle);
			
			float velX = (float) -Math.sin(Math.toRadians(angle));
			float velY = (float) -Math.cos(Math.toRadians(angle));
			
			this.x += velX * walkspeed * tslf;
			this.y += velY * walkspeed * tslf;
		}
		
		if(hitAnimation) {
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				hitAnimation = false;
			}
		}
		
		if(dieAnimation) {
			radius += radiusIncrease * tslf;
			if(radius >= maxRadius) {
				dieAnimation = false;
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
		if(health <= 0 && alive) {
			dieAnimation = true;
			alive = false;
		}
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
