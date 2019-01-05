package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * This is the class of the player named "Eden"
 * @author Paul Kappmeyer
 * @param x The x-position of the player
 * @param y The y-position of the player
 * @param walkspeed This is the number of the pixels the player can run per second
 */
public class Eden {

	//TODO: revise the variable mess
	float x,y;
	int idlewalkspeed = 200;
	int shotwalkspeed = 100;
	int walkspeed = idlewalkspeed;
	float shottime = 0.125f;
	float tsls = shottime;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	int bulletspray = 3;
	float recoil = 0.75f;
	
	boolean tripleMachineGun = true;
	float tripleMachineGunRadius = 10;
	
	boolean circleShot = false;
	int numBulletsPerShot = 36;
	
	float knockback = 40f;
	float velX;
	float velY;
	
	final int IDLESTATE = 0;
	final int SHOOTSTATE = 1;
	int state = 0;
	
	final int UP = 0;
	final int DOWN = 1;
	final int LEFT = 2;
	final int RIGHT = 3;
	int direction;
	int shotDirection = -1;
	
	int size = 16;
	
	boolean canshot;
	
	boolean gotHit = false;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 2f;
	
	/**
	 * Constructor; initializes the player
	 */
	public Eden() {
		x = 400;
		y = 400;
	}
	
