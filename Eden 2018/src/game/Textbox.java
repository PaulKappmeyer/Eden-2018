package game;

import java.awt.Color;
import java.awt.Graphics;

public class Textbox {

	public float x = 50;
	public float y = Globals.height;
	public int width = Globals.width - 100;
	public int height = 100;
	public int speed = 1000;
	public static final int HIDE = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_DOWN = 2;
	public static final int HIGHEST_POINT = 3;
	public int state;
	String[] text;
	int index = 0;

	public Textbox(String[] text) {
		this.text = text;
	}

	public void draw(Graphics g) {
		if(state != HIDE) {
			g.setColor(Color.BLACK);
			g.drawRoundRect((int) (x + Globals.insetX), (int) (y + Globals.insetY), width, height, 10, 10);
			g.drawString(text[index], (int)(x + Globals.insetX + 10), (int)(y + Globals.insetY + 15 ));
		}
	}

	public void update(float tslf) {
		if(state == MOVE_UP) {
			if(y <= Globals.height - height - 10) {
				state = HIGHEST_POINT;
				y = Globals.height - height - 10;
			}else {
				y -= speed * tslf;
			}
		}else if(state == MOVE_DOWN){
			if(y >= Globals.height) {
				y = Globals.height;
				state = HIDE;
			}else {
				y += speed * tslf;
			}
		}
	}

	public void moveUp() {
		state = MOVE_UP;
	}
	public void moveDown() {
		state = MOVE_DOWN;
	}
}
