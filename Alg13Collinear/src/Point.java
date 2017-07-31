import java.util.Comparator;

public class Point implements Comparable<Point> {
  /**
   * compare points by slope to this point
   */
  public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
    @Override
    public int compare(Point q, Point r) {
        Point thisPoint = new Point(x, y);
        double s1 = thisPoint.slopeTo(q);
        double s2 = thisPoint.slopeTo(r);
        
        double d = s1-s2;
        if (d > 0)
            return 1;
        else if (d < 0)
            return -1;
        else
            return 0;
    }
  };
  
  private int x;
  private int y;
  
  /**
   * construct the point (x, y)
   * @param x
   * @param y
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;  
  }
  
  /**
   * draw this point
   */
  public void draw() {
    StdDraw.point(x, y);  
  }
  
  /**
   * draw the line segment from this point to that point
   * @param that
   */
  public void drawTo(Point that) {
    StdDraw.line(this.x, this.y, that.x, that.y);  
  }
  
  /**
   * string representation
   */
  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
  
  /**
   * is this point lexicographically smaller than that point?
   */
  public int compareTo(Point that) {
    if (this.y > that.y) {
      return 1;
    } else if (this.y < that.y) {
      return -1;
    } else {
      if (this.x > that.x) {
        return 1;
      } else if (this.x < that.x) {
        return -1;
      }
    }
    
    return 0;
  }
  
  /**
   * the slope between this point and that point
   * @param that
   * @return slope
   */
  public double slopeTo(Point that) {
    if (this.y == that.y && this.x != that.x) {
      return +0.0;
    } else if (this.x == that.x && this.y != that.y) {
      return Double.POSITIVE_INFINITY; 
    } else if (this.x == that.x && this.y == that.y) {
      return Double.NEGATIVE_INFINITY;
    }
    
    return (double) (that.y - this.y) / (double) (that.x - this.x);
  }
}