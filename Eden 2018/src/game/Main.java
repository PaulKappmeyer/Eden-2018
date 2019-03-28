package game;

/**
 * The starting class
 * @author Paul Kappmeyer
 *
 */
public class Main {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Eden started");
		
		GameLoop game = new GameLoop();
		new Thread(game).start();
	}
}
