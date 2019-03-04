package game;

/**
 * The starting class
 * @author Paul Kappmeyer
 *
 */
public class Main {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Eden started");
		
		Game game = new Game();
		new Thread(game).start();
	}
}
