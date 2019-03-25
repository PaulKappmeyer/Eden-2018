package game;

import java.util.ArrayList;

public class Map {

	Eden player = Globals.player;
	static ArrayList<Stone> stones;
	static Chest chest;
	static Sign sign;
	static RoundStone stoneRound;
	
	public Map() {
		stones = new ArrayList<>();
		stones.add(new Stone(500, 400, 125, 125));
		
		stones.add(new Stone(175, 250, 25, 100));
		stones.add(new Stone(200, 250, 25, 100));
		stones.add(new Stone(225, 250, 25, 100));
		
		stones.add(new Stone(175, 100, 100, 25));
		stones.add(new Stone(175, 125, 100, 25));
		stones.add(new Stone(175, 150, 100, 25));
		
		stones.add(new Stone(300, 250, 25, 100));
		stones.add(new Stone(300, 350, 125, 25));
		stones.add(new Stone(400, 250, 25, 100));
		stones.add(new Stone(300, 225, 75, 25));
		stoneRound = new RoundStone(100, 230, 30);
		chest = new Chest(100, 100);
		sign = new Sign(150, 550);
		Globals.enemies.add(new JumpEnemy(500, 100));
		Globals.enemies.add(new RoundEnemy(600, 100));
	}

	public void update(float tslf) {
		//TODO: Update System
		if(Game.state == Game.RUNNING) {
			Globals.player.update(tslf);
			for (Enemy e : Globals.enemies) {
				e.update(tslf);
			}
			for (Stone stone : stones) {
				stone.update(tslf);	
			}
			stoneRound.update(tslf);
		}

		chest.update(tslf);
		sign.update(tslf);
		
		//Map transition
		checkCollisionPlayerToWall();

		//TODO: Y-Sort
		ysort();
	}

	int runs = 0;
	public void switchMap() {
		Globals.enemies.removeAll(Globals.enemies);
		Globals.enemies.add(new Boss(650, 250));
		Globals.enemies.add(new JumpEnemy(500, 100));
		Globals.enemies.add(new Enemy(500, 200));
		Globals.enemies.add(new Enemy(500, 300));
		Globals.enemies.add(new Enemy(500, 350));
		Globals.enemies.add(new RoundEnemy(600, 100));
		for (int i = 0; i < runs; i++) {
			Globals.enemies.add(new Enemy(600, 50 + Globals.random.nextInt(500)));
		}
		runs ++;
		Globals.player.x = 5;
		if(Globals.player.gun != null) {
			Globals.player.gun.projectiles.removeAll(Globals.player.gun.projectiles);
			Globals.player.gun.shells.removeAll(Globals.player.gun.shells);
		}
	}

	public void beginMapTransition() {
		Game.state = Game.MAP_TRANSITION;
	}

	public void checkCollisionPlayerToWall() {
		if(player.x < 0) {
		}
		if(player.y < 0) {
		}
		if(player.x + player.size >= Globals.width) {
			beginMapTransition();
		}
		if(player.y + player.size > Globals.height) {
		}
	}

	/**
	 * This function sorts the list of the enemies based on their y-position, so a higher y-value gets displayed on top of the lower y-value, 
	 * this creates the feeling of a perspective
	 */
	public static void ysort() {
		//TODO: using Collections . sort();
		ArrayList<Enemy> newEnemies = Globals.enemies;
		for (int i = 0; i < newEnemies.size() - 1; i++) {
			Enemy e = Globals.enemies.get(i);
			Enemy e1 = Globals.enemies.get(i + 1);

			if(e.y > e1.y) {
				newEnemies.set(i, e1);
				newEnemies.set(i + 1, e);
				i = 0;
			}
		}
		Globals.enemies = newEnemies;
	}
}
