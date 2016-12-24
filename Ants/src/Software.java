

import javax.swing.*;

import interfaceTools.Controle;
import interfaceTools.DisplayZone;
import mainElements.Simulator;

import java.awt.*;

public class Software {

  public static void main(String[] args) {
    JFrame soft = new JFrame("Ant colony simulator");

    soft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Container contentPane = soft.getContentPane();
    // assignation du layout manager
    contentPane.setLayout(new BorderLayout()); 

    Simulator simulator = new Simulator();
    //Cercle c1 = new Cercle(100,100,50,Color.RED);
    //figures.addDessinable(c1);
    
    // création du panneau de la zone de dessin
    DisplayZone   dsn  = new DisplayZone();
    //dsn.setModel(figures);
    contentPane.add(BorderLayout.CENTER,dsn);

    // création du panneau de controle
    Controle ctrl = new Controle(dsn,simulator);
    contentPane.add(BorderLayout.WEST, ctrl);

    soft.pack();
    soft.setVisible(true);
  }
}
