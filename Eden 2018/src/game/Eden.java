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
	float walkSpeed;

	float walkVelocityX;
	float walkVeloctiyY;

	final float timeForSpeedUp = 0.125f; // time for full idleWalkSpeed in seconds
	float timeSpeededUp;

	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int SHOOTING = 2;
	int state = 0; // the state the player is currently in

	Gun gun;

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	int direction;

	//Knockback
	boolean gotKnockbacked;
	float maxKnockback;
	float maxKnockbackTime = 0.2f;
	float knockbackVelocityX;
	float knockbackVelocityY;
	float currentKnockbackSpeed;
	float timeKnockedBack;
	final float enemyImpact = 500;
	final float bulletImpact = 250;
	
	//Got-Hit animation
	boolean gotHit = false;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 1f;

	//Shockwave
	boolean shockwave = false;
	float shockwaveX;
	float shockwaveY;
	float maxStunRange = 600;
	float stunRageIncrease = 1000;
	float currentStunRange;

	/**
	 * Constructor; initializes the player
	 */
	public Eden() {
		this.x = 400;
		this.y = 400;
		this.size = 16;
		this.gun = new SinglefireGun(this);
	}

	/**
	 * This Function show the player on the screen; gets called every frame
	 * @param g A Graphics Object to draw the player
	 * @see Graphics
	 */
	public void draw(Graphics g) {
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

		//Shockwave
		g.setColor(Color.BLACK);
		g.drawOval((int)(shockwaveX - currentStunRange/2 + Globals.insetX), (int)(shockwaveY - currentStunRange/2 + Globals.insetY), (int)currentStunRange, (int)currentStunRange);
	}

	/**
	 * This function updates the player; gets called every frame; first checks for input; second updates the movement
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	public void update(float tslf) {
		//Update the gun
		if(gun != null) gun.update(tslf);

		//Check input
		updateInput(tslf);

		//Calculate the walk speed
		if(state == SHOOTING) {
			walkSpeed = shotWalkSpeed;
		}else if(state == WALKING) {
			if(timeSpeededUp >= timeForSpeedUp) {
				walkSpeed = idleWalkSpeed;
			}else {
				timeSpeededUp += tslf;
				walkSpeed = idleWalkSpeed * (timeSpeededUp / timeForSpeedUp);
			}
		}

		checkCollisionPlayerToWall();

		if(!gotHit)checkCollisionPlayerToEnemies();

		//Knockback
		if(gotKnockbacked) {
			if(timeKnockedBack <= maxKnockbackTime) {
				timeKnockedBack += tslf;
				currentKnockbackSpeed = maxKnockback * ((maxKnockbackTime - timeKnockedBack) / maxKnockbackTime);
				this.x += knockbackVelocityX * currentKnockbackSpeed * tslf;
				this.y += knockbackVelocityY * currentKnockbackSpeed * tslf;
			}else {
				gotKnockbacked = false;
				timeKnockedBack = 0;
				currentKnockbackSpeed = 0;
			}
		}
		
		//Got-Hit-animation
		if(gotHit) {
			//Animation
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				gotHit = false;
			}
		}

		//Shockwave
		if(shockwave) {
			currentStunRange += stunRageIncrease * tslf;
			if(currentStunRange >= maxStunRange) {
				shockwave = false;
				currentStunRange = 0;
			}

			for (Enemy e : Globals.enemies) {
				if(Globals.checkCollisionRectangleToCircle(shockwaveX-size/2 - currentStunRange/2, shockwaveY-size/2 - currentStunRange/2, currentStunRange, e.x, e.y, e.size, e.size)) {
					float ecx = e.x + e.size/2;
					float ecy = e.y + e.size/2;

					float distx = shockwaveX - ecx;
					float disty = shockwaveY - ecy;

					//TODO: Rework the angle system
					float newAngle = (float) Math.atan(distx / disty);
					newAngle = (float) Math.toDegrees(newAngle);
					if(shockwaveY > ecy) newAngle =  -90 - (90-newAngle);
					if(shockwaveX < ecx && shockwaveY < ecy) newAngle = -270 - (90-newAngle);

					e.getDamaged(50);
					e.startKnockback(newAngle, e.bulletImpact, e.bulletImpactTime);
				}
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
		//Up
		if(isUpKeyDown() && !(isLeftKeyDown() || isRightKeyDown())) {
			if(state == IDLE) state = WALKING;
			direction = UP;
			x += Math.sin(Math.toRadians(180)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(180)) * walkSpeed * tslf;
		}
		if(isUpKeyDown() && isLeftKeyDown()) {
			if(state == IDLE) state = WALKING;
			direction = UP;
			x += Math.sin(Math.toRadians(180 + 45)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(180 + 45)) * walkSpeed * tslf;
		}
		if(isUpKeyDown() && isRightKeyDown()) {
			if(state == IDLE) state = WALKING;
			direction = UP;
			x += Math.sin(Math.toRadians(180 - 45)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(180 - 45)) * walkSpeed * tslf;
		}
		//Down
		if(isDownKeyDown() && !(isLeftKeyDown() || isRightKeyDown())){
			if(state == IDLE) state = WALKING;
			direction = DOWN;
			x += Math.sin(Math.toRadians(0)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(0)) * walkSpeed * tslf;
		}
		if(isDownKeyDown() && isLeftKeyDown()){
			if(state == IDLE) state = WALKING;
			direction = DOWN;
			x += Math.sin(Math.toRadians(0 - 45)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(0 - 45)) * walkSpeed * tslf;
		}
		if(isDownKeyDown() && isRightKeyDown()){
			if(state == IDLE) state = WALKING;
			direction = DOWN;
			x += Math.sin(Math.toRadians(0 + 45)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(0 + 45)) * walkSpeed * tslf;
		}
		//Left
		if(isLeftKeyDown() && !(isUpKeyDown() || isDownKeyDown())) {
			if(state == IDLE) state = WALKING;
			direction = LEFT ;
			x += Math.sin(Math.toRadians(270)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(270)) * walkSpeed * tslf;
		}
		//Right
		if(isRightKeyDown() && !(isUpKeyDown() || isDownKeyDown())) {
			if(state == IDLE) state = WALKING;
			direction = RIGHT;
			x += Math.sin(Math.toRadians(90)) * walkSpeed * tslf;
			y += Math.cos(Math.toRadians(90)) * walkSpeed * tslf;
		}

		//Reset Walking
		if(state == WALKING && !(isUpKeyDown() || isDownKeyDown() || isLeftKeyDown()|| isRightKeyDown())) {
			timeSpeededUp = 0;
			state = IDLE;
		}

		if(Controls.isKeyDown(KeyEvent.VK_SPACE) && !gotHit && gun != null) {
			if(gun.canShot) {
				if(state == IDLE || state == WALKING) shotDirection = direction;
				//				if(shotDirection == UP && direction == DOWN) shotDirection = direction;
				//				if(shotDirection == DOWN && direction == UP) shotDirection = direction;	
				//				if(shotDirection == LEFT && direction == RIGHT) shotDirection = direction;
				//				if(shotDirection == RIGHT && direction == LEFT) shotDirection = direction;

				state = SHOOTING;
				gun.shot();
			}
		}
		if(state == SHOOTING && !Controls.isKeyDown(KeyEvent.VK_SPACE)) {
			state = Eden.IDLE;
		}

		//Shockwave
		if(Controls.isKeyDown(KeyEvent.VK_R) && shockwave == false) {
			shockwave = true;
			shockwaveX = this.x + size/2;
			shockwaveY = this.y + size/2;
			currentStunRange = size;
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
			if(!e.alive) continue;
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
				
				e.resetWalkspeed();
				
				this.startKnockback(angle - 180, enemyImpact);
				this.gotHit = true;
			}
		}
	}

	/**
	 * 
	 * @param angle
	 * @param ammount
	 */
	public void startKnockback(float angle, float ammount) {
		if(gotKnockbacked) return;
		gotKnockbacked = true;
		calculateKnockbackVelocity(angle);
		maxKnockback = ammount;
	}
	
	/**
	 * This function applies knock-back to the player for example when he gets hit by an enemy
	 * @param angle The angle in which the player gets knocked back
	 */
	private void calculateKnockbackVelocity(float angle) {
		this.knockbackVelocityX = (float) Math.sin(Math.toRadians(angle));
		this.knockbackVelocityY = (float) Math.cos(Math.toRadians(angle));
	}

	//Key inputs
	public boolean isUpKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_W) || Controls.isKeyDown(KeyEvent.VK_UP);
	}
	public boolean isDownKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_S) || Controls.isKeyDown(KeyEvent.VK_DOWN);
	}
	public boolean isLeftKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_A) || Controls.isKeyDown(KeyEvent.VK_LEFT);
	}
	public boolean isRightKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_D) || Controls.isKeyDown(KeyEvent.VK_RIGHT);
	}
}
