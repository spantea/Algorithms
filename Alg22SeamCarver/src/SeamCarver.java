import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
  private int[][] workPict;
  private int width;
  private int height;
    
  /**
   * Create a seam carver object based on the given picture
   * @param picture
   */
  public SeamCarver(Picture picture)   {
    if (picture == null) throw new NullPointerException();
    
    this.width = picture.width();
    this.height = picture.height();
    
    //build workPict
    this.workPict = buildWorkPict(picture);
  }
  
  private int[][] buildWorkPict(Picture picture) {
    int[][] p = new int[height][width];
	for (int i = 0; i < height; i++) {
	  for (int j = 0; j < width; j++) {
		p[i][j] = picture.get(j, i).getRGB();	  
	  }
	}
    return p;
  }
  
  /**
   * Returns current picture
   * @return current picture
   */
  public Picture picture() {
    Picture currPicture = new Picture(width, height);
    for (int col = 0; col < width; col++) {
    	for (int row = 0; row < height; row++) {
    		currPicture.set(col, row, new Color(workPict[row][col]));
    	}
    }
    return currPicture;
  }
  
  /**
   * Returns width of current picture
   * @return width of current picture
   */
  public int width() {
    return width;
  }
  
  /**
   * Returns height of current picture
   * @return height of current picture
   */
  public int height() {
    return height;
  }
  
  /**
   * Returns energy of pixel at column x and row y.
   * @param x column
   * @param y row
   * @return energy of pixel at column x and row y.
   */
  public double energy(int x, int y) {
    if (!areValidIndices(x, y)) throw new IndexOutOfBoundsException();
    if (isTrivialCase(x, y)) return 1000;
    
    return Math.sqrt(xEnergy(x, y) + yEnergy(x, y));
  }
  
  private int xEnergy(int x, int y) {
    Color leftCol = new Color(workPict[y][x - 1]);
    Color rightCol = new Color(workPict[y][x + 1]);
    int rx = rightCol.getRed() - leftCol.getRed();
    int gx = rightCol.getGreen() - leftCol.getGreen();
    int bx = rightCol.getBlue() - leftCol.getBlue();
    int dx2 = rx*rx + gx*gx + bx*bx;
    
    return dx2;
  }
  
  private int yEnergy(int x, int y) {
    Color upCol = new Color(workPict[y - 1][x]);
    Color downCol = new Color(workPict[y + 1][x]);
    int ry = downCol.getRed() - upCol.getRed();
    int gy = downCol.getGreen() - upCol.getGreen();
    int by = downCol.getBlue() - upCol.getBlue(); 
    int dy2 = ry*ry + gy*gy + by*by;
    
    return dy2;
  }
  
  // if the pixel is on the border return true
  private boolean isTrivialCase(int x, int y) {
    if (x == 0 || y == 0 || x == width - 1 || y == height - 1) return true;
    return false;
  }
  
  /**
   * Returns sequence of indices for horizontal seam
   * @return sequence of indices for horizontal seam
   */
  public int[] findHorizontalSeam() {
	if (height == 1) return new int[width];
	
	double[][] energy = computeEnergyOfImage();
    SeamDijkstraSP findSeam = new SeamDijkstraSP(transpose(energy));
    return findSeam.getSeam();
  }
  
  private double[][] transpose (double[][] matrix) {
	  double[][] transpose = new double[width][height];
	  for (int i = 0; i < height; i++) {
		  for (int j = 0; j < width; j++) {
			  transpose[j][i] = matrix[i][j];
		  }
	  }
	  return transpose;
  }
  
  private int[][] transpose(int[][] a) {
	  int[][] t = new int[a[0].length][a.length];
	  for (int i = 0; i < a.length; i++) {
		  for (int j = 0; j < a[0].length; j++) {
			  t[j][i] = a[i][j];
		  }
	  }
	  return t;
  }
  
  /**
   * Sequence of indices for vertical seam
   * @return sequence of indices for vertical seam
   */
  public int[] findVerticalSeam() {
	if (width == 1) return new int[height];
	
    double[][] energy = computeEnergyOfImage();
    SeamDijkstraSP findSeam = new SeamDijkstraSP(energy);
    return findSeam.getSeam();
  }
  
  /**
   * Remove horizontal seam from current picture
   * @param seam
   */
  public void removeHorizontalSeam(int[] seam) {
	isValidHorizontalSeam(seam);
    if (height <= 1) throw new IllegalArgumentException();
    int[][] p = transpose(workPict);
    
    for (int i = 0; i < width; i++) {
    	int numElts = height - (seam[i] + 1);
    	System.arraycopy(p[i], seam[i]+1, p[i] , seam[i], numElts);
    }
    
    workPict = transpose(p);
    height--;
  }
  
  /**
   * Remove vertical seam from current picture
   * @param seam
   */
  public void removeVerticalSeam(int[] seam) {
	isValidVerticalSeam(seam);
    if (width <= 1) throw new IllegalArgumentException();
    
    for (int i = 0; i < height; i++) {
    	int numElts = width - (seam[i] + 1);
    	System.arraycopy(workPict[i], seam[i]+1, workPict[i] , seam[i], numElts);
    }
    width--;
  }
  
  private double[][] computeEnergyOfImage() {
	double[][] energy =  new double[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        energy[row][col] = energy(col, row);       
      }
    }
	
	return energy;
  }
  
  private boolean areValidIndices(int x, int y) {
    if (x >= 0  && x < width && y >= 0 && y < height) return true;
    return false;
  }
  
  private void isValidHorizontalSeam(int[] seam) {
	if (seam == null) throw new NullPointerException();
    if (seam.length != width) throw new IllegalArgumentException("Invalid seam length: " + seam.length);
    for (int i = 0; i < width; i++) {
    	if (seam[i] < 0 || seam[i] >= height) 
    		throw new IllegalArgumentException("Invalid seam, value:" + seam[i] + " , out of bounds");
    	if (i < width - 1 && Math.abs(seam[i] - seam[i+1]) >= 2 )
    		throw new IllegalArgumentException("Invalid seam, value, distance between " + seam[i] + "," + seam[i+1] + " is greater than 1");
    }
  }
  
  private void isValidVerticalSeam(int[] seam) {
	if (seam == null) throw new NullPointerException();
	if (seam.length != height) throw new IllegalArgumentException("Invalid seam length: " + seam.length);
	for (int i = 0; i < height; i++) {
    	if (seam[i] < 0 || seam[i] >= width) 
    		throw new IllegalArgumentException("Invalid seam, value:" + seam[i] + " , out of bounds");
    	if (i < height - 1 && Math.abs(seam[i] - seam[i+1]) >= 2)
    		throw new IllegalArgumentException("Invalid seam, value, distance between " + seam[i] + "," + seam[i+1] + " is greater than 1");
    }
  }
  
