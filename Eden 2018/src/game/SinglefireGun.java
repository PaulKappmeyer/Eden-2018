package game;

public class SinglefireGun extends Gun{

	public static final int SINGLEFIRE = 1;
	int bulletspray = 3;

	public SinglefireGun(Object owner) {
		super(owner);
		mode = SINGLEFIRE;
		damage = 20;
	}

	@Override
	public void shot() {
		float angle = 0;
		if(mode == SINGLEFIRE) {
			switch(owner.shotDirection) {
			case UP:
				angle = 180 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y - Bullet.SIZE, angle));
				break;
			case DOWN:
				angle = 0 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size/2 - Bullet.SIZE/2, owner.y + owner.size, angle));
				break;
			case LEFT:
				angle = 270 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x - Bullet.SIZE, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
				break;
			case RIGHT:
				angle = 90 + -bulletspray/2 + Globals.random.nextInt(bulletspray);
				projectiles.add(new Bullet(owner.x + owner.size, owner.y + owner.size/2 - Bullet.SIZE/2, angle));
				break;
			default:
				break;
			}
			float shellCenterX = (float) (owner.x + owner.size/2 - Shell.SIZE/2 - Math.sin(Math.toRadians(angle)) * owner.size/2);
			float shellCenterY = (float) (owner.y + owner.size/2 - Shell.SIZE/2 - Math.cos(Math.toRadians(angle)) * owner.size/2);
			Game.currentMap.shells.add(new Shell(shellCenterX, shellCenterY, angle));
			applyRecoil(angle);
		}
		canShot = false;
	}
}
