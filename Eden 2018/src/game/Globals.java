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
	public static int fps;

	//TODO: change the method
	public static void checkCollisionProjectileToWall(Projectile projectile) {
		if(projectile.x < 0 || projectile.y < 0 || projectile.x + Bullet.SIZE > Globals.width || projectile.y + Bullet.SIZE > Globals.height) {
			projectile.disable();
		}
	}
}
