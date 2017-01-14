package mainElements;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.Set;

import mapData.Node;
import mapData.Path;
import mapData.Position;

public class Simulator extends Observable {
	public static int radius = 12; // TODO check value
	
	private static int iterations;
	
	private static double p = 0.7; // TODO Check value
	
	private static List<Ant> colony = new ArrayList<>();
	private static int numberOfAnts;
	
	private Node start;
	
	private Node end;
	
	private Set<Node> otherNodes = new HashSet<>();
	
	private Set<Path> paths = new HashSet<>();
	
	ArrayList<Path> shortestPath = new ArrayList<Path>();
	
	private Ant currentAnt = null;
	
	private int currentIteration = -1;
	
	public Simulator (){
		super();
	}
	
	public int getCurrentIteration(){
		return currentIteration;
	}
	
	public void setNbOfIterations(int nb){
		iterations = nb;
	}
	
	public void setNbOfAnts(int nb){
		numberOfAnts = nb;
		createColony();
	}
	
	/**
	 * Add the correct number of ants in the colony.
	 */
	private void createColony() {
		colony = new ArrayList<>();
		for(int i = 0; i < numberOfAnts; i++){
			colony.add(new Ant(start));
		}
	}

	/**
	 * Modify the position of the start node
	 */
	public void modifyStartNode(int xPosition, int yPosition){
		if(start != null){
			otherNodes.remove(start);
			removeAllAssociatedPaths(start);
		}
		start = new Node(xPosition, yPosition);
		otherNodes.add(start);
		
		afterChangesMethod(start);
	}
	
	/**
	 * Delete all paths associated to the specified node
	 */
	private void removeAllAssociatedPaths(Node node) {
		node.unlinkNode();
		for(Path path : paths){
			if(path.getInitialNode() == node || path.getFinalNode() == node){
				paths.remove(path);
			}
		}
	}

	/**
	 * Modify the position of the end node
	 */
	public void modifyEndNode(int xPosition, int yPosition){
		if(end != null){
			otherNodes.remove(end);
		}
		end = new Node(xPosition, yPosition);
		otherNodes.add(end);
		
		afterChangesMethod(end);
	}
	
	/**
	 * Create a node at the specified (x,y) position
	 */
	public void addNode(int xPosition, int yPosition){
		Node newNode = new Node(xPosition, yPosition);
		otherNodes.add(newNode);
		
		afterChangesMethod(newNode);
	}
	
	private void afterChangesMethod(Node node) {
		setChanged();
	    notifyObservers(node);
	}
	
	/**
	 * Reset all data
	 */
	public void reset() {
	    start = null;
	    end = null;
	    otherNodes.clear();
	    paths.clear();
	    shortestPath.clear();
	    
	    setChanged();
	    notifyObservers();

	}
	
	/**
	 * Create a path between two nodes specified by their (x,y) position
	 */
	public void linkNodes(int xParent, int yParent, int xChild, int yChild){
		Node parent = findClosestNode(xParent, yParent, 1.5*radius);
		Node child = findClosestNode(xChild, yChild, 1.5*radius);
		linkNodes(parent, child);
	}
	
	/**
	 * Create the path between the specified initial and final node
	 */
	private void linkNodes(Node initial, Node child){
		if(initial != null && child != null && !isExistingPath(initial, child)){
			initial.addChild(child);
			paths.add(initial.getAvailablePaths().get(initial.getAvailablePaths().size()-1));
		}
		setChanged();
	    notifyObservers();
	}
	
