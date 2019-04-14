/*
 * 
 */
package game;

import java.awt.Graphics;

/**
 * A super basic game-object with a x-/y-position and a size
 * @author Paul
 *
 */
public abstract class Gameobject {
	/** The x-coordinate of the object */
	public float x;
	/** The y-coordinate of the object */
	public float y;
	/** The size of the object */
	public int size;
	
	//-----------------------------------------------------------CONSTRUCTORS------------------------------------------
	/**
	 * Basic constructor, nothing defined
	 */
	public Gameobject() {
	}
	/**
	 * Basic constructor
	 * @param x The x-coordinate of the object
	 * @param y The y-coordinate of the object
	 */
	public Gameobject(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * 
	 * @param x The x-coordinate of the object
	 * @param y The y-coordinate of the object
	 * @param size The width of the object
	 */
	public Gameobject(float x, float y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	//-----------------------------------------------------------METHODS------------------------------------------
	//------------------------DRAWING
	public abstract void draw(Graphics g);
	
	//------------------------UPDATING
	public abstract void update(float tslf);
}
