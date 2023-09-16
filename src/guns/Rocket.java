/*
 * 
 */
package guns;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;
import game.Game;
import game.Globals;
import game.Gameobject;
import game.Obstacle;
import game.GameDrawer;

/**
 * 
 * @author Paul
 *
 */
public class Rocket extends Projectile{

	public static final int SIZE = 6;

	public Rocket() {
		isActive = false;
	}

	//---------------------------------Methods---------------------------------------------------------------

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
		if (this.dieAnimation == false && this.hitSomething == true) {
			return true;
		}
		return false;
	}
	
	//---------------------------------Drawing---------------------------------------------------------------
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillOval((int)x, (int)y, SIZE, SIZE);
		g.setColor(Color.BLACK);
		g.drawOval((int)x, (int)y, SIZE, SIZE);
		if (dieAnimation) {
			g.setColor(new Color(0, 0, 0, 200));
			g.fillOval((int)(x + SIZE/2 - currentRadius/2), (int)(y + SIZE/2 - currentRadius/2), (int)currentRadius, (int)currentRadius);
		}
	}
	//---------------------------------Updating---------------------------------------------------------------
	@Override
	public void update(float tslf) {
		if (!isActive) {
			return;
		}

		if (!hitSomething) {
			searchEnemy(tslf);

			this.x += velocityX * speed * tslf;
			this.y += velocityY * speed * tslf;

			speed += 500 * tslf;

			checkCollisionRocketToStone();
			checkCollisionRocketToWall();
		}
		if (dieAnimation) {
			GameDrawer.addScreenshake(3, 0.005f);

			currentRadius += explosionRadiusIncrease * tslf;
			if (currentRadius >= maxExplosionRadius) {
				dieAnimation = false;
			}
		}
	}
	/**
	 * 
	 * @param tslf
	 */
	public void searchEnemy(float tslf) {
		float nearestDistance = Float.MAX_VALUE;
		Enemy nearestEnemy = null;
		for (Enemy enemy : Game.currentMap.enemies) {
			if (!enemy.alive) {
				continue;
			}
			float ecx = enemy.x + enemy.size/2;
			float ecy = enemy.y + enemy.size/2;
			float pcx = this.x + SIZE/2;
			float pcy = this.y + SIZE/2;

			float distx = pcx - ecx;
			float disty = pcy - ecy;

			if (distx * distx + disty * disty <= nearestDistance) {
				nearestDistance = distx * distx + disty * disty;
				nearestEnemy = enemy;
			}
		}
		if (nearestEnemy != null) {
			float ecx = nearestEnemy.x + nearestEnemy.size/2;
			float ecy = nearestEnemy.y + nearestEnemy.size/2;
			float pcx = this.x + SIZE/2;
			float pcy = this.y + SIZE/2;

			float distx = pcx - ecx;
			float disty = pcy - ecy;

			//TODO: Rework the angle system
			float newAngle = (float) Math.atan(distx / disty);
			newAngle = (float) Math.toDegrees(newAngle);
			if (pcy > ecy) newAngle =  -90 - (90-newAngle);
			if (pcx < ecx && pcy < ecy) newAngle = -270 - (90-newAngle);

			this.velocityX = (float) Math.sin(Math.toRadians(newAngle));
			this.velocityY = (float) Math.cos(Math.toRadians(newAngle));	
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
	public void checkCollisionRocketToStone() {
		for (Obstacle obs : Game.currentMap.obstacles) {
			if (Globals.checkCollisionRectangleToCircle(this.x, this.y, Rocket.SIZE, obs.x, obs.y, obs.width, obs.height)) {
				this.maxExplosionRadius = 30;
				this.hitSomething();
			}
		}
	}
	/**
	 * 
	 */
	public void checkCollisionRocketToWall() {
		if (this.x < 0 || this.y < 0 || this.x + Rocket.SIZE > Game.currentMap.mapWidth || this.y + Rocket.SIZE > Game.currentMap.mapHeight) {
			this.hitSomething();
		}
	}
}
