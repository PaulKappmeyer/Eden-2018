/*
 * 
 */
package game;

import java.awt.Graphics;
import java.io.File;

/**
 * 
 * @author Paul
 *
 */
public final class Game {

	public static Gamestate state = Gamestate.RUNNING;
	public static Map currentMap;
	public static int mapX = 0;
	public static int mapY = 0;
	static ScreenTransition transition = new ScreenTransition();
	
	Map[][]maps;

	public Game() {
		//Maps
		maps = new Map[1][1];
		File path = new File("res/maps/Eden_Testmap_1.2.txt");
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
		if (state == Gamestate.RUNNING) {
			currentMap.update(tslf);

			//Map transition
			checkCollisionPlayerToWall();
		}
		//INTERACTING
		else if (state == Gamestate.INTERACTING){
			for (Obstacle obs : currentMap.obstacles) {
				obs.update(tslf);
			}
		}
		//SCREEN TRANSITION
		else if (Game.state == Gamestate.MAP_TRANSITION_IN) {
			currentMap.update(tslf/2);
			transition.update(tslf);
		}
		else if (Game.state == Gamestate.MAP_TRANSITION_OUT) {
			transition.update(tslf);
		}
		//RESETING : SET NEW MAP
		else if (state == Gamestate.RESET) {
			state = Gamestate.MAP_TRANSITION_OUT;
			switchMap(newPlayerX, newPlayerY, newMap); 
		}
	}

	//--------------------------------------------DRAWING------------------------------------------------------------------------------------
	public void draw(Graphics g) {
		currentMap.draw(g);

		if (Game.state == Gamestate.MAP_TRANSITION_IN || Game.state == Gamestate.MAP_TRANSITION_OUT || Game.state == Gamestate.RESET) {	
			transition.draw(g);
		}
	}

	//---------------------------------------------------------------------------------------------------------------------------------------
	static Direction direction;
	static float newPlayerX;
	static float newPlayerY;
	static Map newMap;
	public void switchMap(float newPlayerX, float newPlayerY, Map newMap) {
		if (Globals.player.gun != null) {
			for (int i = 0; i < Globals.player.gun.projectiles.length; i++) {
				Globals.player.gun.projectiles[i].deactivate();
			}
		}

		Globals.player.x = newPlayerX;
		Globals.player.y = newPlayerY;

		currentMap = newMap;
	}

	public static void beginMapTransition(Direction direction, Map newMap, float newPlayerX, float newPlayerY) {
		if (Game.state == Gamestate.MAP_TRANSITION_IN) {
			return;
		}
		Game.state = Gamestate.MAP_TRANSITION_IN;
		Game.direction = direction;
		Game.newPlayerX = newPlayerX;
		Game.newPlayerY = newPlayerY;
		Game.newMap = newMap;
		transition.startTransition(direction);
	}

	//Transition
	public void checkCollisionPlayerToWall() {
		if (Game.state == Gamestate.MAP_TRANSITION_IN) {
			return;
		}
		if (Globals.player.x <= 0 && mapX > 0) {
			mapX --;
			beginMapTransition(Direction.LEFT, maps[mapX][mapY], Game.currentMap.mapWidth - 5 - Globals.player.size, Globals.player.y);
		}
		if (Globals.player.y <= 0 && mapY > 0) {
			mapY --;
			beginMapTransition(Direction.UP, maps[mapX][mapY], Globals.player.x, Game.currentMap.mapHeight - 5 - Globals.player.size);
		}
		if (Globals.player.x + Globals.player.size >= Game.currentMap.mapWidth && mapX < maps.length-1) {
			mapX ++;
			beginMapTransition(Direction.RIGHT, maps[mapX][mapY], 5, Globals.player.y);
		}
		if (Globals.player.y + Globals.player.size >= Game.currentMap.mapHeight && mapY < maps[mapX].length-1) {
			mapY ++;
			beginMapTransition(Direction.DOWN, maps[mapX][mapY], Globals.player.x, 5);
		}
	}
}
