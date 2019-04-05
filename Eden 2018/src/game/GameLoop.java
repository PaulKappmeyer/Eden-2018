package game;

import input.Input;

public class GameLoop implements Runnable{
	boolean running = true;
	public static final int MAXFPS = 160;
	public static final long MAXLOOPTIME = 1000/MAXFPS;
	
	public static long firstFrame;
	public static int frames;
	Game game;
	Screen screen;
	
	public GameLoop() {
		game = new Game();
		screen = new Screen(game);
	}
	
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
			game.update(tslf);
			screen.update(tslf);
			
			timestamp = System.currentTimeMillis();
			if(timestamp - oldTimestamp > MAXLOOPTIME) {
				System.out.println("Too late");
				continue;
			}
			
			//-----------------------------------Rendering
			screen.repaintScreen();
			
			timestamp = System.currentTimeMillis();
			if(timestamp - oldTimestamp <= MAXLOOPTIME) {
				try {
					Thread.sleep(MAXLOOPTIME - (timestamp - oldTimestamp));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(Input.isEscapeKeyDown())System.exit(0);
		}
	}
}
