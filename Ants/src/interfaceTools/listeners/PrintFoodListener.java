package interfaceTools.listeners;

import java.awt.event.*;
import mainElements.Simulator;

// TODO Listener qui modifie les données (dessinables) lors d'un click dans la zone de dessin
public class PrintFoodListener extends PrintListener {
	  
	public PrintFoodListener(Simulator dessinables) {
		super(dessinables);
	}
	  
	@Override
	public void mouseClicked(MouseEvent e){
		int x = adaptPosition(e.getX());
		int y = adaptPosition(e.getY());
		data.modifyEndNode(x, y);	
	} 
}