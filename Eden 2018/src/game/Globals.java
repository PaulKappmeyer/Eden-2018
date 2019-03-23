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
	
	/**
	 * 
	 * @param centerX1
	 * @param centerY1
	 * @param centerX2
	 * @param centerY2
	 * @return
	 */
	public static float distanceSquared(float centerX1, float centerY1, float centerX2, float centerY2) {
		float distX = centerX1 - centerX2;
		float distY = centerY1 - centerY2;
		return distX * distX + distY * distY;
	}
	
	/**
	 * 
	 * @param circleX
	 * @param circleY
	 * @param circleSize
	 * @param rectangleX
	 * @param rectangleY
	 * @param rectangleWidth
	 * @param rectangleHeight
	 * @return
	 */
	public static boolean checkCollisionRectangleToCircle(float circleX, float circleY, float circleSize, float rectangleX, float rectangleY, float rectangleWidth, float rectangleHeight) {
		float cx = circleX + circleSize/2;
		float cy = circleY + circleSize/2;
		float r = circleSize/2;
		//Collision circle in the rectangle
		if(cx > rectangleX && cx < rectangleX + rectangleWidth && cy > rectangleY && cy < rectangleY + rectangleHeight) {
			return true;
		}
		//Collision top side of the rectangle to the circle
		if(cx > rectangleX && cx < rectangleX + rectangleWidth && Math.abs(rectangleY - cy) <= r) {
			return true;
		}
		//Collision bottom side of the rectangle to the circle
		if(cx > rectangleX && cx < rectangleX + rectangleWidth && Math.abs(rectangleY + rectangleHeight - cy) <= r) {
			return true;
		}
		//Collision left side of the rectangle to the circle
		if(cy > rectangleY && cy < rectangleY + rectangleHeight && Math.abs(rectangleX - cx) <= r) {
			return true;
		}
		//Collision right side of the rectangle to the circle
		if(cy > rectangleY && cy < rectangleY + rectangleHeight && Math.abs(rectangleX + rectangleWidth - cx) <= r) {
			return true;
		}
		//Collision top left corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - rectangleX), Math.abs(cy - rectangleY)) <= r) {
			return true;
		}
		//Collision top right corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - (rectangleX + rectangleWidth)), Math.abs(cy - rectangleY)) <= r) {
			return true;
		}
		//Collision bottom left corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - rectangleX), Math.abs(cy - (rectangleY + rectangleHeight))) <= r) {
			return true;
		}
		//Collision bottom right corner of the rectangle to the circle
		if(Math.hypot(Math.abs(cx - (rectangleX + rectangleWidth)), Math.abs(cy - (rectangleY + rectangleHeight))) <= r) {
			return true;
		}
		//if no Collision happened
		return false;
	}
	
	/**
	 * 
	 * @param rectangleX1
	 * @param rectangleY1
	 * @param rectangleWidth1
	 * @param rectangleHeight1
	 * @param rectangleX2
	 * @param rectangleY2
	 * @param rectangleWidth2
	 * @param rectangleHeight2
	 * @return
	 */
	public static boolean checkCollisionRectangleToRectangle(float rectangleX1, float rectangleY1, float rectangleWidth1, float rectangleHeight1, float rectangleX2, float rectangleY2, float rectangleWidth2, float rectangleHeight2) {
		if(rectangleX1 <= rectangleX2 + rectangleWidth2 && rectangleX1 + rectangleWidth1 >= rectangleX2 && rectangleY1 <= rectangleY2 + rectangleHeight2 && rectangleY1 + rectangleHeight1 >= rectangleY2) {
			return true;
		}
		return false;
	}
}
