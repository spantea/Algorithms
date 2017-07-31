import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fast {
       
    public static void main(String[] args) {
        In in = new In(args[0]); // input file

        int N = in.readInt();
        Point[] pts = new Point[N];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenColor(123, 10, 10);
        
        int n = 0;
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();
            pts[n] = new Point(x, y);
            pts[n].draw();
            n++;
        }
                 
        for (int p = 0; p < N; p++) {          
        	Point[] ps = pts.clone();
        	Arrays.sort(ps, pts[p].SLOPE_ORDER);
        	List<Point> collinearPts = new ArrayList<Point>();
        	boolean first = true;
        	
            for (int q = 1; q < N-1; q++) { 
                //checking if 2 are collinear 
            	//by comparing their slopes relative to a third point p
            	Point p1 = ps[q];
            	Point p2 = ps[q+1];
            	
                double firstSlope = pts[p].slopeTo(p1);
                double nextSlope = pts[p].slopeTo(p2);
           
                //test if slopes are =, => points are collinear
                if (firstSlope == nextSlope) {
                	if (first) {
                		collinearPts.add(p1);
                		first = false;
                	}
                	collinearPts.add(p2);   

                	//if we have the last two points and line longer/= than 3 pts
                	if (q == N - 2 && collinearPts.size() >= 3) {
                		Collections.sort(collinearPts);  
                		//if p is the smaller point of the line print it (avoids overlapping lines)
                    	if(pts[p].compareTo(collinearPts.get(0)) <= 0) {
                    		System.out.print(pts[p]);
                    		for(Point pt : collinearPts) {
                    			System.out.print(" -> " + pt);
                    		}
                    		pts[p].drawTo(collinearPts.get(collinearPts.size()-1));
                    		System.out.println();                  		
                    	}
                    	first = true;
                    	collinearPts.clear();
                	}
                } else if (collinearPts.size() >= 3) {
                	Collections.sort(collinearPts);
                	if(pts[p].compareTo(collinearPts.get(0)) <= 0) {
                		System.out.print(pts[p]);
                		for(Point pt : collinearPts) {
                			System.out.print(" -> " + pt);
                		}
                		pts[p].drawTo(collinearPts.get(collinearPts.size()-1));
                		System.out.println();                		
                	}
                	first = true;
                	collinearPts.clear();
                } else {
                	first = true;
                	collinearPts.clear();
                }
            }
        }
    }
}
