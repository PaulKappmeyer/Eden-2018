package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * The screen as a JFrame filled with a BufferStrategy 
 * @author Paul Kappmeyer
 *
 */
@SuppressWarnings("serial")
public class Screen extends JFrame{
	private BufferStrategy strat;
	
	/**
	 * Constructor; initializes the JFrame and the BufferStrategy
	 */
	public Screen() {
		this.setTitle("Eden");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(false);
		this.setVisible(true);
		this.setResizable(false);
		
		createBufferStrategy(4);
		strat = getBufferStrategy();
		
		Insets i = getInsets();
		Globals.insetX = i.left;
		Globals.insetY = i.top;
		
		this.setSize(Globals.width + Globals.insetX + i.right, Globals.height + Globals.insetY + i.bottom);
		this.setLocationRelativeTo(null);
		
		this.addKeyListener(new Controls());
	}
	
	/**
	 * This function gets called every frame to repaint the screen; calls the function show
	 * {@link #show(Graphics g)}
	 */
	public void repaintScreen(){
		Graphics g = strat.getDrawGraphics();
		show(g);
		g.dispose();
		strat.show();
	}
	
	/**
	 * 
	 * @param g A Graphics Object to draw
	 */
	public void show(Graphics g){
		//Clear the screen
		g.setColor(Color.WHITE);
		g.fillRect(Globals.insetX, Globals.insetY, this.getWidth(), this.getHeight());
		
		g.setColor(Color.BLACK);
		g.drawString(Globals.fps + " fps", 10 + Globals.insetX, 10 + g.getFont().getSize() + Globals.insetY);
		
		//Screenshake
		if(screenshake) {
			g.translate(screenshakeX, screenshakeY);
		}
		
		//Draw the players gun
		if(Globals.player.gun != null)Globals.player.gun.draw(g);
		//Draw the bosses gun
		for (Enemy enemy : Globals.enemies) {
			if(enemy instanceof Boss) {
				Boss boss = (Boss) enemy;
				boss.gun.draw(g);
			}
		}
		//Draw the enemies
		for (Enemy e : Globals.enemies) {
			e.draw(g);
		}
		
		Map.stoneRound.draw(g);
		Map.stone.draw(g);
		Map.chest.draw(g);
		Map.sign.draw(g);
		
		//Draw the player
		Globals.player.draw(g);
		
		//Draw the map transition
		if(Game.state == Game.MAP_TRANSITION || Game.state == Game.MAP_TRANSITION_OUT) {
			g.setColor(Color.BLACK);
			g.fillRect(0 + Globals.insetX, 0 + Globals.insetY, (int)transitionX, Globals.height);
		}
		else if(Game.state == Game.INTERACTING) {
			g.setColor(new Color(100, 100, 100, 50));
			g.fillRect(Globals.insetX, Globals.insetY, this.getWidth(), this.getHeight());
		}
	}
	
	//Screenshake
	public static boolean screenshake;
	public int screenshakeX;
	public int screenshakeY;
	public static int screenshakeStrength = 0;
	public static float screenshakeDuration = 0;
	public float tslsc;
	//Transition
	float transitionX = 0;
	
	public void update(float tslf) {
		//Updating the screenshake 
		if(screenshake) {
			screenshakeX = -screenshakeStrength/2 + Globals.random.nextInt(screenshakeStrength);
			screenshakeY = -screenshakeStrength/2 + Globals.random.nextInt(screenshakeStrength);
			tslsc += tslf;
			if(tslsc >= screenshakeDuration) {
				tslsc = -screenshakeDuration;
				screenshake = false;
				screenshakeStrength = 0;
				screenshakeDuration = 0;
			}
		}
		if(Game.state == Game.MAP_TRANSITION) {
			transitionX += 800*tslf;
			if(transitionX >= Globals.width) {
				transitionX = Globals.width*1.25f;
				Game.state = Game.RESET;
			}
		}
		else if(Game.state == Game.MAP_TRANSITION_OUT) {
			transitionX -= 800*tslf;
			if(transitionX <= 0) {
				transitionX = 0;
				Game.state = Game.RUNNING;
			}
		}
	}
	
	/**
	 * 
	 * @param strength
	 * @param duration
	 */
	public static void addScreenshake(int strength, float duration) {
		screenshakeStrength = strength;
		screenshakeDuration = duration;
		screenshake = true;
	}
}