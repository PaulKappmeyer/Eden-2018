/*
 * 
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import game.Direction;
import game.Game;
import game.Gamestate;
import game.Globals;
import game.Map;
import game.Obstacle;
import player.Eden;

/**
 * 
 * @author Paul
 *
 */
public class House extends Obstacle{

	int doorX;
	int doorY;
	int doorWidth = 16;
	int doorHeight = 16;
	float openTime = 0.25f;
	float opened = 0;
	Map indoor;
	Map oldMap;

	public House(int x, int y, int width, int height) {
		super(x, y, width, height);

		this.doorX = this.x + this.width / 2 - doorWidth-2;
		this.doorY = this.y + this.height - doorHeight;

		ArrayList<Obstacle>obstacles = new ArrayList<>();
		obstacles.add(new Stone(0, 0, Globals.screenWidth, 25));
		obstacles.add(new Stone(0, 25, 25, Globals.screenHeight));
		obstacles.add(new Stone(Globals.screenWidth-25, 25, 25, Globals.screenHeight));
		obstacles.add(new Stone(25, Globals.screenHeight-25, Globals.screenWidth/2 - 25*2, 25));
		obstacles.add(new Stone(Globals.screenWidth/2 + 25/2, Globals.screenHeight-25, Globals.screenWidth/2 - 25 - 25/2, 25));
		indoor = new Map(Game.currentMap.mapWidth, Game.currentMap.mapHeight, 100, 100, obstacles, new ArrayList<>(), Game.currentMap.tiles);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		g.setColor(Color.RED);
		g.fillRect(doorX, doorY, doorWidth, doorHeight);
		g.setColor(Color.BLACK);
		g.drawRect(doorX, doorY, doorWidth, doorHeight);

	}

	@Override
	public void update(float tslf) {
		Eden player = Globals.player;
		if (player.shotDirection == Direction.UP && player.state == Eden.WALKING) {
			if (player.x > doorX - 5 && player.x < doorX + 5 && player.y == this.y + this.height) {
				opened += tslf;
				if (opened >= openTime) {
					if (Game.state == Gamestate.RUNNING) {
						oldMap = Game.currentMap;
						Game.mapY--;
						Game.beginMapTransition(Direction.UP, indoor, Globals.screenWidth/2 - player.size/2, Globals.screenHeight - 25 - player.size);
					}
				}
			}
		}else {
			opened = 0;
		}
		
		if (Game.currentMap.equals(indoor)) {
			System.out.println("yes13");
			if (Globals.player.y + Globals.player.size >= Globals.screenHeight) {
				System.out.println("yes");
				Game.beginMapTransition(Direction.DOWN, oldMap, doorX, doorY + doorHeight + 5);
			}
		}
	}

	class Teleporter{
		int x;
		int y;
		int width;
		int height;
	}
}
