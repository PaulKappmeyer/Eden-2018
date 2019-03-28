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
		
		//Draw the map transition
		if(Game.state == Game.MAP_TRANSITION || Game.state == Game.MAP_TRANSITION_OUT || Game.state == Game.RESET) {
			g.setColor(Color.BLACK);
			g.fillRect((int)transitionX + Globals.insetX, (int)transitionY + Globals.insetY, (int)transitionWidth, (int)transitionHeight);
		}
		else if(Game.state == Game.INTERACTING) {
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
	//Transition
	static float transitionX = 0;
	static float transitionY = 0;
	static float transitionWidth = 0;
	static float transitionHeight = 0;
	int speed = 1400;
	static int direction;
	
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
		
		//Transition IN
		if(Game.state == Game.MAP_TRANSITION) {
			switch (direction) {
			case Eden.RIGHT:
				transitionWidth += speed*tslf;
				if(transitionWidth >= Globals.width) {
					transitionWidth = Globals.width*1.25f;
					Game.state = Game.RESET;
				}
				break;
			case Eden.LEFT:
				transitionX -= speed*tslf;
				if(transitionX <= 0) {
					transitionX = 0 - Globals.width*0.25f;
					transitionWidth = Globals.width*1.25f;
					Game.state = Game.RESET;
				}
				break;
			case Eden.DOWN:
				transitionHeight += speed*tslf;
				if(transitionHeight >= Globals.height) {
					transitionHeight = Globals.height*1.25f;
					Game.state = Game.RESET;
				}
				break;
			case Eden.UP:
				transitionY -= speed*tslf;
				if(transitionY <= 0) {
					transitionY = 0 - Globals.height*0.25f;
					transitionHeight = Globals.height*1.25f;
					Game.state = Game.RESET;
				}
				break;
			default:
				break;
			}
		}
		//Transition out
		else if(Game.state == Game.MAP_TRANSITION_OUT) {
			switch (direction) {
			case Eden.RIGHT:
				transitionWidth -= speed*tslf;
				if(transitionWidth <= 0) {
					Game.state = Game.RUNNING;
				}
				break;
			case Eden.LEFT:
				transitionX += speed*tslf;
				if(transitionX >= Globals.width) {
					Game.state = Game.RUNNING;
				}
				break;
			case Eden.DOWN:
				transitionHeight -= speed*tslf;
				if(transitionHeight <= 0) {
					Game.state = Game.RUNNING;
				}
				break;
			case Eden.UP:
				transitionY += speed * tslf;
				if(transitionY >= Globals.height) {
					Game.state = Game.RUNNING;
				}
				break;
			default:
				break;
			}
		}
	}
	
	//SET TRANSITION
	public static void startTransition(int direction) {
		Screen.direction = direction;
		switch (direction) {
		case Eden.RIGHT:
			transitionX = 0;
			transitionY = 0;
			transitionWidth = 0;
			transitionHeight = Globals.height;
			break;
		case Eden.LEFT:
			transitionX = Globals.width;
			transitionY = 0;
			transitionWidth = Globals.width;
			transitionHeight = Globals.height;
			break;
		case Eden.DOWN:
			transitionX = 0;
			transitionY = 0;
			transitionWidth = Globals.width;
			transitionHeight = 0;
			break;
		case Eden.UP:
			transitionX = 0;
			transitionY = Globals.height;
			transitionWidth = Globals.width;
			transitionHeight = Globals.height;
			break;
		default:
			break;
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