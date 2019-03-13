package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Chest {

	public int x;
	public int y;
	public int size = 16;
	public static int triggerDistance = 75;
	boolean pressed;
	boolean checkForPlayer = true;

	Textbox textbox;
	
	public Chest(int x, int y) {
		this.x = x;
		this.y = y;
		String[]text = new String[]{"Ohhh das ist eine Kiste! Zum interagieren q drücken", "Hier erhälst du deine erste Waffe!", "Mit Leertaste kannst du sie abfeuern", "Jetz erledige den Gegner!"};
		textbox = new Textbox(text);
	}

	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(x + Globals.insetX, y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawRect(x + Globals.insetX, y + Globals.insetY, size, size);
		
		textbox.draw(g);
	}

	public void interact() {
		if(textbox.state == Textbox.HIGHEST_POINT) {
			if(Controls.isKeyDown(KeyEvent.VK_Q) && !pressed) {
				checkForPlayer = false;
				Game.state = Game.INTERACTING;
				textbox.index++;
				if(textbox.index >= textbox.text.length) {
					textbox.index = textbox.text.length - 1;
					textbox.disappear();
					Globals.player.gun = new Gun(Globals.player);
					Globals.player.gun.mode = Gun.SINGLEFIRE;
					Game.state = Game.RUNNING;

				}
				pressed = true;
			}
			if(!Controls.isKeyDown(KeyEvent.VK_Q) && pressed) {
				pressed = false;
			}
		}
	}

	public void update(float tslf) {
		checkForPlayer();

		textbox.update(tslf);
		
		interact();
	}

	public void checkForPlayer() {
		float halfsize = this.size/2;
		float playercenterx = Globals.player.x + halfsize;
		float playercentery = Globals.player.y + halfsize;
		float enemycenterx = this.x + halfsize;
		float enemycentery = this.y + halfsize;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;
		float distanceToPlayer = distx * distx + disty * disty;
		if(distanceToPlayer < triggerDistance * triggerDistance) {
			if(checkForPlayer) {
				textbox.appear();
			}
		}else {
			checkForPlayer = true;
			textbox.disappear();
		}
	}
}
