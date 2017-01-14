package interfaceTools.listeners;

import java.awt.event.*;
import mainElements.Simulator;

public abstract class PrintListener extends MouseAdapter {
	protected Simulator data;
	  
	public PrintListener(Simulator dessinables) {
		super();
		data = dessinables;
	}
	
	protected int adaptPosition(int position){
		return position;//Math.max(0, position-(Simulator.radius/2));
	}
	  
	public abstract void mouseClicked(MouseEvent e);
}