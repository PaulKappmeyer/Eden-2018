package game;

import java.awt.Color;
import java.awt.Graphics;

public class ScreenTransition {

	//Transition
	float transitionX = 0;
	float transitionY = 0;
	float transitionWidth = 0;
	float transitionHeight = 0;
	int speed = 1400;
	int direction;

	//DRAW
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect((int)transitionX + Globals.insetX, (int)transitionY + Globals.insetY, (int)transitionWidth, (int)transitionHeight);
	}

	//UPDATE
	public void update(float tslf) {
		//Transition in
		if(Game.state == Game.MAP_TRANSITION) {
			updateTransitionIn(tslf);
		}
		//Transition out
		else if(Game.state == Game.MAP_TRANSITION_OUT) {
			updateTransitionOut(tslf);
		}
	}

	//TRANSITION IN
	private void updateTransitionIn(float tslf) {
		switch (direction) {
		case Eden.RIGHT:
			transitionWidth += speed*tslf;
			if(transitionWidth >= Globals.width) {
				transitionWidth = Globals.width*1.25f;
				Game.state = Game.RESET;
			}
			break;
		case Eden.LEFT:
			transitionX -= speed*tslf;
			if(transitionX <= 0) {
				transitionX = 0 - Globals.width*0.25f;
				transitionWidth = Globals.width*1.25f;
				Game.state = Game.RESET;
			}
			break;
		case Eden.DOWN:
			transitionHeight += speed*tslf;
			if(transitionHeight >= Globals.height) {
				transitionHeight = Globals.height*1.25f;
				Game.state = Game.RESET;
			}
			break;
		case Eden.UP:
			transitionY -= speed*tslf;
			if(transitionY <= 0) {
				transitionY = 0 - Globals.height*0.25f;
				transitionHeight = Globals.height*1.25f;
				Game.state = Game.RESET;
			}
			break;
		default:
			break;
		}
	}

	//TRANSITION OUT
	private void updateTransitionOut(float tslf) {
		switch (direction) {
		case Eden.RIGHT:
			transitionWidth -= speed*tslf;
			if(transitionWidth <= 0) {
				Game.state = Game.RUNNING;
			}
			break;
		case Eden.LEFT:
			transitionX += speed*tslf;
			if(transitionX >= Globals.width) {
				Game.state = Game.RUNNING;
			}
			break;
		case Eden.DOWN:
			transitionHeight -= speed*tslf;
			if(transitionHeight <= 0) {
				Game.state = Game.RUNNING;
			}
			break;
		case Eden.UP:
			transitionY += speed * tslf;
			if(transitionY >= Globals.height) {
				Game.state = Game.RUNNING;
			}
			break;
		default:
			break;
		}
	}

	//SET TRANSITION
	public void startTransition(int direction) {
		this.direction = direction;
		switch (direction) {
		case Eden.RIGHT:
			transitionX = 0;
			transitionY = 0;
			transitionWidth = 0;
			transitionHeight = Globals.height;
			break;
		case Eden.LEFT:
			transitionX = Globals.width;
			transitionY = 0;
			transitionWidth = Globals.width;
			transitionHeight = Globals.height;
			break;
		case Eden.DOWN:
			transitionX = 0;
			transitionY = 0;
			transitionWidth = Globals.width;
			transitionHeight = 0;
			break;
		case Eden.UP:
			transitionX = 0;
			transitionY = Globals.height;
			transitionWidth = Globals.width;
			transitionHeight = Globals.height;
			break;
		default:
			break;
		}
	}
}