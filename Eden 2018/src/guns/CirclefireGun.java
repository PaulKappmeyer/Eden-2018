package guns;

import game.Game;
import game.Object;

public class CirclefireGun extends Gun{

	public static final int CIRCLESHOT = 3;
	public static final int CIRCLESHOT_SINGLE = 5;
	int numBulletsPerShot = 36;
	int a = 0;
	
	public CirclefireGun(Object owner, int mode) {
		super(owner);
		this.mode = mode;
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
				projectiles.add(new Bullet(centerX, centerY, angle));
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
			projectiles.add(new Bullet(centerX, centerY, angle));
			Game.currentMap.shells.add(new Shell(centerX, centerY, angle));
			applyRecoil(angle);
			a++;
		}
		//----------------------------------------------------------------------------------------------------
		canShot = false;
	}

}
