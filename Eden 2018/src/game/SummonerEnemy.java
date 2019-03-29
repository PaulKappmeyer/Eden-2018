package game;

public class SummonerEnemy extends Enemy{

	float spawnTime = 3f;
	float tsls;
	
	public SummonerEnemy(float x, float y) {
		super(x, y);
		maxWalkspeed = 25;
		size = 20;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		
		tsls += tslf;
		if(tsls >= spawnTime) {
			resetWalkspeed();
			
			Enemy e = new Enemy(this.x, this.y);
			e.followplayer = true;
			e.startKnockback(0, 100, 1);
			Enemy e1 = new Enemy(this.x, this.y);
			e1.followplayer = true;
			e1.startKnockback(180, 100, 1);
			Game.currentMap.enemies.add(e);
			Game.currentMap.enemies.add(e1);
			
			tsls = 0;
		}
	}
	
}
