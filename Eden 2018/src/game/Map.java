package game;

import java.util.ArrayList;

public class Map {

	Eden player = Globals.player;
	static Stone stone;
	
	public Map() {
		stone = new Stone(500, 400, 125, 20);
		Globals.enemies.add(new Enemy(500, 100));
	}
	
	public void update(float tslf) {
		//TODO: Update System
		Globals.player.update(tslf);
		for (Enemy e : Globals.enemies) {
			e.update(tslf);
		}
		
		stone.update(tslf);
		
		//Map transition
		checkCollisionPlayerToWall();
		
		//TODO: Y-Sort
		ysort();
	}
	
	int runs = 0;
	public void switchMap() {
		Globals.enemies.removeAll(Globals.enemies);
		Globals.enemies.add(new Boss(450, 250));
		Globals.enemies.add(new Enemy(500, 100));
		Globals.enemies.add(new Enemy(500, 200));
		Globals.enemies.add(new Enemy(500, 300));
		Globals.enemies.add(new Enemy(500, 350));
		for (int i = 0; i < runs; i++) {
			Globals.enemies.add(new Enemy(600, 50 + Globals.random.nextInt(500)));
		}
		runs ++;
		Globals.player.x = 5;
		Globals.player.gun.projectiles.removeAll(Globals.player.gun.projectiles);
		Globals.player.gun.shells.removeAll(Globals.player.gun.shells);
//		for (int i = 0; i < 4; i++) {
//			Globals.enemies.add(new Enemy(Globals.random.nextInt(400), Globals.random.nextInt(400)));	
//		}
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
//		Collections.sort(Globals.enemies, new Comparator<Enemy>() {
//			@Override
//			public int compare(Enemy e, Enemy e1)
//			{
//				if(e.y > e1.y) {
//					return Globals.enemies.indexOf(e1);
//				}
//				return Globals.enemies.indexOf(e1);
//			}
//		});
		
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
