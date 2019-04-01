package guns;

import game.Game;
import game.Globals;
import game.Object;

public class TripleMachineGun extends Gun{

	public static final int TRIPLEMACHINEGUN = 2;
	float tripleMachineGunRadius = 10;
	int bulletspray = 3;
	
	public TripleMachineGun(Object owner) {
		super(owner);
		mode = TRIPLEMACHINEGUN;
	}

	@Override
	public void shot() {
		float angle = 0;
		if(mode == TRIPLEMACHINEGUN) {
			switch (owner.shotDirection) {
			case UP:
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle + tripleMachineGunRadius));
				break;
			case DOWN:
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle));
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle + tripleMachineGunRadius));
				break;
			case LEFT:
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle + tripleMachineGunRadius));
				break;
			case RIGHT:
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle - tripleMachineGunRadius));
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle + tripleMachineGunRadius));
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
}
