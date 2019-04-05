package guns;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;
import game.Game;
import game.Globals;
import game.Object;
import game.Obstacle;
import game.Screen;

public class Rocket extends Projectile{
	//TODO: Combine in one class with Bullet
	public static final int SIZE = 6;

	public Rocket(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.speed = 100;
		this.velocityX = (float) Math.sin(Math.toRadians(angle));
		this.velocityY = (float) Math.cos(Math.toRadians(angle));
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, SIZE, SIZE);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, SIZE, SIZE);
		if(dieAnimation) {
			g.setColor(new Color(0, 0, 0, 200));
			g.fillOval((int)(x + SIZE/2 - currentRadius/2 + Globals.insetX), (int)(y + SIZE/2 - currentRadius/2 + Globals.insetY), (int)currentRadius, (int)currentRadius);
		}
	}

	@Override
	public void update(float tslf) {
		if(!disabled) {
			searchEnemy(tslf);

			this.x += velocityX * speed * tslf;
			this.y += velocityY * speed * tslf;

			speed += 500 * tslf;

			checkCollisionRocketToStone();
			checkCollisionRocketToWall();
		}
		if(dieAnimation) {
			Screen.addScreenshake(3, 0.005f);

			currentRadius += explosionRadiusIncrease * tslf;
			if(currentRadius >= maxExplosionRadius) {
				dieAnimation = false;
			}
		}
	}

	/**
	 * 
	 */
	public void checkCollisionRocketToStone() {
		for (Obstacle obs : Game.currentMap.obstacles) {
			if(Globals.checkCollisionRectangleToCircle(this.x, this.y, Rocket.SIZE, obs.x, obs.y, obs.width, obs.height)) {
				this.maxExplosionRadius = 30;
				this.disable();
			}
		}
	}
	/**
	 * 
	 */
	public void checkCollisionRocketToWall() {
		if(this.x < 0 || this.y < 0 || this.x + Rocket.SIZE > Globals.width || this.y + Rocket.SIZE > Globals.height) {
			this.disable();
		}
	}

	public void searchEnemy(float tslf) {
		float nearestDistance = Float.MAX_VALUE;
		Enemy nearestEnemy = null;
		for (Enemy enemy : Game.currentMap.enemies) {
			if(!enemy.alive)continue;
			float ecx = enemy.x + enemy.size/2;
			float ecy = enemy.y + enemy.size/2;
			float pcx = this.x + SIZE/2;
			float pcy = this.y + SIZE/2;

			float distx = pcx - ecx;
			float disty = pcy - ecy;

			if(distx * distx + disty * disty <= nearestDistance) {
				nearestDistance = distx * distx + disty * disty;
				nearestEnemy = enemy;
			}
		}
		if(nearestEnemy != null) {
			float ecx = nearestEnemy.x + nearestEnemy.size/2;
			float ecy = nearestEnemy.y + nearestEnemy.size/2;
			float pcx = this.x + SIZE/2;
			float pcy = this.y + SIZE/2;

			float distx = pcx - ecx;
			float disty = pcy - ecy;

			//TODO: Rework the angle system
			float newAngle = (float) Math.atan(distx / disty);
			newAngle = (float) Math.toDegrees(newAngle);
			if(pcy > ecy) newAngle =  -90 - (90-newAngle);
			if(pcx < ecx && pcy < ecy) newAngle = -270 - (90-newAngle);

			this.velocityX = (float) Math.sin(Math.toRadians(newAngle));
			this.velocityY = (float) Math.cos(Math.toRadians(newAngle));	
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
		return Globals.checkCollisionRectangleToCircle(this.x, this.y, SIZE, obj.x, obj.y, obj.size, obj.size);
	}
}
