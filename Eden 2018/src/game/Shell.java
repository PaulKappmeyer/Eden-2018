package game;

import java.awt.Color;
import java.awt.Graphics;

public class Shell {

	float x;
	float y;
	float velocityX;
	float velocityY;
	float speed;
	int size;
	int direction;

	public Shell(float x, float y, int direction) {
		this.x = x;
		this.y = y;
		this.size = 5;
		this.direction = direction;
		this.setVelocity(direction);
		this.speed = 300;
	}

	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
	}

	public void update(float tslf) {
		this.x += velocityX * speed * tslf;
		this.y += velocityY * speed * tslf;
		speed *= 0.9f;
	}

	public void setVelocity(int direction) {
		switch (direction) {
		case Eden.UP:
			this.velocityY = 1;
			this.velocityX = 0;
			break;
		case Eden.DOWN:
			this.velocityY = -1;
			this.velocityX = 0;
			break;
		case Eden.LEFT:
			this.velocityY = 0;
			this.velocityX = 1;
			break;
		case Eden.RIGHT:
			this.velocityY = 0;
			this.velocityX = -1;
			break;
		default:
			break;
		}
	}
}
