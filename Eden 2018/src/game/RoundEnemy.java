package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class RoundEnemy extends Enemy{

	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Float> angles = new ArrayList<>();
	float radius = 30;

	public RoundEnemy(float x, float y) {
		super(x, y);
		size = 20;
		this.maxWalkspeed = 20;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		float enemyCenterX = this.x + size/2;
		float enemyCenterY = this.y + size/2;
		if(Globals.player.gun != null) {
			for (Projectile projectile : Globals.player.gun.projectiles) {
				float bulletCenterX = projectile.x + Bullet.SIZE / 2;
				float bulletCenterY = projectile.y + Bullet.SIZE / 2;

				float distance = Globals.distanceSquared(enemyCenterX, enemyCenterY, bulletCenterX, bulletCenterY);
				if(distance <= radius * radius) {
					Bullet b = new Bullet(projectile.x, projectile.y, projectile.angle - 180);
					this.bullets.add(b);
					this.angles.add(new Float(0));
					//Remove old
					projectile.disable();
					projectile.dieAnimation = false;

				}
			}
		}

		//Bullets
		int index = -1;
		for (Bullet bullet : bullets) {
			index++;
			Float angle = angles.get(index);
			if(angle < 360) {
				bullet.x = (float) (this.x + size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(bullet.angle + angle)) * radius);
				bullet.y = (float) (this.y + size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(bullet.angle + angle)) * radius);
				angle += 620 * tslf;
				angles.set(index, angle);
			}else {
				bullet.update(tslf);
				//Hit the player?
				if(bullet.disabled) continue;
				if(bullet.checkCollisionToObject(Globals.player)) {
					Globals.player.startKnockback(bullet.angle, Globals.player.bulletImpact);
					Globals.player.gotHit = true;
					bullet.maxExplosionRadius = 30;
					bullet.disable();
				}
			}
		}
		//Removal
		ArrayList<Bullet> removableBullets = new ArrayList<Bullet>();
		index = 0;
		for (Bullet bullet : bullets) {
			if(bullet.canBeRemoved()) {
				removableBullets.add(bullet);
				angles.remove(index);
			}
			index++;
		}
		for (Bullet b : removableBullets) {
			bullets.remove(b);
		}

	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		if(isInHitAnimation) {
			if(blink > blinktime) {
				g.setColor(Color.WHITE);
				g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
			if(blink > blinktime*2) {
				blink -= blinktime*2;
			}
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}

		//Bullets
		for (Bullet bullet : bullets) {
			g.setColor(Color.RED);
			bullet.draw(g);
		}
	}
}
