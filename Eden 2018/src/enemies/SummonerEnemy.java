package enemies;

import game.Game;

public class SummonerEnemy extends ZombieEnemy{

	float spawnTime = 3f;
	float tsls;
	float power = 200;
	float time = 1;
	
	public SummonerEnemy(float x, float y) {
		super(x, y);
		MAX_WALK_SPEED = 25;
		size = 20;
		
		this.MAX_HEALTH = 200;
		this.health = MAX_HEALTH;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		
		tsls += tslf;
		if(tsls >= spawnTime) {
			resetSpeedUp();
			
			ZombieEnemy e = new ZombieEnemy(this.x, this.y);
			e.followplayer = true;
			e.startKnockback(Math.sin(Math.toRadians(walkAngle + 135)), Math.cos(Math.toRadians(walkAngle + 135)), power, time);
			ZombieEnemy e1 = new ZombieEnemy(this.x, this.y);
			e1.followplayer = true;
			e1.startKnockback(Math.sin(Math.toRadians(walkAngle - 135)), Math.cos(Math.toRadians(walkAngle - 135)), power, time);
			ZombieEnemy e2 = new ZombieEnemy(this.x, this.y);
			e2.followplayer = true;
			e2.startKnockback(this.walkVelocityX, this.walkVelocityY, power, time);
			Game.currentMap.enemies.add(e);
			Game.currentMap.enemies.add(e1);
			Game.currentMap.enemies.add(e2);
			 
			tsls = 0;
		}
	}
	
}
