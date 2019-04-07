package enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import game.Globals;
import guns.Bullet;
import guns.Projectile;

public class RoundEnemy extends ZombieEnemy{

	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	ArrayList<Float> angles = new ArrayList<>();
	float radius;
	boolean shotAtPlayer = true;

	public RoundEnemy(float x, float y) {
		super(x, y);
		size = 20;
		radius = size + 1;
		this.MAX_WALK_SPEED = 20;
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		//Catch the bullets
		if(Globals.player.gun != null) {
			float enemyCenterX = this.x + size/2;
			float enemyCenterY = this.y + size/2;
			for (Projectile projectile : Globals.player.gun.projectiles) {
				if(!projectile.isActive)continue;
				float bulletCenterX = projectile.x + Bullet.SIZE / 2;
				float bulletCenterY = projectile.y + Bullet.SIZE / 2;

				float distance = Globals.distanceSquared(enemyCenterX, enemyCenterY, bulletCenterX, bulletCenterY);
				if(distance <= radius * radius) {
					if(!followplayer) followplayer = true;
					//Add a new Bullet
					Bullet b = new Bullet();
					b.velocityX = 0;
					b.velocityY = 0;
					b.activate(0, 0, 0, 500);
					this.bullets.add(b);
					this.angles.add(new Float(0));
					//Remove old
					projectile.deactivate();
				}
			}
		}

		//Turning the bullet around the enemy
		int index = -1;
		if(!angles.isEmpty()) {
			for (Float angle : angles) {
				index++;
				if(angle == -1f) continue;
				Bullet bullet = bullets.get(index);
				if(!bullet.hitSomething) {
					if(angle < 360 * 2) {
						bullet.x = (float) (this.x + size/2 - Bullet.SIZE/2 + Math.sin(Math.toRadians(bullet.angle + angle)) * radius);
						bullet.y = (float) (this.y + size/2 - Bullet.SIZE/2 + Math.cos(Math.toRadians(bullet.angle + angle)) * radius);
						angle += bullet.speed * tslf;
						angles.set(index, angle);
					}else {
						if(shotAtPlayer) {
							float ecx = this.x + this.size/2;
							float ecy = this.y + this.size/2;
							float pcx = Globals.player.x + Globals.player.size/2;
							float pcy = Globals.player.y + Globals.player.size/2;

							float distx = pcx - ecx;
							float disty = pcy - ecy;

							//TODO: Rework the angle system
							float newAngle = (float) Math.atan(distx / disty);
							newAngle = (float) Math.toDegrees(newAngle);
							if(pcy > ecy) newAngle =  -90 - (90-newAngle);
							if(pcx < ecx && pcy < ecy) newAngle = -270 - (90-newAngle);

							bullet.velocityX = (float) Math.sin(Math.toRadians(newAngle + 180));
							bullet.velocityY = (float) Math.cos(Math.toRadians(newAngle + 180));
						}else {
							bullet.velocityX = (float) Math.sin(Math.toRadians(bullet.angle + angle));
							bullet.velocityY = (float) Math.cos(Math.toRadians(bullet.angle + angle));
						}
						resetSpeedUp();
						angle = -1f;
						angles.set(index, angle);
					}
				}
			}
		}
		//Updating the bullets
		for (Bullet bullet : bullets) {
			bullet.update(tslf);
			//Hit the player?
			if(!bullet.hitSomething) {
				if(bullet.checkCollisionToObject(Globals.player)) {
					Globals.player.startKnockback(bullet.angle, Globals.player.bulletImpact, 1f);
					Globals.player.gotHit = true;
					bullet.maxExplosionRadius = 30;
					bullet.hitSomething();
				}
			}
		}
		//Removal of the bullets
		if(!bullets.isEmpty()) {
			ArrayList<Bullet> removableBullets = new ArrayList<Bullet>();
			for (Bullet bullet : bullets) {
				if(bullet.canBeDeactivated()) {
					removableBullets.add(bullet);
				}
			}
			for (Bullet b : removableBullets) {
				angles.remove(bullets.indexOf(b));
				bullets.remove(b);
			}
		}
	}
	
	@Override
	public void getHitByProjectile(Projectile p, float damage) {
		//Add a new Bullet
		Bullet b = new Bullet();
		b.velocityX = 0;
		b.velocityY = 0;
		b.isActive = true;
		this.bullets.add(b);
		this.angles.add(new Float(0));
		//Remove old
		p.deactivate();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		g.setColor(Color.BLACK);
		g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
		
		g.setColor(Color.BLUE);
		switch (lookDirection) {
		case UP:
			g.fillOval((int)(this.x + size/2 - a/2 + Globals.insetX), (int)this.y + Globals.insetY, a, a);
			break;
		case DOWN:
			g.fillOval((int)(this.x + size/2 - a/2 + Globals.insetX), (int)this.y + size - a + Globals.insetY, a, a);
			break;
		case RIGHT:
			g.fillOval((int)(this.x + size - a + Globals.insetX), (int)this.y + size/2 - a/2 + Globals.insetY, a, a);
			break;
		case LEFT:
			g.fillOval((int)(this.x + Globals.insetX), (int)this.y + size/2 - a/2 + Globals.insetY, a, a);
			break;
		default:
			break;
		}
		
		if(isInHitAnimation) {
			if(showBlink) {
				g.setColor(Color.WHITE);
				g.fillOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
				g.drawOval((int)x + Globals.insetX, (int)y + Globals.insetY, this.size, this.size);
			}
		}
		if(isInDieAnimation) {
			g.setColor(Color.BLACK);
			g.fillOval((int)(x + size/2 - radius/2 + Globals.insetX), (int)(y + size/2 - radius/2 + Globals.insetY), (int)radius, (int)radius);
		}

		//Bullets
		for (Bullet bullet : bullets) {
			g.setColor(Color.RED);
			bullet.draw(g);
		}
	}
}
