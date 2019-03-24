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
			g.drawRoundRect((int) (x + Globals.insetX), (int) (y + Globals.insetY), width, height, 10, 10);
			g.setColor(new Color(100, 100, 100, 100));
			g.fillRoundRect((int) (x + Globals.insetX), (int) (y + Globals.insetY), width, height, 10, 10);
			g.setColor(Color.BLACK);
			g.drawString(text[index], (int)(x + Globals.insetX + 10), (int)(y + Globals.insetY + 15 ));
		}
	}

	public void update(float tslf) {
		if(state == APPEAR) {
			if(direction == FROM_BOTTOM) {
				if(y <= Globals.height - height - 10) {
					state = HIGHEST_POINT;
					y = Globals.height - height - 10;
				}else {
					y -= speed * tslf;
				}
			}else if(direction == FROM_TOP) {
				if(y >= 10) {
					state = HIGHEST_POINT;
					y = 10;
				}else {
					y += speed * tslf;
				}
			}
		}else if(state == DISAPPEAR){
			if(direction == FROM_BOTTOM) {
				if(y >= Globals.height) {
					y = Globals.height;
					state = HIDE;
				}else {
					y += speed * tslf;
				}
			}else if(direction == FROM_TOP) {
				if(y <= -height) {
					y = -height;
					state = HIDE;
				}else {
					y -= speed * tslf;
				}
			}
		}
	}

	public void appear() {
		if (Globals.player.y < Globals.height/2) {
			direction = FROM_BOTTOM;
			if(state == HIDE) y = Globals.height;
		}else {
			direction = FROM_TOP;
			if(state == HIDE) y = -height;
		}
		state = APPEAR;
	}
	public void disappear() {
		if(state == HIDE) return;
		state = DISAPPEAR;
	}
}
