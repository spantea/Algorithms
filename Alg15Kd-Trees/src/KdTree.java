import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean X_COMPARE = true;
    private static final boolean Y_COMPARE = false;
    
    private static class Node {
       private Point2D p;      // the point
       private RectHV rect;    // the axis-aligned rectangle corresponding to this node
       private Node lb;        // the left/bottom subtree
       private Node rt;        // the right/top subtree
    }
    
    private int N;
    private Node root;
    
    /**
     * construct an empty set of points
     */
    public KdTree() {
        
    }
    
    /**
     * is the set empty? 
     * @return true if the set is empty
     */
    public boolean isEmpty() {
        return N == 0;
    }
    
    /**
     * number of points in the set 
     * @return number of points in the set 
     */
    public int size() {
        return N;
    }
    
    /**
     * add the point to the set (if it is not already in the set)
     * @param p
     */
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("called insert() with null argument");
        
        if (isEmpty()) {
        	root = new Node();
        	root.p = p;
        	root.rect = new RectHV(0, 0, 1, 1);
        	N++;
        }
        root = insert(root, p, X_COMPARE);
    }
    
    private Node insert(Node x, Point2D p, boolean orientation) {
        if (x == null) {
            x = new Node();
            x.p = p;
            N++;
            return x;
        }
        
        if (p.equals(x.p))  
          return x;
        else {
          Comparator<Point2D> cmp = null;
          if (orientation == X_COMPARE)
            cmp = Point2D.X_ORDER;
          else 
            cmp = Point2D.Y_ORDER;
          
          RectHV xRect = null;
          if (cmp.compare(p, x.p) < 0) {
            //smaller rectangle case
            if (orientation == X_COMPARE) {
              //if split is on x axis take left rectangle
              xRect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
            } else {
              //if split is on y axis take bottom rectangle
              xRect = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
            }
            x.lb = insert(x.lb, p, !orientation);
            x.lb.rect = xRect;
          } else {
            //greater rectangle case
            if (orientation == X_COMPARE) {
              //if split is on x axis take right rectangle
              xRect = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
            } else {
              //if split is on y axis take top rectangle
              xRect = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.rect.ymax());
            }
            x.rt = insert(x.rt, p, !orientation);
            x.rt.rect = xRect;
          }               
        }   
        return x;
    }
    
    /**
     * does the set contain point p? 
     * @param p
     * @return does the set contain point p? 
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("called contains() with null argument");      
        return get(root, p, X_COMPARE) != null;
    }
    
    private Node get(Node x, Point2D p, boolean orientation) {
        if (x == null) return null;     
        if (p.equals(x.p)) return x; 
        
        int cmp = 0;
        if (orientation == X_COMPARE) 
            cmp = Point2D.X_ORDER.compare(p, x.p);
        else if (orientation == Y_COMPARE)
            cmp = Point2D.Y_ORDER.compare(p, x.p);
                
        if (cmp < 0) {
            return get(x.lb, p, !orientation);
        } else
            return get(x.rt, p, !orientation);
    }
    
    /**
     * draw all points to standard draw
     */
    public void draw() {
      draw(root, X_COMPARE);
    }
    
    private void draw(Node x, boolean orientation) {
      if (x == null) return;
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      StdDraw.point(x.p.x(), x.p.y());
      
      //draw the lines
      if (orientation == X_COMPARE) {
          StdDraw.setPenColor(StdDraw.RED);
          StdDraw.setPenRadius(0.001);
          StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
      } else {
          StdDraw.setPenColor(StdDraw.BLUE);
          StdDraw.setPenRadius(0.001);
          StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
      }
      
      draw(x.lb, !orientation);
      draw(x.rt, !orientation);
    }
    
    /**
     * all points that are inside the rectangle 
     * @param rect
     * @return all points that are inside the rectangle 
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("called range() with null argument");
        if (root == null) return new ArrayList<Point2D>();
        List<Point2D> ptsInRect = new ArrayList<Point2D>();
        range(rect, root, ptsInRect);
        return ptsInRect;
    }
    
    private void range(RectHV rect, Node x, List<Point2D> ptsInRect) {
        if (x.rect.intersects(rect)) {
            if (rect.contains(x.p)) ptsInRect.add(x.p);
            if (x.lb != null && x.lb.rect.intersects(rect))
                range(rect, x.lb, ptsInRect);
            if (x.rt != null && x.rt.rect.intersects(rect))
                range(rect, x.rt, ptsInRect);
        }
    }
    
    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * @param p
     * @return a nearest neighbor in the set to point p; null if the set is empty
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("called nearest() with null argument");
        if (isEmpty()) return null;

        return nearest(root, p, root.p, Double.MAX_VALUE);
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D nrstPt, double nearestDist) {
      if (x == null) return nrstPt;
      
        double dist2Rect = x.rect.distanceSquaredTo(p);
        if (dist2Rect < nearestDist) {
          double nearestDist1 = p.distanceTo(nrstPt);
          double dist2Pt = p.distanceSquaredTo(x.p);
          if (dist2Pt < nearestDist1) {
            nrstPt = x.p;
            nearestDist1 = dist2Pt;
          }
          
          if (x.lb != null && x.lb.rect.contains(p)) {
            nrstPt = nearest(x.lb, p, nrstPt, nearestDist); 
            nrstPt = nearest(x.rt, p, nrstPt, nearestDist); 
          } else {
            nrstPt = nearest(x.rt, p, nrstPt, nearestDist);
            nrstPt = nearest(x.lb, p, nrstPt, nearestDist);
          }
        }
        return nrstPt;
    }
    
  public static void main(String... args) {
      KdTree tree = new KdTree();
//      tree.insert(new Point2D(0.7, 0.2));
//      tree.insert(new Point2D(0.5, 0.4));
//      tree.insert(new Point2D(0.2, 0.3));
//      tree.insert(new Point2D(0.4, 0.7));
//      tree.insert(new Point2D(0.9, 0.6));
      
//      double x,y = 0;
//      x = 0.4; y = 0.1;
//      tree.insert(new Point2D(x,y));
//      x = 0.3; y = 0.5;
//      tree.insert(new Point2D(x,y));
//      x = 0.9; y = 0.3;
//      tree.insert(new Point2D(x,y));
//      x = 0.1; y = 0.7;     
//      tree.insert(new Point2D(x,y));          
//      System.out.println(tree.size());
//      tree.draw();
      
      for (int i=0; i<5; i++) {
        Point2D p = new Point2D(Math.random(), Math.random());
        tree.insert(p);
        System.out.println(p);
      }
      Point2D test = new Point2D(Math.random(), Math.random());
      tree.insert(test);
      System.out.println(test);
      tree.draw();
      System.out.println("-------------------------------------------");
      System.out.println("Nearest point to: " + test);
      System.out.println("is: " + tree.nearest(test));
      
//      Point2D p1 = new Point2D(0.47060100503423663, 0.17152565918555973);
//      Point2D p2 = new Point2D(0.37863702497161444, 0.6674492744146553);
//      Point2D p3 = new Point2D(0.8246046047138422, 0.3790899729736047);
//      Point2D p4 = new Point2D(0.025094515401127282, 0.42294942950688885);
//      Point2D p5 = new Point2D(0.6948266479383476, 0.2255194575113172);
//      Point2D p6 = new Point2D(0.4163319821889603, 0.739235794470986);
//      Point2D test = new Point2D(0.4163319821889603, 0.739235794470986);
//      tree.insert(p1);
//      tree.insert(p2);
//      tree.insert(p3);
//      tree.insert(p4);
//      tree.insert(p5);
//      tree.insert(p6);
//      tree.draw();
//      System.out.println(tree.nearest(test));
  }
}