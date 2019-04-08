package game;

import java.awt.Graphics;
import java.io.File;

public class Game {

	static int mapX = 0;
	static int mapY = 0;
	Map[][]maps;
	public static Map currentMap;

	public static Gamestate state = Gamestate.RUNNING;

	static ScreenTransition transition = new ScreenTransition();

	public Game() {
		maps = new Map[1][1];
		
		File path = new File(".\\src\\maps\\Eden_Testmap_1.txt");

		try {
			maps[0][0] = MapLoader.loadMap(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		currentMap = maps[mapX][mapY];
	}

	//TODO:Update System
	public void update(float tslf) {
		//RUNNING
		if(state == Gamestate.RUNNING) {
			currentMap.update(tslf);

			//Map transition
			checkCollisionPlayerToWall();
		}
		//INTERACTING
		else if(state == Gamestate.INTERACTING){
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

	//--------------------------------------------DRAWING------------------------------------------------------------------------------------
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
