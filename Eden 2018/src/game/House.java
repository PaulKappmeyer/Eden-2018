package game;

import java.awt.Color;
import java.awt.Graphics;

public class House extends Obstacle{

	int doorX;
	int doorY;
	int doorWidth = 16;
	int doorHeight = 16;

	public House(int x, int y, int width, int height) {
		super(x, y, width, height);

		this.doorX = this.x + this.width / 2 - doorWidth-2;
		this.doorY = this.y + this.height - doorHeight;
	}

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

	public void update(float tslf) {
		Eden player = Globals.player;
		if(Globals.checkCollisionRectangleToRectangle(this.x, this.y, this.width, this.height, player.x, player.y, player.size, player.size)) {
			
		}
	}
}
