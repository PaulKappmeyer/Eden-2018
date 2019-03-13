package game;

import java.awt.Color;
import java.awt.Graphics;

public class Stone {

	int x;
	int y;
	int width;
	int height;

	public Stone(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x + Globals.insetX, y + Globals.insetY, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x + Globals.insetX, y + Globals.insetY, width, height);
	}

	int a = 3;
	public void update(float tslf) {
		//TODO: new Collision system
		//Collision with player
		{
			Eden p = Globals.player;
			if(Globals.checkCollisionRectangleToRectangle(p.x, p.y, p.size, p.size, this.x, this.y, this.width, this.height)) {
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
					p.x = this.x + this.width;
				}
				if(newAngle >= 100 && newAngle <= 255) {
					p.y = this.y + this.height;
				}
				if(newAngle >= 255 && newAngle <= 280) {
					p.x = this.x - p.size;
				}
			}
		}

		//Collision with bullets 
		for (Projectile projectilce : Globals.player.gun.projectiles) {
			if(Globals.checkCollisionRectangleToCircle(projectilce.x, projectilce.y, Bullet.SIZE, this.x, this.y, this.width, this.height)) {
				projectilce.maxExplosionRadius = 30;
				projectilce.disable();
			}
		}
		for (Enemy e : Globals.enemies) {
			if(e.x + e.size > this.x && e.x + e.size < this.x + a && e.x < this.x && e.y + e.size > this.y && e.y < this.y + this.height) {
				e.x = this.x - e.size;
			}
			if(e.y < this.y + this.height && e.y > this.y + this.height - a && e.x + e.size > this.x && e.x < this.x + this.width) {
				e.y = this.y + this.height;
			}
			if(e.y + e.size > this.y && e.y + e.size < this.y + a && e.y < this.y && e.x + e.size > this.x && e.x < this.x + this.width) {
				e.y = this.y - e.size;
			}
			if(e.x < this.x + this.width && e.x > this.x + this.width - a && e.y + e.size > this.y && e.y < this.y + this.height) {
				e.x = this.x + this.width;
			}

			if(e instanceof Boss) {
				Boss b = (Boss) e;
				for (Projectile projectilce : b.gun.projectiles) {
					if(Globals.checkCollisionRectangleToCircle(projectilce.x, projectilce.y, Bullet.SIZE, this.x, this.y, this.width, this.height)) {
						projectilce.maxExplosionRadius = 30;
						projectilce.disable();
					}
				}
			}
		}

		//Collision with shells
		for (Shell s : Globals.player.gun.shells) {
			if(s.collided) continue;
			if(Globals.checkCollisionRectangleToCircle(s.x, s.y, Shell.SIZE, this.x, this.y, this.width, this.height)) {
				s.velocityX *= -0.5;
				s.velocityY *= -0.5;
				s.collided = true;
			}
		}
	}
}
