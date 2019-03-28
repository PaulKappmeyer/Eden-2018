package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Map {

	Eden player = Globals.player;
	ArrayList<Enemy> enemies;
	ArrayList<Stone> stones;
	ArrayList<Shell> shells;
	Chest chest;
	Sign sign;
	
	public Map(int playerX, int playerY, ArrayList<Stone> stones, ArrayList<Enemy> enemies, Chest chest, Sign sign) {
		this.player.x = playerX;
		this.player.y = playerY;
		this.stones = stones;
		this.enemies = enemies;
		this.shells = new ArrayList<Shell>();
		this.chest = chest;
		this.sign = sign;
	}
	
	public void draw(Graphics g) {
		//Draw the shells
		for (Shell shell : shells) {
			shell.draw(g);
		}
		//Draw the players gun
		if(Globals.player.gun != null)Globals.player.gun.draw(g);
		//Draw the bosses gun
		for (Enemy enemy : enemies) {
			if(enemy instanceof Boss) {
				Boss boss = (Boss) enemy;
				boss.gun.draw(g);
			}
		}
		//Draw the enemies
		for (Enemy e : enemies) {
			e.draw(g);
		}
		
		for (Stone stone : Game.currentMap.stones) {
			stone.draw(g);
		}
		if(chest != null) chest.draw(g);
		if(sign != null) sign.draw(g);
		
		//Draw the player
		Globals.player.draw(g);
	}

	public void update(float tslf) {
		//TODO: Update System
		if(Game.state == Game.RUNNING) {
			//Updating the shells
			for (Shell shell : shells) {
				shell.update(tslf);
			}
			//Updating the player
			Globals.player.update(tslf);
			//Updating the enemies
			for (Enemy e : enemies) {
				e.update(tslf);
			}
			
			//Removal of the enemies
			for (int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				if(e.canBeRemoved())enemies.remove(e);
			}
		}

		if(chest != null) chest.update(tslf);
		if(sign != null) sign.update(tslf);

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
