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

import map.Node;
import map.Path;
import map.Position;

public class Simulator extends Observable {
	public static int radius = 12; // TODO check value
	
	private static int iterations;
	
	private static double p = 0.4; // TODO Check value
	
	private static List<Ant> colony = new ArrayList<>();
	private static int numberOfAnts;
	
	private Node start;
	
	private Node end;
	
	private Set<Node> otherNodes = new HashSet<>();
	
	private Set<Path> paths = new HashSet<>();
	
	ArrayList<Path> shortestPath = new ArrayList<Path>();
	
	public Simulator (){
		super();
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

	public void modifyStartNode(int xPosition, int yPosition){
		if(start != null){
			otherNodes.remove(start);
			removeAllAssociatedPaths(start);
		}
		start = new Node(xPosition, yPosition);
		otherNodes.add(start);
		
		afterChangesMethod(start);
	}
	
	private void removeAllAssociatedPaths(Node node) {
		node.unlinkNode();
		for(Path path : paths){
			if(path.getInitialNode() == node || path.getFinalNode() == node){
				paths.remove(path);
			}
		}
	}

	public void modifyEndNode(int xPosition, int yPosition){
		if(end != null){
			otherNodes.remove(end);
		}
		end = new Node(xPosition, yPosition);
		otherNodes.add(end);
		
		afterChangesMethod(end);
	}
	
	public void addNode(int xPosition, int yPosition){
		Node newNode = new Node(xPosition, yPosition);
		otherNodes.add(newNode);
		
		afterChangesMethod(newNode);
	}
	
	private void afterChangesMethod(Node node) {
		setChanged();
	    notifyObservers(node);
	}
	
	public void reset() {
	    start = null;
	    end = null;
	    otherNodes.clear();
	    paths.clear();
	    shortestPath.clear();
	    
	    setChanged();
	    notifyObservers();

	}
	
	public void linkNodes(int xParent, int yParent, int xChild, int yChild){
		Node parent = findClosestNode(xParent, yParent, 1.5*radius);
		Node child = findClosestNode(xChild, yChild, 1.5*radius);
		linkNodes(parent, child);
	}
	
	private void linkNodes(Node parent, Node child){
		if(parent != null && child != null && !isExistingPath(parent, child)){
			parent.addChild(child);
			paths.add(parent.getAvailablePaths().get(parent.getAvailablePaths().size()-1));
		}
		setChanged();
	    notifyObservers();
	}
	
	private boolean isExistingPath(Node parent, Node child) {
		for(Path path : paths){
			if(path.getInitialNode() == parent && path.getFinalNode() == child){
				return true;
			}
		}
		return false;
	}

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
			// All ants making their way to the end node (target point)
			HashSet<Ant> antsAtTarget = new HashSet<>();
			while(antsAtTarget.size()!=colony.size()){
				for(Ant ant : colony){
					if(!ant.getCurrentNode().equals(end)){
						Node currentNode = ant.getCurrentNode();
						Path selectedPath = selectPathRandomly(currentNode.getAvailablePaths());
						ant.setNodePosition(selectedPath.getFinalNode());
						ant.addAVisitedPath(selectedPath);
					} else {
						antsAtTarget.add(ant);
					}
				}
			}
			
			// Update pheromone
			HashMap<Path, Double> pheromonePerPath = new HashMap<>();
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
				pheromonePerPath.put(path, pheromonePerPath.get(path)+1.0/smallestDistance);
			}
			
			updatePheromone(start, pheromonePerPath, new HashSet<>());
			normalizeAllProba(start, new HashSet<Node>());
			
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

	private static void normalizeAllProba(Node currentNode, HashSet<Node> alreadyVisitedNodes) {
		if(!alreadyVisitedNodes.contains(currentNode)){
			currentNode.reshapeProbabilities();
			alreadyVisitedNodes.add(currentNode);
			for(Path path : currentNode.getAvailablePaths()){
				normalizeAllProba(path.getFinalNode(), alreadyVisitedNodes);
			}
		}
	}

	private void allAntsComingBackToColony() {
		for(Ant ant : colony){
			ant.backToColony(start);
		}
	}

	private static void updatePheromone(Node currentNode, HashMap<Path, Double> pheromonePerPath, HashSet<Path> alreadyVisited) {
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

	private static Path selectPathRandomly(List<Path> availablePaths) {
		Random random = new Random();
		double randomNumber = random.nextDouble();
		double counter = 0.0;
		for(Path path : availablePaths){
			counter += path.getProbability();
			if(randomNumber < counter){
				return path;
			}
		}
		return null;
	}

	public void print(Graphics gc) {
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
		for(Path path : paths){
			Node i = path.getInitialNode();
			Node f = path.getFinalNode();
//			gc.drawLine(adaptPosition(i.getXPosition()), adaptPosition(i.getYPosition()), adaptPosition(f.getXPosition()), adaptPosition(f.getYPosition()));
			gc.drawLine(i.getXPosition(), i.getYPosition(), f.getXPosition(), f.getYPosition());
		}
		
		if(!shortestPath.isEmpty()){
			gc.setColor(Color.MAGENTA);
			for(Path path : shortestPath){
				Node i = path.getInitialNode();
				Node f = path.getFinalNode();
				gc.drawLine(i.getXPosition(), i.getYPosition(), f.getXPosition(), f.getYPosition());
			}
		}
	}

	private void printNode(Graphics gc, Node node) {
		if(node != null){
			gc.fillOval(adaptPosition(node.getXPosition()), adaptPosition(node.getYPosition()), radius, radius);
		}
	}
	
	private int adaptPosition(int position){
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