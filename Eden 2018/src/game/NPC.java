/*
 * 
 */
package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * 
 * @author Paul
 *
 */
public class NPC extends MovingObject{

	Textbox textbox = new Textbox(new String[]{"Hallo, was geht?", "ich heisse Juergen"});
	
	public NPC(float x, float y) {
		this.x = x;
		this.y = y;
		this.size = 16;

		this.walkDirection = Direction.DOWN;
	}

	int a = 5;
	@Override
	public void draw(Graphics g) {
		//Drawing of the player
		g.setColor(Color.CYAN);
		g.fillRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);
		g.setColor(Color.BLACK);
		g.drawRect((int)x + Globals.insetX, (int)y + Globals.insetY, size, size);

		g.setColor(Color.RED);
		switch (walkDirection) {
		case UP:
			g.fillOval((int)(this.x + size/2 - a/2 + Globals.insetX), (int)this.y + Globals.insetY, a, a);
			break;
		case DOWN:
			g.fillOval((int)(this.x + size/2 - a/2 + Globals.insetX), (int)this.y + size - a + Globals.insetY, a, a);
			break;
		case RIGHT:
			g.fillOval((int)(this.x + size - a + Globals.insetX), (int)this.y + size/2 - a/2 + Globals.insetY, a, a);
			break;
		case LEFT:
			g.fillOval((int)(this.x + Globals.insetX), (int)this.y + size/2 - a/2 + Globals.insetY, a, a);
			break;
		default:
			break;
		}
		
		textbox.draw(g);
	}

	boolean checkForPlayer = true;
	@Override
	public void update(float tslf) {
		super.update(tslf);

		//Check for player and look at him
		float playercenterx = Globals.player.x +  Globals.player.size/2;
		float playercentery = Globals.player.y +  Globals.player.size/2;
		float enemycenterx = this.x + this.size/2;
		float enemycentery = this.y + this.size/2;
		float distx = enemycenterx - playercenterx;
		float disty = enemycentery - playercentery;

		float walkAngle = (float) Math.atan(distx / disty);
		walkAngle = (float) Math.toDegrees(walkAngle);
		if(playercentery > enemycentery) walkAngle =  -90 - (90-walkAngle);
		if(playercenterx < enemycenterx && playercentery < enemycentery) walkAngle = -270 - (90-walkAngle);

		walkVelocityX = -Math.sin(Math.toRadians(walkAngle));
		walkVelocityY = -Math.cos(Math.toRadians(walkAngle));

		if(distx == 0 && disty == 0) {
			walkVelocityX = 0;
			walkVelocityY = 0;
		}

		walkDirection = getDirection(walkVelocityX, walkVelocityY);
		
		textbox.update(tslf);
		
		float distanceToPlayer = distx * distx + disty * disty;
		if(distanceToPlayer < 100 * 100) {
			if(checkForPlayer) {
				textbox.appear();
			}
		}else {
			checkForPlayer = true;
			textbox.disappear();
		}
	}
}
