package interfaceTools;

import javax.swing.*;

import mainElements.Simulator;

import java.awt.*;
import java.util.*;

public class DisplayZone extends JPanel implements Observer {
	private static final int xSize = 1000;
	private static final int ySize = 600;
	
	private Simulator simulator;
  
  public DisplayZone() {
    super();
    
    setOpaque(true);
    setBackground(Color.white);
    setPreferredSize(new Dimension(xSize,ySize));
  }
  
  public void setModel(Simulator printables) {
	simulator = printables;
  	printables.addObserver(this);
  }

  public void update(Observable obs, Object o) {
  	this.paintComponent(this.getGraphics());
  }

  public void paintComponent(Graphics gc) {
  	super.paintComponent(gc);
  	simulator.print(gc);
  }
}