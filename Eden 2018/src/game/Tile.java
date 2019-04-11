package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {

	float x;
	float y;
	int width;
	int height;
	int id;
	BufferedImage image;
	
	public Tile(float x, float y) {
		this.x = x;
		this.y = y;
		this.width = 16;
		this.height = 16;
	}
	public Tile(int x, int y, int width, int height, int id, BufferedImage image) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.image = image;
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, (int)x + Globals.insetX, (int)y + Globals.insetY, width, height,  null);
	}
	
	public void update(float tslf) {
	}
}
