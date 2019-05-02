/*
 * 
 */
package enemies;

import java.awt.Color;
import java.awt.Graphics;

import game.Collision;
import game.Globals;
import game.Obstacle;

/**
 * 
 * @author Paul
 *
 */
public class JumpEnemy extends ZombieEnemy{

	float chargeTime = 1.1f;
	float timeCharged = 0;
	boolean startCharge;
	float distance = 120;
	//Jump
	boolean startJump = false;
	boolean isJumping = false;
	float jumpVeloctiyX;
	float jumpVeloctiyY;
	float jumpSpeed = 1000;
	float currentJumpSpeed = 0;
	float timeJumped = 0;
	float jumpTime = 0.2f;

	public JumpEnemy(float x, float y) {
		super(x, y);
		size = 18;
		
		this.MAX_HEALTH = 500;
		this.health = MAX_HEALTH;
		
		bulletImpact = 150;
	}

	int xa;
	int ya;
	@Override
	public void draw(Graphics g) {
		if(!showBlink) {
			if(startCharge) {
				xa = Globals.random.nextInt(5);
				ya = Globals.random.nextInt(5);
				g.translate(xa, ya);
			}
			g.setColor(Color.RED);
			g.fillRoundRect((int)x, (int)y, this.size, this.size, 10, 10);
			g.setColor(Color.BLACK);
			g.drawRoundRect((int)x, (int)y, this.size, this.size, 10, 10);
			
			g.setColor(Color.BLUE);
			switch (lookDirection) {
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
			
			if(startCharge) {
				g.translate(-xa, -ya);
			}
			
			healthbar.draw(g);
		}

		if(isInHitAnimation) {
			if(showBlink) {
				g.setColor(Color.WHITE);
				g.fillRoundRect((int)x, (int)y, this.size, this.size, 10, 10);
				g.drawRoundRect((int)x, (int)y, this.size, this.size, 10, 10);
			}
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2), (int)(y + size/2 - radius/2), (int)radius, (int)radius);
		}
	}

	@Override
	public void update(float tslf) {
		if(alive) {
			//Knockback
			updateKnockback(tslf);

			//Check for range to start follow player
			float playercenterx = Globals.player.x + Globals.player.size/2;
			float playercentery = Globals.player.y + Globals.player.size/2;
			float enemycenterx = this.x + size/2;
			float enemycentery = this.y + size/2;
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

				walkVelocityX = (float) -Math.sin(Math.toRadians(angle));
				walkVelocityY = (float) -Math.cos(Math.toRadians(angle));

				if(distx == 0 && disty == 0) {
					walkVelocityX = 0;
					walkVelocityY = 0;
				}
				
				walkDirection = getDirection(walkVelocityX, walkVelocityY);
				lookDirection = walkDirection;

				//Speed Up
				updateSpeedUp(tslf);

				//Search for player
				if(!startCharge && !startJump && !isJumping && followplayer) {
					float distanceToPlayer = distx * distx + disty * disty;
					if(distanceToPlayer < distance * distance && distanceToPlayer > Globals.player.size * Globals.player.size) {
						startCharge = true;
						currentWalkSpeed = 0;
						isSpeedingUP = false;
					}
				}
				//Charge
				if(startCharge) {
					timeCharged += tslf;
					if(timeCharged >= chargeTime) {
						stopCharge();
						startJump = true;
					}
				}
				//Start Jump
				if(startJump) {
					angle = (float) Math.atan(distx / disty);
					angle = (float) Math.toDegrees(angle);
					if(playercentery > enemycentery) angle =  -90 - (90-angle);
					if(playercenterx < enemycenterx && playercentery < enemycentery) angle = -270 - (90-angle);

					jumpVeloctiyX = (float) -Math.sin(Math.toRadians(angle));
					jumpVeloctiyY = (float) -Math.cos(Math.toRadians(angle));

					isJumping = true;
					startJump = false;
				}
				//Update Jump
				if(isJumping) {
					if(timeJumped <= jumpTime) {
						timeJumped += tslf;
						currentJumpSpeed = jumpSpeed * ((jumpTime - timeJumped) / jumpTime);
					} else {
						isJumping = false;
						resetSpeedUp();
						timeJumped = 0;
						currentJumpSpeed = 0;
					}
				}

				//Collision with obstacles like: stones, houses etc
				checkCollisionToObstacles(tslf);
			}
		}

		//Got-Hit animation
		updateGotHitAnimation(tslf);
		
		//Die animation
		updateDieAnimation(tslf);
	}

	public void stopCharge() {
		timeCharged = 0;
		startCharge = false;
	}
	
	@Override
	public void checkCollisionToObstacles(float tslf) {
		float nextX = (float) (this.x + (walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed + jumpVeloctiyX * currentJumpSpeed) * tslf);
		float nextY = (float) (this.y + (walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed + jumpVeloctiyY * currentJumpSpeed) * tslf);

		if(nextX == this.x && nextY == this.y) return;

		Obstacle[] collisions = Collision.checkCollisionMovingobjToObstacle(this, nextX, nextY);
		
		Obstacle obs = collisions[Collision.TOP_SIDE];
		if(obs != null) {
			walkVelocityY = 0;
			knockbackVelocityY = 0;
			jumpVeloctiyY = 0;
			nextY = obs.y - size;
		}
		obs = collisions[Collision.BOTTOM_SIDE];
		if(obs != null) {
			walkVelocityY = 0;
			knockbackVelocityY = 0;
			jumpVeloctiyY = 0;
			nextY = obs.y + obs.height;
		}
		obs = collisions[Collision.LEFT_SIDE];
		if(obs != null) {
			walkVelocityX = 0;
			knockbackVelocityX = 0;
			jumpVeloctiyX = 0;
			nextX = obs.x - size;
		}
		obs = collisions[Collision.RIGHT_SIDE];
		if(obs != null) {
			walkVelocityX = 0;
			knockbackVelocityX = 0;
			jumpVeloctiyX = 0;
			nextX = obs.x + obs.width;
		}

		this.x = nextX;
		this.y = nextY;
	}
}
