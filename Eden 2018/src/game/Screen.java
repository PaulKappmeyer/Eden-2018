package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

//The Screen as a JFrame filled with a Bufferstrategy 
@SuppressWarnings("serial")
public class Screen extends JFrame{
	private BufferStrategy strat;
	
	//-------------------------------------------------------- Constructor
	public Screen() {
		super("Eden");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
		createBufferStrategy(2);
		strat = getBufferStrategy();
		
		Insets i = getInsets();
		Globals.insetX = i.left;
		Globals.insetY = i.top;
		
		this.setSize(Globals.width + Globals.insetX + i.right, Globals.height + Globals.insetY + i.bottom);
		this.setLocationRelativeTo(null);
		
		this.addKeyListener(new Controls());
	}
	
	//------------------------------------------------------------ Paints the Screen
	public void repaintScreen(){
		Graphics g = strat.getDrawGraphics();
		show(g);
		g.dispose();
		strat.show();
	}
	
	//-------------------------------------------------------------- Drawing, every frame
	public void show(Graphics g){
		//Clear the Screen
		g.setColor(Color.WHITE);
		g.fillRect(0 + Globals.insetX, 0 + Globals.insetY, Globals.width, Globals.height);
		
		//Draw the Player
		g.setColor(Color.BLACK);
		Globals.player.show(g);
	}
}