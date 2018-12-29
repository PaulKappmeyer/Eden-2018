package game;

//The starting Class
public class Main {

	public static void main(String[] args) {
		System.out.println("Eden started");
		
		Screen sc = new Screen();
		
		long lastFrame = System.currentTimeMillis();
		while(true){
			long thisFrame = System.currentTimeMillis();
			float tslf = (float)(thisFrame - lastFrame) / 1000f;
			lastFrame = thisFrame;
			sc.repaintScreen();
			
			Globals.player.Update(tslf);
		}
	}
	
}
