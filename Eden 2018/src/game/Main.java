package game;

public class Main {

	public static void main(String[] args) {
		System.out.println("Eden started");
		
		Screen sc = new Screen();
		
		while(true) {
			sc.repaintScreen();
		}
	}
	
}
