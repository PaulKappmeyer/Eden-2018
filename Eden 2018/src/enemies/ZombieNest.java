/*
 * 
 */
package enemies;

import game.Direction;
import game.Game;

public class ZombieNest extends Enemy{

	int numOfSpawns = 10;
	
	public ZombieNest(float x, float y) {
		super(x, y);
		this.size = 16;
		this.lookDirection = Direction.UNDEFINED;
		
		this.MAX_HEALTH = 2000;
		this.health = MAX_HEALTH;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
	}
	
	@Override
	public void getDamaged(float damage) {
		super.getDamaged(damage);
		
		if(alive == false) {
			float centerX = (float) (this.x + this.size/2);
			float centerY = (float) (this.y + this.size/2);
			
			for (int i = 0; i < numOfSpawns; i++) {
				float angle = 360/numOfSpawns * i;
				ZombieEnemy z = new ZombieEnemy(centerX, centerY);
				z.followplayer = true;
				z.startKnockback(Math.sin(Math.toRadians(angle)), Math.cos(Math.toRadians(angle)), 1000, 0.1f);
				Game.currentMap.enemies.add(z);
			}
		}
	}
	
}
