/*
 * 
 */
package enemies;

import game.Game;

/**
 * 
 * @author Paul
 *
 */
public class BossEnemy extends ZombieEnemy{

	float timeSinceLastSpawn;
	float spawnTime = 0.1f;
	int numOfSpawns = 16;
	int atSpawn = 0;

	float attackTime = 5;
	float timeSinceLastAttack;

	public BossEnemy(float x, float y) {
		super(x, y);
		this.size = 20;
		this.MAX_WALK_SPEED = 12;

		this.MAX_HEALTH = 1000;
		this.health = MAX_HEALTH;

		bulletImpact = 50;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);

		if(timeSinceLastAttack >= attackTime) {
			this.resetSpeedUp();
			this.followplayer = false;
			if(timeSinceLastSpawn >= spawnTime) {
				float centerX = (float) (this.x + this.size/2);
				float centerY = (float) (this.y + this.size/2);

				float angle = 360/numOfSpawns * atSpawn;
				ZombieEnemy z = new ZombieEnemy(centerX, centerY);
				z.followplayer = true;
				float sin = (float) Math.sin(Math.toRadians(angle));
				float cos = (float) Math.cos(Math.toRadians(angle));

				this.lookDirection = getDirection(sin, cos);
				
				z.startKnockback(sin, cos, 1000, 0.1f);
				Game.currentMap.enemies.add(z);
				
				atSpawn ++;
				timeSinceLastSpawn = 0;
			} else {
				timeSinceLastSpawn += tslf;
			}

			if(atSpawn == numOfSpawns) {
				atSpawn = 0;
				timeSinceLastAttack = 0;
			}
		} else {
			timeSinceLastAttack += tslf;
		}
	}
}
