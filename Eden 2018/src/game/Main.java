package game;

import java.awt.event.KeyEvent;

/**
 * The starting class
 * @author Paul Kappmeyer
 *
 */
public class Main {

	public static float tsls = 5;
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Eden started");
		
		Screen sc = new Screen();
		
		long lastFrame = System.currentTimeMillis();
		while(true){
			long thisFrame = System.currentTimeMillis();
			float tslf = (float)(thisFrame - lastFrame) / 1000f;
			lastFrame = thisFrame;
			sc.repaintScreen();
			
//			Globals.enemies.add(new Enemy(100, 100));
//			System.out.println(Globals.enemies.size());
			
			tsls += tslf;
			if(tsls >= 5) {
				tsls -= 5;
				Globals.enemies.add(new Enemy(100, 100));
				Globals.enemies.add(new Enemy(100, 500));
				Globals.enemies.add(new Enemy(500, 100));
				Globals.enemies.add(new Enemy(500, 500));
			}
			
			Globals.player.update(tslf);
			for (Enemy e : Globals.enemies) {
				e.update(tslf);
			}
			
			if(Controls.isKeyDown(KeyEvent.VK_ESCAPE))System.exit(0);
			
			Thread.sleep(0);
		}
	}
	
}
