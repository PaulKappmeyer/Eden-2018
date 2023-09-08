/*
 * 
 */
package enemies;

import java.awt.Color;
import java.awt.Graphics;

import game.Direction;
import game.Healthbar;
import game.MovingObject;
import guns.Projectile;

/**
 * 
 * @author Paul
 *
 */
public abstract class Enemy extends MovingObject {
	//POSITION
	Direction lookDirection = Direction.DOWN;
	
	//KNOCKBACK
	boolean gotKnockbacked;
	double knockbackVelocityX;
	double knockbackVelocityY;
	float MAX_KNOCKBACK_SPEED;
	float currentKnockbackSpeed;
	//KNOCKBACK TIMEING
	float MAX_KNOCKBACK_TIME = 1f;
	float timeKnockedBack;
	
	//HEALTH
	public float MAX_HEALTH;
	public float health;
	public boolean alive = true;
	Healthbar healthbar = new Healthbar(this);
	
	
	//Get hit by bullet 
	public float bulletImpact = 325;
	public float bulletImpactTime = 0.1f;
	
	//Got-Hit animation
	public boolean isInHitAnimation = false;
	float timeBlinked;
	float swapBlinkTime = 0.05f;
	int blinksDone;
	int maxBlinks = 15;
	boolean showBlink;
	//Die animation
	boolean isInDieAnimation = false;
	float radius = 0;
	float radiusIncrease = 1600;
	float maxRadius = 50;
	
	//-----------------------------------------------------------CONSTRUCTORS------------------------------------------
	public Enemy() {
	}
	
	public Enemy(float x, float y) {
		super(x, y);
	}
	
	public Enemy(float x, float y, int size) {
		super(x, y, size);
	}
	
	//-----------------------------------------------------------METHODS------------------------------------------
	//------------------------DRAWING
	/**
	 * This function draws the enemy to the screen
	 * @param g
	 */
	int a = 5;
	public void draw(Graphics g) {
		if (!showBlink) {
			g.setColor(Color.RED);
			g.fillRect((int)x, (int)y, this.size, this.size);
			g.setColor(Color.BLACK);
			g.drawRect((int)x, (int)y, this.size, this.size);
			
			g.setColor(Color.BLUE);
			switch (lookDirection) {
			case UP:
				g.fillOval((int)(this.x + size/2 - a/2), (int)this.y, a, a);
				break;
			case DOWN:
				g.fillOval((int)(this.x + size/2 - a/2), (int)this.y + size - a, a, a);
				break;
			case RIGHT:
				g.fillOval((int)(this.x + size - a), (int)this.y + size/2 - a/2, a, a);
				break;
			case LEFT:
				g.fillOval((int)(this.x), (int)this.y + size/2 - a/2, a, a);
				break;
			default:
				break;
			}
		}
		if (isInHitAnimation) {
			if (showBlink) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x, (int)y, this.size, this.size);
				g.drawRect((int)x, (int)y, this.size, this.size);
			}
		}
		if (isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2), (int)(y + size/2 - radius/2), (int)radius, (int)radius);
		}
		
		healthbar.draw(g);
	}
	
	//------------------------UPDATING
	/**
	 * This function does all the updating of the enemy like: updating the knockback, updating the animations, set new position, check for collision etc.
	 * @param tslf Time-since-last-frame or delta time
	 */
	public void update(float tslf) {
		super.update(tslf);
		
		//Knockback
		updateKnockback(tslf);
		
		//Speed up
		updateSpeedUp(tslf);
		
		//Got-Hit animation
		updateGotHitAnimation(tslf);

		//Die animation
		updateDieAnimation(tslf);
	}
	
	//------------------------KNOCKBACK UPDATING
	/**
	 * This function updates the knockback: calculating the current knockback speed and reset the speed up
	 * @param tslf
	 */
	public void updateKnockback(float tslf) {
		if (gotKnockbacked) {
			if (timeKnockedBack <= MAX_KNOCKBACK_TIME) {
				timeKnockedBack += tslf;
				resetSpeedUp();
				currentKnockbackSpeed = MAX_KNOCKBACK_SPEED * ((MAX_KNOCKBACK_TIME - timeKnockedBack) / MAX_KNOCKBACK_TIME);
			} else {
				gotKnockbacked = false;
				timeKnockedBack = 0;
				currentKnockbackSpeed = 0;
			}
		}
	}

	//------------------------Animations
	/**
	 * This function updates the got hit animation
	 * @param tslf
	 */
	public void updateGotHitAnimation(float tslf) {
		if (isInHitAnimation) {
			timeBlinked += tslf;
			if (timeBlinked > swapBlinkTime) {
				showBlink = !showBlink;
				blinksDone ++;
				timeBlinked -= swapBlinkTime;
			}
			if (blinksDone > maxBlinks) {
				timeBlinked = 0;
				blinksDone = 0;
				isInHitAnimation = false;
			}
		}
	}
	/**
	 * This function updates the death animation
	 * @param tslf
	 */
	public void updateDieAnimation(float tslf) {
		if (isInDieAnimation) {
			radius += radiusIncrease * tslf;
			if (radius >= maxRadius) {
				isInDieAnimation = false;
			}
		}
	}
	
	//------------------------KNOCKBACK METHODS
	/**
	 * 
	 * @param knockbackVelocityX
	 * @param knockbackVelocityY
	 * @param amount
	 * @param time
	 */
	public void startKnockback(double knockbackVelocityX, double knockbackVelocityY, float amount, float time) {
		double length = (knockbackVelocityX * knockbackVelocityX + knockbackVelocityY * knockbackVelocityY);
		if (Math.round(length) != 1) System.err.println(this.toString() + ".startKnockback: Directions vectors should have a length of 1 insead of: " + length + " x:" + knockbackVelocityX + " y:" + knockbackVelocityY);
		
		gotKnockbacked = true;
		this.knockbackVelocityX = knockbackVelocityX;
		this.knockbackVelocityY = knockbackVelocityY;
		MAX_KNOCKBACK_SPEED = amount;
		MAX_KNOCKBACK_TIME = time;
	}
	
	//------------------------GET DAMEGED METHODS
	/**
	 * This function gets called when the enemy got hit by a bullet
	 * @param b The bullet which hit the enemy
	 * {@link #applyKnockback(float angle)}
	 */
	public void getHitByProjectile(Projectile p, float damage) {
		startKnockback(p.velocityX, p.velocityY, bulletImpact, bulletImpactTime);
		getDamaged(damage);
	}
	/**
	 * This function applies damage to the enemy
	 * @param damage
	 */
	public void getDamaged(float damage) {
		isInHitAnimation = true;
		health -= damage;
		if (health <= 0 && alive) {
			isInDieAnimation = true;
			alive = false;
		}
		resetSpeedUp();
	}
	
	//------------------------Removing
	/**
	 * This function checks if the enemy can be removed: for example when dead and death animation is finished
	 * @return
	 */
	public boolean canBeRemoved() {
		if (alive) return false;
		if (isInDieAnimation) return false;
		return true;
	}
}
