package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Gun {
	
	int mode;
	float recoil = 0.75f;
	//Delay
	float shottime = 0.125f;
	//Time since last shot
	float tsls = shottime;
	//Can shot again?
	boolean canShot;

	float damage;
	Object owner;

	ArrayList<Projectile> projectiles;
	ArrayList<Shell> shells;

	public Gun(Object owner) {
		this.owner = owner;
		this.canShot = false;
		this.damage = 50;
		
		shells = new ArrayList<>();
		projectiles = new ArrayList<>();
	}

	/**
	 * This function draws the projectiles and the shells
	 * @param g
	 */
	public void draw(Graphics g) {
		for (Shell shell : shells) {
			shell.draw(g);
		}
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
		
		//Updating the shells
		for (Shell shell : shells) {
			shell.update(tslf);
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
			for (Enemy e : Globals.enemies) {
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
					Globals.player.startKnockback(projectile.angle, Globals.player.bulletImpact);
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
		owner.x -= (float) Math.sin(Math.toRadians(angle)) * recoil;
		owner.y -= (float) Math.cos(Math.toRadians(angle)) * recoil;
	}

}
