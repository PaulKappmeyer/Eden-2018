/*
 * 
 */
package enemies;

import java.awt.Color;
import java.awt.Graphics;

import guns.CirclefireGun;
import guns.Gun;
import guns.Projectile;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Boss extends ZombieEnemy{

	public Gun gun;

	public Boss(float x, float y) {
		super(x, y);
		this.size = 32;
		this.MAX_WALK_SPEED = 12;
		
		this.MAX_HEALTH = 1000;
		this.health = MAX_HEALTH;
		
		bulletImpact = 50;
		//Gun
		gun = new CirclefireGun(this, CirclefireGun.CIRCLESHOT);
		gun.shottime = 5f;
		radiusIncrease = 200;
	}

	@Override
	public void draw(Graphics g) {
		//Drawing of the boss
		if (alive) {
			if (!showBlink) {
				g.setColor(Color.DARK_GRAY);
				g.fillRect((int)x, (int)y, size, size);
				g.setColor(Color.BLACK);
				g.drawRect((int)x, (int)y, this.size, this.size);
				
				g.setColor(Color.BLUE);
				switch (lookDirection) {
				case UP:
					g.fillOval((int)(this.x + size/2 - a/2), (int)this.y, a, a);
					break;
				case DOWN:
					g.fillOval((int)(this.x + size/2 - a/2), (int)this.y + size - a, a, a);
					break;
				case RIGHT:
					g.fillOval((int)(this.x + size - a), (int)this.y + size/2 - a/2, a, a);
					break;
				case LEFT:
					g.fillOval((int)(this.x), (int)this.y + size/2 - a/2, a, a);
					break;
				default:
					break;
				}
			}
			if (isInHitAnimation) {
				if (showBlink) {
					g.setColor(Color.WHITE);
					g.fillRect((int)x, (int)y, this.size, this.size);
					g.drawRect((int)x, (int)y, this.size, this.size);
				}
			}
		}
		if (isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2), (int)(y + size/2 - radius/2), (int)radius, (int)radius);
		}
		
		healthbar.draw(g);
	}

	@Override
	public boolean canBeRemoved() {
		if (super.canBeRemoved()) {
			for (Projectile projectile : gun.projectiles) {
				if (projectile.isActive) return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		if (alive) {
			if (followplayer) {
				if (gun.canShot) {
					gun.shot();
					resetSpeedUp();
				}
			}
		}

		gun.update(tslf);
	}
}
