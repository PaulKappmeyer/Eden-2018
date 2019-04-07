package guns;

import game.Game;
import game.Globals;
import game.MovingObject;

public class TripleMachineGun extends Gun{

	public static final int TRIPLEMACHINEGUN = 2;
	float tripleMachineGunRadius = 10;
	int bulletspray = 3;
	
	public TripleMachineGun(MovingObject owner) {
		super(owner);
		mode = TRIPLEMACHINEGUN;
		
		projectiles = new Bullet[100];
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i] = new Bullet();
		}
	}

	@Override
	public void shot() {
		float angle = 0;
		if(mode == TRIPLEMACHINEGUN) {
			switch (owner.walkDirection) {
			case UP:
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireBullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle - tripleMachineGunRadius, bulletSpeed);
				fireBullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle, bulletSpeed);
				fireBullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle + tripleMachineGunRadius, bulletSpeed);
				break;
			case DOWN:
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireBullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle - tripleMachineGunRadius, bulletSpeed);
				fireBullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle, bulletSpeed);
				fireBullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle + tripleMachineGunRadius, bulletSpeed);
				break;
			case LEFT:
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireBullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle - tripleMachineGunRadius, bulletSpeed);
				fireBullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle, bulletSpeed);
				fireBullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle + tripleMachineGunRadius, bulletSpeed);
				break;
			case RIGHT:
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				fireBullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle - tripleMachineGunRadius, bulletSpeed);
				fireBullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle, bulletSpeed);
				fireBullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle + tripleMachineGunRadius, bulletSpeed);
				break;

			default:
				break;
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			applyRecoil(angle);
		}
		canShot = false;
	}

	@Override
	public void shot(float angle) {
		angle += -bulletspray/2 + Globals.random.nextInt(bulletspray);
		
		float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
		float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
		fireBullet(centerX, centerY, angle - tripleMachineGunRadius, bulletSpeed);
		fireBullet(centerX, centerY, angle, bulletSpeed);
		fireBullet(centerX, centerY, angle + tripleMachineGunRadius, bulletSpeed);
		
		float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
		float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
		Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
		Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
		Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
		applyRecoil(angle);
		canShot = false;
	}
	
	public void fireBullet(float x, float y, float angle, float speed) {
		for (int i = 0; i < projectiles.length; i++) {
			if(!projectiles[i].isActive) {
				projectiles[i].activate(x, y, angle, speed);
				break;
			}
		}
	}
}
