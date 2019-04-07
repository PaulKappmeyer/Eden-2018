package guns;

import java.awt.Color;
import java.awt.Graphics;

import enemies.ZombieEnemy;
import game.Eden;
import game.Game;
import game.Globals;
import game.MovingObject;

public abstract class Gun {

	int mode;
	//Delay
	public float shottime = 0.125f;
	//Time since last shot
	float tsls = shottime;
	//Can shot again?
	public boolean canShot;

	float bulletSpeed = 600;

	float damage;
	MovingObject owner;

	public Projectile[] projectiles;

	public Gun(MovingObject owner) {
		this.owner = owner;
		this.canShot = false;
		this.damage = 50;

		projectiles = new Projectile[100];
	}

	/**
	 * This function draws the projectiles and the shells
	 * @param g
	 */
	public void draw(Graphics g) {
		Color c = Color.BLACK;
		if(owner instanceof Eden) c = Color.YELLOW;
		if(owner instanceof ZombieEnemy) c = Color.RED;
		for (Projectile projectile : projectiles) {
			if(!projectile.isActive) continue;
			g.setColor(c);
			projectile.draw(g);
		}
	}

	/**
	 * 
	 * @param tslf
	 */
	public void update(float tslf) {
		//Shooting
		if(tsls >= shottime) {
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
			
			if(projectile.canBeDeactivated()) {
				projectile.deactivate();
			}	
		}

		//Collision
		checkCollisionProjectilesToObjects();
	}
	
	/**
	 * 
	 */
	public void checkCollisionProjectilesToObjects() {
		if(owner == Globals.player) {
			for (ZombieEnemy e : Game.currentMap.enemies) {
				if(!e.alive) continue;
				for (Projectile projectile : projectiles) {
					if(projectile.hitSomething || !projectile.isActive) continue;
					if(projectile.checkCollisionToObject(e)) {
						e.getHitByProjectile(projectile, damage);      
						projectile.maxExplosionRadius = 30;
						projectile.hitSomething();
					}
				}
			}

		}else if(owner instanceof ZombieEnemy) {
			for (Projectile projectile : projectiles) {
				if(projectile.hitSomething || !projectile.isActive) continue;
				if(projectile.checkCollisionToObject(Globals.player)) {
					Globals.player.startKnockback(projectile.angle, Globals.player.bulletImpact, 0.2f);
					Globals.player.gotHit = true;
					projectile.maxExplosionRadius = 30;
					projectile.hitSomething();
				}
			}
		}
	}

	/**
	 * This function creates a shot based on the {@link #mode}, adds projectiles to the {@link #projectiles}-array, and calls the function {@link #applyRecoil(float)}
	 */
	public abstract void shot();

	public abstract void shot(float angle);

	/**
	 * This function applies recoil to the player
	 * @param angle The angle in which the player should get the recoil
	 */
	public void applyRecoil(float angle) {
		Globals.player.startKnockback(angle - 180, 25f, 0.1f);
	}

}
