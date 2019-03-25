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
	float currentWalkspeed = 0;
	float timeForSpeedUp = 0.65f;
	float timeSpeededUp;
	boolean speedUp = true;
	//Health
	int health = 200;
	boolean alive = true;
	//Got-Hit animation
	boolean isInHitAnimation = false;
	float blink;
	float blinktime = 0.05f;
	float blinkfromStart;
	float maxBlinkTime = 1f;
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
		g.setColor(Color.RED);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		if(isInHitAnimation) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
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

				walkVelocityX =  -Math.sin(Math.toRadians(angle));
				walkVelocityY =  -Math.cos(Math.toRadians(angle));

				if(speedUp) {
					if(currentWalkspeed < maxWalkspeed) {
						timeSpeededUp += tslf;
						currentWalkspeed = maxWalkspeed * (timeSpeededUp / timeForSpeedUp);
					}else {
						timeSpeededUp = 0;
						currentWalkspeed = maxWalkspeed;
						speedUp = false;
					}
				}

				//Collision with stone
				float nextX = (float) (this.x + walkVelocityX * currentWalkspeed * tslf);
				float nextY = (float) (this.y + walkVelocityY * currentWalkspeed * tslf);
				//Left side of stone
				if(walkVelocityX > 0 && walkVelocityX <= 1) {
					for (Stone stone : Map.stones) {
						if(nextY + size > stone.y && nextY < stone.y + stone.height && this.x < stone.x && nextX + size > stone.x) {
							System.out.println("Collision Left Side");
							walkVelocityX = 0;
							this.x = stone.x - size;
						}
					}
				}
				//Right side of stone
				if(walkVelocityX < 0 && walkVelocityX >= -1) {
					for (Stone stone : Map.stones) {
						if(nextY + size > stone.y && nextY < stone.y + stone.height && this.x + size > stone.x + stone.width && nextX < stone.x + stone.width) {
							System.out.println("Collision Right Side");
							walkVelocityX = 0;
							this.x = stone.x + stone.width;
						}	
					}
				}
				//Top side of stone
				if(walkVelocityY > 0 && walkVelocityY <= 1) {
					for (Stone stone : Map.stones) {
						if(nextX + size > stone.x && nextX < stone.x + stone.width && this.y < stone.y && nextY + size > stone.y) {
							System.out.println("Collision Top Side");
							walkVelocityY = 0;
							this.y = stone.y - size;
						}
					}
				}
				//Bottom side of stone
				if(walkVelocityY < 0 && walkVelocityY >= -1) {
					for (Stone stone : Map.stones) {
						if(nextX + size > stone.x && nextX < stone.x + stone.width && this.y + size > stone.y + stone.height && nextY < stone.y + stone.height) {
							System.out.println("Collsion Bottom Side");
							walkVelocityY = 0;
							this.y = stone.y + stone.height;
						}
					}
				}
				//Movement
				x += walkVelocityX * currentWalkspeed * tslf;
				y += walkVelocityY * currentWalkspeed * tslf;
			}
		}

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

		//Got-Hit animation
		if(isInHitAnimation) {
			blink += tslf;
			blinkfromStart += tslf;
			if(blinkfromStart > maxBlinkTime) {
				blinkfromStart = 0;
				isInHitAnimation = false;
			}
		}

		//Die animation
		if(isInDieAnimation) {
			radius += radiusIncrease * tslf;
			if(radius >= maxRadius) {
				isInDieAnimation = false;
			}
		}
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
		currentWalkspeed = 0;
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
