import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
	private SET<Point2D> bst;
	
	/**
	 * construct an empty set of points
	 */
	public PointSET() {
		bst = new SET<Point2D>();
	}
	
	/**
	 * is the set empty? 
	 * @return true if the set is empty
	 */
	public boolean isEmpty() {
		return bst.isEmpty();
	}
	
	/**
	 * number of points in the set 
	 * @return number of points in the set 
	 */
	public int size() {
		return bst.size();
	}
	
	/**
	 * add the point to the set (if it is not already in the set)
	 * @param p
	 */
	public void insert(Point2D p) {
		if (p == null) throw new NullPointerException("called insert() with null argument");
		if (!bst.contains(p)) bst.add(p);		
	}
	
	/**
	 * does the set contain point p? 
	 * @param p
	 * @return does the set contain point p? 
	 */
	public boolean contains(Point2D p) {
		if (p == null) throw new NullPointerException("called contains() with null argument");
		return bst.contains(p);
	}
	
	/**
	 * draw all points to standard draw
	 */
	public void draw() {
		Iterator<Point2D> bstIterator = bst.iterator();
		while (bstIterator.hasNext()) {
			Point2D p = bstIterator.next();
			p.draw();
		}
	}
	
	/**
	 * all points that are inside the rectangle 
	 * @param rect
	 * @return all points that are inside the rectangle 
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new NullPointerException("called range() with null argument");
		
		List<Point2D> pointsInRect = new ArrayList<>();
		Iterator<Point2D> bstIterator = bst.iterator();
		while (bstIterator.hasNext()) {
			Point2D p = bstIterator.next();
			if (rect.contains(p)) {
				pointsInRect.add(p);
			}
		}
		return pointsInRect;
	}
	
	/**
	 * a nearest neighbor in the set to point p; null if the set is empty
	 * @param p
	 * @return a nearest neighbor in the set to point p; null if the set is empty
	 */
	public Point2D nearest(Point2D p) {
		if (p == null) throw new NullPointerException("called nearest() with null argument");
		
		double distance = -1.0; 
		Point2D nearestP = null;
		
		Iterator<Point2D> bstIterator = bst.iterator();
		while (bstIterator.hasNext()) {
			Point2D p2 = bstIterator.next();
			double d = p.distanceTo(p2);
			if (distance == -1.0 || d < distance) {
				nearestP = p2;
				distance = d;
			}
		}
		return nearestP;
	}
}
