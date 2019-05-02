/*
 * 
 */
package player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import enemies.Enemy;
import game.Collision;
import game.Direction;
import game.Game;
import game.Globals;
import game.MovingObject;
import game.Obstacle;
import guns.Gun;
import guns.SinglefireGun;
import guns.TripleMachineGun;
import input.Input;
import input.KeyboardinputManager;
import input.MouseinputManager;
import objects.BulletBouncer;

/**
 * This is the class of the player named "Eden"
 * @author Paul Kappmeyer
 * {@link #x} The x-position of the player <br>
 * {@link #y} The y-position of the player <br>
 * {@link #walkSpeed} This is the number of the pixels the player can run per second <br>
 */
public class Eden extends MovingObject{

	//TODO: revise the variable mess

	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int SHOOTING = 2;

	public int state = 0; // the state the player is currently in
	public Gun gun;
	public Direction shotDirection;
	//Got-Hit
	public final float bulletImpact = 250;
	public final float bulletImpactTime = 0.25f;
	//Got-Hit animation
	public boolean gotHit = false;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 1f;
	//Got-Hit enemy
	float enemyImpact = 500;
	/** The walking speed in idle mode (in pixels per seconds) */
	int idleWalkSpeed = 250;
	int shotWalkSpeed = 200;
	int MAX_HEALTH;
	float health;
	//Knockback
	boolean gotKnockbacked;
	float maxKnockback;
	float maxKnockbackTime;
	double knockbackVelocityX;
	double knockbackVelocityY;
	float currentKnockbackSpeed;
	float timeKnockedBack;
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

		walkDirection = Direction.UP;
		shotDirection = Direction.UP;

