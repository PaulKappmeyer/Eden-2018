package game;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Window extends JFrame{
	private BufferStrategy strat;
	public int insetX;
	public int insetY;
	
	public Window(String title, int width, int height) {
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(false);
		this.setVisible(true);
		this.setResizable(false);
		
		createBufferStrategy(2);
		strat = getBufferStrategy();
		
		Insets i = getInsets();
		insetX = i.left;
		insetY = i.top;
		
		this.setSize(width + insetX + i.right, height + insetY + i.bottom);
		this.setLocationRelativeTo(null);
	}
	
	
	/**
	 * This function gets called every frame to repaint the screen; calls the function show
	 * {@link #show(Graphics g)}
	 */
	public void repaintScreen(){
		Graphics g = strat.getDrawGraphics();
		draw(g);
		g.dispose();
		strat.show();
	}
	
	public void draw(Graphics g) {
	};
}
