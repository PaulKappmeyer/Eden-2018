package game;

import java.awt.Color;
import java.awt.Graphics;

public class Textbox {

	float x = 50;
	float y = Globals.height;
	float width = 0;
	float maxWidth = Globals.width - 100;
	float height = 100;
	int speed = 1000;
	float time;
	public static final int HIDE = 0;
	public static final int APPEAR = 1;
	public static final int DISAPPEAR = 2;
	public static final int HIGHEST_POINT = 3;
	public int state = HIDE;
	public static final int FROM_TOP = 1;
	public static final int FROM_BOTTOM = 2;
	public int direction;
	String[] text;
	int index = 0;

	public Textbox(String[] text) {
		this.text = text;
	}

	public void draw(Graphics g) {
		if(state != HIDE) {
			g.setColor(Color.BLACK);
			g.drawRoundRect((int) (x + Globals.insetX), (int) (y + Globals.insetY), (int)width, (int)height, 10, 10);
			g.setColor(new Color(100, 100, 100, 100));
			g.fillRoundRect((int) (x + Globals.insetX), (int) (y + Globals.insetY), (int)width, (int)height, 10, 10);
			if(state == HIGHEST_POINT) {
				g.setColor(Color.BLACK);
				g.drawString(text[index], (int)(x + Globals.insetX + 10), (int)(y + Globals.insetY + 15 ));
			}
		}
	}

	public void update(float tslf) {
		if(state == APPEAR) {
			if(direction == FROM_BOTTOM) {
				if(y <= Globals.height - height - 10) {
					state = HIGHEST_POINT;
					width = maxWidth;
					time = 1;
					y = Globals.height - height - 10;
				}else {
					time += tslf;
					width = maxWidth * time;
					y -= speed * tslf;
				}
			}else if(direction == FROM_TOP) {
				if(y >= 10) {
					state = HIGHEST_POINT;
					width = maxWidth;
					time = 1;
					y = 10;
				}else {
					time += tslf;
					width = maxWidth * time;
					y += speed * tslf;
				}
			}
		}else if(state == DISAPPEAR){
			if(direction == FROM_BOTTOM) {
				if(y >= Globals.height) {
					y = Globals.height;
					time = 0;
					state = HIDE;
				}else {
					time -= tslf;
					width = maxWidth * time;
					y += speed * tslf;
				}
			}else if(direction == FROM_TOP) {
				if(y <= -height) {
					y = -height;
					time = 0;
					state = HIDE;
				}else {
					time -= tslf;
					width = maxWidth * time;
					y -= speed * tslf;
				}
			}
		}
	}

	public void appear() {
		if (Globals.player.y < Globals.height/2) {
			direction = FROM_BOTTOM;
			if(state == HIDE) {
				width = 0;
				y = Globals.height;
			}
		}else {
			direction = FROM_TOP;
			if(state == HIDE) {
				width = 0;
				y = -height;
			}
		}
		state = APPEAR;
	}
	public void disappear() {
		if(state == HIDE) return;
		state = DISAPPEAR;
	}
}
