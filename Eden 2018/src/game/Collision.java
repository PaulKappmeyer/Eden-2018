package game;

public class Collision {

	public static final int TOP_SIDE = 0;
	public static final int BOTTOM_SIDE = 1;
	public static final int LEFT_SIDE = 2;
	public static final int RIGHT_SIDE = 3;

	/**
	 * 
	 * @param move
	 * @param nextX
	 * @param nextY
	 * @return
	 */
	public static Obstacle[] checkCollisionMovingobjToObstacle(Object move, float nextX, float nextY) {
		Obstacle[] sides = new Obstacle[4];

		//TOP SIDE OF OBSTACLE
		if(move.y < nextY) {
			Obstacle check = null;
			int min = -1;
			for (Obstacle obs : Game.currentMap.obstacles) {
				if(move.x + move.size > obs.x && move.x < obs.x + obs.width && move.y < obs.y) {
					if(min == -1) {
						min = obs.y;
						check = obs;
					} else if(obs.y < min) {
						min = obs.y;
						check = obs;
					}
				}
			}
			if(check != null) {
				if(nextY + move.size > check.y) {
					sides[TOP_SIDE] = check;
				}
			}
		}
		//BOTTOM SIDE OF OBSTACLE
		if(move.y > nextY) {
			Obstacle check = null;
			int max = -1;
			for (Obstacle obs : Game.currentMap.obstacles) {
				if(move.x + move.size > obs.x && move.x < obs.x + obs.width && move.y + move.size > obs.y + obs.height) {
					if(max == -1) {
						max = obs.y;
						check = obs;
					} else if(obs.y + obs.height > max) {
						max = obs.y;
						check = obs;
					}
				}
			}
			if(check != null) {
				if(nextY < check.y + check.height) {
					sides[BOTTOM_SIDE] = check;
				}
			}
		}
		//LEFT SIDE OF OBSTACLE
		if(move.x < nextX) {
			Obstacle check = null;
			int min = -1;
			for (Obstacle obs : Game.currentMap.obstacles) {
				if(move.y + move.size > obs.y && move.y < obs.y + obs.height && move.x < obs.x) {
					if(min == -1) {
						min = obs.x;
						check = obs;
					} else if(obs.x < min) {
						min = obs.x;
						check = obs;
					}
				}
			}
			if(check != null) {
				if(nextX + move.size > check.x) {
					sides[LEFT_SIDE] = check;
				}
			}
		}
		//RIGHT SIDE OF OBSTACLE
		if(move.x > nextX) {
			Obstacle check = null;
			int max = -1;
			for (Obstacle obs : Game.currentMap.obstacles) {
				if(move.y + move.size > obs.y && move.y < obs.y + obs.height && move.x + move.size > obs.x + obs.width) {
					if(max == -1) {
						max = obs.x;
						check = obs;
					} else if(obs.x + obs.width > max) {
						max = obs.x;
						check = obs;
					}
				}
			}
			if(check != null) {
				if(nextX < check.x + check.width) {
					sides[RIGHT_SIDE] = check;
				}	
			}
		}

		return sides;
	}
}
