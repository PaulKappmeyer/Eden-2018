package guns;

import java.awt.Color;
import java.awt.Graphics;

import game.Game;
import game.Globals;
import game.Object;
import game.Obstacle;
import game.Screen;

public class Bullet extends Projectile{

	public static final int SIZE = 6;

	public Bullet() {
		isActive = false;
	}

	//---------------------------------Methods---------------------------------------------------------------
	
	/**
	 *
	 * @param x The x-position of the bullet
	 * @param y The y-position of the bullet
	 * @param angle The angle at which the bullet is shot; the velocity is calculate on the angle
	 * @param speed
	 */
	@Override
	public void activate(float x, float y, float angle, float speed) {
		isActive = true;
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = speed;
		this.velocityX = (float) Math.sin(Math.toRadians(angle));
		this.velocityY = (float) Math.cos(Math.toRadians(angle));
		
		hitSomething = false;
		dieAnimation = false;
		maxExplosionRadius = 80;
		explosionRadiusIncrease = 1600;
		currentRadius = 0;
	}

	@Override
	public void deactivate() {
		isActive = false;
	}
	
	@Override
	public void hitSomething() {
		this.angle = 0;
		this.velocityX = 0;
		this.velocityY = 0;
		this.hitSomething = true;
		this.dieAnimation = true;
	}

	@Override
	public boolean canBeDeactivated() {
		if(this.dieAnimation == false && this.hitSomething == true) {
			return true;
		}
		return false;
	}
	//---------------------------------Drawing---------------------------------------------------------------
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
			g.setColor(new Color(0, 0, 0, 200));
			g.fillOval((int)(x + SIZE/2 - currentRadius/2 + Globals.insetX), (int)(y + SIZE/2 - currentRadius/2 + Globals.insetY), (int)currentRadius, (int)currentRadius);
		}
	}
	//---------------------------------Updating---------------------------------------------------------------
	/**
	 * This function updates the bullet; adds the velocity to the position
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	@Override
	public void update(float tslf) {
		if(!isActive) return;
		
		if(!hitSomething) {
			x += velocityX * speed * tslf;
			y += velocityY * speed * tslf;

			checkCollisionBulletToStone();
			checkCollisionBulletToWall();
		}
		if(dieAnimation) {
			Screen.addScreenshake(3, 0.005f);

			currentRadius += explosionRadiusIncrease * tslf;
			if(currentRadius >= maxExplosionRadius) {
				dieAnimation = false;
			}
		}
	}

	//---------------------------------Collision---------------------------------------------------------------
	/**
	 * 
	 * @param obj The object to check with
	 * @return true if the collide; false if not
	 */
	@Override
	public boolean checkCollisionToObject(Object obj) {
		return Globals.checkCollisionRectangleToCircle(this.x, this.y, SIZE, obj.x, obj.y, obj.size, obj.size);
	}
	
	/**
	 * 
	 */
	public void checkCollisionBulletToStone() {
		for (Obstacle obs : Game.currentMap.obstacles) {
			if(Globals.checkCollisionRectangleToCircle(this.x, this.y, Bullet.SIZE, obs.x, obs.y, obs.width, obs.height)) {
				this.maxExplosionRadius = 30;
				this.hitSomething();
			}
		}
//		RoundStone rs = Game.currentMap.stoneRound;
//		if(Globals.checkCollisionCircleToCircle(this.x, this.y, Bullet.SIZE, rs.x, rs.y, rs.size)) {
//			this.maxExplosionRadius = 30;
//			this.disable();
//		}
	}
	/**
	 * 
	 */
	public void checkCollisionBulletToWall() {
		if(this.x < 0 || this.y < 0 || this.x + Bullet.SIZE > Globals.width || this.y + Bullet.SIZE > Globals.height) {
			this.hitSomething();
		}
	}
}
