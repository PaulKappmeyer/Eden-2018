package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Enemy extends Object{

	//TODO: Revise the variable mess
	int triggerdistance = 300;
	boolean followplayer;
	int walkspeed = 100;
	float knockback = 6.25f;
	int health = 200;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 1f;
	boolean isInHitAnimation = false;
	float radius = 0;
	float radiusIncrease = 1600;
	float maxRadius = 50;
	boolean isInDieAnimation = false;
	
	boolean alive = true;
	
	/**
	 * Constructor; initializes the enemy
	 * @param x The x-position
	 * @param y The y-position
	 */
	public Enemy(float x, float y) {
		this.x = x;
		this.y = y;
		this.size = 16;
		this.walkspeed = walkspeed + -10 + Globals.random.nextInt(20);
	}
	
	
	/**
	 * This function draws the enemy as a red rectangle to the screen
	 * @param g The graphics object to draw
	 */
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		if(isInHitAnimation) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
			}
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}

	/**
	 * This function updates the enemy; if player is in range follow him
	 * @param tslf
	 */
	public void update(float tslf) {
		if(alive) {
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
		}
		
		if(isInHitAnimation) {
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				isInHitAnimation = false;
			}
		}
		
		if(isInDieAnimation) {
			radius += radiusIncrease * tslf;
			if(radius >= maxRadius) {
				isInDieAnimation = false;
			}
		}
	}
	
	/**
	 * 
	 * @param b The bullet which hit the enemy
	 * {@link #applyKnockback(float angle)}
	 */
	public void getHitByBullet(Projectile p, float damage) {
		applyKnockback(p.angle);
		isInHitAnimation = true;
		health -= damage;
		if(health <= 0 && alive) {
			isInDieAnimation = true;
			alive = false;
		}
		if(!followplayer) followplayer = true;
	}
	public void getHitByBullet(Rocket r, float damage) {
		applyKnockback(r.angle);
		isInHitAnimation = true;
		health -= damage;
		if(health <= 0 && alive) {
			isInDieAnimation = true;
			alive = false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean canBeRemoved() {
		if(alive) return false;
		if(isInDieAnimation) return false;
		
		return true;
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
