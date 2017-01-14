package interfaceTools.listeners;

import java.awt.event.*;
import mainElements.Simulator;

public class PrintPathListener extends PrintListener {
	private boolean firstCall;
	private int previousX;
	private int previousY;
	  
	public PrintPathListener(Simulator dessinables) {
		super(dessinables);
		firstCall = true;
	}
	  
	@Override
	public void mouseClicked(MouseEvent e){
		if(firstCall){
			previousX = adaptPosition(e.getX());
			previousY = adaptPosition(e.getY());
		} else {
			int x = adaptPosition(e.getX());
			int y = adaptPosition(e.getY());
			data.linkNodes(previousX, previousY, x, y);
		}
		firstCall = !firstCall;
	}
}