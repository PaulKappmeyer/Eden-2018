package guns;

import java.awt.Color;
import java.awt.Graphics;

import game.Direction;
import game.Game;
import game.Globals;
import game.Obstacle;

public class Shell {

	public float x;
	public float y;
	float velocityX;
	float velocityY;
	float baseSpeed = 15;
	float speedVariation = 5;
	float finalSpeed;
	float speed;
	public static final int SIZE = 4;
	int direction;
	int angleVariation = 30;
	boolean disabled = false;

	boolean collided = false;

	public Shell(float x, float y, int direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.setVelocity(direction);
		this.finalSpeed = (float)(baseSpeed + Globals.random.nextFloat()*speedVariation);
	}
	public Shell(float x, float y, float angle) {
		this.x = x;
		this.y = y;
		this.setVelocity(angle - 180);
		this.finalSpeed = (float)(baseSpeed + Globals.random.nextFloat()*speedVariation);
	}

	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, SIZE, SIZE);
	}

	float time;
	public void update(float tslf) {
		if(!disabled) {
			this.x += velocityX * speed * tslf;
			this.y += velocityY * speed * tslf;
			time += tslf;
			speed = finalSpeed * (0.5f / time);
			if(speed <= finalSpeed) {
				disabled = true;
			}

			checkCollisionToStone();
		}
	}

	/**
	 * 
	 */
	public void checkCollisionToStone() {
		if(this.collided) return;
		for (Obstacle obs : Game.currentMap.obstacles) {
			if(Globals.checkCollisionRectangleToCircle(this.x, this.y, Shell.SIZE, obs.x, obs.y, obs.width, obs.height)) {
				this.velocityX *= -0.5;
				this.velocityY *= -0.5;
				this.collided = true;
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
	public void setVelocity(Direction direction) {
		int angle = 0; //in degrees;
		switch (direction) {
		case UP:
			angle = 0;
			break;
		case DOWN:
			angle = 180;
			break;
		case LEFT:
			angle = 90;
			break;
		case RIGHT:
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
