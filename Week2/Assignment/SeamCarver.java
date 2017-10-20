import java.awt.Color;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
	private int[][] energy;
	private int[][] distTo;
	private int[][] edgeTo;
	private Picture pic;

	// create a seam carver object based on the given picture
	public SeamCarver (Picture picture) {
		pic = picture;
		process(pic);
	}

	private void process(Picture picture) {
		int width = picture.width();
		int height = picture.height();
		energy = new int[width][height]; 
		edgeTo = new int[width][height];
		distTo = new int[width][height];

		for(int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if(row == 0 || col == 0 || row == height - 1 || col == width -1) 
					energy[col][row] = 195075;
			}
		} 
		for(int col = 1; col < width - 1; col++) {
			for (int row = 1; row < height - 1; row++) {
					energy[col][row] = cacuEnergy(col, row);
			}
		}
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (row == 0) {
					edgeTo[col][row] = col;
					distTo[col][row] = 0;
				}
				else{
					edgeTo[col][row] = -1;
					distTo[col][row] = Integer.MAX_VALUE;
				}
			}
		}
	}

	private int cacuEnergy(int col, int row) {
		Color colorxl = pic.get(col-1, row);
		Color colorxr = pic.get(col+1, row);
		Color coloryu = pic.get(col, row-1);
		Color coloryd = pic.get(col, row+1);
		int xr = (colorxr.getRed() - colorxl.getRed());
		int xg = (colorxr.getGreen() - colorxl.getGreen());
		int xb = (colorxr.getBlue() - colorxl.getBlue());
		int yr = (coloryu.getRed() - coloryd.getRed());
		int yg = (coloryu.getGreen() - coloryd.getGreen());
		int yb =  (coloryu.getBlue() - coloryd.getBlue());
		int energy = xr*xr + xg*xg + xb*xb + yr*yr + yg*yg + yb*yb;
		return energy;
	}

	// current picture
	public Picture picture() {
		return pic;
	}

	//width of current picture
	public int width() {
		return pic.width();
	}

	//height of current picture
	public int height() {
		return pic.height();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		return energy[x][y];
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		transpose();
		int seam[] = findSeam();
		transpose();
		return seam;

	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		int seam[] = findSeam();
		return seam;
	}

	private int[] findSeam() {
		int width = energy.length;
		int height = energy[0].length;
		int seam[] = new int[height];
		int mindis = Integer.MAX_VALUE;
		int minedge = -1;


		for (int row = 0; row < height-1; row++) {
			for (int col = 0; col < width; col++) {
				relax(col,row);
			}
		}

		for (int col = 0; col < width; col++) {
			if (distTo[col][height-1] < mindis) {
				mindis = distTo[col][height-1];
				minedge = col;
			}
		}
		for (int row = height-1; row >= 0; row--) {
			//System.out.print("Print Row: " + row);
			seam[row] = minedge;
			// seam[row] = edgeTo[minedge][row];
			minedge = edgeTo[minedge][row];
		}
		return seam;

	}

	private void transpose() {
		int width = energy.length;
		int height = energy[0].length;
		int[][] energyT = new int[height][width];
		int[][] distToT = new int[height][width];
		int[][] edgeToT = new int[height][width];
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				energyT[row][col] = energy[col][row];
				//distToT[row][col] = distTo[col][row];
				//edgeToT[row][col]= edgeTo[col][row];
			}
		}
		for (int col = 0; col < height; col++) {
			for (int row = 0; row < width; row++) {
				if (row == 0) {
					edgeToT[col][row] = col;
					distToT[col][row] = 0;
				}
				else{
					edgeToT[col][row] = -1;
					distToT[col][row] = Integer.MAX_VALUE;
				}
			}
		}

		energy = energyT;
		distTo = distToT;
		edgeTo = edgeToT;
	}

	private void relax(int col, int row) {
		int width = energy.length;
		int height = energy[0].length;
		//col=0; two options
		if (col == 0) {
			if(distTo[col][row+1] > distTo[col][row] + energy[col][row+1]) {
				edgeTo[col][row+1] = col;
				distTo[col][row+1] = distTo[col][row] + energy[col][row+1];
			}
			if(distTo[col+1][row+1] > distTo[col][row] + energy[col+1][row+1]) {
				edgeTo[col+1][row+1] = col;
				distTo[col+1][row+1] = distTo[col][row] + energy[col+1][row+1];
			}
		}
		//col=row-1, two options
		else if (col == width-1) {
			if(distTo[col][row+1] > distTo[col][row] + energy[col][row+1]) {
				edgeTo[col][row+1] = col;
				distTo[col][row+1] = distTo[col][row] + energy[col][row+1];
			}
			if(distTo[col-1][row+1] > distTo[col][row] + energy[col-1][row+1]) {
				edgeTo[col-1][row+1] = col;
				distTo[col-1][row+1] = distTo[col][row] + energy[col-1][row+1];
			}
		}
		//middle of the col, three options
		else {
			if(distTo[col][row+1] > distTo[col][row] + energy[col][row+1]) {
				edgeTo[col][row+1] = col;
				distTo[col][row+1] = distTo[col][row] + energy[col][row+1];
			}
			if(distTo[col-1][row+1] > distTo[col][row] + energy[col-1][row+1]) {
				edgeTo[col-1][row+1] = col;
				distTo[col-1][row+1] = distTo[col][row] + energy[col-1][row+1];
			}
			if(distTo[col+1][row+1] > distTo[col][row] + energy[col+1][row+1]) {
				edgeTo[col+1][row+1] = col;
				distTo[col+1][row+1] = distTo[col][row] + energy[col+1][row+1];
			}
		}

	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		int width = energy.length;
		int height = energy[0].length-1;
		if (seam.length != width) throw new java.lang.IllegalArgumentException();
		if (seam == null) throw new java.lang.NullPointerException();
		Picture newpic = new Picture(width, height);
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				if (row < seam[col]) newpic.set(col,row,pic.get(col,row));
				else newpic.set(col,row,pic.get(col,row+1));
			}
		}
		pic = newpic;
		process(pic);
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		int width = energy.length-1;
		int height = energy[0].length;
		if (seam.length != height) throw new java.lang.IllegalArgumentException();
		if (seam == null) throw new java.lang.NullPointerException();
		Picture newpic = new Picture(width, height);
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (col < seam[row]) newpic.set(col,row,pic.get(col,row));
				else newpic.set(col,row,pic.get(col+1,row));
			}
		}
		pic = newpic;
		process(pic);
	}

	   public static void main(String[] args) {

	   }
}