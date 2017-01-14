package mapData;
import java.util.ArrayList;
import java.util.List;

/**
 * Intersection of paths.
 */
public class Node {
	
	/**
	 * Position of the node in the map
	 */
	private Position nodePosition;
	
	/**
	 * Previous nodes connected to this one.
	 */
	private List<Node> parents;
	
	/**
	 * Paths aiming at this node's children.
	 */
	private List<Path> children;
	
	public Node(){
		this(0, 0);
	}
	
	public Node(int x, int y){
		nodePosition = new Position(x, y);
		parents = new ArrayList<>();
		children = new ArrayList<>();
	}
	
	public Position getPosition(){
		return this.nodePosition;
	}

	public int getXPosition() {
		return nodePosition.getX();
	}

	public void setX(int x) {
		this.nodePosition.setX(x);
	}

	public int getYPosition() {
		return nodePosition.getY();
	}

	public void setY(int y) {
		this.nodePosition.setY(y);
	}

	public List<Node> getParents() {
		return parents;
	}
	
	public void unlinkNode(){
		for(Node parent : parents){
			parent.removeChild(this);
		}
		for(Path path : children){
			path.getFinalNode().removeParent(this);
			children.remove(path);
		}
	}

	public void setParent(Node parent) {
		if(!parents.contains(parent)){
			parents.add(parent);
		}
	}
	
	public boolean removeParent(Node parent){
		return parents.remove(parent);
	}

	public List<Path> getAvailablePaths() {
		return children;
	}
	
	public void addChild(Node child){
		double distanceToParent = Position.computeDistance(this.getPosition(), child.getPosition());
		child.setParent(this);
		Path newPath = new Path(this, child, distanceToParent, 0.0);
		children.add(newPath);
		actualizeProbabilities();
	}
	
	/**
	 * Set all path probability at the same value
	 */
	private void actualizeProbabilities() {
		double proba = 1.0/children.size();
		for(Path path : children){
			path.setProbability(proba);
		}
	}
	
	/**
	 * Make sure that the sum of all path probability is 1
	 */
	public void reshapeProbabilities(){
		double total = 0.0;
		for(Path path : children){
			total += path.getProbability();
		}
		for(Path path : children){
			path.setProbability(path.getProbability()/total);
		}
	}

	public void removeChild(Node child){
		for(Path path : children){
			if(path.getFinalNode().equals(child)){
				children.remove(path);
				break;
			}
		}
		actualizeProbabilities();
	}
	
	/**
	 * Check if the current node is the final node of the map.
	 */
	public boolean isFinalNode(){
		return children.isEmpty();
	}
	
	/**
	 * Check if the current node is the initial node of the map.
	 */
	public boolean isInitialNode(){
		return parents.isEmpty();
	}

	/**
	 * Leave pheromone on the specified path = increase its probability
	 */
	public void leavePheromone(Path selectedPath, double weight) {
		double newProba = selectedPath.getProbability()+weight/selectedPath.getDistance();
		selectedPath.setProbability(newProba);
		reshapeProbabilities();
	}
}
