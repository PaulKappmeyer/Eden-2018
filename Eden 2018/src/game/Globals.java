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
	public static int width = 800;
	public static int height = 600;
	public static int insetX;
	public static int insetY;
	public static Eden player = new Eden();
	public static Random random = new Random();
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	public static void checkCollisionBulletToWall(Bullet b) {
		if(b.x < 0) {
			b.x = 0;
			b.velX = 0;
			b.velY = 0;
		}
		if(b.y < 0) {
			b.y = 0;
			b.velX = 0;
			b.velY = 0;
		}
		if(b.x + Bullet.size > Globals.width) {
			b.x = Globals.width - Bullet.size;
			b.velX = 0;
			b.velY = 0;
		}
		if(b.y + Bullet.size > Globals.height) {
			b.y = Globals.height - Bullet.size;
			b.velX = 0;
			b.velY = 0;
		}
	}
}