//  private void printEnergy() {
//	double[][] energy = computeEnergyOfImage();
//    for (int i = 0; i < height; i++) {
//        for (int j=0; j < width; j++) {
//          StdOut.printf("%.2f ", energy[i][j]);
//        }
//        System.out.println();
//      } 
//  }
//  
//  private void printPicture() {
//    for (int i = 0; i < height; i++) {
//      for (int j = 0; j < width; j++) {
//    	  System.out.print("(" + workPict[i][j].getRed() + " " + workPict[i][j].getGreen() + " " + workPict[i][j].getBlue() + ") "); 
//      }
//      System.out.println();
//	}
//  }
//
//  public static void main(String... args) {
////	int[][] a = new int[2][3];
////	int counter = 1;
////	for (int i = 0; i < 2; i++) {
////		for (int j = 0; j< 3; j++) {
////			a[i][j] = counter;
////			counter++;
////			System.out.print(a[i][j] + " ");
////		}
////		System.out.println();
////	}
////	
////	int[][] tb = new int[3][2];
////	for (int i = 0; i < 3; i++) {
////		for (int j = 0; j < 2; j++) {
////			tb[i][j] = a[j][3-1-i];
////			System.out.print(tb[i][j] + " ");
////		}
////		System.out.println();
////	}
//	  
//    SeamCarver sc = new SeamCarver(new Picture("test/3x7.png"));
//    sc.printPicture();
//
//    System.out.println();
//    for (int e : sc.findHorizontalSeam()) {
//        System.out.println(e);
//    }
//    sc.removeHorizontalSeam(sc.findHorizontalSeam());
//    sc.printPicture();
//  }
}
