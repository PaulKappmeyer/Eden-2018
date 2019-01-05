package game;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet {

	float x,y;
	static int size = 6;
	float velX,velY;
	float speed = 450;
	float angle;
	boolean disabled = false;
	float radius = 0;
	float radiusIncrease = 1600;
	float maxRadius = 80;
	boolean dieAnimation = false;
	
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
		this.velX = (float) Math.sin(Math.toRadians(angle));
		this.velY = (float) Math.cos(Math.toRadians(angle));
	}
	
	/**
	 * This function draws the bullet as a yellow dot to the screen
	 * @param g The graphics object to draw
	 */
	public void show(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		if(dieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}
	
	/**
	 * This function updates the bullet; adds the velocity to the position
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	public void update(float tslf) {
		if(!disabled) {
			this.x += velX * speed * tslf;
			this.y += velY * speed * tslf;
			
			Globals.checkCollisionBulletToWall(this);
		}
		if(dieAnimation) {
			Screen.addScreenshake(3, 0.05f);
			radius += radiusIncrease * tslf;
			if(radius >= maxRadius) {
				dieAnimation = false;
			}
		}
	}	


	/**
	 * 
	 * @param e The enemy to check with
	 * @param size The size of the enemy
	 * @return true if the collide; false if not
	 */
	public boolean checkCollisionToEnemy(Enemy e) {
		float cx = this.x + Bullet.size/2;
		float cy = this.y + Bullet.size/2;
		int r = Bullet.size/2;
		
		//Collision circle in the rectangle
		if(cx > e.x && cx < e.x + e.size && cy > e.y && cy < e.y + e.size) {
			return true;
		}
		//Collision top side of the rectangle to the circle
		if(cx > e.x && cx < e.x + e.size && Math.abs(e.y - cy) <= r) {
			return true;
		}
		//Collision bottom side of the rectangle to the circle
		if(cx > e.x && cx < e.x + e.size && Math.abs(e.y + e.size - cy) <= r) {
			return true;
		}
		//Collision left side of the rectangle to the circle
		if(cy > e.y && cy < e.y + e.size && Math.abs(e.x - cx) <= r) {
			return true;
		}
		//Collision right side of the rectangle to the circle
		if(cy > e.y && cy < e.y + e.size && Math.abs(e.x + e.size - cx) <= r) {
			return true;
		}
		//Collision top left corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - e.x), Math.abs(cy - e.y)) <= r) {
			return true;
		}
		//Collision top right corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - (e.x + e.size)), Math.abs(cy - e.y)) <= r) {
			return true;
		}
		//Collision bottom left corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - e.x), Math.abs(cy - (e.y + e.size))) <= r) {
			return true;
		}
		//Collision bottom right corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - (e.x + e.size)), Math.abs(cy - (e.y + size))) <= r) {
			return true;
		}
		//if no Collision happened
		return false;
	}
	
	/**
	 * This function disables the bullet so it does not get updated and moves or does damage to enemies, the state is picked for example when the bullet hit the wall
	 */
	public void disable() {
		this.angle = 0;
		this.velX = 0;
		this.velY = 0;
		this.disabled = true;
		this.dieAnimation = true;
	}
}
