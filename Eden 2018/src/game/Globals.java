package game;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class for all the global variables
 * @author Paul Kappmeyer
 * @param width The width of the window
 * @param height The height of the window
 * @param insetX The value of the left window bounding; should be added whenever something is drawn to the screen
 * @param insetY The value of the top window bounding; should be added whenever something is drawn to the screen
 *
 */
public class Globals {
	public static int width = 1000;
	public static int height = 700;
	public static int insetX;
	public static int insetY;
	public static Eden player = new Eden();
	public static Random random = new Random();
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static int fps;

	//TODO: change the method
	public static void checkCollisionBulletToWall(Bullet b) {
		//		if(b.x < 0) {
		//			b.x = 0;
		//			b.disable();
		//		}
		//		if(b.y < 0) {
		//			b.y = 0;
		//			b.disable();
		//		}
		//		if(b.x + Bullet.size > Globals.width) {
		//			b.x = Globals.width - Bullet.size;
		//			b.disable();
		//		}
		//		if(b.y + Bullet.size > Globals.height) {
		//			b.y = Globals.height - Bullet.size;
		//			b.disable();
		//		}
		if(b.x < 0 || b.y < 0 || b.x + Bullet.size > Globals.width || b.y + Bullet.size > Globals.height) {
			b.disable();
		}
	}

	public static void checkCollisionRocketToWall(Rocket r) {
		//			if(b.x < 0) {
		//				b.x = 0;
		//				b.disable();
		//			}
		//			if(b.y < 0) {
		//				b.y = 0;
		//				b.disable();
		//			}
		//			if(b.x + Bullet.size > Globals.width) {
		//				b.x = Globals.width - Bullet.size;
		//				b.disable();
		//			}
		//			if(b.y + Bullet.size > Globals.height) {
		//				b.y = Globals.height - Bullet.size;
		//				b.disable();
		//			}
		if(r.x < 0 || r.y < 0 || r.x + Bullet.size > Globals.width || r.y + Bullet.size > Globals.height) {
			r.disable();
		}
	}
}
