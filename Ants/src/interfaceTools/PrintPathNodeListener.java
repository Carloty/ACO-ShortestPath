package interfaceTools;

import java.awt.event.*;
import mainElements.Simulator;

public class PrintPathNodeListener extends PrintListener {
	  
	public PrintPathNodeListener(Simulator dessinables) {
		super(dessinables);
	}
	  
	public void mouseClicked(MouseEvent e){
		int x = adaptPosition(e.getX());
		int y = adaptPosition(e.getY());
		data.addNode(x, y);
	} 
}