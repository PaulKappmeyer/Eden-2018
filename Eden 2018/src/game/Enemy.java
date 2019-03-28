package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Enemy extends Object{

	//TODO: Revise the variable mess
	//Follow player distance
	int triggerDistance = 300;
	boolean followplayer;
	//Movement
	double walkVelocityX;
	double walkVelocityY;
	int maxWalkspeed = 80;
	float currentWalkSpeed = 0;
	float timeForSpeedUp = 0.65f;
	float timeSpeededUp;
	boolean speedUp = true;
	//Health
	int health = 200;
	boolean alive = true;
	//Got-Hit animation
	boolean isInHitAnimation = false;
	float timeBlinked;
	float swapBlinkTime = 0.05f;
	int blinksDone;
	int maxBlinks = 15;
	boolean showBlink;
	//Die animation
	boolean isInDieAnimation = false;
	float radius = 0;
	float radiusIncrease = 1600;
	float maxRadius = 50;
	//Knockback
	boolean gotKnockbacked;
	float maxKnockback;
	float maxKnockbackTime;
	float knockbackVelocityX;
	float knockbackVelocityY;
	float currentKnockbackSpeed;
	float timeKnockedBack;
	float bulletImpact = 325;
	float bulletImpactTime = 0.1f;

	/**
	 * Constructor; initializes the enemy
	 * @param x The x-position
	 * @param y The y-position
	 */
	public Enemy(float x, float y) {
		this.x = x;
		this.y = y;
		this.size = 16;
		this.maxWalkspeed = maxWalkspeed + -10 + Globals.random.nextInt(20);
	}


	/**
	 * This function draws the enemy as a red rectangle to the screen
	 * @param g The graphics object to draw
	 */
	public void draw(Graphics g) {
		if(!showBlink) {
			g.setColor(Color.RED);
			g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			g.setColor(Color.BLACK);
			g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		}
		if(isInHitAnimation) {
			if(showBlink) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}

	/**
	 * This function updates the enemy; if player is in range follow him
	 * @param tslf
	 */
	public void update(float tslf) {
		if(alive) {
			//Knockback
			updateKnockback(tslf);

			//Check for range to start follow player
			float halfsize = this.size/2;
			float playercenterx = Globals.player.x + halfsize;
			float playercentery = Globals.player.y + halfsize;
			float enemycenterx = this.x + halfsize;
			float enemycentery = this.y + halfsize;
			float distx = enemycenterx - playercenterx;
			float disty = enemycentery - playercentery;
			if(!followplayer) {
				float distanceToPlayer = distx * distx + disty * disty;
				if(distanceToPlayer < triggerDistance * triggerDistance) {
					followplayer = true;
				}
			}
			//Movement follow player
			if(followplayer) {
				float angle = (float) Math.atan(distx / disty);
				angle = (float) Math.toDegrees(angle);
				if(playercentery > enemycentery) angle =  -90 - (90-angle);
				if(playercenterx < enemycenterx && playercentery < enemycentery) angle = -270 - (90-angle);

				walkVelocityX = -Math.sin(Math.toRadians(angle));
				walkVelocityY = -Math.cos(Math.toRadians(angle));

				if(distx == 0 && disty == 0) {
					walkVelocityX = 0;
					walkVelocityY = 0;
				}

				if(speedUp) {
					if(currentWalkSpeed < maxWalkspeed) {
						timeSpeededUp += tslf;
						currentWalkSpeed = maxWalkspeed * (timeSpeededUp / timeForSpeedUp);
					}else {
						timeSpeededUp = 0;
						currentWalkSpeed = maxWalkspeed;
						speedUp = false;
					}
				}

				//Collision with stone
				checkCollisionToStones(tslf);

				//Movement
				this.x += (float) ((walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
				this.y += (float) ((walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);
			}
		}

		//Got-Hit animation
		updateGotHitAnimation(tslf);

		//Die animation
		updateDieAnimation(tslf);
	}

	public void updateKnockback(float tslf) {
		if(gotKnockbacked) {
			if(timeKnockedBack <= maxKnockbackTime) {
				timeKnockedBack += tslf;
				resetWalkspeed();
				currentKnockbackSpeed = maxKnockback * ((maxKnockbackTime - timeKnockedBack) / maxKnockbackTime);
			}else {
				gotKnockbacked = false;
				timeKnockedBack = 0;
				currentKnockbackSpeed = 0;
			}
		}
	}

	public void updateGotHitAnimation(float tslf) {
		if(isInHitAnimation) {
			timeBlinked += tslf;
			if(timeBlinked > swapBlinkTime) {
				showBlink = !showBlink;
				blinksDone ++;
				timeBlinked -= swapBlinkTime;
			}
			if(blinksDone > maxBlinks) {
				timeBlinked = 0;
				blinksDone = 0;
				isInHitAnimation = false;
			}
		}
	}

	public void updateDieAnimation(float tslf) {
		if(isInDieAnimation) {
			radius += radiusIncrease * tslf;
			if(radius >= maxRadius) {
				isInDieAnimation = false;
			}
		}
	}

	/**
	 * 
	 * @param tslf
	 */
	public void checkCollisionToStones(float tslf) {
		float nextX = (float) (this.x + (walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
		float nextY = (float) (this.y + (walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);
		if(nextX == this.x && nextY == this.y) return;
		
		//Top side of stone
		if(this.y < nextY) {
			for (Stone stone : Game.currentMap.stones) {
				if(Collision.isCollidingTopSideOfStone(this.x, this.y, this.size, this.size, this.x, nextY, stone)) {
					walkVelocityY = 0;
					knockbackVelocityY = 0;
					this.y = stone.y - size;
				}
			}
		}
		//Bottom side of stone
		if(this.y > nextY) {
			for (Stone stone : Game.currentMap.stones) {
				if(Collision.isCollidingBottomSideOfStone(this.x, this.y, this.size, this.size, this.x, nextY, stone)) {
					walkVelocityY = 0;
					knockbackVelocityY = 0;
					this.y = stone.y + stone.height;
				}
			}
		}
		//Left side of stone
		if(this.x < nextX) {
			for (Stone stone : Game.currentMap.stones) {
				if(Collision.isCollidingLeftSideOfStone(this.x, this.y, this.size, this.size, nextX, this.y, stone)) {
					walkVelocityX = 0;
					knockbackVelocityX = 0;
					this.x = stone.x - size;
				}
			}
		}
		//Right side of stone
		if(this.x > nextX) {
			for (Stone stone : Game.currentMap.stones) {
				if(Collision.isCollidingRightSideOfStone(this.x, this.y, this.size, this.size, nextX, this.y, stone)) {
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

	/**
	 * 
	 * @param b The bullet which hit the enemy
	 * {@link #applyKnockback(float angle)}
	 */
	public void getHitByProjectile(Projectile p, float damage) {
		startKnockback(p.angle, this.bulletImpact, this.bulletImpactTime);
		getDamaged(damage);
	}
	public void getDamaged(float damage) {
		isInHitAnimation = true;
		health -= damage;
		if(health <= 0 && alive) {
			isInDieAnimation = true;
			alive = false;
		}
		resetWalkspeed();
		if(!followplayer) followplayer = true;
	}

	public void resetWalkspeed() {
		currentWalkSpeed = 0;
		timeSpeededUp = 0;
		speedUp = true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean canBeRemoved() {
		if(alive) return false;
		if(isInDieAnimation) return false;

		return true;
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
	private void calculateKnockbackVelocity(float angle) {
		this.knockbackVelocityX = (float) Math.sin(Math.toRadians(angle));
		this.knockbackVelocityY = (float) Math.cos(Math.toRadians(angle));
	}
}
