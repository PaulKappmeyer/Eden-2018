package game;

import java.awt.Color;
import java.awt.Graphics;

public class Chest {

	public int x;
	public int y;
	public int size = 16;
	public static int triggerDistance = 100;
	boolean moveTextBoxUp = false;
	boolean moveTextBoxDown = false;
	boolean showTextBox = false;
	float textBoxX = 50;
	float textBoxY = Globals.height;
	int textBoxHeight = 100;
	int textBoxWidth = Globals.width - 100;
	float textBoxSpeed = 1000;
	String text;
	
	public Chest(int x, int y) {
		this.x = x;
		this.y = y;
		this.text = "Ohhh das ist eine Kiste!";
	}

	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(x + Globals.insetX, y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawRect(x + Globals.insetX, y + Globals.insetY, size, size);

		if(showTextBox) {
			g.drawRoundRect((int) (textBoxX + Globals.insetX), (int) (textBoxY + Globals.insetY), textBoxWidth, textBoxHeight, 10, 10);
			g.drawString(text, (int)(textBoxX + Globals.insetX + 10), (int)(textBoxY + Globals.insetY + 15 ));
		}
	}

	public void update(float tslf) {
		float halfsize = this.size/2;
		float playercenterx = Globals.player.x + halfsize;
		float playercentery = Globals.player.y + halfsize;
		float enemycenterx = this.x + halfsize;
		float enemycentery = this.y + halfsize;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;
		float distanceToPlayer = distx * distx + disty * disty;
		if(distanceToPlayer < triggerDistance * triggerDistance) {
			moveTextBoxUp = true;
			showTextBox = true;
		}else {
			moveTextBoxDown = true;
		}

		if(moveTextBoxUp) {
			if(textBoxY <= Globals.height - textBoxHeight - 10) {
				moveTextBoxUp = false;
				textBoxY = Globals.height - textBoxHeight - 10;
			}else {
				textBoxY -= textBoxSpeed * tslf;
			}
		}else if(moveTextBoxDown){
			if(textBoxY >= Globals.height) {
				textBoxY = Globals.height;
				moveTextBoxDown = false;
				showTextBox = false;
			}else {
				textBoxY += textBoxSpeed * tslf;
			}
		}
	}
}