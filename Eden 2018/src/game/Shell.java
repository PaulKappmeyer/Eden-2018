package game;

import java.awt.Color;
import java.awt.Graphics;

public class Shell {

	float x;
	float y;
	float velocityX;
	float velocityY;
	int baseSpeed = 400;
	int speedVariation = 200;
	float speed;
	static final int SIZE = 4;
	int direction;
	int angleVariation = 30;
	boolean disabled = false;

	public Shell(float x, float y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.setVelocity(direction);
		this.speed = baseSpeed + Globals.random.nextInt(speedVariation);
	}
	public Shell(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.setVelocity(angle - 180);
		this.speed = baseSpeed + Globals.random.nextInt(speedVariation);
	}

	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, SIZE, SIZE);
	}

	public void update(float tslf) {
		if(!disabled) {
			this.x += velocityX * speed * tslf;
			this.y += velocityY * speed * tslf;
			speed *= 0.9f;
			if(speed <= 1f) {
				disabled = true;
			}
		}
	}

	/**
	 * This function takes a calculated angle and just sets the velocity
	 * @param angle The calculated angle
	 */
	public void setVelocity(float angle) {
		angle += -angleVariation/2 + Globals.random.nextFloat()*angleVariation;
		this.velocityX = (float) Math.sin(Math.toRadians(angle));
		this.velocityY = (float) Math.cos(Math.toRadians(angle));
	}
	/**
	 * This function takes the direction at which was shot and calculates an angle at which the shell should fly and sets the velocity
	 * @param direction The direction at which was shot
	 */
	public void setVelocity(int direction) {
		int angle = 0; //in degrees;
		switch (direction) {
		case Eden.UP:
			angle = 0;
			break;
		case Eden.DOWN:
			angle = 180;
			break;
		case Eden.LEFT:
			angle = 90;
			break;
		case Eden.RIGHT:
			angle = 270;
			break;
		default:
			break;
		}
		angle += -angleVariation/2 + Globals.random.nextFloat()*angleVariation;
		this.velocityX = (float) Math.sin(Math.toRadians(angle));
		this.velocityY = (float) Math.cos(Math.toRadians(angle));
	}
}
