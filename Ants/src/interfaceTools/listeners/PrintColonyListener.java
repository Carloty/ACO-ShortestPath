package interfaceTools.listeners;

import java.awt.event.*;
import mainElements.Simulator;

public class PrintColonyListener extends PrintListener {
	  
	public PrintColonyListener(Simulator dessinables) {
		super(dessinables);
	}
	  
	public void mouseClicked(MouseEvent e){
		int x = adaptPosition(e.getX());
		int y = adaptPosition(e.getY());
		data.modifyStartNode(x, y);	
	} 
}