
public class SeamDijkstraSP {
  private int width;
  private int height;
  private int[] edgeTo;
  private double[] distEnergy;
  private double[][] energy;
  private int[] seam;

  public SeamDijkstraSP(double[][] energy) {
    this.height = energy.length;
    this.width = energy[0].length;
    this.edgeTo = new int[height * width];
    this.distEnergy = new double[height * width];
    this.seam = new int[height];
    this.energy = energy;
    
    for (int i = 0; i < height * width; i++) 
      distEnergy[i] = Double.POSITIVE_INFINITY;

    for (int i = 0; i < width; i++) distEnergy[i] = 1000.0;  
    
    for (int i = 0; i < height - 1; i++) {
      for (int j = 0; j < width; j++) {
          for (int to : getNeighbors(i,j)) {
            int from = width * i + j;
            relax(from, to);
          }
      }
    }
    
    seam = computeSeam();
  }
  
  public int[] getSeam() {
    return seam;
  }
  
  private int[] computeSeam() {
    double minEnergy = Double.MAX_VALUE;
    int minSeamDest = -1;

    for (int i = (height - 1) * width; i < height * width; i++) {
      if (minEnergy > distEnergy[i]) {
        minEnergy = distEnergy[i];
        minSeamDest = i;
      }
    }

    int[] seam = new int[height];
    for (int i = height - 1, p = minSeamDest; i >= 0; i--, p = edgeTo[p]) {
      seam[i] = p % width;
    }
    return seam;
  }

  private int[] getNeighbors(int i, int j) {
    int[]n = null;
    if (j > 0 && j < width - 1) {
      n = new int[3];
      n[0] = indexFromIJ(i + 1, j - 1);
      n[1] = indexFromIJ(i + 1, j);
      n[2] = indexFromIJ(i + 1, j + 1);
    } else if (j == 0) {
      n = new int[2];
      n[0] = indexFromIJ(i + 1, j);
      n[1] = indexFromIJ(i + 1, j + 1);
    } else if (j == width - 1) {
      n = new int[2];
      n[0] = indexFromIJ(i + 1, j - 1);
      n[1] = indexFromIJ(i + 1, j);
    }  
    return n;
  }

  private void relax(int from, int to) {     
    int i = to / width;
    int j = to % width;
    if (distEnergy[to] > distEnergy[from] + energy[i][j]) {
        distEnergy[to] = distEnergy[from] + energy[i][j];
        edgeTo[to] = from;
    }
  }
  
  private int indexFromIJ (int i, int j) {
    return width * i + j;
  }

  public double getSeamEnergy() {
    double minEnergy = Double.MAX_VALUE;
    int minSeamDest = -1;
    
    for (int i = (height - 1) * width; i < height * width; i++) {
      if (minEnergy > distEnergy[i]) minSeamDest = i;
    }

    return distEnergy[minSeamDest];
  }

}