package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

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
		obstacles.add(new Stone(0, 0, Globals.width, 25));
		obstacles.add(new Stone(0, 25, 25, Globals.height));
		obstacles.add(new Stone(Globals.width-25, 25, 25, Globals.height));
		obstacles.add(new Stone(25, Globals.height-25, Globals.width/2 - 25*2, 25));
		obstacles.add(new Stone(Globals.width/2 + 25/2, Globals.height-25, Globals.width/2 - 25 - 25/2, 25));
		indoor = new Map(100, 100, obstacles, new ArrayList<>());
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(x + Globals.insetX, y + Globals.insetY, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x + Globals.insetX, y + Globals.insetY, width, height);
		g.setColor(Color.RED);
		g.fillRect(doorX + Globals.insetX, doorY + Globals.insetY, doorWidth, doorHeight);
		g.setColor(Color.BLACK);
		g.drawRect(doorX + Globals.insetX, doorY + Globals.insetY, doorWidth, doorHeight);

	}

	@Override
	public void update(float tslf) {
		Eden player = Globals.player;
		if(player.shotDirection == Direction.UP && player.state == Eden.WALKING) {
			if(player.x > doorX - 5 && player.x < doorX + 5 && player.y == this.y + this.height) {
				opened += tslf;
				if(opened >= openTime) {
					if(Game.state == Gamestate.RUNNING) {
						oldMap = Game.currentMap;
						Game.mapY--;
						Game.beginMapTransition(Direction.UP, indoor, Globals.width/2 - player.size/2, Globals.height - 25 - player.size);
					}
				}
			}
		}else {
			opened = 0;
		}
		
		if(Game.currentMap.equals(indoor)) {
			System.out.println("yes13");
			if(Globals.player.y + Globals.player.size >= Globals.height) {
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
