package interfaceTools;

import javax.swing.*;

import mainElements.Simulator;

import java.awt.*;
import java.awt.event.*;

public class Controle extends JPanel {

  private DisplayZone displayZone;
  private Simulator simulator;

  public Controle(DisplayZone _zone, Simulator _printables) {
    displayZone = _zone;
    simulator = _printables;

    // Associe les donn�es (printables) avec la zone d'affichage
    _zone.setModel(_printables);

    // assignation du layout manager
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 

    // creation du groupe d'exclusivite des boutons cercle et rect
    ButtonGroup bg = new ButtonGroup();

    // JRadioButton colonyButton, ajout au panneau, et assignation d'un listener
    JRadioButton colonyButton = new JRadioButton("Fourmili�re", false);
    colonyButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    colonyButton.setEnabled(true);
    colonyButton.setForeground(Color.RED);
    bg.add(colonyButton);
    add(colonyButton);

    // Ajouter le listener lié à la sélection du radio cercle
    PrintColonyListener colonyListener = new PrintColonyListener(_printables);
    colonyButton.addItemListener(new GestionOutil(colonyListener));
    //_dessin.addMouseListener(cercleListener);

    // JRadioButton rectangle, ajout au panneau, et assignation d'un listener
    JRadioButton basicNodeButton = new JRadioButton("Etape", false);
    basicNodeButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    bg.add(basicNodeButton);
    add(basicNodeButton);

    // Ajouter le listener lié à la sélection du radio rectangle
    PrintPathNodeListener pathNodeListener = new PrintPathNodeListener(_printables);
    basicNodeButton.addItemListener(new GestionOutil(pathNodeListener));
    //_dessin.addMouseListener(rectangleListener);
    
    // JRadioButton rectangle, ajout au panneau, et assignation d'un listener
    JRadioButton foodButton = new JRadioButton("Nourriture", false);
    foodButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    foodButton.setForeground(Color.BLUE);
    bg.add(foodButton);
    add(foodButton);

    // Ajouter le listener lié à la sélection du radio rectangle
    PrintFoodListener foodListener = new PrintFoodListener(_printables);
    foodButton.addItemListener(new GestionOutil(foodListener));
    
    // JRadioButton rectangle, ajout au panneau, et assignation d'un listener
    JRadioButton pathButton = new JRadioButton("Chemin", false);
    pathButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    bg.add(pathButton);
    add(pathButton);

    // Ajouter le listener lié à la sélection du radio rectangle
    PrintPathListener pathListener = new PrintPathListener(_printables);
    pathButton.addItemListener(new GestionOutil(pathListener));
    
    // TODO add box to indicate the number of ants and the number of iterations
    JLabel labelAnts = new JLabel("Nombre de fourmis");
    labelAnts.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    add(labelAnts);
    JTextField nbAntsText = new JTextField("20");
    nbAntsText.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    add(nbAntsText);
    
    JLabel labelIterations = new JLabel("Nombre d'aller-retour");
    labelIterations.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    add(labelIterations);
    JTextField nbIterationsText = new JTextField("50");
    nbIterationsText.setAlignmentY(JComponent.LEFT_ALIGNMENT);
    add(nbIterationsText);
    
    // zone de glue
    add(Box.createGlue());
    
    // bouton d'effacement
    JButton simuButton = new JButton("Simuler !");
    simuButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
    add(simuButton);

    // Listener li� � l'effacement par le bouton effacer
    simuButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int nbAnts = getNbInTextField(nbAntsText);
			int nbIterations = getNbInTextField(nbIterationsText);
			if(nbAnts != -1 && nbIterations != -1){
				simulator.setNbOfAnts(nbAnts);
				simulator.setNbOfIterations(nbIterations);
				simulator.launch();
			}
		}
		
		private int getNbInTextField(JTextField textField){
			String text = textField.getText();
			try {
				Integer nb = Integer.parseInt(text);
				return nb;
			} catch (Exception e) {}
			return -1;
		}
    });

    // bouton d'effacement
    JButton deleteButton = new JButton("Effacer");
    deleteButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
    add(deleteButton);

    // Listener li� � l'effacement par le bouton effacer
    deleteButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		  simulator.vider();
		}
    });

    // bouton pour quitter
    JButton exitButton = new JButton("Quitter");
    exitButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
    add(exitButton);

    exitButton.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		  System.exit(0);
		}
    });

  }

  // Classe GestionOutil qui en fonction de la sélection
  // du bouton radio permet d'associer le bon listener.
  // gestion des outils activation/desactivation
  private class GestionOutil implements ItemListener {
    private MouseListener listener;

    public GestionOutil(MouseListener _listener) {
    	listener = _listener;
    }

    public void itemStateChanged(ItemEvent e){
	if (e.getStateChange()==ItemEvent.SELECTED){
		displayZone.addMouseListener(listener);
	} else {
		displayZone.removeMouseListener(listener);
	}
    }
  }
}