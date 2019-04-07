package game;

import java.awt.Graphics;
import java.util.ArrayList;

import enemies.Boss;
import enemies.Enemy;
import enemies.ZombieEnemy;
import enemies.JumpEnemy;
import enemies.RoundEnemy;
import enemies.SummonerEnemy;

public class Game {

	static int mapX = 1;
	static int mapY = 1;
	Map[][]maps;
	public static Map currentMap;

	public static Gamestate state = Gamestate.RUNNING;

	static ScreenTransition transition = new ScreenTransition();

	public Game() {
		ArrayList<Obstacle>obstacles = new ArrayList<>();
		obstacles.add(new Stone(500, 400, 125, 125));
		obstacles.add(new Stone(175, 250, 25, 100));
		obstacles.add(new Stone(200, 250, 25, 100));
		obstacles.add(new Stone(225, 250, 25, 100));
		obstacles.add(new Stone(175, 100, 100, 25));
		obstacles.add(new Stone(175, 125, 100, 25));
		obstacles.add(new Stone(175, 150, 100, 25));
		obstacles.add(new Stone(300, 250, 25, 100));
		obstacles.add(new Stone(300, 350, 125, 25));
		obstacles.add(new Stone(400, 250, 25, 100));
		obstacles.add(new Stone(300, 225, 75, 25));

		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		enemies.add(new JumpEnemy(500, 100));
		enemies.add(new ZombieEnemy(600, 100));
		enemies = new ArrayList<Enemy>();
		enemies.add(new Boss(650, 250));
		enemies.add(new JumpEnemy(500, 100));
		enemies.add(new ZombieEnemy(500, 200));
		enemies.add(new ZombieEnemy(500, 300));
		enemies.add(new SummonerEnemy(500, 350));
		enemies.add(new SummonerEnemy(500, 250));
		enemies.add(new SummonerEnemy(500, 150));
		enemies.add(new SummonerEnemy(500, 170));
		enemies.add(new RoundEnemy(600, 100));

		//enemies = new ArrayList<ZombieEnemy>();
		obstacles.add(new Chest(100, 100, 16, 16));
		obstacles.add(new Sign(150, 550, 16, 16));
		obstacles.add(new House(500, 100, 64, 32));
		obstacles.add(new BulletBouncer(350, 400, 10, 100));
		obstacles.add(new BulletBouncer(150, 400, 10, 100));
		maps = new Map[3][3];
		maps[1][1] = new Map(100, 400, obstacles, enemies);

		enemies = new ArrayList<>();
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, 0, Globals.width, 25));
		obstacles.add(new Stone(0, 25, 25, Globals.height));
		maps[0][0] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, 0, Globals.width, 25));
		maps[1][0] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, 0, Globals.width, 25));
		obstacles.add(new Stone(Globals.width - 25, 25, 25, Globals.height));
		maps[2][0] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, 0, 25, Globals.height));
		maps[0][1] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(Globals.width - 25, 0, 25, Globals.height));
		maps[2][1] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, 0, 25, Globals.height));
		obstacles.add(new Stone(25, Globals.height-25, Globals.width, 25));
		maps[0][2] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, Globals.height-25, Globals.width, 25));
		maps[1][2] = new Map(400, 400, obstacles, enemies);
		obstacles = new ArrayList<>();
		obstacles.add(new Stone(Globals.width-25, 0, 25, Globals.height));
		obstacles.add(new Stone(0, Globals.height-25, Globals.width, 25));
		maps[2][2] = new Map(400, 400, obstacles, enemies);

		currentMap = maps[mapX][mapY];
	}

	//TODO:Update System
	public void update(float tslf) {
		//RUNNING
		if(state == Gamestate.RUNNING) {
			currentMap.update(tslf);
			//Map transition
			checkCollisionPlayerToWall();

			//INTERACTING
		}else if(state == Gamestate.INTERACTING){
			for (Obstacle obs : currentMap.obstacles) {
				obs.update(tslf);
			}
		}

		//SCREEN TRANSITION
		else if(Game.state == Gamestate.MAP_TRANSITION_IN) {
			currentMap.update(tslf/2);
			transition.update(tslf);
		}
		else if(Game.state == Gamestate.MAP_TRANSITION_OUT) {
			transition.update(tslf);
		}

		//RESETING : SET NEW MAP
		else if(state == Gamestate.RESET) {
			state = Gamestate.MAP_TRANSITION_OUT;
			switchMap(newPlayerX, newPlayerY, newMap); 
		}
	}

	public void draw(Graphics g) {
		currentMap.draw(g);

		if(Game.state == Gamestate.MAP_TRANSITION_IN || Game.state == Gamestate.MAP_TRANSITION_OUT || Game.state == Gamestate.RESET) {	
			transition.draw(g);
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------
	static Direction direction;
	static float newPlayerX;
	static float newPlayerY;
	static Map newMap;
	public void switchMap(float newPlayerX, float newPlayerY, Map newMap) {
		if(Globals.player.gun != null) {
			for (int i = 0; i < Globals.player.gun.projectiles.length; i++) {
				Globals.player.gun.projectiles[i].deactivate();
			}
		}

		Globals.player.x = newPlayerX;
		Globals.player.y = newPlayerY;
		
		currentMap = newMap;
	}

	public static void beginMapTransition(Direction direction, Map newMap, float newPlayerX, float newPlayerY) {
		if(Game.state == Gamestate.MAP_TRANSITION_IN) return;
		Game.state = Gamestate.MAP_TRANSITION_IN;
		Game.direction = direction;
		Game.newPlayerX = newPlayerX;
		Game.newPlayerY = newPlayerY;
		Game.newMap = newMap;
		transition.startTransition(direction);
	}

	//Transition
	public void checkCollisionPlayerToWall() {
		if(Game.state == Gamestate.MAP_TRANSITION_IN) return;
		if(Globals.player.x <= 0 && mapX > 0) {
			mapX --;
			beginMapTransition(Direction.LEFT, maps[mapX][mapY], Globals.width - 5 - Globals.player.size, Globals.player.y);
		}
		if(Globals.player.y <= 0 && mapY > 0) {
			mapY --;
			beginMapTransition(Direction.UP, maps[mapX][mapY], Globals.player.x, Globals.height - 5 - Globals.player.size);
		}
		if(Globals.player.x + Globals.player.size >= Globals.width && mapX < maps.length-1) {
			mapX ++;
			beginMapTransition(Direction.RIGHT, maps[mapX][mapY], 5, Globals.player.y);
		}
		if(Globals.player.y + Globals.player.size >= Globals.height && mapY < maps[mapX].length-1) {
			mapY ++;
			beginMapTransition(Direction.DOWN, maps[mapX][mapY], Globals.player.x, 5);
		}
	}
}
