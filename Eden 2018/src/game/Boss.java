package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class Boss extends Enemy{
	
	float tsls;
	float shotTime = 5;
	boolean shot = false;
	int numBulletsPerShot = 10;
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Boss(float x, float y) {
		super(x, y);
		size = 32;
		walkspeed = 10;
		followplayer = true;
		health = 5000;
		knockback = 0.5f;
	}
	
	public void show(Graphics g) {
		if(shot) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		}
		
		for (Bullet b : bullets) {
			b.show(g);
		}
	}
	
	public void update(float tslf) {
		if(shot) {
			updateShot(tslf);
		}else {
			super.update(tslf);
			tsls += tslf;
			if(tsls >= shotTime) {
				tsls = 0;
				shot = true;
			}
		}
		
		for (Bullet b : bullets) {
			b.update(tslf);
		}
	}
	
	public void updateShot(float tslf) {
		shot();
		shot = false;
	}
	
	public void shot() {
		for (int i = 0; i < numBulletsPerShot; i++) {
			float angle = 360/numBulletsPerShot * i;
			float cx = (float) (x + size/2 - Bullet.size/2 + Math.sin(Math.toRadians(angle)) * size);
			float cy = (float) (y + size/2 - Bullet.size/2 + Math.cos(Math.toRadians(angle)) * size);
			Bullet b = new Bullet(cx, cy, angle);
			b.color = Color.RED;
			bullets.add(b);
		}
	}
}
