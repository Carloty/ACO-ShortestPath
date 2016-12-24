package mainElements;
import java.util.ArrayList;
import java.util.List;

import map.Node;
import map.Path;

public class Ant {	
	private Node nodePosition;
	
	private List<Path> visitedPaths;
	
	public Ant(){
		this(null);
	}
	
	public Ant(Node position){
		super();
		setNodePosition(position);
		visitedPaths = new ArrayList<>();
	}
	
	public double getXPosition(){
		return nodePosition.getXPosition();
	}
	
	public double getYPosition(){
		return nodePosition.getYPosition();
	}

	public Node getCurrentNode() {
		return nodePosition;
	}

	public void setNodePosition(Node nodePosition) {
		this.nodePosition = nodePosition;
	}
	
	public void addAVisitedPath(Path path){
		visitedPaths.add(path);
	}
	
	public List<Path> getVisitedPaths(){
		return visitedPaths;
	}
	
	public void backToColony(Node colonyPosition){
		setNodePosition(nodePosition);
		visitedPaths.clear();
	}
	
	public double getTraveledDistance(){
		double distance = 0.0;
		for(Path path : visitedPaths){
			distance += path.getDistance();
		}
		return distance;
	}
}
