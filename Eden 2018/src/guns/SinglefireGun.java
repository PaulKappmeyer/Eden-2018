package guns;

import game.Game;
import game.Globals;
import game.MovingObject;

public class SinglefireGun extends Gun{

	public static final int SINGLEFIRE = 1;
	int bulletspray = 0;

	public SinglefireGun(MovingObject owner) {
		super(owner);
		mode = SINGLEFIRE;
		damage = 50;
		
		projectiles = new Bullet[100];
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i] = new Bullet();
		}
	}

	@Override
	public void shot() {
		float angle = 0;
		if(mode == SINGLEFIRE) {
			switch(owner.walkDirection) {
			case UP:
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireProjectile(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle, bulletSpeed);
				break;
			case DOWN:
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireProjectile(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle, bulletSpeed);
				break;
			case LEFT:
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireProjectile(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle, bulletSpeed);
				break;
			case RIGHT:
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireProjectile(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle, bulletSpeed);
				break;
			default:
				break;
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			
			applyRecoil(-Math.sin(Math.toRadians(angle)), -Math.cos(Math.toRadians(angle)));
		}
		canShot = false;
	}

	@Override
	public void shot(float angle) {
		if(bulletspray > 0) angle += -bulletspray/2 + Globals.random.nextInt(bulletspray);
		
		float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
		float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
		fireProjectile(centerX, centerY, angle, bulletSpeed);
		float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
		float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
		Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
		
		applyRecoil(-Math.sin(Math.toRadians(angle)), -Math.cos(Math.toRadians(angle)));
	
		canShot = false;
	}
}
