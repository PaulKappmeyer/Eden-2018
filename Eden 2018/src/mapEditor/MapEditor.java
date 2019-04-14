/*
 * 
 */
package mapEditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import game.Globals;
import game.Window;
import input.MouseinputManager;

@SuppressWarnings("serial")
/**
 * 
 * @author Paul
 *
 */
public class MapEditor extends Window{

	JFrame settings;
	JSlider sliderForPercentage;
	JSlider sliderForNumDots;
	JSlider sliderForSpeed;
	JCheckBox boxForPreviousRule;
	JCheckBox boxForRandomLocation;
	JButton reset;
	
	MouseinputManager m;
	
	Object obj = new Object(100, 100, 16, 16);
	Object selected;
	
	public MapEditor() {
		super("Eden MapEditor", 1000, 800);
		
		m = new MouseinputManager();
		super.addMouseListener(m);
		this.addMouseMotionListener(m);
		
		Globals.insetX = this.insetX;
		Globals.insetY = this.insetY;
		
		//Creating the settings window
		settings = new JFrame("settings");
		settings.setSize(400, 400);
		settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		settings.setVisible(true);
		settings.setResizable(false);
		JPanel panel = new JPanel();
		sliderForPercentage = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		sliderForPercentage.setToolTipText("Prozent des Weges");
		sliderForPercentage.setMajorTickSpacing(10);
		sliderForPercentage.setMinorTickSpacing(5);
		sliderForPercentage.setSnapToTicks(true);
		sliderForPercentage.setPaintTicks(true);
		sliderForPercentage.setPaintLabels(true);
		sliderForPercentage.setPaintTrack(true);
		sliderForPercentage.setVisible(true);
		
		sliderForNumDots = new JSlider(JSlider.HORIZONTAL, 1, 20, 3);
		sliderForNumDots.setToolTipText("Anzahl Punkte");
		sliderForNumDots.setMajorTickSpacing(2);
		sliderForNumDots.setMinorTickSpacing(1);
		sliderForNumDots.setSnapToTicks(true);
		sliderForNumDots.setPaintTicks(true);
		sliderForNumDots.setPaintLabels(true);
		sliderForNumDots.setPaintTrack(true);
		sliderForNumDots.setVisible(true);
		
		sliderForSpeed = new JSlider(JSlider.HORIZONTAL, 1, 100, 25);
		sliderForSpeed.setToolTipText("Geschwindigkeit");
		sliderForSpeed.setMajorTickSpacing(25);
		sliderForSpeed.setSnapToTicks(false);
		sliderForSpeed.setPaintTicks(true);
		sliderForSpeed.setPaintLabels(true);
		sliderForSpeed.setPaintTrack(true);
		sliderForSpeed.setVisible(true);
		
		boxForPreviousRule = new JCheckBox("can take Previous?", true);
		boxForPreviousRule.setToolTipText("Darf vorherigen Punkt wählen?");
		
		boxForRandomLocation = new JCheckBox("random Location?", false);
		boxForRandomLocation.setToolTipText("Zufällig Position Punkte?");
		
		reset = new JButton("reset?");
		reset.setToolTipText("Zurücksetzen?");
		
		panel.add(reset);
		panel.add(sliderForPercentage);
		panel.add(sliderForNumDots);
		panel.add(sliderForSpeed);
		panel.add(boxForPreviousRule);
		panel.add(boxForRandomLocation);
		settings.add(panel);
		settings.pack();
	}
	
	public static void main(String[] args) throws InterruptedException {
		MapEditor me = new MapEditor();
		
		while(true){
			
			me.update();
			
			me.repaintScreen();
			
			Thread.sleep(15);
		}
	}
	
	public void update() {
		if(MouseinputManager.isButtonDown(MouseEvent.BUTTON1)) {
			float x = MouseinputManager.getMouseX();
			float y = MouseinputManager.getMouseY();
			System.out.println(x + "  " + y);
			if(x >= obj.x && x <= obj.x + obj.w && y >= obj.y && y <= obj.y + obj.h) {
				selected = obj;
			}
		}else {
			selected = null;
		}
		
		if(selected != null) {
			obj.x = (int) MouseinputManager.getMouseX();
			obj.y = (int) MouseinputManager.getMouseY();
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.clearRect(insetX, insetY, 1000, 800);
		//Bounding of the map
		g.setColor(Color.BLACK);
		g.drawRect(90 + insetX, 90 + insetY, 800, 600);
		
		obj.draw(g);
	}
	
	
	class Object{
		int x, y, w, h;
		Color c;
		
		public Object(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.c = Color.BLUE;
		}
		
		public void draw(Graphics g) {
			g.setColor(c);
			g.fillRect(x + insetX, y + insetY, w, h);
			g.setColor(Color.BLACK);
			g.drawRect(x + insetX, y + insetY, w, h);
		}
	}
}