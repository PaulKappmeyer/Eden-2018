package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Gun {
	
	int mode;
	float recoilSpeed = 5.75f;
	float recoilVelocityX;
	float recoilVelocityY;
	//Delay
	float shottime = 0.125f;
	//Time since last shot
	float tsls = shottime;
	//Can shot again?
	boolean canShot;
	
	float damage;
	Object owner;

	ArrayList<Projectile> projectiles;

	public Gun(Object owner) {
		this.owner = owner;
		this.canShot = false;
		this.damage = 50;
		
		projectiles = new ArrayList<>();
	}

	/**
	 * This function draws the projectiles and the shells
	 * @param g
	 */
	public void draw(Graphics g) {
		Color c = Color.BLACK;
		if(owner instanceof Eden) c = Color.YELLOW;
		if(owner instanceof Enemy) c = Color.RED;
		for (Projectile projectile : projectiles) {
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
		
		//Updating the projectiles
		if(!projectiles.isEmpty()) {
			for (Projectile projectile : projectiles) {
				projectile.update(tslf);
			}

			checkCollisionProjectilesToObjects();

			//Removal of the bullets
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile projectile = projectiles.get(i);
				if(projectile.canBeRemoved()) {
					projectiles.remove(projectile);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void checkCollisionProjectilesToObjects() {
		if(owner == Globals.player) {
			for (Enemy e : Game.currentMap.enemies) {
				if(!e.alive) continue;
				for (Projectile projectile : projectiles) {
					if(projectile.disabled) continue;
					if(projectile.checkCollisionToObject(e)) {
						e.getHitByProjectile(projectile, damage);      
						projectile.maxExplosionRadius = 30;
						projectile.disable();
					}
				}
			}

		}else if(owner instanceof Enemy) {
			for (Projectile projectile : projectiles) {
				if(projectile.disabled) continue;
				if(projectile.checkCollisionToObject(Globals.player)) {
					Globals.player.startKnockback(projectile.angle, Globals.player.bulletImpact, 0.2f);
					Globals.player.gotHit = true;
					projectile.maxExplosionRadius = 30;
					projectile.disable();
				}
			}
		}
	}

	/**
	 * This function creates a shot based on the {@link #mode}, adds projectiles to the {@link #projectiles}-array, and calls the function {@link #applyRecoil(float)}
	 */
	public abstract void shot();

	/**
	 * This function applies recoil to the player
	 * @param angle The angle in which the player should get the recoil
	 */
	public void applyRecoil(float angle) {
		Globals.player.startKnockback(angle - 180, 10f, 0.1f);
		Globals.player.walkVelocityX -= (float) Math.sin(Math.toRadians(angle));
		recoilVelocityY -= (float) Math.cos(Math.toRadians(angle));
	}

}
