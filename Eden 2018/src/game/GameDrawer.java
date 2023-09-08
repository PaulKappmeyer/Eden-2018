/*
 * 
 */
package game;

import java.awt.Color;
import java.awt.Graphics;

import input.KeyboardinputManager;
import input.MouseinputManager;

/**
 * The screen as a JFrame filled with a BufferStrategy 
 * @author Paul Kappmeyer
 *
 */
@SuppressWarnings("serial")
public final class GameDrawer extends Window{
	
	private Game game;
	
	/**
	 * Constructor; initializes the JFrame and the BufferStrategy
	 */
	public GameDrawer(Game game) {
		super("Eden", Globals.screenWidth, Globals.screenHeight);
		
		this.addKeyListener(new KeyboardinputManager());
		MouseinputManager m = new MouseinputManager();
		this.addMouseListener(m);
		this.addMouseMotionListener(m);
		
		this.game = game;
	}
	
	/**
	 * 
	 * @param g A Graphics Object to draw
	 */
	@Override
	public void draw(Graphics g){
		//Clear the screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		//Game
		game.draw(g);
		
		//Draw the alpha mask
		if (Game.state == Gamestate.INTERACTING) {
			g.setColor(new Color(100, 100, 100, 50));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		//FPS
		g.setColor(Color.RED);
		g.drawString(Globals.fps + " fps", 5, g.getFont().getSize());
	}
	
	//Screenshake
	public static boolean screenshake;
	public static int screenshakeX;
	public static int screenshakeY;
	public static int screenshakeStrength = 0;
	public static float screenshakeDuration = 0;
	public static float tslsc;
	
	public void update(float tslf) {
		//Updating the screenshake 
		if (screenshake) {
			screenshakeX = -screenshakeStrength/2 + Globals.random.nextInt(screenshakeStrength);
			screenshakeY = -screenshakeStrength/2 + Globals.random.nextInt(screenshakeStrength);
			tslsc += tslf;
			if (tslsc >= screenshakeDuration) {
				tslsc = -screenshakeDuration;
				screenshake = false;
				screenshakeStrength = 0;
				screenshakeDuration = 0;
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