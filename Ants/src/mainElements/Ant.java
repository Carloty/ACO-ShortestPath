package mainElements;
import java.util.ArrayList;
import java.util.List;

import mapData.Node;
import mapData.Path;

/**
 * Class representing an Ant.
 */
public class Ant {
	
	/**
	 * Current node where the ant is.
	 */
	private Node nodePosition;
	
	/**
	 * Paths previously visited by the ant.
	 */
	private List<Path> visitedPaths;
	
	public Ant(){
		this(null);
	}
	
	/**
	 * Create ant at the specified node.
	 * @param position Current node where the ant is.
	 */
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
	
	/**
	 * Set the specified node as the current position of the ant
	 * and reset the visited paths.
	 * @param colonyPosition Node where the colony is supposed to be.
	 */
	public void backToColony(Node colonyPosition){
		setNodePosition(colonyPosition);
		visitedPaths.clear();
	}
	
	/**
	 * Get the distance traveled by the ant.
	 */
	public double getTraveledDistance(){
		double distance = 0.0;
		for(Path path : visitedPaths){
			distance += path.getDistance();
		}
		return distance;
	}
}
