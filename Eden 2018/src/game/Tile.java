package game;

import java.awt.Color;
import java.awt.Graphics;

public class Tile {

	float x;
	float y;
	int width;
	int height;
	int id;
	
	public Tile(float x, float y) {
		this.x = x;
		this.y = y;
		this.width = 16;
		this.height = 16;
	}
	public Tile(int x, int y, int width, int height, int id) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, width, height);
	}
	
	public void update(float tslf) {
		
	}
}
