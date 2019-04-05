package input;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import game.Globals;

public class MouseinputManager implements MouseInputListener{

	private static boolean[] mousebutton = new boolean[5];
	private static float mouseX;
	private static float mouseY;
	
	public static boolean isButtonDown(int mouseButtoon) {
		if(mouseButtoon >= 0 && mouseButtoon < mousebutton.length && mousebutton[mouseButtoon]) return true;
		else return false;
	}
	public static float getMouseX () {
		return mouseX;
	}
	public static float getMouseY () {
		return mouseY;
	}
	
	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		int button = mouseEvent.getButton();
		if(button >= 0 && button < mousebutton.length) mousebutton[button] = false;
	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		int button = mouseEvent.getButton();
		if(button >= 0 && button < mousebutton.length) mousebutton[button] = true;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		int button = mouseEvent.getButton();
		if(button >= 0 && button < mousebutton.length) mousebutton[button] = false;
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		int button = mouseEvent.getButton();
		if(button >= 0 && button < mousebutton.length) mousebutton[button] = true;
		
		mouseX = mouseEvent.getX() - Globals.insetX;
		mouseY = mouseEvent.getY() - Globals.insetY;
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		mouseX = mouseEvent.getX() - Globals.insetX;
		mouseY = mouseEvent.getY() - Globals.insetY;
	}

}
