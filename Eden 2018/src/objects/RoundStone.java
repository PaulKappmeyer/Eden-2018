/*
 * 
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;

import enemies.Enemy;
import game.Game;
import game.Globals;
import player.Eden;

/**
 * 
 * @author Paul
 *
 */
public class RoundStone {

	int x;
	int y;
	int size;
	
	public RoundStone(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(x, y, size, size);
		g.setColor(Color.BLACK);
		g.drawOval(x, y, size, size);
	}
	
	public void update(float tslf) {
		//TODO: new Collision system
		float width = this.size;
		float height = this.size;
		//Collision with player
		{
			Eden p = Globals.player;
			if(Globals.checkCollisionRectangleToRectangle(p.x, p.y, p.size, p.size, this.x, this.y, width, height)) {
				float pcx = p.x + p.size/2;
				float pcy = p.y + p.size/2;
				float ecx = this.x + width/2;
				float ecy = this.y + height/2;

				float distx = pcx - ecx;
				float disty = pcy - ecy;

				//TODO: Rework the angle system
				float newAngle = (float) Math.atan(distx / disty);
				newAngle = (float) Math.toDegrees(newAngle);
				if(pcx >= ecx && pcy <= ecy) newAngle *= -1;
				if(pcx >= ecx && pcy >= ecy) newAngle = 180 - newAngle;
				if(pcx <= ecx && pcy >= ecy) newAngle = 180 - newAngle;
				if(pcx <= ecx && pcy <= ecy) newAngle = 360 - newAngle;

				if(newAngle >= 280 && newAngle <= 360 || newAngle >= 0 && newAngle <= 80) {
					p.y = this.y - p.size;
				}
				if(newAngle >= 80 && newAngle <= 100) {
					p.x = this.x + width;
				}
				if(newAngle >= 100 && newAngle <= 255) {
					p.y = this.y + height;
				}
				if(newAngle >= 255 && newAngle <= 280) {
					p.x = this.x - p.size;
				}
			}
		}

		//Collision with Enemies 
		for (Enemy e : Game.currentMap.enemies) {
			if(Globals.checkCollisionRectangleToRectangle(e.x, e.y, e.size, e.size, this.x, this.y, width, height)) {
				float pcx = e.x + e.size/2;
				float pcy = e.y + e.size/2;
				float ecx = this.x + width/2;
				float ecy = this.y + height/2;

				float distx = pcx - ecx;
				float disty = pcy - ecy;

				//TODO: Rework the angle system
				float newAngle = (float) Math.atan(distx / disty);
				newAngle = (float) Math.toDegrees(newAngle);
				if(pcx >= ecx && pcy <= ecy) newAngle *= -1;
				if(pcx >= ecx && pcy >= ecy) newAngle = 180 - newAngle;
				if(pcx <= ecx && pcy >= ecy) newAngle = 180 - newAngle;
				if(pcx <= ecx && pcy <= ecy) newAngle = 360 - newAngle;

				if(newAngle >= 280 && newAngle <= 360 || newAngle >= 0 && newAngle <= 80) {
					e.y = this.y - e.size;
				}
				if(newAngle >= 80 && newAngle <= 100) {
					e.x = this.x + width;
				}
				if(newAngle >= 100 && newAngle <= 255) {
					e.y = this.y + height;
				}
				if(newAngle >= 255 && newAngle <= 280) {
					e.x = this.x - e.size;
				}
			}
		}
	}
}
