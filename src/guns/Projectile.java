package guns;

import java.awt.Graphics;

import game.Gameobject;

public abstract class Projectile {

	public float x;
	public float y;
	public float velocityX;
	public float velocityY;
	public float speed;
	public float angle;
	public boolean isActive; //shows if the projectile is free in the object pool
	public boolean hitSomething; //show if the projectile hit something 
	//Die animation attributes
	public boolean dieAnimation;			//if is in die animation
	public float maxExplosionRadius;		//maximal radius
	
	float explosionRadiusIncrease; 	//increase per second
	float currentRadius; 			//current radius
	
	public Projectile() {
		isActive = false;
		
		hitSomething = false;
		dieAnimation = false;
		maxExplosionRadius = 80;
		explosionRadiusIncrease = 1600;
		currentRadius = 0;
	}
	
	/**
	 * 
	 * @param g
	 */
	public abstract void draw(Graphics g);
	/**
	 * 
	 * @param tslf
	 */
	public abstract void update(float tslf) ;
	/**
	 * This function disables the projectile so it does not get updated and moves or does damage to enemies, the state is picked for example when the projectile hits the wall
	 */
	public abstract void hitSomething();
	/**
	 * This function activates the bullet like if it would get add to the array
	 */
	public abstract void activate(float x, float y, float angle, float speed);
	/**
	 * This function deactivates the bullet so it is free in the object pool
	 */
	public abstract void deactivate();
	/**
	 * This function shows that the bullet can be deactivated, because the hit-animation is finished
	 * @return
	 */
	public abstract boolean canBeDeactivated();
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public abstract boolean checkCollisionToObject(Gameobject obj);
}