	/**
	 * Check if the path between the specified initial and final node is already existing
	 */
	private boolean isExistingPath(Node parent, Node child) {
		for(Path path : paths){
			if(path.getInitialNode() == parent && path.getFinalNode() == child){
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the closest existing node to the specified position
	 */
	private Node findClosestNode(int x, int y, double radius) {
		for(Node node : otherNodes){
			if(Position.computeDistance(x,y,node.getXPosition(), node.getYPosition()) <= radius){
				return node;
			}
		}
		return null;
	}

	public void launch() {
		for(int i = 0; i < iterations; i++){
			System.out.println("Iteration : "+i);
			currentIteration = i;
			// All ants making their way to the end node (target point)
			for(Ant ant : colony){
				while(!ant.getCurrentNode().equals(end)){
					Node currentNode = ant.getCurrentNode();
					Path selectedPath = selectPathRandomly(ant, currentNode.getAvailablePaths());
					ant.setNodePosition(selectedPath.getFinalNode());
					ant.addAVisitedPath(selectedPath);
//					currentNode.leavePheromone(selectedPath,10.0);
				}
			}
			
			// Update pheromone
			HashMap<Path, Double> pheromonePerPath = new HashMap<>();
			// Find fastest ant : the one with the smallest traveled distance
			double smallestDistance = Double.MAX_VALUE;
			Ant fastestAnt = null;
			for(Ant ant : colony){
				double distance = ant.getTraveledDistance();
				if(distance < smallestDistance){
					fastestAnt = ant;
					smallestDistance = distance;
				}
				for(Path path : ant.getVisitedPaths()){
					if(pheromonePerPath.containsKey(path)){
						pheromonePerPath.put(path, pheromonePerPath.get(path)+1.0/distance);
					} else {
						pheromonePerPath.put(path, 1.0/distance);
					}
				}
			}
			// Double update of pheromone on the path of the fastest ant
			for(Path path : fastestAnt.getVisitedPaths()){
				pheromonePerPath.put(path, pheromonePerPath.get(path)+2.0/smallestDistance);
			}
			
			updatePheromone(start, pheromonePerPath, new HashSet<>());
			normalizeAllProba(start, new HashSet<Node>());
			
			// Visualize the traveled paths of each ant
			for(Ant ant : colony){
				printVisitedPaths(ant);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.err.println("Something went wrong !");
				}
			}
			
			allAntsComingBackToColony();
		}
		
		// Print likeliest path
		Node currentNode = start;
		shortestPath = new ArrayList<Path>();
		while(currentNode != end){
			Path likeliestPath = null;
			double proba = Double.MIN_VALUE;
			for(Path path : currentNode.getAvailablePaths()){
				if(path.getProbability() > proba){
					likeliestPath = path;
					proba = path.getProbability();
				}
			}
			System.out.println(proba);
			shortestPath.add(likeliestPath);
			currentNode = likeliestPath.getFinalNode();
		}
		
		setChanged();
	    notifyObservers();
	}

	/**
	 * Set the specified ant to current one to visualize its traveled path on the graph.
	 */
	private void printVisitedPaths(Ant ant) {
		currentAnt = ant;
		setChanged();
	    notifyObservers();
	}

	/**
	 * Recursive function normalizing all path probabilities.
	 * @param currentNode The current node from which the paths are normalized
	 * @param alreadyVisitedNodes Peths already updated
	 */
	private static void normalizeAllProba(Node currentNode, HashSet<Node> alreadyVisitedNodes) {
		if(!alreadyVisitedNodes.contains(currentNode)){
			currentNode.reshapeProbabilities();
			alreadyVisitedNodes.add(currentNode);
			for(Path path : currentNode.getAvailablePaths()){
				normalizeAllProba(path.getFinalNode(), alreadyVisitedNodes);
			}
		}
	}

	/**
	 * Reset data on traveled paths for each ant
	 */
	private void allAntsComingBackToColony() {
		for(Ant ant : colony){
			ant.backToColony(start);
		}
	}
	/**
	 * Recursive function updating all path probabilities
	 * @param currentNode The paths having this node as initial node are updated
	 * @param pheromonePerPath Map giving the updated probability value of each path
	 * @param alreadyVisited Paths already updated
	 */
	private void updatePheromone(Node currentNode, HashMap<Path, Double> pheromonePerPath, HashSet<Path> alreadyVisited) {
		for(Path path : currentNode.getAvailablePaths()){
			if(!alreadyVisited.contains(path)){
				double proba;
				if (pheromonePerPath.containsKey(path)) {
					proba = pheromonePerPath.get(path);
				} else { 
					proba = 0.0;
				}
				path.setProbability(path.getProbability()*(1-p)+proba);
				alreadyVisited.add(path);
				updatePheromone(path.getFinalNode(), pheromonePerPath, alreadyVisited);
			}
		}
	}
	
	/**
	 * Select randomly the next path taken by the specified ant, among the possible paths.
	 */
	private static Path selectPathRandomly(Ant ant, List<Path> availablePaths) {
		Random random = new Random();
		int cpt = 10; // To avoid infinite loop
		while(cpt>0){
			double randomNumber = random.nextDouble();
			double counter = 0.0;
			for(Path path : availablePaths){
				counter += path.getProbability();
				if(randomNumber < counter){
					if(!ant.getVisitedPaths().contains(path)){
						// Select this path if it hasn't been already chosen by the ant
						return path;
					} else {
						break; // Compute randomly another path
					}
				}
			}
		}
		return availablePaths.get(0);
	}

	/**
	 * Print elements on graphic
	 */
	public void print(Graphics gc) {
		// Print number of the current iteration
		gc.drawString("Itération courante = "+(currentIteration+1), 5, 15);
		
		// Print start node if any
		gc.setColor(Color.RED);
		printNode(gc, start);
		
		// Print end node if any
		gc.setColor(Color.BLUE);
		printNode(gc, end);
		
		// Print other nodes if any
		gc.setColor(Color.BLACK);
		for(Node node : otherNodes){
			if(node != start && node != end){
				printNode(gc, node);
			}
		}
		
		// Print links between nodes if any
		printLinks(gc);
	}

	private void printLinks(Graphics gc) {
		gc.setColor(Color.BLACK);
		for(Path path : paths){
			Node i = path.getInitialNode();
			Node f = path.getFinalNode();
			
			gc.drawLine(i.getXPosition(), i.getYPosition(), f.getXPosition(), f.getYPosition());
		}
		
		if(currentAnt != null) {
			gc.setColor(Color.CYAN);
			for(Path visitedPath : currentAnt.getVisitedPaths()){
				Node i = visitedPath.getInitialNode();
				Node f = visitedPath.getFinalNode();
				gc.drawLine(i.getXPosition(), i.getYPosition(), f.getXPosition(), f.getYPosition());
			}
		}
		
		if(!shortestPath.isEmpty()) {
			gc.setColor(Color.MAGENTA);
			for(Path path : shortestPath){
				Node i = path.getInitialNode();
				Node f = path.getFinalNode();
				gc.drawLine(i.getXPosition(), i.getYPosition(), f.getXPosition(), f.getYPosition());
			}
			gc.drawString("Probabilité du plus court chemin trouvé : "+shortestPath.get(0).getProbability(), 5, 30);
		}
	}
	
	private void printNode(Graphics gc, Node node) {
		if(node != null){
			gc.fillOval(adaptPosition(node.getXPosition()), adaptPosition(node.getYPosition()), radius, radius);
		}
	}
	
	private int adaptPosition(int position) {
		return Math.max(0, position-(Simulator.radius/2));
	}

	public void linkAllNodes() {
		for(Node nodeI : otherNodes){
			for(Node nodeF : otherNodes){
				if(nodeI != nodeF){
					linkNodes(nodeI, nodeF);
				}
			}
		}
	}
}