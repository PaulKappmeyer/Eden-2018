package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
	float currentWalkSpeed;

	double walkVelocityX;
	double walkVelocityY;

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
	float maxKnockbackTime;
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

	//Bombs
	ArrayList<Bomb> bombs = new ArrayList<>();

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

		//Bombs
		for (Bomb bomb : bombs) {
			bomb.draw(g);
		}
	}

	/**
	 * This function updates the player; gets called every frame; first checks for input; second updates the movement
	 * @param tslf The time since the last frame in seconds; should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	public void update(float tslf) {
		//Update the gun
		if(gun != null) gun.update(tslf);

		//Knockback
		if(gotKnockbacked) {
			if(timeKnockedBack <= maxKnockbackTime) {
				timeKnockedBack += tslf;
				currentKnockbackSpeed = maxKnockback * ((maxKnockbackTime - timeKnockedBack) / maxKnockbackTime);
				resetWalking();
			}else {
				stopKnockback();
			}
		}

		//Check input
		updateInput(tslf);

		//Calculate the walk speed
		if(state == SHOOTING) {
			currentWalkSpeed = shotWalkSpeed;
		}else if(state == WALKING) {
			if(timeSpeededUp >= timeForSpeedUp) {
				currentWalkSpeed = idleWalkSpeed;
			}else {
				timeSpeededUp += tslf;
				currentWalkSpeed = idleWalkSpeed * (timeSpeededUp / timeForSpeedUp);
			}
		}

		//Collision with stone
		checkCollisionToStones(tslf);

		//Movement
		this.x += (float) ((walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
		this.y += (float) ((walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);

		//Collision
		checkCollisionPlayerToWall();
		//Collision to enemies
		if(!gotHit)checkCollisionPlayerToEnemies();

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

					//e.getDamaged(50);
					e.startKnockback(newAngle, e.bulletImpact, e.bulletImpactTime);
				}
			}
		}

		//Removal of the enemies
		for (int i = 0; i < Globals.enemies.size(); i++) {
			Enemy e = Globals.enemies.get(i);
			if(e.canBeRemoved())Globals.enemies.remove(e);
		}
		//Bombs
		for (Bomb bomb : bombs) {
			bomb.update(tslf);
		}
		//Removal of the bombs
		for (int i = 0; i < bombs.size(); i++) {
			Bomb b = bombs.get(i);
			if(!b.ticking && !b.explode) bombs.remove(b);
		}
	};

	public void checkCollisionToStones(float tslf) {
		float nextX = (float) (this.x + (walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
		float nextY = (float) (this.y + (walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);
		if(nextX == this.x && nextY == this.y) return;
		
		//Top side of stone
		if(this.y < nextY) {
			for (Stone stone : Map.stones) {
				if(isCollidingTopSideOfStone(this.x, nextY, stone)) {  
					walkVelocityY = 0;
					knockbackVelocityY = 0;
					this.y = stone.y - size;
				}
			}
		}
		//Bottom side of stone
		if(this.y > nextY) {
			for (Stone stone : Map.stones) {
				if(isCollidingBottomSideOfStone(this.x, nextY, stone)) {
					walkVelocityY = 0;
					knockbackVelocityY = 0;
					this.y = stone.y + stone.height;
				}
			}
		}
		//Left side of stone
		if(this.x < nextX) {
			for (Stone stone : Map.stones) {
				if(isCollidingLeftSideOfStone(nextX, this.y, stone)) {
					walkVelocityX = 0;
					knockbackVelocityX = 0;
					stopKnockback();
					this.x = stone.x - size;
				}
			}
		}
		//Right side of stone
		if(this.x > nextX) {
			for (Stone stone : Map.stones) {
				if(isCollidingRightSideOfStone(nextX, this.y, stone)) {
					walkVelocityX = 0;
					knockbackVelocityX = 0;
					this.x = stone.x + stone.width;
				}	
			}
		}
	}
	
	public boolean isCollidingTopSideOfStone(float nextX, float nextY, Stone stone) {
		if(nextX + size > stone.x && nextX < stone.x + stone.width && this.y < stone.y && nextY + size > stone.y) {
			return true;
		}
		return false;
	}
	public boolean isCollidingBottomSideOfStone(float nextX, float nextY, Stone stone) {
		if(nextX + size > stone.x && nextX < stone.x + stone.width && this.y + size > stone.y + stone.height && nextY < stone.y + stone.height) {
			return true;
		}
		return false;
	}
	public boolean isCollidingLeftSideOfStone(float nextX, float nextY, Stone stone) {
		if(nextY + size > stone.y && nextY < stone.y + stone.height && this.x < stone.x && nextX + size > stone.x) {
			return true;
		}
		return false;
	}
	public boolean isCollidingRightSideOfStone(float nextX, float nextY, Stone stone) {
		if(nextY + size > stone.y && nextY < stone.y + stone.height && this.x + size > stone.x + stone.width && nextX < stone.x + stone.width) {
			return true;
		}
		return false;
	}

	public void stopKnockback() {
		knockbackVelocityX = 0;
		knockbackVelocityY = 0;
		gotKnockbacked = false;
		timeKnockedBack = 0;
		currentKnockbackSpeed = 0;
	}

	/**
	 * This function checks the input, either update the {@link #x} or {@link #y} position or start shooting
	 * @param tslf
	 */
	public void updateInput(float tslf) {
		//Up
		if(isUpKeyDown() && !(isLeftKeyDown() || isRightKeyDown())) {
			if(state == IDLE) state = WALKING;
			direction = UP;
			walkVelocityX = 0;
			walkVelocityY = -1;
		}
		if(isUpKeyDown() && isLeftKeyDown()) {
			if(state == IDLE) state = WALKING;
			direction = UP;
			walkVelocityX = Math.sin(Math.PI * 5/4);
			walkVelocityY = Math.cos(Math.PI * 5/4);
		}
		if(isUpKeyDown() && isRightKeyDown()) {
			if(state == IDLE) state = WALKING;
			direction = UP;
			walkVelocityX = Math.sin(Math.PI * 3/4);
			walkVelocityY = Math.cos(Math.PI * 3/4);
		}
		//Down
		if(isDownKeyDown() && !(isLeftKeyDown() || isRightKeyDown())){
			if(state == IDLE) state = WALKING;
			direction = DOWN;
			walkVelocityX = 0;
			walkVelocityY = 1;
		}
		if(isDownKeyDown() && isLeftKeyDown()){
			if(state == IDLE) state = WALKING;
			direction = DOWN;
			walkVelocityX = Math.sin(Math.PI * 7/4);
			walkVelocityY = Math.cos(Math.PI * 7/4);
		}
		if(isDownKeyDown() && isRightKeyDown()){
			if(state == IDLE) state = WALKING;
			direction = DOWN;
			walkVelocityX = Math.sin(Math.PI * 1/4);
			walkVelocityY = Math.cos(Math.PI * 1/4);
		}
		//Left
		if(isLeftKeyDown() && !(isUpKeyDown() || isDownKeyDown())) {
			if(state == IDLE) state = WALKING;
			direction = LEFT ;
			walkVelocityX = -1;
			walkVelocityY = 0;
		}
		//Right
		if(isRightKeyDown() && !(isUpKeyDown() || isDownKeyDown())) {
			if(state == IDLE) state = WALKING;
			direction = RIGHT;
			walkVelocityX = 1;
			walkVelocityY = 0;
		}

		//Reset Walking
		if(state == WALKING && !(isUpKeyDown() || isDownKeyDown() || isLeftKeyDown()|| isRightKeyDown())) {
			resetWalking();
			state = IDLE;
		}

		//Shooting
		if(Controls.isKeyDown(KeyEvent.VK_SPACE) && !gotHit && gun != null) {
			if(gun.canShot) {
				if(state == IDLE || state == WALKING) shotDirection = direction;

				state = SHOOTING;
				gun.shot();
				resetWalking();
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

		//Bombs
		if(Controls.isKeyDown(KeyEvent.VK_Q)) {
			if(gun.canShot) bombs.add(new Bomb(this.x, this.y));
			gun.canShot = false;
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

				if(distx == 0 && disty == 0) angle = 0;
				
				this.startKnockback(angle - 180, enemyImpact, 0.2f);
				this.gotHit = true;
				return;
			}
		}
	}

	/**
	 * 
	 * @param angle
	 * @param ammount
	 */
	public void startKnockback(float angle, float ammount, float time) {
		if(gotKnockbacked) return;
		gotKnockbacked = true;
		calculateKnockbackVelocity(angle);
		maxKnockback = ammount;
		maxKnockbackTime = time;
	}

	/**
	 * This function applies knock-back to the player for example when he gets hit by an enemy
	 * @param angle The angle in which the player gets knocked back
	 */
	private void calculateKnockbackVelocity(float angle) {
		this.knockbackVelocityX = (float) Math.sin(Math.toRadians(angle));
		this.knockbackVelocityY = (float) Math.cos(Math.toRadians(angle));
	}

	/**
	 * 
	 */
	public void resetWalking() {
		walkVelocityX = 0;
		walkVelocityY = 0;
		currentWalkSpeed = 0;
		timeSpeededUp = 0;
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
