package game;

import java.awt.Color;
import java.awt.Graphics;

public class Tile {

	float x;
	float y;
	int width;
	int height;
	
	public Tile(float x, float y) {
		this.x = x;
		this.y = y;
		this.width = 16;
		this.height = 16;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, width, height);
	}
	
	public void update(float tslf) {
		
	}
}
