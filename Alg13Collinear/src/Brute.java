import java.util.Arrays;

public class Brute {
  
    public static void main(String[] args) {
        In in = new In(args[0]); // input file

        int N = in.readInt();
        Point[] pts = new Point[N];

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        
        int n = 0;
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();

            pts[n] = new Point(x, y);
            pts[n].draw();
            n++;
        }
        
        Arrays.sort(pts);
        Out out = new Out();
        
        for (int p = 0; p < N; p++) {
            for (int q = p+1; q < N; q++) {
                double slopePQ = pts[p].slopeTo(pts[q]);
                
                for (int r = q+1; r < N; r++) {
                    double slopePR = pts[p].slopeTo(pts[r]);
                    
                    if (slopePQ == slopePR) {
                        for (int s = r+1; s < N; s++) {
                            double slopePS = pts[p].slopeTo(pts[s]);
                            
                            if (slopePR == slopePS) {
                                out.println(pts[p] + " -> " + pts[q] + " -> " + pts[r] + " -> " + pts[s]);
                                pts[p].drawTo(pts[s]);
                            }
                            
                        }
                    }
                
                }
            }
        }
    }
}
