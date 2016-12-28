

import javax.swing.*;

import interfaceTools.Controle;
import interfaceTools.DisplayZone;
import mainElements.Simulator;

import java.awt.*;

/**
 * Main class.
 */
public class Software {

  public static void main(String[] args) {
    JFrame soft = new JFrame("Ant colony simulator");
    soft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Container contentPane = soft.getContentPane();
    contentPane.setLayout(new BorderLayout()); 

    Simulator simulator = new Simulator();
    
    DisplayZone   dsn  = new DisplayZone();
    contentPane.add(BorderLayout.CENTER,dsn);

    Controle ctrl = new Controle(dsn,simulator);
    contentPane.add(BorderLayout.WEST, ctrl);

    soft.pack();
    soft.setVisible(true);
  }
}