package mapData;

/**
 * Point on the map
 */
public class Position {
	
	private int x;
	
	private int y;
	
	public Position(int xInitial, int yInitial){
		x = xInitial;
		y = yInitial;
	}
	
	public Position(){
		x = 0;
		y = 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public static double computeDistance(Position p1, Position p2){
		return computeDistance(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	public static double computeDistance(int x1, int y1, int x2, int y2){
		return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}
}
