package guns;

import game.Game;
import game.Globals;
import game.Object;

public class RocketLuncher extends Gun{

	public static final int ROCKET_SINGLE_FIRE_MODE = 4;
	int bulletspray = 3;
	
	public RocketLuncher(Object owner) {
		super(owner);
		mode = ROCKET_SINGLE_FIRE_MODE;
		shottime = 0.5f;
		
		projectiles = new Rocket[100];
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i] = new Bullet();
		}
	}

	@Override
	public void shot() {
		float angle = 0;
		if(mode == ROCKET_SINGLE_FIRE_MODE) {
			switch (owner.shotDirection) {
			case UP:
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				break;
			case DOWN:
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				break;
			case LEFT:
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				break;
			case RIGHT:
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				break;
			default:
				break;
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
			float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
			fireRocket(centerX, centerY, angle, bulletSpeed);
			applyRecoil(angle);
		}
		canShot = false;
	}

	@Override
	public void shot(float angle) {
		shot();
	}
	
	public void fireRocket(float x, float y, float angle, float speed) {
		for (int i = 0; i < projectiles.length; i++) {
			if(!projectiles[i].isActive) {
				projectiles[i].activate(x, y, angle, speed);
				break;
			}
		}
	}
}
