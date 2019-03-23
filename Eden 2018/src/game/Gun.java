package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Gun {
	public static final int SINGLEFIRE = 1;
	public static final int TRIPLEMACHINEGUN = 2;
	public static final int CIRCLESHOT = 3;
	public static final int ROCKET_SINGLE_FIRE_MODE = 4;
	public static final int CIRCLESHOT_ROUND = 5;
	int mode;

	//Single
	int bulletspray = 3;
	float recoil = 0.75f;
	//Triple machine gun
	float tripleMachineGunRadius = 10;
	//Circle shot
	int numBulletsPerShot = 36;
	//Delay
	float shottime = 0.125f;
	//Time since last shot
	float tsls = shottime;
	//Can shot again?
	boolean canShot;

	float damage;
	Object owner;

	ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	ArrayList<Shell> shells = new ArrayList<Shell>();

	public Gun(Object owner) {
		this.owner = owner;
		this.canShot = false;
		this.mode = SINGLEFIRE;
		this.damage = 50;
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

	public void update(float tslf) {
		//Shooting
		if(tsls >= shottime) {
			canShot = true;
			tsls -= shottime;
		}else {
			tsls += tslf;	
		}

		for (Projectile projectile : projectiles) {
			projectile.update(tslf);
		}
		for (Shell shell : shells) {
			shell.update(tslf);
		}

		checkCollisionBulletsToObjects();

		//Removal of the bullets
		ArrayList<Projectile> removableProjectiles = new ArrayList<Projectile>();
		for (Projectile projectile : projectiles) {
			if(projectile.canBeRemoved()) {
				removableProjectiles.add(projectile);
			}
		}
		for (Projectile p : removableProjectiles) {
			projectiles.remove(p);
		}
	}

	public void checkCollisionBulletsToObjects() {
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
	public void shot() {
		//TODO: Shot mechanics
		float angle = 0;
		//--------------------------------------------------------------------------
		if(mode == CIRCLESHOT) {
			for (int i = 0; i < numBulletsPerShot; i++) {
				angle = 360/numBulletsPerShot * i;
				float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
				float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
				projectiles.add(new Bullet(centerX, centerY, angle));
				float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
				float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
				shells.add(new Shell(shellCenterX, shellCenterY, angle));
				if(numBulletsPerShot == 1)applyRecoil(angle);
			}
		}
		//--------------------------------------------------------------------------------------------------
		else if(mode == TRIPLEMACHINEGUN) {
			if(owner.shotDirection == Eden.UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle + tripleMachineGunRadius));
			}
			if(owner.shotDirection == Eden.DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle + tripleMachineGunRadius));
			}
			if(owner.shotDirection == Eden.LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle + tripleMachineGunRadius));
			}
			if(owner.shotDirection == Eden.RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle + tripleMachineGunRadius));
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			shells.add(new Shell(shellCenterX, shellCenterY, angle));
			shells.add(new Shell(shellCenterX, shellCenterY, angle));
			shells.add(new Shell(shellCenterX, shellCenterY, angle));
			applyRecoil(angle);
		}
		//---------------------------------------------------------------------------------------------------
		else if(mode == SINGLEFIRE) {
			if(owner.shotDirection == Eden.UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle));
			}
			if(owner.shotDirection == Eden.DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle));
			}
			if(owner.shotDirection == Eden.LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
			}
			if(owner.shotDirection == Eden.RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			shells.add(new Shell(shellCenterX, shellCenterY, angle));
			applyRecoil(angle);
		}
		//---------------------------------------------------------------------------------------------------
		else if(mode == ROCKET_SINGLE_FIRE_MODE) {
			if(owner.shotDirection == Eden.UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			if(owner.shotDirection == Eden.DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			if(owner.shotDirection == Eden.LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			if(owner.shotDirection == Eden.RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			shells.add(new Shell(shellCenterX, shellCenterY, angle));
			float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
			float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
			projectiles.add(new Rocket(centerX, centerY, angle));
			applyRecoil(angle);
		}
		//----------------------------------------------------------------------------------------------------
		else if(mode == CIRCLESHOT_ROUND) {
			angle = 360/numBulletsPerShot * a;
			float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
			float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
			projectiles.add(new Bullet(centerX, centerY, angle));
			shells.add(new Shell(centerX, centerY, angle));
			applyRecoil(angle);
			a++;
		}
		//----------------------------------------------------------------------------------------------------
		canShot = false;
	}

	int a = 0;
	
	/**
	 * This function applies recoil to the player
	 * @param angle The angle in which the player should get the recoil
	 */
	public void applyRecoil(float angle) {
		owner.x -= (float) Math.sin(Math.toRadians(angle)) * recoil;
		owner.y -= (float) Math.cos(Math.toRadians(angle)) * recoil;
	}

}
