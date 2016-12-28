package interfaceTools;

import javax.swing.*;

import mainElements.Simulator;

import java.awt.*;
import java.awt.event.*;

/**
 * Panel to configure ACO settings
 */
public class Controle extends JPanel {
	private static final int suggestedNumberOfAnts = 100;
	private static final int suggestedNumberOfIterations = 1000;

	/**
	 * Zone where all nodes and paths are displayed.
	 */
	private DisplayZone displayZone;
	
	/**
	 * ACO simulator,containing all nodes and paths data.
	 */
	private Simulator simulator;
	
	public Controle(DisplayZone _zone, Simulator _simu) {
		displayZone = _zone;
		simulator = _simu;
		
		_zone.setModel(_simu);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
		
		// Group of buttons to create / modify nodes
		ButtonGroup bg = new ButtonGroup();
		
		// Button to set the colony position
		JRadioButton colonyButton = new JRadioButton("Fourmilière", false);
		colonyButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		colonyButton.setEnabled(true); // FIXME
		colonyButton.setForeground(Color.RED);
		bg.add(colonyButton);
		add(colonyButton);
		
		PrintColonyListener colonyListener = new PrintColonyListener(_simu);
		colonyButton.addItemListener(new ToolManager(colonyListener));
		
		// Button to add a basic path node
		JRadioButton basicNodeButton = new JRadioButton("Etape", false);
		basicNodeButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		bg.add(basicNodeButton);
		add(basicNodeButton);
		
		PrintPathNodeListener pathNodeListener = new PrintPathNodeListener(_simu);
		basicNodeButton.addItemListener(new ToolManager(pathNodeListener));
		
		// Button to add the goal position (food place)
		JRadioButton foodButton = new JRadioButton("Nourriture", false);
		foodButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		foodButton.setForeground(Color.BLUE);
		bg.add(foodButton);
		add(foodButton);
		
		PrintFoodListener foodListener = new PrintFoodListener(_simu);
		foodButton.addItemListener(new ToolManager(foodListener));
		
		// Button to add path
		JRadioButton pathButton = new JRadioButton("Chemin", false);
		pathButton.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		bg.add(pathButton);
		add(pathButton);
		
		PrintPathListener pathListener = new PrintPathListener(_simu);
		pathButton.addItemListener(new ToolManager(pathListener));
		
		// Button to link all nodes
		JButton linkButton = new JButton("Relier les étapes");
		linkButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		add(linkButton);
		
		linkButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.linkAllNodes();
			}
		});
		
		// Text field to indicate the number of ants
		JLabel labelAnts = new JLabel("Nombre de fourmis");
		labelAnts.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		add(labelAnts);
		JTextField nbAntsText = new JTextField(suggestedNumberOfAnts+"");
		nbAntsText.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		add(nbAntsText);
		
		// Text field to indicate the number of iterations
		JLabel labelIterations = new JLabel("Nombre d'aller-retour");
		labelIterations.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		add(labelIterations);
		JTextField nbIterationsText = new JTextField(suggestedNumberOfIterations+"");
		nbIterationsText.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		add(nbIterationsText);
		
		// Glue zone
		add(Box.createGlue());
		
		// Button to launch the simulation
		JButton simuButton = new JButton("Simuler !");
		simuButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		add(simuButton);
		
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
		
		// Delete button
		JButton deleteButton = new JButton("Effacer");
		deleteButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		add(deleteButton);
		
		deleteButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.reset();
			}
		});
		
		// Exit button
		JButton exitButton = new JButton("Quitter");
		exitButton.setAlignmentY(JComponent.CENTER_ALIGNMENT);
		add(exitButton);
		
		exitButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	  }
	
	  // Associate the correct listener
	  private class ToolManager implements ItemListener {
		private MouseListener listener;
		
		public ToolManager(MouseListener _listener) {
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