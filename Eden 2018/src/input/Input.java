/*
 * 
 */
package input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * 
 * @author Paul
 *
 */
public final class Input {

	//Key inputs

	public static boolean isUpKeyDown() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_W) || KeyboardinputManager.isKeyDown(KeyEvent.VK_UP);
	}
	public static boolean isDownKeyDown() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_S) || KeyboardinputManager.isKeyDown(KeyEvent.VK_DOWN);
	}
	public static boolean isLeftKeyDown() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_A) || KeyboardinputManager.isKeyDown(KeyEvent.VK_LEFT);
	}
	public static boolean isRightKeyDown() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_D) || KeyboardinputManager.isKeyDown(KeyEvent.VK_RIGHT);
	}

	public static boolean isSpecialKey1Down() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_R);
	}
	
	public static boolean isSpecialKey2Down() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_Q);
	}
	
	public static boolean isInteractingKeyDown() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_E);
	}
	
	public static boolean isEscapeKeyDown() {
		return KeyboardinputManager.isKeyDown(KeyEvent.VK_ESCAPE);
	}
	
	public static boolean isShootingKeyDown() {
		return MouseinputManager.isButtonDown(MouseEvent.BUTTON1);
//		return KeyboardinputManager.isKeyDown(KeyEvent.VK_SPACE); 
	}

}
