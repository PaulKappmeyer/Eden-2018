package game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

//This is the class of the Player named Eden
public class Eden {

	float x,y;
	int walkspeed = 100;
	
	//--------------------------------------- Constructor
	public Eden() {
		x = 400;
		y = 400;
	}
	
	public void show(Graphics g) {
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, 16, 16);
	}
	
	public void Update(float tslf) {
		if(Controls.isKeyDown(KeyEvent.VK_W)) {
			y -= walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_A)) {
			x -= walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_S)) {
			y += walkspeed * tslf;
		}
		if(Controls.isKeyDown(KeyEvent.VK_D)) {
			x += walkspeed * tslf;
		}
	}
}
