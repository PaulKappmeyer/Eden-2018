package game;

import java.awt.Color;
import java.awt.Graphics;

public class JumpEnemy extends Enemy{

	float chargeTime = 1.1f;
	float timeCharged = 0;
	boolean startCharge;
	int a = 50;
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
		health = 500;
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
			g.fillRoundRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size, 10, 10);
			g.setColor(Color.BLACK);
			g.drawRoundRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size, 10, 10);
			if(startCharge) {
				g.translate(-xa, -ya);
			}
		}

		if(isInHitAnimation) {
			if(showBlink) {
				g.setColor(Color.WHITE);
				g.fillRoundRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size, 10, 10);
				g.drawRoundRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size, 10, 10);
			}
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}

	@Override
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

				walkVelocityX = (float) -Math.sin(Math.toRadians(angle));
				walkVelocityY = (float) -Math.cos(Math.toRadians(angle));

				if(distx == 0 && disty == 0) {
					walkVelocityX = 0;
					walkVelocityY = 0;
				}

				//Speed Up
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

				//Search for player
				if(!startCharge && !startJump && !isJumping && followplayer) {
					float distanceToPlayer = distx * distx + disty * disty;
					if(distanceToPlayer < distance * distance && distanceToPlayer > Globals.player.size * Globals.player.size) {
						startCharge = true;
						currentWalkSpeed = 0;
						speedUp = false;
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
					}else {
						isJumping = false;
						resetWalkspeed();
						timeJumped = 0;
						currentJumpSpeed = 0;
					}
				}

				//Collision with stone
				checkCollisionToStones(tslf);
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
	public void checkCollisionToStones(float tslf) {
		float nextX = (float) (this.x + (walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed + jumpVeloctiyX * currentJumpSpeed) * tslf);
		float nextY = (float) (this.y + (walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed + jumpVeloctiyY * currentJumpSpeed) * tslf);
		if(nextX == this.x && nextY == this.y) return;

		//Top side of stone
		if(this.y < nextY) {
			for (Stone stone : Map.stones) {
				if(isCollidingTopSideOfStone(this.x, nextY, stone)) {  
					walkVelocityY = 0;
					knockbackVelocityY = 0;
					jumpVeloctiyY = 0;
					nextY = stone.y - size;
				}
			}
		}
		//Bottom side of stone
		if(this.y > nextY) {
			for (Stone stone : Map.stones) {
				if(isCollidingBottomSideOfStone(this.x, nextY, stone)) {
					walkVelocityY = 0;
					knockbackVelocityY = 0;
					jumpVeloctiyY = 0;
					nextY = stone.y + stone.height;
				}
			}
		}
		//Left side of stone
		if(this.x < nextX) {
			for (Stone stone : Map.stones) {
				if(isCollidingLeftSideOfStone(nextX, this.y, stone)) {
					walkVelocityX = 0;
					knockbackVelocityX = 0;
					jumpVeloctiyX = 0;
					nextX = stone.x - size;
				}
			}
		}
		//Right side of stone
		if(this.x > nextX) {
			for (Stone stone : Map.stones) {
				if(isCollidingRightSideOfStone(nextX, this.y, stone)) {
					walkVelocityX = 0;
					knockbackVelocityX = 0;
					jumpVeloctiyX = 0;
					nextX = stone.x + stone.width;
				}	
			}
		}

		this.x = nextX;
		this.y = nextY;
	}
}
