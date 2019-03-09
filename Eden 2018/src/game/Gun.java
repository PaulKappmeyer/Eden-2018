package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Gun {
	public static final int SINGLEFIRE = 1;
	public static final int TRIPLEMACHINEGUN = 2;
	public static final int CIRCLESHOT = 3;
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
	float tsls = shottime;
	//Can shot again?
	boolean canShot;

	float damage;
	Object owner;
	Color color;
	
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Shell> shells = new ArrayList<Shell>();
	
	public Gun(Object owner) {
		this.owner = owner;
		this.canShot = false;
		this.mode = SINGLEFIRE;
		this.damage = 50;
		this.color = Color.YELLOW;
	}

	public void draw(Graphics g) {
		for (Bullet bullet : bullets) {
			bullet.draw(g, color);
		}
		for (Shell shell : shells) {
			shell.draw(g);
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

		for (Bullet bullet : bullets) {
			bullet.update(tslf);
		}
		for (Shell shell : shells) {
			shell.update(tslf);
		}

		checkCollisionBulletsToObjects();

		//Bullets
		ArrayList<Bullet> toRemoveBullet = new ArrayList<Bullet>();
		for (Bullet b : bullets) {

			if(b.canBeRemoved()) {
				toRemoveBullet.add(b);
			}
		}
		for (Bullet b : toRemoveBullet) {
			bullets.remove(b);
		}
	}
	
	public void checkCollisionBulletsToObjects() {
		if(owner == Globals.player) {
			for (Bullet b : bullets) {
				if(b.disabled) continue;
				for (Enemy e : Globals.enemies) {
					if(b.checkCollisionToObject(e)) {

						e.getHitByBullet(b, damage);      
						b.maxExplosionRadius = 30;
						b.disable();

					}
				}
			}
		}else if(owner instanceof Enemy) {
			for (Bullet b : bullets) {
				if(b.disabled) continue;
				if(b.checkCollisionToObject(Globals.player)) {

					Globals.player.gotHit = true;
					b.maxExplosionRadius = 30;
					b.disable();

				}
			}
		}
	}

	/**
	 * This function creates a shot based on the {@link #mode}, adds bullets to the {@link #bullets}-array, and calls the function {@link #applyRecoil(float)}
	 */
	public void shot() {
		//TODO: Shot mechanics
		float angle = 0;
		//--------------------------------------------------------------------------
		if(mode == CIRCLESHOT) {
			for (int i = 0; i < numBulletsPerShot; i++) {
				angle = 360/numBulletsPerShot * i;
				float centerX = (float) (owner.x + owner.size/2 - Bullet.size/2 + Math.sin(Math.toRadians(angle)) * owner.size);
				float centerY = (float) (owner.y + owner.size/2 - Bullet.size/2 + Math.cos(Math.toRadians(angle)) * owner.size);
				bullets.add(new Bullet(centerX, centerY, angle));
				if(numBulletsPerShot == 1)applyRecoil(angle);
			}
		}
		//--------------------------------------------------------------------------------------------------
		else if(mode == TRIPLEMACHINEGUN) {
			if(owner.shotDirection == Eden.UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y - Bullet.size, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y - Bullet.size, angle));
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y - Bullet.size, angle + tripleMachineGunRadius));
			}
			if(owner.shotDirection == Eden.DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y + owner.size, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y + owner.size, angle));
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y + owner.size, angle + tripleMachineGunRadius));
			}
			if(owner.shotDirection == Eden.LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x - Bullet.size, owner.y + owner.size/2 - Bullet.size/2, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(owner.x - Bullet.size, owner.y + owner.size/2 - Bullet.size/2, angle));
				bullets.add(new Bullet(owner.x - Bullet.size, owner.y + owner.size/2 - Bullet.size/2, angle + tripleMachineGunRadius));
			}
			if(owner.shotDirection == Eden.RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.size/2, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.size/2, angle));
				bullets.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.size/2, angle + tripleMachineGunRadius));
			}
			applyRecoil(angle);
		}
		//---------------------------------------------------------------------------------------------------
		else if(mode == SINGLEFIRE) {
			if(owner.shotDirection == Eden.UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y - Bullet.size, angle));
			}
			if(owner.shotDirection == Eden.DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x + owner.size/2 - Bullet.size/2, owner.y + owner.size, angle));
			}
			if(owner.shotDirection == Eden.LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x - Bullet.size, owner.y + owner.size/2 - Bullet.size/2, angle));
			}
			if(owner.shotDirection == Eden.RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.size/2, angle));
			}
			applyRecoil(angle);
		}
		float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2);
		float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2);
		shells.add(new Shell(shellCenterX, shellCenterY, owner.shotDirection));
		//----------------------------------------------------------------------------------------------------
		canShot = false;
	}

	/**
	 * This function applies recoil to the player
	 * @param angle The angle in which the player should get the recoil
	 */
	public void applyRecoil(float angle) {
		owner.x -= (float) Math.sin(Math.toRadians(angle)) * recoil;
		owner.y -= (float) Math.cos(Math.toRadians(angle)) * recoil;
	}

}