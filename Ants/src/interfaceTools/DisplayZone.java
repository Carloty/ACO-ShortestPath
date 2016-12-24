package interfaceTools;

import javax.swing.*;

import mainElements.Simulator;

import java.awt.*;
import java.util.*;

public class DisplayZone extends JPanel implements Observer {
//  private PrintableData data;
  private Simulator simulator;
  
  public DisplayZone() {
    super();
    
    setOpaque(true);
    setBackground(Color.white);
    setPreferredSize(new Dimension(300,200));

  }
  
  // Associe les donn�es à la zone de dessin et les d�clare observables par Dessin
  public void setModel(Simulator printables) {
	simulator = printables;
  	printables.addObserver(this);
  }

  
  // Dessine le ou les figures contenues dans les données
  // si une figure est reçu ne dessiner que celle-ci
  // sinon tout redessinner
  public void update(Observable obs, Object o) {
  	this.paintComponent(this.getGraphics());
  }

  // Dessine les figures contenues dans les données
  public void paintComponent(Graphics gc) {
  	super.paintComponent(gc);
  	simulator.print(gc);
  }
}