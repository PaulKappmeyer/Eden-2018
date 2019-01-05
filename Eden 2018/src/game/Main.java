package game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The starting class
 * @author Paul Kappmeyer
 *
 */
public class Main {

	public static float tsls = 5;
	public static long firstFrame;
	public static int frames;
	public static float spawntime = 5f;
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Eden started");
		
		Screen sc = new Screen();
		
		long lastFrame = System.currentTimeMillis();
		while(true){
			long thisFrame = System.currentTimeMillis();
			float tslf = (float)(thisFrame - lastFrame) / 1000f;
			lastFrame = thisFrame;
			sc.repaintScreen();
			if(thisFrame > firstFrame + 1000){
				firstFrame = thisFrame;
				Globals.fps = frames;
				frames = 0;
			}
			frames++;
			
			//TODO: Spawn algorithm
			tsls += tslf;
			if(tsls >= spawntime) {
				tsls -= spawntime;
				Globals.enemies.add(new Enemy(100, 100));
				Globals.enemies.add(new Enemy(100, 500));
				Globals.enemies.add(new Enemy(500, 100));
				Globals.enemies.add(new Enemy(500, 500));
//				for (int i = 0; i < 4; i++) {
//					Globals.enemies.add(new Enemy(Globals.random.nextInt(400), Globals.random.nextInt(400)));	
//				}
			}
			
			//TODO: Removal of the Bullet
//			if(Globals.player.bullets.size() >= 500) {
//				Globals.player.bullets.remove(0);
//			}
			
			//TODO:Update System
			sc.update(tslf);
			
			//TODO: Y-Sort
			ysort();
			
			if(Controls.isKeyDown(KeyEvent.VK_ESCAPE))System.exit(0);
			
			Thread.sleep(0);
		}
	}
	
	/**
	 * This function sorts the list of the enemies based on their y-position, so a higher y-value gets displayed on top of the lower y-value, 
	 * this creates the feeling of a perspective
	 */
	public static void ysort() {
		//TODO: using Collections . sort();
//		Collections.sort(Globals.enemies, new Comparator<Enemy>() {
//			@Override
//			public int compare(Enemy e, Enemy e1)
//			{
//				if(e.y > e1.y) {
//					return Globals.enemies.indexOf(e1);
//				}
//				return Globals.enemies.indexOf(e1);
//			}
//		});
		
		ArrayList<Enemy> newEnemies = Globals.enemies;
		for (int i = 0; i < newEnemies.size() - 1; i++) {
			Enemy e = Globals.enemies.get(i);
			Enemy e1 = Globals.enemies.get(i + 1);
			
			if(e.y > e1.y) {
				newEnemies.set(i, e1);
				newEnemies.set(i + 1, e);
				i = 0;
			}
		}
		Globals.enemies = newEnemies;
	}
}
