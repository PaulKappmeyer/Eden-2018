package game;

/**
 * 
 * @author Paul
 *
 */
public class Object {

	/**
	 * The X-coordinate of the object
	 */
	public float x;
	/**
	 * The Y-coordinate of the object
	 */
	public float y;
	/**
	 * The size of the object meaning it is an square
	 */
	public int size;
	/**
	 * The direction in which the object is potentially shooting
	 */
	public Direction shotDirection;
	
	//--------------------------------------------Constructors
	/**
	 * Basic constructor, nothing defined
	 */
	public Object() {
	}
	/**
	 * Basic constructor
	 * @param x The X-coordinate of the object
	 * @param y The Y-coordinate of the object
	 */
	public Object(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
}