		this.MAX_HEALTH = 5000;
		this.health = MAX_HEALTH;
	}

	//-----------------------------------------------------------------------------------------------------------------DRAWING
	/**
	 * This Function show the player on the screen; gets called every frame
	 * @param g A Graphics Object to draw the player
	 * @see Graphics
	 */
	int a = 5;
	public void draw(Graphics g) {
		//Drawing of the player
		g.setColor(Color.BLUE);
		g.fillRect((int)x, (int)y, size, size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x, (int)y, size, size);

		g.setColor(Color.RED);
		switch (shotDirection) {
		case UP:
			g.fillOval((int)(this.x + size/2 - a/2), (int)this.y, a, a);
			break;
		case DOWN:
			g.fillOval((int)(this.x + size/2 - a/2), (int)this.y + size - a, a, a);
			break;
		case RIGHT:
			g.fillOval((int)(this.x + size - a), (int)this.y + size/2 - a/2, a, a);
			break;
		case LEFT:
			g.fillOval((int)(this.x), (int)this.y + size/2 - a/2, a, a);
			break;
		default:
			break;
		}

		//Got-Hit-animation
		if(gotHit) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x, (int)y, this.size, this.size);
				g.drawRect((int)x, (int)y, this.size, this.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
			}
		}

		//Shockwave
		g.setColor(Color.BLACK);
		g.drawOval((int)(shockwaveX - currentStunRange/2), (int)(shockwaveY - currentStunRange/2), (int)currentStunRange, (int)currentStunRange);

		//Bombs
		for (Bomb bomb : bombs) {
			bomb.draw(g);
		}
	}

	//-----------------------------------------------------------------------------------------------------------------UPDATIN
	/**
	 * This function updates the player; gets called every frame; first checks for input; second updates the movement
	 * @param tslf The time since the last frame in seconds; 
	 * 	should be multiplied whenever the position of something is changed to get a fluently movement independent from the frames per second
	 */
	public void update(float tslf) {	
		//Update the gun
		if(gun != null) gun.update(tslf);

		//Knockback
		if(gotKnockbacked) {
			if(timeKnockedBack <= maxKnockbackTime) {
				timeKnockedBack += tslf;
				currentKnockbackSpeed = maxKnockback * ((maxKnockbackTime - timeKnockedBack) / maxKnockbackTime);
			}else {
				stopKnockback();
			}
		}

		//Check input
		updateInput(tslf);

		//Calculate the walk speed
		if(state == SHOOTING) {
			if(timeSpeededUp >= TIME_FOR_MAX_SPEED) {
				currentWalkSpeed = shotWalkSpeed;
			}else {
				timeSpeededUp += tslf;
				currentWalkSpeed = shotWalkSpeed * (timeSpeededUp / TIME_FOR_MAX_SPEED);
			}
		}else if(state == WALKING) {
			shotDirection = walkDirection;
			if(timeSpeededUp >= TIME_FOR_MAX_SPEED) {
				currentWalkSpeed = idleWalkSpeed;
			}else {
				timeSpeededUp += tslf;
				currentWalkSpeed = idleWalkSpeed * (timeSpeededUp / TIME_FOR_MAX_SPEED);
			}
		}

		//Collision with objects like: stones, house etc.
		checkCollisionToObstacles(tslf);
		//Collision with the screen
		checkCollisionPlayerToWall();
		//Collision to enemies
		if(!gotHit)checkCollisionPlayerToEnemies();


		//Movement
		this.x += (float) ((walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
		this.y += (float) ((walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);

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

			for (Enemy e : Game.currentMap.enemies) {
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
					e.startKnockback(-e.walkVelocityX, -e.walkVelocityY, e.bulletImpact, e.bulletImpactTime);
				}
			}
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
	}

	//-----------------------------------------------------------------------------------------------------------------INPUT
	/**
	 * This function checks the input, either update the {@link #x} or {@link #y} position or start shooting
	 * @param tslf
	 */
	public void updateInput(float tslf) {
		//Up
		if(Input.isUpKeyDown() && !(Input.isLeftKeyDown() || Input.isRightKeyDown())) {
			if(state == IDLE) state = WALKING;
			walkDirection = Direction.UP;
			walkVelocityX = 0;
			walkVelocityY = -1;
		}
		if(Input.isUpKeyDown() && Input.isLeftKeyDown()) {
			if(state == IDLE) state = WALKING;
			walkVelocityX = Math.sin(Math.PI * 5/4);
			walkVelocityY = Math.cos(Math.PI * 5/4);
		}
		if(Input.isUpKeyDown() && Input.isRightKeyDown()) {
			if(state == IDLE) state = WALKING;
			walkVelocityX = Math.sin(Math.PI * 3/4);
			walkVelocityY = Math.cos(Math.PI * 3/4);
		}
		//Down
		if(Input.isDownKeyDown() && !(Input.isLeftKeyDown() || Input.isRightKeyDown())){
			if(state == IDLE) state = WALKING;
			walkDirection = Direction.DOWN;
			walkVelocityX = 0;
			walkVelocityY = 1;
		}
		if(Input.isDownKeyDown() && Input.isLeftKeyDown()){
			if(state == IDLE) state = WALKING;
			walkVelocityX = Math.sin(Math.PI * 7/4);
			walkVelocityY = Math.cos(Math.PI * 7/4);
		}
		if(Input.isDownKeyDown() && Input.isRightKeyDown()){
			if(state == IDLE) state = WALKING;
			walkVelocityX = Math.sin(Math.PI * 1/4);
			walkVelocityY = Math.cos(Math.PI * 1/4);
		}
		//Left
		if(Input.isLeftKeyDown() && !(Input.isUpKeyDown() || Input.isDownKeyDown())) {
			if(state == IDLE) state = WALKING;
			walkDirection = Direction.LEFT ;
			walkVelocityX = -1;
			walkVelocityY = 0;
		}
		//Right
		if(Input.isRightKeyDown() && !(Input.isUpKeyDown() || Input.isDownKeyDown())) {
			if(state == IDLE) state = WALKING;
			walkDirection = Direction.RIGHT;
			walkVelocityX = 1;
			walkVelocityY = 0;
		}

		//Reset Walking
		if(state != IDLE && !(Input.isUpKeyDown() || Input.isDownKeyDown() || Input.isLeftKeyDown()|| Input.isRightKeyDown())) {
			resetSpeedUp();
			state = IDLE;
		}

		//Switch Weapon
		if(KeyboardinputManager.isKeyDown(KeyEvent.VK_1)) {
			this.gun = new SinglefireGun(this);
		}
		else if(KeyboardinputManager.isKeyDown(KeyEvent.VK_2)) {
			this.gun = new TripleMachineGun(this);
		}


		//Shooting
		if(Input.isShootingKeyDown() && !gotHit && gun != null) {
			if(gun.canShot) {
				if(state == IDLE || state == WALKING) shotDirection = walkDirection;

				state = SHOOTING;
				//TODO: Rework the angle system
				float mcx = MouseinputManager.getMouseX() + -Game.currentMap.worldX;
				float mcy = MouseinputManager.getMouseY() + -Game.currentMap.worldY;
				float pcx = this.x + this.size/2;
				float pcy = this.y + this.size/2;

				float distx = pcx - mcx;
				float disty = pcy - mcy;
				float angle = (float) Math.atan(distx / disty);
				angle = (float) Math.toDegrees(angle);
				if(pcy > mcy) angle =  -90 - (90-angle);
				if(pcx < mcx && pcy < mcy) angle = -270 - (90-angle); 
				float sin = (float) Math.sin(Math.toRadians(angle));
				float cos = (float) Math.cos(Math.toRadians(angle));

				if(cos >= -1 && cos <= -0.45) {
					shotDirection = Direction.UP;
				}
				if(cos >= 0.45 && cos <= 1) {
					shotDirection = Direction.DOWN;
				}
				if(sin >= -1 && sin <= -0.45) {
					shotDirection = Direction.LEFT;
				}
				if(sin >= 0.45 && sin <= 1) {
					shotDirection = Direction.RIGHT;
				}

				gun.shot(angle);
				//				resetWalking();
			}
		}
		if(state == SHOOTING && !Input.isShootingKeyDown()) {
			state = Eden.IDLE;
		}

		//Shockwave
		if(Input.isSpecialKey1Down() && shockwave == false) {
			shockwave = true;
			shockwaveX = this.x + size/2;
			shockwaveY = this.y + size/2;
			currentStunRange = size;
		}

		//Bombs
		if(Input.isSpecialKey2Down()) {
			if(gun.canShot) bombs.add(new Bomb(this.x, this.y));
			gun.canShot = false;
		}
	}

	//-----------------------------------------------------------------------------------------------------------------COLLISION
	public void checkCollisionToObstacles(float tslf) {
		float nextX = (float) (this.x + (walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
		float nextY = (float) (this.y + (walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);
		if(nextX == this.x && nextY == this.y) return;

		Obstacle[] collisions = Collision.checkCollisionMovingobjToObstacle(this, nextX, nextY);

		//COLLISION WITH THE TOP SIDE OF THE OBSTACLE
		Obstacle obs = collisions[Collision.TOP_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				BulletBouncer b = ((BulletBouncer) obs);
				startKnockback(0, -1, b.power, b.time);
				this.resetSpeedUp();
				this.y = obs.y - size;	
			}else {
				walkVelocityY = 0;
				knockbackVelocityY = 0;
				this.y = obs.y - size;	
			}
		}
		//COLLISION WIDTH THE BOTTOM SIDE OF THE OBSTACLE
		obs = collisions[Collision.BOTTOM_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				BulletBouncer b = ((BulletBouncer) obs);
				startKnockback(0, 1, b.power, b.time);
				this.resetSpeedUp();
				this.y = obs.y + obs.height;
			}else {
				walkVelocityY = 0;
				knockbackVelocityY = 0;
				this.y = obs.y + obs.height;
			}
		}
		//COLLISION WITH THE LEFT SIDE OF THE OBSTACLE
		obs = collisions[Collision.LEFT_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				BulletBouncer b = ((BulletBouncer) obs);
				startKnockback(-1, 0, b.power, b.time);
				this.resetSpeedUp();
				this.x = obs.x - size;
			}else {
				walkVelocityX = 0;
				knockbackVelocityX = 0;
				this.x = obs.x - size;
			}
		}
		//COLLISION WITH THE RIGHT SIDE OF THE OBSTACLE
		obs = collisions[Collision.RIGHT_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				BulletBouncer b = ((BulletBouncer) obs);
				startKnockback(1, 0, b.power, b.time);
				this.resetSpeedUp();
				this.x = obs.x + obs.width;
			}else {
				walkVelocityX = 0;
				knockbackVelocityX = 0;
				this.x = obs.x + obs.width;
			}
		}
	}

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
		if(this.x + this.size > Game.currentMap.mapWidth) {
			this.x = Game.currentMap.mapWidth - this.size;
		}
		if(this.y + this.size > Game.currentMap.mapHeight) {
			this.y = Game.currentMap.mapHeight - this.size;
		}
	}

	/**
	 * This function checks for a collision with the player and a enemy
	 * if there is a collision calls {@link #applyKnockback(float)}, and sets {@link #gotHit} true
	 */
	public void checkCollisionPlayerToEnemies() {
		for (Enemy e : Game.currentMap.enemies) {
			if(!e.alive) continue;
			if(this.x + this.size > e.x && this.x < e.x + e.size && this.y + this.size > e.y && this.y < e.y + e.size) {
				e.resetSpeedUp();
				this.startKnockback(e.walkVelocityX, e.walkVelocityY, enemyImpact, 0.2f);
				this.resetSpeedUp();
				this.gotHit = true;
				return;
			}
		}
	}

	//-----------------------------------------------------------------------------------------------------------------KNOCKBACK
	public void startKnockback(double knockbackVelocityX, double knockbackVelocityY, float ammount, float time) {
		double length = (knockbackVelocityX * knockbackVelocityX + knockbackVelocityY * knockbackVelocityY);
		if(Math.round(length) != 1) System.err.println("Eden.startKnockback: Directions vectors should have a length of 1 insead of: " + length + " x:" + knockbackVelocityX + " y:" + knockbackVelocityY);

		gotKnockbacked = true;
		this.knockbackVelocityX = knockbackVelocityX;
		this.knockbackVelocityY = knockbackVelocityY;
		maxKnockback = ammount;
		maxKnockbackTime = time;
	}

	public void stopKnockback() {
		knockbackVelocityX = 0;
		knockbackVelocityY = 0;
		gotKnockbacked = false;
		timeKnockedBack = 0;
		currentKnockbackSpeed = 0;
	}
}
