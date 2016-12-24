package map;
public class Path {
	private Node initialNode;
	
	private Node finalNode;
	
	private double distance;
	
	private double probability;
	
	public Path(Node start, Node end, double pathDistance, double proba){
		setInitialNode(start);
		setFinalNode(end);
		setDistance(pathDistance);
		setProbability(proba);
	}

	public Node getInitialNode() {
		return initialNode;
	}

	public void setInitialNode(Node initialNode) {
		this.initialNode = initialNode;
	}

	public Node getFinalNode() {
		return finalNode;
	}

	public void setFinalNode(Node finalNode) {
		this.finalNode = finalNode;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
}
