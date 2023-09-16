/*
 * 
 */
package game;

import java.awt.Graphics;

/**
 * 
 * @author Paul
 *
 */
public abstract class Obstacle {

	/** The X-coordinate of the obstacle */
	public int x;
	/** The Y-coordinate of the obstacle */
	public int y;
	/** The width of the obstacle */
	public int width;
	/** The height of the obstacle */
	public int height;
	/** If it gets checked for collision */
	public boolean isObstacle = true;
	
	//--------------------------------------------Constructors
	/**
	 * Basic constructor, nothing defined
	 */
	public Obstacle() {
	}
	
	/**
	 * Basic constructor
	 * @param x The X-coordinate of the object
	 * @param y The Y-coordinate of the object
	 */
	public Obstacle(int x, int y) {
		this.x = x;
		this.y = y;
	}	
	
	public Obstacle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g) {
	}
	
	public void update(float tslf) {
	}
}
