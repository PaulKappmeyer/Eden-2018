/*
 * 
 */
package game;

/**
 * 
 * @author Paul
 *
 */
public abstract class MovingObject extends Gameobject{

	//MOVEMENT
	public Direction walkDirection = Direction.UNDEFINED;
	public double walkVelocityX;
	public double walkVelocityY;
	//SPEEDING UP WHEN STARTING WALK
	public boolean isSpeedingUP = true;
	public float TIME_FOR_MAX_SPEED = 0.65f;
	public float timeSpeededUp;
	public float MAX_WALK_SPEED = 100f;
	public float currentWalkSpeed;

	//-----------------------------------------------------------CONSTRUCTORS------------------------------------------
	/**
	 * Basic constructor, nothing defined
	 */
	public MovingObject() {
	}
	/**
	 * Basic constructor
	 * @param x The x-coordinate of the object
	 * @param y The y-coordinate of the object
	 */
	public MovingObject(float x, float y) {
		super(x, y);
	}
	/**
	 * 
	 * @param x The x-coordinate of the object
	 * @param y The y-coordinate of the object
	 * @param size The size of the object
	 */
	public MovingObject(float x, float y, int size) {
		super(x, y, size);
	}

	//-----------------------------------------------------------METHODS------------------------------------------
	//------------------------DRAWING

	//------------------------UPDATING
	/**
	 * This function does all the updating of the enemy like: updating the knockback, updating the animations, set new position, check for collision etc.
	 * @param tslf Time-since-last-frame or delta time
	 */
	public void update(float tslf) {
		//Speed up
		updateSpeedUp(tslf);
		
		//Set direction
		walkDirection = getDirection(walkVelocityX, walkVelocityY);
	}
	/**
	 * This function updates the speed-up / acceleration of the enemy when started to walk
	 * @param tslf
	 */
	public void updateSpeedUp(float tslf) {
		if (isSpeedingUP) {
			if (currentWalkSpeed < MAX_WALK_SPEED) {
				timeSpeededUp += tslf;
				currentWalkSpeed = MAX_WALK_SPEED * (timeSpeededUp / TIME_FOR_MAX_SPEED);
			} else {
				timeSpeededUp = 0;
				currentWalkSpeed = MAX_WALK_SPEED;
				isSpeedingUP = false;
			}
		}
	}
	/**
	 * This function resets the speed-up / acceleration of the enemy so its current walk speed is equal to 0 again
	 */
	public void resetSpeedUp() {
		currentWalkSpeed = 0;
		timeSpeededUp = 0;
		isSpeedingUP = true;
	}

	//------------------------Directions
	/**
	 * This function takes two velocities and return the direction relating to them
	 * @param velocityX The velocity in x-direction : -1 <= velX && velX <= 1
	 * @param velocityY The velocity in y-direction : -1 <= velY && velY <= 1
	 * @return The direction
	 */
	public Direction getDirection(double velocityX, double velocityY) {
		if (velocityY >= -1 && velocityY <= -0.45) {
			return(Direction.UP);
		}
		if (velocityY >= 0.45 && velocityY <= 1) {
			return(Direction.DOWN);
		}
		if (velocityX >= -1 && velocityX <= -0.45) {
			return(Direction.LEFT);
		}
		if (velocityX >= 0.45 && velocityX <= 1) {
			return(Direction.RIGHT);
		}
		return Direction.UNDEFINED;
	}
}
