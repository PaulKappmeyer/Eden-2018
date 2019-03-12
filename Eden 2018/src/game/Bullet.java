package game;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Projectile{

	public static final int SIZE = 6;
	
	/**
	 * 
	 * @param x The x-position of the bullet
	 * @param y The y-position of the bullet
	 * @param angle The angle at which the bullet is shot; the velocity is calculate on the angle
	 */
	public Bullet(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = 450;
		this.velocityX = (float) Math.sin(Math.toRadians(angle));
		this.velocityY = (float) Math.cos(Math.toRadians(angle));
	}
	
	/**
	 * This function draws the bullet as a yellow dot to the screen
	 * @param g The graphics object to draw
	 */
	@Override
	public void draw(Graphics g) {
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, SIZE, SIZE);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, SIZE, SIZE);
		if(dieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + SIZE/2 - currentRadius/2 + Globals.insetX), (int)(y + SIZE/2 - currentRadius/2 + Globals.insetY), (int)currentRadius, (int)currentRadius);
		}
	}
	
	/**
	 * This function updates the bullet; adds the velocity to the position
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	@Override
	public void update(float tslf) {
		if(!disabled) {
			x += velocityX * speed * tslf;
			y += velocityY * speed * tslf;
			
			Globals.checkCollisionProjectileToWall(this);
		}
		if(dieAnimation) {
			Screen.addScreenshake(3, 0.005f);
			
			currentRadius += explosionRadiusIncrease * tslf;
			if(currentRadius >= maxExplosionRadius) {
				dieAnimation = false;
			}
		}
	}	

	@Override
	public boolean canBeRemoved() {
		if(this.dieAnimation == false && this.disabled == true) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param obj The object to check with
	 * @return true if the collide; false if not
	 */
	@Override
	public boolean checkCollisionToObject(Object obj) {
		float cx = this.x + Bullet.SIZE/2;
		float cy = this.y + Bullet.SIZE/2;
		int r = Bullet.SIZE/2;
		
		
		//Collision circle in the rectangle
		if(cx > obj.x && cx < obj.x + obj.size && cy > obj.y && cy < obj.y + obj.size) {
			return true;
		}
		//Collision top side of the rectangle to the circle
		if(cx > obj.x && cx < obj.x + obj.size && Math.abs(obj.y - cy) <= r) {
			return true;
		}
		//Collision bottom side of the rectangle to the circle
		if(cx > obj.x && cx < obj.x + obj.size && Math.abs(obj.y + obj.size - cy) <= r) {
			return true;
		}
		//Collision left side of the rectangle to the circle
		if(cy > obj.y && cy < obj.y + obj.size && Math.abs(obj.x - cx) <= r) {
			return true;
		}
		//Collision right side of the rectangle to the circle
		if(cy > obj.y && cy < obj.y + obj.size && Math.abs(obj.x + obj.size - cx) <= r) {
			return true;
		}
		//Collision top left corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - obj.x), Math.abs(cy - obj.y)) <= r) {
			return true;
		}
		//Collision top right corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - (obj.x + obj.size)), Math.abs(cy - obj.y)) <= r) {
			return true;
		}
		//Collision bottom left corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - obj.x), Math.abs(cy - (obj.y + obj.size))) <= r) {
			return true;
		}
		//Collision bottom right corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - (obj.x + obj.size)), Math.abs(cy - (obj.y + SIZE))) <= r) {
			return true;
		}
		//if no Collision happened
		return false;
	}
}
