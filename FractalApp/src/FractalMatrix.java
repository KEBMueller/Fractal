import java.io.File;

public class FractalMatrix {
	
	public static int MAX_LENGTH = 50;
	/* [CurrentFractal, NextFractal, 1Alpha, 1Length,2 Alpha, 2Length, color]  */
	Integer[][] actualmatrix;
	String name;
	File file;
	
	public FractalMatrix(int length, String name) {
		this.name = name;
		this.file = null;
		actualmatrix = new Integer[7][length];
		 for(int i = 0; i < 7; i++) {
			 for(int j = 0; j < length; j++) {
				 actualmatrix[i][j] = new Integer(0);
			 }
		 }
	}

	public void editmatrix(int x, int y, int n) {
		if(isXYsafe(x,y))
			actualmatrix[x][y] = n;
	}

	public Integer[][] getMatrix(){
		return actualmatrix;
	}

	public int getXY(int x, int y) {
		if(isXYsafe(x,y))
			return actualmatrix[x][y];
		return 0;
	}


	public boolean isXYsafe(int x, int y) {
		return isXsafe(x) && isYsafe(y);
	}

	public boolean isXsafe(int x) {
		if(x < 0 || x >= actualmatrix.length)
			return false;
		return true;
	}

	public boolean isYsafe(int y) {
		if(y < 0 || y >= actualmatrix[0].length)
			return false;
		return true;
	}



}