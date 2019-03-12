package game;

import java.awt.Graphics;

public abstract class Projectile {

	float x;
	float y;
	float velocityX;
	float velocityY;
	float speed;
	float angle;
	boolean disabled;
	//Die animation attributes
	boolean dieAnimation;			//if is in die animation
	float maxExplosionRadius;		//maximal radius
	float explosionRadiusIncrease; 	//increase per second
	float currentRadius; 			//current radius
	
	public Projectile() {
		disabled = false;
		dieAnimation = false;
		maxExplosionRadius = 80;
		explosionRadiusIncrease = 1600;
		currentRadius = 0;
	}
	
	public void draw(Graphics g) {};
	
	public void update(float tslf) {};
	
	/**
	 * This function disables the projectile so it does not get updated and moves or does damage to enemies, the state is picked for example when the projectile hits the wall
	 */
	public void disable() {
		this.angle = 0;
		this.velocityX = 0;
		this.velocityY = 0;
		this.disabled = true;
		this.dieAnimation = true;
	};
	
	public boolean canBeRemoved() {
		return false;
	};
	
	public boolean checkCollisionToObject(Object obj) {
		return false;
	};
}
