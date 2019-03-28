package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Game {

	int mapX = 1;
	int mapY = 1;
	Map[][]maps;
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
		enemies = new ArrayList<Enemy>();
		enemies.add(new Boss(650, 250));
		enemies.add(new JumpEnemy(500, 100));
		enemies.add(new Enemy(500, 200));
		enemies.add(new Enemy(500, 300));
		enemies.add(new Enemy(500, 350));
		enemies.add(new RoundEnemy(600, 100));

		Chest chest = new Chest(100, 100);
		Sign sign = new Sign(150, 550);
		maps = new Map[3][3];
		maps[1][1] = new Map(100, 400, stones, enemies, chest, sign);
		
		enemies = new ArrayList<>();
		stones = new ArrayList<>();
		stones.add(new Stone(0, 0, Globals.width, 25));
		stones.add(new Stone(0, 25, 25, Globals.height));
		maps[0][0] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
		stones.add(new Stone(0, 0, Globals.width, 25));
		maps[1][0] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
		stones.add(new Stone(0, 0, Globals.width, 25));
		stones.add(new Stone(Globals.width - 25, 25, 25, Globals.height));
		maps[2][0] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
		stones.add(new Stone(0, 0, 25, Globals.height));
		maps[0][1] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
//		maps[1][1] = new Map(400, 400, stones, enemies, chest, sign);
//		stones = new ArrayList<>();
//		stones.add(new Stone(Globals.width - 25, 0, 25, Globals.height));
		maps[2][1] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
		stones.add(new Stone(0, 0, 25, Globals.height));
		stones.add(new Stone(25, Globals.height-25, Globals.width, 25));
		maps[0][2] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
		stones.add(new Stone(0, Globals.height-25, Globals.width, 25));
		maps[1][2] = new Map(400, 400, stones, enemies, null, null);
		stones = new ArrayList<>();
		stones.add(new Stone(Globals.width-25, 0, 25, Globals.height));
		stones.add(new Stone(0, Globals.height-25, Globals.width, 25));
		maps[2][2] = new Map(400, 400, stones, enemies, null, null);
		
		currentMap = maps[mapX][mapY];
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
	int direction;
	public void switchMap() {
		if(Globals.player.gun != null) {
			Globals.player.gun.projectiles.removeAll(Globals.player.gun.projectiles);
		}
		switch (direction) {
		case Eden.RIGHT:
			Globals.player.x = 5;
			break;
		case Eden.LEFT:
			Globals.player.x = Globals.width - 5 - Globals.player.size;
			break;
		case Eden.DOWN:
			Globals.player.y = 5;
			break;
		case Eden.UP:
			Globals.player.y = Globals.height - 5 - Globals.player.size;
			break;
		default:
			break;
		}
		currentMap = maps[mapX][mapY];
	}

	public void beginMapTransition(int direction) {
		Game.state = Game.MAP_TRANSITION;
		this.direction = direction;
		Screen.startTransition(direction);
	}

	public void checkCollisionPlayerToWall() {
		if(Globals.player.x <= 0 && mapX > 0) {
			beginMapTransition(Eden.LEFT);
			mapX --;
		}
		if(Globals.player.y <= 0 && mapY > 0) {
			beginMapTransition(Eden.UP);
			mapY --;
		}
		if(Globals.player.x + Globals.player.size >= Globals.width && mapX < maps.length-1) {
			beginMapTransition(Eden.RIGHT);
			mapX ++;
		}
		if(Globals.player.y + Globals.player.size >= Globals.height && mapY < maps[mapX].length-1) {
			beginMapTransition(Eden.DOWN);
			mapY ++;
		}
	}
}
