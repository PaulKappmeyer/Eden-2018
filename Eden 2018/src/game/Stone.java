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
		Eden p = Globals.player;
		//TODO: new Collision system
		if(p.x + p.size > this.x && p.x + p.size < this.x + a && p.x < this.x && p.y + p.size > this.y && p.y < this.y + this.height) {
			p.x = this.x - p.size;
		}
		if(p.y < this.y + this.height && p.y > this.y + this.height - a && p.x + p.size > this.x && p.x < this.x + this.width) {
			p.y = this.y + this.height;
		}
		if(p.y + p.size > this.y && p.y + p.size < this.y + a && p.y < this.y && p.x + p.size > this.x && p.x < this.x + this.width) {
			p.y = this.y - p.size;
		}
		if(p.x < this.x + this.width && p.x > this.x + this.width - a && p.y + p.size > this.y && p.y < this.y + this.height) {
			p.x = this.x + this.width;
		}

		for (Projectile projectilce : p.gun.projectiles) {
			if(Globals.checkCollisionRectangleToCircle(projectilce.x, projectilce.y, Bullet.SIZE, this.x, this.y, this.width, this.height)) {
				projectilce.maxExplosionRadius = 30;
				projectilce.disable();
			}
		}
		for (Enemy e : Globals.enemies) {
			if(e.x + e.size > this.x && e.x + e.size < this.x + a && e.x < this.x && e.y + e.size > this.y && e.y < this.y + this.height) {
				e.x = this.x - e.size;
			}
			if(e.y < this.y + this.height && e.y + e.size > this.y + this.height && e.x + e.size > this.x && e.x < this.x + this.width) {
				e.y = this.y + this.height;
			}
			if(e.y + e.size > this.y && e.y < this.y && e.x + e.size > this.x && e.x < this.x + this.width) {
				e.y = this.y - e.size;
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
	}
}
