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
	private Game game;
	
	/**
	 * Constructor; initializes the JFrame and the BufferStrategy
	 */
	public Screen(Game game) {
		this.setTitle("Eden");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(false);
		this.setVisible(true);
		this.setResizable(false);
		
		createBufferStrategy(2);
		strat = getBufferStrategy();
		
		Insets i = getInsets();
		Globals.insetX = i.left;
		Globals.insetY = i.top;
		
		this.setSize(Globals.width + Globals.insetX + i.right, Globals.height + Globals.insetY + i.bottom);
		this.setLocationRelativeTo(null);
		
		this.addKeyListener(new Controls());
		
		this.game = game;
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
		
		//Screenshake
		if(screenshake) {
			g.translate(screenshakeX, screenshakeY);
		}
		
		//Game
		game.draw(g);
		
		//Draw the alpha mask
		if(Game.state == Game.INTERACTING) {
			g.setColor(new Color(100, 100, 100, 50));
			g.fillRect(Globals.insetX, Globals.insetY, this.getWidth(), this.getHeight());
		}
		
		//FPS
		g.setColor(Color.BLACK);
		g.drawString(Globals.fps + " fps", 10 + Globals.insetX, 10 + g.getFont().getSize() + Globals.insetY);
	}
	
	//Screenshake
	public static boolean screenshake;
	public int screenshakeX;
	public int screenshakeY;
	public static int screenshakeStrength = 0;
	public static float screenshakeDuration = 0;
	public float tslsc;
	
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