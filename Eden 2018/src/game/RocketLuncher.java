package game;

public class RocketLuncher extends Gun{

	public static final int ROCKET_SINGLE_FIRE_MODE = 4;
	int bulletspray = 3;
	
	public RocketLuncher(Object owner) {
		super(owner);
		mode = ROCKET_SINGLE_FIRE_MODE;
		shottime = 0.5f;
	}

	@Override
	public void shot() {
		float angle = 0;
		if(mode == ROCKET_SINGLE_FIRE_MODE) {
			if(owner.shotDirection == Eden.UP) {
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			if(owner.shotDirection == Eden.DOWN) {
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			if(owner.shotDirection == Eden.LEFT){
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			if(owner.shotDirection == Eden.RIGHT) {
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
			float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
			projectiles.add(new Rocket(centerX, centerY, angle));
			applyRecoil(angle);
		}
		canShot = false;
	}
	
}
