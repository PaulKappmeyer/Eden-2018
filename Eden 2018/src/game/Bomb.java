package game;

import java.awt.Color;
import java.awt.Graphics;

public class Bomb {

	float x;
	float y;
	int size = 16;
	//Blink
	boolean ticking = true;
	float maxBlinkTime = 0.5f;
	float blinkTime;
	float decrease = 0.05f;
	int maxBlinks = 100;
	int blinks = 0;
	//Explode
	boolean explode = false;
	float radius = 0;
	float radiusIncrease = 2500;
	float maxRadius = 250;

	Color color;

	public Bomb(float x, float y) {
		this.x = x;
		this.y = y;
		color = Color.BLACK;
	}

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);

		if(explode) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}

	public void update(float tslf) {
		if(ticking) {
			//Ticking
			blinkTime += tslf;
			if(blinkTime >= maxBlinkTime) {
				blinkTime = 0;
				maxBlinkTime -= decrease;
				blinks ++;
				//swap Color
				if(this.color == Color.BLACK) color = Color.WHITE;
				else if(this.color == Color.WHITE) color = Color.BLACK;
			}
			//Explode
			if(blinks >= maxBlinks) {
				blinks = 0;
				blinkTime = 0;
				maxBlinkTime = 0.5f;
				ticking = false;
				explode = true;
				
				//Collision
				for (Enemy e : Globals.enemies) {
					if(Globals.checkCollisionRectangleToCircle(this.x + size/2 - maxRadius/2, this.y + size/2 -maxRadius/2, maxRadius, e.x, e.y, e.size, e.size)) {
						float ecx = e.x + e.size/2;
						float ecy = e.y + e.size/2;

						float distx = this.x + size/2 - ecx;
						float disty = this.y + size/2 - ecy;

						//TODO: Rework the angle system
						float newAngle = (float) Math.atan(distx / disty);
						newAngle = (float) Math.toDegrees(newAngle);
						if(this.y + size/2 > ecy) newAngle =  -90 - (90-newAngle);
						if(this.x + size/2 < ecx && this.y + size/2 < ecy) newAngle = -270 - (90-newAngle);

						e.getDamaged(200);
						e.startKnockback(newAngle, maxRadius * 2, 0.35f);
					}
				}
			}
		}
		//Explosion animation
		if(explode) {
			radius += radiusIncrease * tslf;
			if(radius >= maxRadius) {
				explode = false;
			}
		}
	}

}
