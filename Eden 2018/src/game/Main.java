package game;

import java.awt.event.KeyEvent;

/**
 * The starting class
 * @author Paul Kappmeyer
 *
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Eden started");
		
		Screen sc = new Screen();
		
		long lastFrame = System.currentTimeMillis();
		while(true){
			long thisFrame = System.currentTimeMillis();
			float tslf = (float)(thisFrame - lastFrame) / 1000f;
			lastFrame = thisFrame;
			sc.repaintScreen();
			
			Globals.player.Update(tslf);
			
			if(Controls.isKeyDown(KeyEvent.VK_ESCAPE))System.exit(0);
			
			Thread.sleep(5);
		}
	}
	
}
