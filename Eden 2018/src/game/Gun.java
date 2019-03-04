package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Gun {

	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
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
	
	Eden owner;
	
	public Gun(Eden owner) {
		this.owner = owner;
		this.canShot = true;
		this.mode = SINGLEFIRE;
	}
	
	public void draw(Graphics g) {
		for (Bullet b : bullets) {
			b.show(g);
		}
	}
	
	public void update(float tslf) {
		//Shooting
		if(tsls >= shottime) {
			if(!Controls.isKeyDown(KeyEvent.VK_SPACE)) owner.state = Eden.IDLESTATE;
			canShot = true;
			tsls -= shottime;
		}else {
			tsls += tslf;	
		}
		
		//Bullets
		ArrayList<Bullet> toRemoveBullet = new ArrayList<Bullet>();
		for (Bullet b : bullets) {
			b.update(tslf);
			
			if(b.dieAnimation == false && b.disabled == true) {
				toRemoveBullet.add(b);
			}
			if(b.disabled) continue;
			
			for (Enemy e : Globals.enemies) {
				if(b.checkCollisionToEnemy(e)) {
					
					e.getHitByBullet(b);      
					b.maxRadius = 30;
					b.disable();
					
				}
			}
		}
		for (Bullet b : toRemoveBullet) {
			bullets.remove(b);
		}
	}
	
	/**
	 * This function creates a new bullet when the player did a shot an applies recoil to the player
	 * {@link }
	 */
	public void shot() {
		//TODO: Shot mechanics
		float angle = 0;
		if(mode == CIRCLESHOT) {
			for (int i = 0; i < numBulletsPerShot; i++) {
				angle = 360/numBulletsPerShot * i;
				float cx = (float) (owner.x + owner.size/2 - Bullet.size/2 + Math.sin(Math.toRadians(angle)) * owner.size);
				float cy = (float) (owner.y + owner.size/2 - Bullet.size/2 + Math.cos(Math.toRadians(angle)) * owner.size);
				bullets.add(new Bullet(cx, cy, angle));
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
