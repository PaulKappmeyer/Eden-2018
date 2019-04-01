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
	Direction direction;

	//DRAW
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect((int)transitionX + Globals.insetX, (int)transitionY + Globals.insetY, (int)transitionWidth, (int)transitionHeight);
	}

	//UPDATE
	public void update(float tslf) {
		//Transition in
		if(Game.state == Gamestate.MAP_TRANSITION_IN) {
			updateTransitionIn(tslf);
		}
		//Transition out
		else if(Game.state == Gamestate.MAP_TRANSITION_OUT) {
			updateTransitionOut(tslf);
		}
	}

	//TRANSITION IN
	private void updateTransitionIn(float tslf) {
		switch (direction) {
		case RIGHT:
			transitionWidth += speed*tslf;
			if(transitionWidth >= Globals.width) {
				transitionWidth = Globals.width*1.25f;
				Game.state = Gamestate.RESET;
			}
			break;
		case LEFT:
			transitionX -= speed*tslf;
			if(transitionX <= 0) {
				transitionX = 0 - Globals.width*0.25f;
				transitionWidth = Globals.width*1.25f;
				Game.state = Gamestate.RESET;
			}
			break;
		case DOWN:
			transitionHeight += speed*tslf;
			if(transitionHeight >= Globals.height) {
				transitionHeight = Globals.height*1.25f;
				Game.state = Gamestate.RESET;
			}
			break;
		case UP:
			transitionY -= speed*tslf;
			if(transitionY <= 0) {
				transitionY = 0 - Globals.height*0.25f;
				transitionHeight = Globals.height*1.25f;
				Game.state = Gamestate.RESET;
			}
			break;
		default:
			break;
		}
	}

	//TRANSITION OUT
	private void updateTransitionOut(float tslf) {
		switch (direction) {
		case RIGHT:
			transitionWidth -= speed*tslf;
			if(transitionWidth <= 0) {
				Game.state = Gamestate.RUNNING;
			}
			break;
		case LEFT:
			transitionX += speed*tslf;
			if(transitionX >= Globals.width) {
				Game.state = Gamestate.RUNNING;
			}
			break;
		case DOWN:
			transitionHeight -= speed*tslf;
			if(transitionHeight <= 0) {
				Game.state = Gamestate.RUNNING;
			}
			break;
		case UP:
			transitionY += speed * tslf;
			if(transitionY >= Globals.height) {
				Game.state = Gamestate.RUNNING;
			}
			break;
		default:
			break;
		}
	}

	//SET TRANSITION
	public void startTransition(Direction direction) {
		this.direction = direction;
		switch (direction) {
		case RIGHT:
			transitionX = 0;
			transitionY = 0;
			transitionWidth = 0;
			transitionHeight = Globals.height;
			break;
		case LEFT:
			transitionX = Globals.width;
			transitionY = 0;
			transitionWidth = Globals.width;
			transitionHeight = Globals.height;
			break;
		case DOWN:
			transitionX = 0;
			transitionY = 0;
			transitionWidth = Globals.width;
			transitionHeight = 0;
			break;
		case UP:
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