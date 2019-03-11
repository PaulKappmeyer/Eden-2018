package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * This is the class of the player named "Eden"
 * @author Paul Kappmeyer
 * {@link #x} The x-position of the player <br>
 * {@link #y} The y-position of the player <br>
 * {@link #walkSpeed} This is the number of the pixels the player can run per second <br>
 */
public class Eden extends Object{

	//TODO: revise the variable mess
	/**
	 * The walking speed in idle mode (in pixels per seconds)
	 */
	int idleWalkSpeed = 250;
	int shotWalkSpeed = 75;
	int walkSpeed = idleWalkSpeed;
	
	Gun gun;
	
	float knockback = 40f;
	float velX;
	float velY;
	
	public static final int IDLESTATE = 0;
	public static final int SHOOTSTATE = 1;
	int state = 0;
	
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	int direction;
	
	boolean gotHit = false;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 2f;
	
	/**
	 * Constructor; initializes the player
	 */
	public Eden() {
		this.x = 400;
		this.y = 400;
		this.size = 16;
		gun = new Gun(this);
		gun.mode = Gun.ROCKET_SINGLE_FIRE_MODE;
	}
	
	/**
	 * This Function show the player on the screen; gets called every frame
	 * @param g A Graphics Object to draw the player
	 * @see Graphics
	 */
	public void draw(Graphics g) {
		//Drawing of the gun
		gun.draw(g);
		//Drawing of the player
		g.setColor(Color.BLUE);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		
		//Got-Hit-animation
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
	}
	
	/**
	 * This function updates the player; gets called every frame; first checks for input; second updates the movement
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	public void update(float tslf) {
		//Update the gun
		gun.update(tslf);
		
		//Check input
		updateInput(tslf);
		
		//Calculate the walk speed
		if(state == SHOOTSTATE) {
			walkSpeed = shotWalkSpeed;
		}else if(state == IDLESTATE) {
			walkSpeed = idleWalkSpeed;
		}
		
		checkCollisionPlayerToWall();
		
		if(!gotHit)checkCollisionPlayerToEnemies();
		
		if(gotHit) {
			this.x += velX * tslf;
			this.y += velY * tslf;
			// TODO: slow down velX and velY
			//			this.velX *= (0.5 * tslf);
			//			this.velY *= (0.5 * tslf);
			
			//Animation
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				gotHit = false;
			}
		}
		
		//Removal of the enemies
		for (int i = 0; i < Globals.enemies.size(); i++) {
			Enemy e = Globals.enemies.get(i);
			if(e.canBeRemoved())Globals.enemies.remove(e);
		}
	};
	
	/**
	 * This function checks the input, either update the {@link #x} or {@link #y} position or start shooting
	 * @param tslf
	 */
	public void updateInput(float tslf) {
		if(Controls.isKeyDown(KeyEvent.VK_W)) {
			direction = UP;
			y -= walkSpeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_A)) {
			direction = LEFT;
			x -= walkSpeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_S)) {
			direction = DOWN;
			y += walkSpeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_D)) {
			direction = RIGHT;
			x += walkSpeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_SPACE) && !gotHit) {
			if(gun.canShot) {
				if(state == IDLESTATE) shotDirection = direction;
//				if(shotDirection == UP && direction == DOWN) shotDirection = direction;
//				if(shotDirection == DOWN && direction == UP) shotDirection = direction;	
//				if(shotDirection == LEFT && direction == RIGHT) shotDirection = direction;
//				if(shotDirection == RIGHT && direction == LEFT) shotDirection = direction;
				
				state = SHOOTSTATE;
				gun.shot();
			}
		}
		if(state == SHOOTSTATE && !Controls.isKeyDown(KeyEvent.VK_SPACE)) {
			state = Eden.IDLESTATE;
		}
	};
	
	
	/**
	 * This function checks the {@link #x} and {@link #y} position of the player and looks for collision with the bounding of the map
	 */
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
	
	/**
	 * This function checks for a collision with the player and a enemy
	 * if there is a collision calls {@link #applyKnockback(float)}, and sets {@link #gotHit} true
	 */
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
	
	/**
	 * This function applies knock-back to the player for example when he gets hit by an enemy
	 * @param angle The angle in which the player gets knocked back
	 */
	public void applyKnockback(float angle) {
		//TODO: Remove the - Math.sin
		this.velX = (float) -Math.sin(Math.toRadians(angle)) * knockback;
		this.velY = (float) -Math.cos(Math.toRadians(angle)) * knockback;
	}
}
