package game;

import java.awt.event.KeyEvent;

public class Input {

	//Key inputs
	
	public static boolean isUpKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_W) || Controls.isKeyDown(KeyEvent.VK_UP);
	}
	public static boolean isDownKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_S) || Controls.isKeyDown(KeyEvent.VK_DOWN);
	}
	public static boolean isLeftKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_A) || Controls.isKeyDown(KeyEvent.VK_LEFT);
	}
	public static boolean isRightKeyDown() {
		return Controls.isKeyDown(KeyEvent.VK_D) || Controls.isKeyDown(KeyEvent.VK_RIGHT);
	}
	
}
