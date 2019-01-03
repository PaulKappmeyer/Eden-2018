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
		//Clear the Screen
		g.setColor(Color.WHITE);
		g.fillRect(0 + Globals.insetX, 0 + Globals.insetY, this.getWidth(), this.getHeight());
		
		//Draw the Player
		Globals.player.show(g);
		
		for (Enemy e : Globals.enemies) {
			e.show(g);
		}
		
		for (Bullet b : Globals.player.bullets) {
			b.show(g);
		}
		
		g.setColor(Color.BLACK);
		g.drawString(Globals.fps + " fps", 10 + Globals.insetX, 10 + g.getFont().getSize() + Globals.insetY);
	}
}