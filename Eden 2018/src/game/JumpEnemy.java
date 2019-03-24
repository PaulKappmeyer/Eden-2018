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
	float jumpVelX;
	float jumpVelY;
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
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillRoundRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size, 10, 10);
		g.setColor(Color.BLACK);
		g.drawRoundRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size, 10, 10);
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
	
	@Override
	public void update(float tslf) {
		super.update(tslf);
		
		//Check for range to start follow player
		float halfsize = this.size/2;
		float playercenterx = Globals.player.x + halfsize;
		float playercentery = Globals.player.y + halfsize;
		float enemycenterx = this.x + halfsize;
		float enemycentery = this.y + halfsize;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;
		if(!startCharge && !startJump && !isJumping && followplayer) {
			float distanceToPlayer = distx * distx + disty * disty;
			if(distanceToPlayer < distance * distance) {
				startCharge = true;
				currentWalkspeed = 0;
				speedUp = false;
			}
		}
		
		if(startCharge) {
			x += (-a/2 + Globals.random.nextInt(a)) * tslf;
			y += (-a/2 + Globals.random.nextInt(a)) * tslf;
			
			timeCharged += tslf;
			if(timeCharged >= chargeTime) {
				timeCharged = 0;
				startCharge = false;
				startJump = true;
			}
		}
		
		if(startJump) {
			float angle = (float) Math.atan(distx / disty);
			angle = (float) Math.toDegrees(angle);
			if(playercentery > enemycentery) angle =  -90 - (90-angle);
			if(playercenterx < enemycenterx && playercentery < enemycentery) angle = -270 - (90-angle);

			jumpVelX = (float) -Math.sin(Math.toRadians(angle));
			jumpVelY = (float) -Math.cos(Math.toRadians(angle));
			
			isJumping = true;
			startJump = false;
		}
		
		if(isJumping) {
			if(timeJumped <= jumpTime) {
				timeJumped += tslf;
				currentJumpSpeed = jumpSpeed * ((jumpTime - timeJumped) / jumpTime);
				this.x += jumpVelX * currentJumpSpeed * tslf;
				this.y += jumpVelY * currentJumpSpeed * tslf;
			}else {
				isJumping = false;
				resetWalkspeed();
				timeJumped = 0;
				currentJumpSpeed = 0;
			}
		}
	}
	
}
