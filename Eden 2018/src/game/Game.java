package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Game {

	Map map1;
	Map map2;
	static Map currentMap;
	
	public static final int RUNNING = 1;
	public static final int MAP_TRANSITION = 3;
	public static final int RESET = 2;
	public static final int MAP_TRANSITION_OUT = 4;
	public static final int INTERACTING = 5;
	public static int state = RUNNING;

	public Game() {
		ArrayList<Stone>stones = new ArrayList<>();
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
		
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		enemies.add(new JumpEnemy(500, 100));
		enemies.add(new Enemy(600, 100));
		map1 = new Map(200, 500, stones, enemies);
		
		enemies = new ArrayList<Enemy>();
		enemies.add(new Boss(650, 250));
		enemies.add(new JumpEnemy(500, 100));
		enemies.add(new Enemy(500, 200));
		enemies.add(new Enemy(500, 300));
		enemies.add(new Enemy(500, 350));
		enemies.add(new RoundEnemy(600, 100));
		map2 = new Map(100, 100, new ArrayList<Stone>(), enemies);
		
		currentMap = map1;
	}
	
	public void update(float tslf) {
		//TODO:Update System
		if(state == RUNNING) {
			currentMap.update(tslf);
			//Map transition
			checkCollisionPlayerToWall();
			
		}else if(state == INTERACTING){
			currentMap.update(tslf);
		}
		else if(state == RESET) {
			state = MAP_TRANSITION_OUT;
			switchMap(); 
		}
	}
	
	public void draw(Graphics g) {
		currentMap.draw(g);
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------
	
	public void switchMap() {
		Globals.player.x = 5;
		if(Globals.player.gun != null) {
			Globals.player.gun.projectiles.removeAll(Globals.player.gun.projectiles);
		}
		currentMap = map2;
	}

	public void beginMapTransition() {
		Game.state = Game.MAP_TRANSITION;
	}

	public void checkCollisionPlayerToWall() {
		if(Globals.player.x < 0) {
		}
		if(Globals.player.y < 0) {
		}
		if(Globals.player.x + Globals.player.size >= Globals.width) {
			beginMapTransition();
		}
		if(Globals.player.y + Globals.player.size > Globals.height) {
		}
	}
}
