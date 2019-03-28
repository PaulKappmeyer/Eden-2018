package game;

import java.awt.Graphics;
import java.util.ArrayList;

public class Game {

	Map map1;
	Map currentMap;
	
	public static final int RUNNING = 1;
	public static final int MAP_TRANSITION = 3;
	public static final int RESET = 2;
	public static final int MAP_TRANSITION_OUT = 4;
	public static final int INTERACTING = 5;
	public static int state = RUNNING;

	public Game() {
		ArrayList<Stone>stones = new ArrayList<>();
		stones.add(new Stone(500, 400, 125, 125));
		stones.add(new Stone(175, 250, 25, 100));
		stones.add(new Stone(200, 250, 25, 100));
		stones.add(new Stone(225, 250, 25, 100));
		stones.add(new Stone(175, 100, 100, 25));
		stones.add(new Stone(175, 125, 100, 25));
		stones.add(new Stone(175, 150, 100, 25));
		stones.add(new Stone(300, 250, 25, 100));
		stones.add(new Stone(300, 350, 125, 25));
		stones.add(new Stone(400, 250, 25, 100));
		stones.add(new Stone(300, 225, 75, 25));
		
		map1 = new Map(100, 100, stones, Globals.enemies);
		currentMap = map1;
	}
	
	public void update(float tslf) {
		//TODO:Update System
		if(state == RUNNING || state == INTERACTING) currentMap.update(tslf);
		if(state == RESET) {
			state = MAP_TRANSITION_OUT;
			currentMap.switchMap(); 
		}
	}
	
	public void draw(Graphics g) {
		currentMap.draw(g);
	}
}