	/**
	 * This Function show the player on the screen; gets called every frame
	 * @param g A Graphics Object to draw the player
	 * @see Graphics
	 */
	public void show(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		
		if(gotHit) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
			}
		}
		
		for (Bullet b : bullets) {
			b.show(g);
		}
	}
	
	/**
	 * This function updates the player; gets called every frame; first checks for input; second updates the movement
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	public void update(float tslf) {
		if(state == SHOOTSTATE) {
			walkspeed = shotwalkspeed;
		}else if(state == IDLESTATE) {
			walkspeed = idlewalkspeed;
		}
		
		if(Controls.isKeyDown(KeyEvent.VK_W)) {
			direction = UP;
			y -= walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_A)) {
			direction = LEFT;
			x -= walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_S)) {
			direction = DOWN;
			y += walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_D)) {
			direction = RIGHT;
			x += walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_SPACE) && !gotHit) {
			if(canshot) {
				if(state == IDLESTATE) shotDirection = direction;
				if(shotDirection == UP && direction == DOWN) shotDirection = direction;
				if(shotDirection == DOWN && direction == UP) shotDirection = direction;	
				if(shotDirection == LEFT && direction == RIGHT) shotDirection = direction;
				if(shotDirection == RIGHT && direction == LEFT) shotDirection = direction;
				
				state = SHOOTSTATE;
				canshot = false;
				shot();
			}
		}
		
		checkCollisionPlayerToWall();
		
		if(!gotHit)checkCollisionPlayerToEnemies();
		
		if(gotHit) {
			this.x += velX * tslf;
			this.y += velY * tslf;
			// TODO: slow down velX and velY
//			this.velX *= (0.5 * tslf);
//			this.velY *= (0.5 * tslf);
		}
		if(gotHit) {
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				gotHit = false;
			}
		}
		
		if(tsls >= shottime) {
			if(!Controls.isKeyDown(KeyEvent.VK_SPACE)) state = IDLESTATE;
			canshot = true;
			tsls -= shottime;
		}else {
			tsls += tslf;	
		}
		
		ArrayList<Bullet> toRemoveBullet = new ArrayList<Bullet>();
		for (Bullet b : bullets) {
			b.update(tslf);
			
			for (Enemy e : Globals.enemies) {
				if(b.checkCollisionToEnemy(e)) {
					if(b.disabled) continue;
					
					e.getHitByBullet(b);
					if(e.health <= 0 && e.alive) {
						e.dieAnimation = true;
						e.alive = false;
					}
					toRemoveBullet.add(b);
				}
			}
			if(b.dieAnimation == false && b.disabled == true) {
				toRemoveBullet.add(b);
			}
		}
		for (Bullet b : toRemoveBullet) {
			bullets.remove(b);
		}
		for (int i = 0; i < Globals.enemies.size(); i++) {
			Enemy e = Globals.enemies.get(i);
			if(!e.alive && e.dieAnimation == false)Globals.enemies.remove(e);
		}
	}
	
	/**
	 * This function creates a new bullet when the player did a shot an applies recoil to the player
	 * {@link #applyRecoil(float angle)}
	 */
	public void shot() {
		//TODO: Shot mechanics
		float angle = 0;
		if(circleShot) {
			for (int i = 0; i < numBulletsPerShot; i++) {
				angle = 360/numBulletsPerShot * i;
				float cx = (float) (x + size/2 - Bullet.size/2 + Math.sin(Math.toRadians(angle)) * size);
				float cy = (float) (y + size/2 - Bullet.size/2 + Math.cos(Math.toRadians(angle)) * size);
				bullets.add(new Bullet(cx, cy, angle));
				if(numBulletsPerShot == 1)applyRecoil(angle);
			}
		}
		//--------------------------------------------------------------------------------------------------
		else if(tripleMachineGun) {
			if(shotDirection == UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y - Bullet.size, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y - Bullet.size, angle ));
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y - Bullet.size, angle + tripleMachineGunRadius));
			}
			if(shotDirection == DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y + size, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y + size, angle));
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y + size, angle + tripleMachineGunRadius));
			}
			if(shotDirection == LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x - Bullet.size, y + size/2 - Bullet.size/2, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(x - Bullet.size, y + size/2 - Bullet.size/2, angle));
				bullets.add(new Bullet(x - Bullet.size, y + size/2 - Bullet.size/2, angle + tripleMachineGunRadius));
			}
			if(shotDirection == RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x + size, y + size/2 - Bullet.size/2, angle - tripleMachineGunRadius));
				bullets.add(new Bullet(x + size, y + size/2 - Bullet.size/2, angle));
				bullets.add(new Bullet(x + size, y + size/2 - Bullet.size/2, angle + tripleMachineGunRadius));
			}
			applyRecoil(angle);
		}
		//---------------------------------------------------------------------------------------------------
		else {
			if(shotDirection == UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y - Bullet.size, angle));
			}
			if(shotDirection == DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x + size/2 - Bullet.size/2, y + size, angle));
			}
			if(shotDirection == LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x - Bullet.size, y + size/2 - Bullet.size/2, angle));
			}
			if(shotDirection == RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				bullets.add(new Bullet(x + size, y + size/2 - Bullet.size/2, angle));
			}
			applyRecoil(angle);
		}
	}
	
	/**
	 * This function applies recoil to the player
	 * @param angle The angle in which the player should get the recoil
	 */
	public void applyRecoil(float angle) {
		this.x -= (float) Math.sin(Math.toRadians(angle)) * recoil;
		this.y -= (float) Math.cos(Math.toRadians(angle)) * recoil;
	}
	
	public void checkCollisionPlayerToWall() {
		if(this.x < 0) {
			this.x = 0;
		}
		if(this.y < 0) {
			this.y = 0;
		}
		if(this.x + this.size > Globals.width) {
			this.x = Globals.width - this.size;
		}
		if(this.y + this.size > Globals.height) {
			this.y = Globals.height - this.size;
		}
	}
	
	public void checkCollisionPlayerToEnemies() {
		for (Enemy e : Globals.enemies) {
			if(this.x + this.size > e.x && this.x < e.x + e.size && this.y + this.size > e.y && this.y < e.y + e.size) {
				float ecx = e.x + e.size/2;
				float ecy = e.y + e.size/2;
				float pcx = this.x + this.size/2;
				float pcy = this.y + this.size/2;
				
				float distx = pcx - ecx;
				float disty = pcy - ecy;
				
				//TODO: Rework the angle system
				float angle = (float) Math.atan(distx / disty);
				angle = (float) Math.toDegrees(angle);
				if(pcy > ecy) angle =  -90 - (90-angle);
				if(pcx < ecx && pcy < ecy) angle = -270 - (90-angle); 
				
				this.applyKnockback(angle);
				this.gotHit = true;
			}
		}
	}
	
	public void applyKnockback(float angle) {
		//TODO: Remove the - Math.sin
		this.velX = (float) -Math.sin(Math.toRadians(angle)) * knockback;
		this.velY = (float) -Math.cos(Math.toRadians(angle)) * knockback;
	}
}
