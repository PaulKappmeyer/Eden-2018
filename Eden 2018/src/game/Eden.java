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

	float x,y;
	int idlewalkspeed = 200;
	int shotwalkspeed = 40;
	int walkspeed = idlewalkspeed;
	float shottime = 0.125f;
	float tsls = shottime;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	int bulletspray = 7;
	float knockback = 0.75f;
	
	final int IDLESTATE = 0;
	final int SHOOTSTATE = 1;
	int state = 0;
	
	final int UP = 0;
	final int DOWN = 1;
	final int LEFT = 2;
	final int RIGHT = 3;
	int direction = 0;
	
	int size = 16;
	
	boolean canshot;
	
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
		if(Controls.isKeyDown(KeyEvent.VK_SPACE)) {
			if(canshot) {
				state = SHOOTSTATE;
				canshot = false;
				shot();
			}
		}
		
		checkCollisionPlayerToWall();
		
		if(tsls >= shottime) {
			state = IDLESTATE;
			canshot = true;
			tsls = 0;
		}else {
			tsls += tslf;	
		}
		
		ArrayList<Bullet> toRemoveBullet = new ArrayList<Bullet>();
		for (Bullet b : bullets) {
			b.update(tslf);
			
			for (Enemy e : Globals.enemies) {
				if(b.checkCollisionToEnemy(e, e.size)) {
					e.getHitByBullet(b);
					if(e.health <= 0 && e.alive) {
						e.dieAnimation = true;
						e.alive = false;
					}
					toRemoveBullet.add(b);
				}
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
		float angle = 0;
		if(direction == UP) {
			angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			bullets.add(new Bullet(x + size/2 - Bullet.size/2, y - Bullet.size, angle));
		}
		if(direction == DOWN) {
			angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			bullets.add(new Bullet(x + size/2 - Bullet.size/2, y + size, angle));
		}
		if(direction == LEFT){
			angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			bullets.add(new Bullet(x - Bullet.size, y + size/2 - Bullet.size/2, angle));
		}
		if(direction == RIGHT) {
			angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			bullets.add(new Bullet(x + size, y + size/2 - Bullet.size/2, angle));
		}
		applyRecoil(angle);
	}
	
	/**
	 * This function applies recoil to the player
	 * @param angle The angle in which the player should get the recoil
	 */
	public void applyRecoil(float angle) {
		this.x -= (float) Math.sin(Math.toRadians(angle)) * knockback;
		this.y -= (float) Math.cos(Math.toRadians(angle)) * knockback;
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
}
