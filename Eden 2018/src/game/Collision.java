package game;

public class Collision {

	
	public static boolean isCollidingTopSideOfStone(float x, float y, int width, int height, float nextX, float nextY, Stone stone) {
		if(nextX + width > stone.x && nextX < stone.x + stone.width && y < stone.y && nextY + height > stone.y) {
			return true;
		}
		return false;
	}
	public static boolean isCollidingBottomSideOfStone(float x, float y, int width, int height, float nextX, float nextY, Stone stone) {
		if(nextX + width > stone.x && nextX < stone.x + stone.width && y + height > stone.y + stone.height && nextY < stone.y + stone.height) {
			return true;
		}
		return false;
	}
	public static boolean isCollidingLeftSideOfStone(float x, float y, int width, int height, float nextX, float nextY, Stone stone) {
		if(nextY + height > stone.y && nextY < stone.y + stone.height && x < stone.x && nextX + width > stone.x) {
			return true;
		}
		return false;
	}
	public static boolean isCollidingRightSideOfStone(float x, float y, int width, int height, float nextX, float nextY, Stone stone) {
		if(nextY + height > stone.y && nextY < stone.y + stone.height && x + width > stone.x + stone.width && nextX < stone.x + stone.width) {
			return true;
		}
		return false;
	}
	
}
