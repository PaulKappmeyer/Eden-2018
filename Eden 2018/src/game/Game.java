package game;

import java.awt.event.KeyEvent;

public class Game implements Runnable{
	boolean running = true;
	public static final int FPS = 120;
	public static final long MAXLOOPTIME = 1000/FPS;
	
	public static long firstFrame;
	public static int frames;
	
	Screen sc = new Screen();
	Map map = new Map();
	
	public static final int RUNNING = 1;
	public static final int FREEZE = 2;
	public static final int MAP_TRANSITION = 3;
	public static final int MAP_TRANSITION_OUT = 4;
	public static int state = RUNNING;
	
	@Override
	public void run() {
		long timestamp;
		long oldTimestamp;
		
		long lastFrame = System.currentTimeMillis();
		while(running){
			long thisFrame = System.currentTimeMillis();
			float tslf = (float)(thisFrame - lastFrame) / 1000f;
			lastFrame = thisFrame;
			
			if(thisFrame > firstFrame + 1000){
				firstFrame = thisFrame;
				Globals.fps = frames;
				frames = 0;
			}
			frames++;
			
			oldTimestamp = System.currentTimeMillis();
			
			//----------------------------------Updating
			
			//TODO:Update System
			if(state == RUNNING) map.update(tslf);
			sc.update(tslf);
			if(state == FREEZE) {
				state = MAP_TRANSITION_OUT;
				map.switchMap(); 
			}
			
			if(Controls.isKeyDown(KeyEvent.VK_ESCAPE))System.exit(0);
			
			timestamp = System.currentTimeMillis();
			if(timestamp - oldTimestamp > MAXLOOPTIME) {
				System.out.println("Too late");
				continue;
			}
			
			//-----------------------------------Rendering
			sc.repaintScreen();
			
			timestamp = System.currentTimeMillis();
			if(timestamp - oldTimestamp <= MAXLOOPTIME) {
				try {
					Thread.sleep(MAXLOOPTIME - (timestamp - oldTimestamp));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
