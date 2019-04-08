package game;

import java.awt.Graphics;
import java.util.ArrayList;

import enemies.Boss;
import enemies.Enemy;
import guns.Shell;

public class Map {

	Eden player = Globals.player;
	public ArrayList<Enemy> enemies;
	public ArrayList<Obstacle> obstacles;
	public ArrayList<Shell> shells;
	public Tile[][] tiles;
	int tilesWidth;
	int tilesHeight;
	
	public Map(int playerX, int playerY, ArrayList<Obstacle> obstacles, ArrayList<Enemy> enemies, Tile [][] tiles) {
		this.player.x = playerX;
		this.player.y = playerY;
		this.obstacles = obstacles;
		this.enemies = enemies;
		this.shells = new ArrayList<Shell>();
		this.tiles = tiles;
		this.tilesWidth = tiles.length;
		this.tilesHeight = tiles[0].length;
	}

	public void draw(Graphics g) {
		//Drawing the tiles
		for (int i = 0; i < tilesWidth; i++) {
			for (int j = 0; j < tilesHeight; j++) {
				tiles[i][j].draw(g);
			}
		}
		
		//Drawing the shells
		for (Shell shell : shells) {
			shell.draw(g);
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
			e.draw(g);
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
	}
	
	//TODO: UPDATE
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
