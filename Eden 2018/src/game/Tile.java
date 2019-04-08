package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Tile {

	float x;
	float y;
	int width;
	int height;
	int id;
	
	static BufferedImage image_8;
	static BufferedImage image_9;
	static BufferedImage image_10;
	static BufferedImage image_12;
	static BufferedImage image_14;
	static BufferedImage image_15;
	static BufferedImage image_16;
	static BufferedImage image_21;
	static BufferedImage image_31;
	static BufferedImage image_36;
	static BufferedImage image_37;
	static BufferedImage image_42;
	BufferedImage image;
	
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
		
		switch (id) {
		case 8:
			image = image_8;
			break;
		case 9:
			image = image_9;
			break;
		case 10:
			image = image_10;
			break;
		case 12:
			image = image_12;
			break;
		case 14:
			image = image_14;
			break;
		case 15:
			image = image_15;
			break;
		case 16:
			image = image_16;
			break;
		case 21:
			image = image_21;
			break;
		case 31:
			image = image_31;
			break;
		case 36:
			image = image_36;
			break;
		case 37:
			image = image_37;
			break;
		case 42:
			image = image_42;
			break;
			
		default:
			System.out.println("id not found:" + id);
			image = image_31;
			break;
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, (int)x + Globals.insetX, (int)y + Globals.insetY, null);
	}
	
	public void update(float tslf) {
		
	}
}
