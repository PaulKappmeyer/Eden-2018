package guns;

import game.Game;
import game.Object;

public class CirclefireGun extends Gun{

	public static final int CIRCLESHOT = 3;
	public static final int CIRCLESHOT_SINGLE = 5;
	int numBulletsPerShot = 36;
	int a = 0;
	
	public CirclefireGun(Object owner) {
		super(owner);
		this.mode = CIRCLESHOT;
		
		projectiles = new Bullet[100];
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i] = new Bullet();
		}
	}
	public CirclefireGun(Object owner, int mode) {
		super(owner);
		this.mode = mode;
		
		projectiles = new Bullet[100];
		for (int i = 0; i < projectiles.length; i++) {
			projectiles[i] = new Bullet();
		}
	}
	
	@Override
	public void shot() {
		float angle = 0;
		//--------------------------------------------------------------------------
		if(mode == CIRCLESHOT) {
			for (int i = 0; i < numBulletsPerShot; i++) {
				angle = 360/numBulletsPerShot * i;
				float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
				float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
				fireBullet(centerX, centerY, angle, bulletSpeed);
				float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
				float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
				Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
				if(numBulletsPerShot == 1)applyRecoil(angle);
			}
		}
		//----------------------------------------------------------------------------------------------------
		else if(mode == CIRCLESHOT_SINGLE) {
			angle = 360/numBulletsPerShot * a;
			float centerX = (float) (owner.x + owner.size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(angle)) * owner.size/2);
			float centerY = (float) (owner.y + owner.size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(angle)) * owner.size/2);
			fireBullet(centerX, centerY, angle, bulletSpeed);
			Game.currentMap.shells.add(new Shell(centerX, centerY, angle));
			applyRecoil(angle);
			a++;
		}
		//----------------------------------------------------------------------------------------------------
		canShot = false;
	}

	@Override
	public void shot(float angle) {
		shot();
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
