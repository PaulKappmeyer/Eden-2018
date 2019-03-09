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
		walkspeed = 10;
		followplayer = true;
		health = 1000;
		knockback = 0.5f;
		gun = new Gun(this);
		gun.mode = Gun.CIRCLESHOT;
		gun.shottime = 5;
		gun.color = Color.RED;
	}
	
	public void show(Graphics g) {
		if(gun.canShot) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		}
		
		gun.draw(g);
	}
	
	public void update(float tslf) {
		super.update(tslf);
		if(gun.canShot) {
			gun.shot();
		}
		
		gun.update(tslf);
	}
}
