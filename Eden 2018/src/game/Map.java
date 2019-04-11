package game;

import java.awt.Graphics;
import java.util.ArrayList;

import enemies.Boss;
import enemies.Enemy;
import guns.Shell;
import player.Eden;

public class Map {

	public int mapWidth;
	public int mapHeight;
	Eden player = Globals.player;
	public ArrayList<Enemy> enemies;
	public ArrayList<Obstacle> obstacles;
	public ArrayList<Shell> shells;
	public Tile[][] tiles;
	int tilesWidth;
	int tilesHeight;
	public float worldX;
	public float worldY;

	//-----------------------------------------------------------CONSTRUCTORS------------------------------------------
	public Map(int mapWidth, int mapHeight, int playerX, int playerY, ArrayList<Obstacle> obstacles, ArrayList<Enemy> enemies, Tile [][] tiles) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.player.x = playerX;
		this.player.y = playerY;
		this.obstacles = obstacles;
		this.enemies = enemies;
		this.shells = new ArrayList<Shell>();
		this.tiles = tiles;
		this.tilesWidth = tiles.length;
		this.tilesHeight = tiles[0].length;
	}

	//-----------------------------------------------------------DRAWING------------------------------------------
	public void draw(Graphics g) {
		worldX = -Globals.player.x + Globals.screenWidth / 2;
		worldY = -Globals.player.y + Globals.screenHeight / 2;
		if(worldX > 0) worldX = 0;
		if(worldY > 0) worldY = 0;
		if(Math.abs(worldX) > mapWidth - Globals.screenWidth) {
			worldX = -mapWidth + Globals.screenWidth;
		}
		if(Math.abs(worldY) > mapHeight - Globals.screenHeight) {
			worldY = -mapHeight + Globals.screenHeight;
		}

		g.translate((int)worldX, (int)worldY);

		//Drawing the tiles
		for (int i = 0; i < tilesWidth; i++) {
			for (int j = 0; j < tilesHeight; j++) {
				Tile t = tiles[i][j];
				if(t.x + worldX < Globals.screenWidth && t.x + t.width > Math.abs(worldX) && t.y + worldY < Globals.screenHeight && t.y + t.height > Math.abs(worldY)) {
					t.draw(g);
				}
			}
		}
		
		//Drawing the shells
		for (Shell shell : shells) {
			if(shell.x + worldX < Globals.screenWidth && shell.x + Shell.SIZE > Math.abs(worldX) && shell.y + worldY < Globals.screenHeight && shell.y + Shell.SIZE > Math.abs(worldY)) {
				shell.draw(g);
			}
		}

		//Drawing the players gun
		if(Globals.player.gun != null)Globals.player.gun.draw(g);
		//Drawing the bosses gun
		for (Enemy enemy : enemies) {
			if(enemy instanceof Boss) {
				Boss boss = (Boss) enemy;
				boss.gun.draw(g);
			}
		}
		//Drawing the enemies
		for (Enemy e : enemies) {
			if(e.x + worldX < Globals.screenWidth && e.x + e.size > Math.abs(worldX) && e.y + worldY < Globals.screenHeight && e.y + e.size > Math.abs(worldY)) {
				e.draw(g);
			}
		}

		//Screenshake
		if(GameDrawer.screenshake) {
			g.translate(GameDrawer.screenshakeX, GameDrawer.screenshakeY);
		}

		//Drawing the obstacles
		for (Obstacle obs : obstacles) {
			obs.draw(g);
		}

		//Screenshake
		if(GameDrawer.screenshake) {
			g.translate(-GameDrawer.screenshakeX, -GameDrawer.screenshakeY);
		}

		//Drawing the player
		Globals.player.draw(g);

		g.translate((int)-worldX, (int)-worldY);
	}

	//TODO: UPDATE
	//-----------------------------------------------------------UPDATING------------------------------------------
	public void update(float tslf) {
		//Updating the shells
		for (Shell shell : shells) {
			shell.update(tslf);
		}

		//Updating the obstacles
		for (Obstacle obs : obstacles) {
			obs.update(tslf);
		}

		//Updating the player
		Globals.player.update(tslf);

		//Updating the enemies && Removal of the enemies; note: this loop runs backwards because we may delete objects
		int size = enemies.size() - 1;
		for (int i = size; i >= 0; i--) {
			Enemy e = enemies.get(i);
			e.update(tslf);
			if(e.canBeRemoved())enemies.remove(e);
		}

		//TODO: Y-Sort
		ysort();
	}

	//-----------------------------------------------------------METHODS------------------------------------------
	/**
	 * This function sorts the list of the enemies based on their y-position, so a higher y-value gets displayed on top of the lower y-value, 
	 * this creates the feeling of a perspective
	 */
	public void ysort() {
		//TODO: using Collections . sort();
		ArrayList<Enemy> newEnemies = enemies;
		for (int i = 0; i < newEnemies.size() - 1; i++) {
			Enemy e = enemies.get(i);
			Enemy e1 = enemies.get(i + 1);

			if(e.y > e1.y) {
				newEnemies.set(i, e1);
				newEnemies.set(i + 1, e);
				i = 0;
			}
		}
		enemies = newEnemies;
	}
}
