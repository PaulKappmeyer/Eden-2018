package enemies;

import java.awt.Graphics;

import game.Collision;
import game.Globals;
import game.Obstacle;
import objects.BulletBouncer;

/**
 * 
 * @author Paul Kappmeyer
 *
 */
public class ZombieEnemy extends Enemy{

	//Follow player distance
	int triggerDistance = 300;
	boolean followplayer;
	float walkAngle;
	
	/**
	 * Constructor; initializes the enemy
	 * @param x The x-position
	 * @param y The y-position
	 */
	public ZombieEnemy(float x, float y) {
		this.x = x;
		this.y = y;
		this.size = 16;
		this.MAX_WALK_SPEED += -10 + Globals.random.nextInt(20); //Randomize walk speed
		
		this.MAX_HEALTH = 100;
		this.health = MAX_HEALTH;
	}

	/**
	 * This function draws the enemy as a red rectangle to the screen
	 * @param g The graphics object to draw
	 */
	public void draw(Graphics g) {
		super.draw(g);
	}

	/**
	 * This function updates the enemy; if player is in range follow him
	 * @param tslf
	 */
	public void update(float tslf) {
		if(alive) {
			//Check for range to start follow player
			float playercenterx = Globals.player.x +  Globals.player.size/2;
			float playercentery = Globals.player.y +  Globals.player.size/2;
			float enemycenterx = this.x + this.size/2;
			float enemycentery = this.y + this.size/2;
			float distx = enemycenterx - playercenterx;
			float disty = enemycentery - playercentery;
			if(!followplayer) {
				float distanceToPlayer = distx * distx + disty * disty;
				if(distanceToPlayer < triggerDistance * triggerDistance) {
					followplayer = true;
				}
			}
			//Movement follow player
			if(followplayer) {
				walkAngle = (float) Math.atan(distx / disty);
				walkAngle = (float) Math.toDegrees(walkAngle);
				if(playercentery > enemycentery) walkAngle =  -90 - (90-walkAngle);
				if(playercenterx < enemycenterx && playercentery < enemycentery) walkAngle = -270 - (90-walkAngle);

				walkVelocityX = -Math.sin(Math.toRadians(walkAngle));
				walkVelocityY = -Math.cos(Math.toRadians(walkAngle));

				if(distx == 0 && disty == 0) {
					walkVelocityX = 0;
					walkVelocityY = 0;
				}

				walkDirection = getDirection(walkVelocityX, walkVelocityY);
				lookDirection = walkDirection;
				
				//Collision with obstacles
				checkCollisionToObstacles(tslf);

				//Movement
				this.x += (float) ((walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
				this.y += (float) ((walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);
			}
		}
		
		super.update(tslf);
	}

	/**
	 * 
	 * @param tslf
	 */
	public void checkCollisionToObstacles(float tslf) {
		float nextX = (float) (this.x + (walkVelocityX * currentWalkSpeed + knockbackVelocityX * currentKnockbackSpeed) * tslf);
		float nextY = (float) (this.y + (walkVelocityY * currentWalkSpeed + knockbackVelocityY * currentKnockbackSpeed) * tslf);
		if(nextX == this.x && nextY == this.y) return;
		
		Obstacle[] collisions = Collision.checkCollisionMovingobjToObstacle(this, nextX, nextY);
		
		//COLLISION WITH THE TOP SIDE OF THE OBSTACLE
		Obstacle obs = collisions[Collision.TOP_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				startKnockback(0, -1, 100, 0.4f);
				this.y = obs.y - size;	
			}else {
				walkVelocityY = 0;
				knockbackVelocityY = 0;
				this.y = obs.y - size;	
			}
		}
		//COLLISION WIDTH THE BOTTOM SIDE OF THE OBSTACLE
		obs = collisions[Collision.BOTTOM_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				startKnockback(0, 1, 100, 0.4f);
				this.y = obs.y + obs.height;
			}else {
				walkVelocityY = 0;
				knockbackVelocityY = 0;
				this.y = obs.y + obs.height;
			}
		}
		//COLLISION WITH THE LEFT SIDE OF THE OBSTACLE
		obs = collisions[Collision.LEFT_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				startKnockback(-1, 0, 100, 0.4f);
				this.x = obs.x - size;
			}else {
				walkVelocityX = 0;
				knockbackVelocityX = 0;
				this.x = obs.x - size;
			}
		}
		//COLLISION WITH THE RIGHT SIDE OF THE OBSTACLE
		obs = collisions[Collision.RIGHT_SIDE];
		if(obs != null) {
			if(obs instanceof BulletBouncer) {
				startKnockback(1, 0, 100, 0.4f);
				this.x = obs.x + obs.width;
			}else {
				walkVelocityX = 0;
				knockbackVelocityX = 0;
				this.x = obs.x + obs.width;
			}
		}
	}
	
	@Override
	public void getDamaged(float damage) {
		super.getDamaged(damage);
		if(!followplayer) followplayer = true;
	}
}
