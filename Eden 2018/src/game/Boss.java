package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Boss extends Enemy{

	Gun gun;

	public Boss(float x, float y) {
		super(x, y);
		size = 32;
		maxWalkspeed = 10;
		followplayer = true;
		health = 1000;
		bulletImpact = 50;
		//Gun
		gun = new Gun(this);
		gun.mode = Gun.CIRCLESHOT;
		gun.shottime = 5f;
		radiusIncrease = 200;
	}

	@Override
	public void draw(Graphics g) {
		//Drawing of the boss
		if(alive) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
			g.setColor(Color.BLACK);
			g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
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
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}
	}

	@Override
	public boolean canBeRemoved() {
		if(super.canBeRemoved()) {
			if(gun.shells.size() != 0) return false;
			for (Projectile projectile : gun.projectiles) {
				if(!projectile.canBeRemoved()) return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		if(alive) {
			if(gun.canShot) {
				gun.shot();
			}
		}

		gun.update(tslf);
	}
}
