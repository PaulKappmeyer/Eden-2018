/*
 * 
 */
package objects;

import java.awt.Color;
import java.awt.Graphics;

import game.Game;
import game.Gamestate;
import game.Globals;
import game.Obstacle;
import game.Textbox;
import input.Input;

/**
 * 
 * @author Paul
 *
 */
public class Sign extends Obstacle{

	public static int triggerDistance = 16;
	
	boolean pressed;
	boolean checkForPlayer = true;
	Textbox textbox;
	
	public Sign(int x, int y, int width, int height) {
		super(x, y, width, height);
		String[]text = new String[]{"KEIN TEXT HINTERLEGT! ENTWICKLER FRAGEN!"};
		textbox = new Textbox(text);
		isObstacle = false;
	}
	
	public Sign(int x, int y, int width, int height, String[]text) {
		super(x, y, width, height);
		textbox = new Textbox(text);
		isObstacle = false;
	}

	public void draw(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
		
		textbox.draw(g);
	}

	public void interact() {
		if (textbox.state == Textbox.HIGHEST_POINT) {
			if (Input.isInteractingKeyDown() && !pressed) {
				checkForPlayer = false;
				Game.state = Gamestate.INTERACTING;
				textbox.index++;
				if (textbox.index >= textbox.text.length) {
					textbox.index = textbox.text.length - 1;
					textbox.disappear();
					Game.state = Gamestate.RUNNING;

				}
				pressed = true;
			}
			if (!Input.isInteractingKeyDown() && pressed) {
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
		float halfsize = Globals.player.size/2;
		float playercenterx = Globals.player.x + halfsize;
		float playercentery = Globals.player.y + halfsize;
		float enemycenterx = this.x + halfsize;
		float enemycentery = this.y + halfsize;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;
		float distanceToPlayer = distx * distx + disty * disty;
		if (distanceToPlayer < triggerDistance * triggerDistance) {
			if (checkForPlayer) {
				textbox.appear();
			}
		} else {
			checkForPlayer = true;
			textbox.disappear();
		}
	}
	
}
