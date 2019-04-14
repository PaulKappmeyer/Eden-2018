/*
 * 
 */
package guns;

import java.awt.Color;
import java.awt.Graphics;

import game.Collision;
import game.Game;
import game.Globals;
import game.Gameobject;
import game.Obstacle;
import objects.BulletBouncer;
import game.GameDrawer;

/**
 * 
 * @author Paul
 *
 */
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
			//Collision
			checkCollisionToObstacles(tslf);
			checkCollisionToWall();

			//Movement
			x += velocityX * speed * tslf;
			y += velocityY * speed * tslf;
		}
		if(dieAnimation) {
			GameDrawer.addScreenshake(3, 0.005f);

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
	public boolean checkCollisionToObject(Gameobject obj) {
		return Globals.checkCollisionRectangleToCircle(this.x, this.y, SIZE, obj.x, obj.y, obj.size, obj.size);
	}

	/**
	 * 
	 */
	public void checkCollisionToObstacles(float tslf) {	
		float nextX = (float) (this.x + (velocityX * speed) * tslf);
		float nextY = (float) (this.y + (velocityY * speed) * tslf);
		if(nextX == this.x && nextY == this.y) return;

		Obstacle[] collisions = Collision.checkCollisionProjectileToObstacle(this, nextX, nextY);

		//COLLISION WITH THE TOP SIDE OF THE OBSTACLE
		Obstacle obs = collisions[Collision.TOP_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) { //IF IT HIT THE BULLET-BOUNCER LET IT BOUNCE OFF
				velocityX *= -1;
			}else { 							//IF IT HIT ANYTHING ELSE LIKE: HOUSE; STONE; etc. LET IT EXPLODE
				this.maxExplosionRadius = 30;
				this.hitSomething();
			}
			this.maxExplosionRadius = 30;
			this.hitSomething();
			this.y = obs.y - Bullet.SIZE;	
		}
		//COLLISION WIDTH THE BOTTOM SIDE OF THE OBSTACLE
		obs = collisions[Collision.BOTTOM_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				velocityY *= -1;
			}else {
				this.maxExplosionRadius = 30;
				this.hitSomething();
			}
			this.y = obs.y + obs.height;
		}
		//COLLISION WITH THE LEFT SIDE OF THE OBSTACLE
		obs = collisions[Collision.LEFT_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				velocityX *= -1;
			}else {
				this.maxExplosionRadius = 30;
				this.hitSomething();
			}
			this.x = obs.x - Bullet.SIZE;
		}
		//COLLISION WITH THE RIGHT SIDE OF THE OBSTACLE
		obs = collisions[Collision.RIGHT_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				velocityX *= -1;
			}else {
				this.maxExplosionRadius = 30;
				this.hitSomething();
			}
			this.x = obs.x + obs.width;
		}
	}
	/**
	 * Checks if the bullet goes out of the screen
	 */
	public void checkCollisionToWall() {
		if(this.x < 0 || this.y < 0 || this.x + Bullet.SIZE > Game.currentMap.mapWidth || this.y + Bullet.SIZE > Game.currentMap.mapHeight) {
			this.hitSomething();
		}
	}
}
