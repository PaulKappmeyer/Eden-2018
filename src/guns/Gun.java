/*
 * 
 */
package guns;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;
import game.Game;
import game.Globals;
import game.MovingObject;
import player.Eden;

/**
 * 
 * @author Paul
 *
 */
public abstract class Gun {

	public Projectile[] projectiles;
	/** Delay */
	public float shottime = 0.125f;
	/** Can shot again? */
	public boolean canShot;
	
	int mode;
	/** Time since last shot */
	float tsls = shottime;
	float bulletSpeed = 600;
	float damage;
	MovingObject owner;

	//-----------------------------------------------------------CONSTRUCTORS------------------------------------------
	public Gun(MovingObject owner) {
		this.owner = owner;
		this.canShot = false;
		this.damage = 50;

		projectiles = new Projectile[100];
	}

	//-----------------------------------------------------------METHODS------------------------------------------
	//------------------------DRAWING
	/**
	 * This function draws the projectiles and the shells
	 * @param g
	 */
	public void draw(Graphics g) {
		Color c = Color.BLACK;
		if (owner instanceof Eden) {
			c = Color.YELLOW;
		}
		else if (owner instanceof Enemy) {
			c = Color.RED;
		}
		for (Projectile projectile : projectiles) {
			if (!projectile.isActive) {
				continue;
			}
			g.setColor(c);
			projectile.draw(g);
		}
	}

	//------------------------UPDATING
	/**
	 * 
	 * @param tslf
	 */
	public void update(float tslf) {
		//Shooting
		if (tsls >= shottime) {
			canShot = true;
			tsls -= shottime;
		}else {
			tsls += tslf;	
		}

		//Updating the projectiles && Removal of the bullets
		int size = projectiles.length;
		for (int i = 0; i < size; i++) {
			Projectile projectile = projectiles[i];
			
			projectile.update(tslf);
			
			if (projectile.canBeDeactivated()) {
				projectile.deactivate();
			}	
		}

		//Collision
		checkCollisionProjectilesToObjects();
	}
	
	//------------------------SHOOTING
	/**
	 * This functions searches for a deactivated projectile in the object-pool and activates it; If the object-pool has got no free projectile it does nothing
	 * This function is like adding a projectile to a list, but instead uses the object-pool
	 * @param x The x-position of the new projectile
	 * @param y The y-position of the new projectile
	 * @param angle
	 * @param speed The speed of the new projectile
	 */
	public void fireProjectile(float x, float y, float angle, float speed) {
		for (int i = 0; i < projectiles.length; i++) {
			if (!projectiles[i].isActive) {
				Projectile p = projectiles[i];
				p.activate(x, y, angle, speed);
				return;
			}
		}
	}
	
	/**
	 * This function creates a shot based on the {@link #mode}, adds projectiles to the {@link #projectiles}-array, and calls the function {@link #applyRecoil(float)}
	 */
	public abstract void shot();

	public abstract void shot(float angle);
	
	//------------------------SHOOTING - RECOIL
	/**
	 * This function applies knockback to the player
	 * @param velocityX
	 * @param velocityY
	 */
	public void applyRecoil(double velocityX, double velocityY) {
		Globals.player.startKnockback(velocityX, velocityY, 25f, 0.1f);
	}
	
	//------------------------COLLISION
	/**
	 * This function checks for collision with either the player or the enemies depending on who shot
	 */
	public void checkCollisionProjectilesToObjects() {
		if (owner == Globals.player) {
			int size = Game.currentMap.enemies.size() - 1;
			for (int i = size; i >= 0; i--) {
				Enemy e = Game.currentMap.enemies.get(i);
				if (!e.alive) {
					continue;
				}
				for (Projectile projectile : projectiles) {
					if (projectile.hitSomething || !projectile.isActive) {
						continue;
					}
					if (projectile.checkCollisionToObject(e)) {
						e.getHitByProjectile(projectile, damage);      
						projectile.maxExplosionRadius = 30;
						projectile.hitSomething();
					}
				}
			}
		} else if (owner instanceof Enemy) {
			for (Projectile projectile : projectiles) {
				if (projectile.hitSomething || !projectile.isActive) {
					continue;
				}
				if (projectile.checkCollisionToObject(Globals.player)) {
					Globals.player.startKnockback(projectile.velocityX, projectile.velocityY, Globals.player.bulletImpact, Globals.player.bulletImpactTime);
					Globals.player.resetSpeedUp();
					Globals.player.gotHit = true;
					projectile.maxExplosionRadius = 30;
					projectile.hitSomething();
				}
			}
		}
	}
}
