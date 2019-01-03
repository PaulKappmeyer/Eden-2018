package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A class to access all the inputs like keyboard or mouse 
 * @author Paul Kappmeyer
 * @param keys The boolean Array which set the index of the key as true if it is pressed
 * @see KeyListener
 */

//TODO: implement mouse 
public class Controls implements KeyListener{

	private static boolean [] keys = new boolean[1024];
	public static boolean isKeyDown(int keyCode){
		if(keyCode >= 0 && keyCode<keys.length && keys[keyCode]){
				return true;
		}
		else return false;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode >= 0 && keyCode<keys.length) keys[keyCode] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode >= 0 && keyCode<keys.length) keys[keyCode] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}	
}
