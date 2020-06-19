import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class FractalMatrix {
	
	/* [CurrentFractal, NextFractal, 1Alpha, 1Length,2 Alpha, 2Length, color]  */
	Integer[][] actualmatrix;
	
	public FractalMatrix(int length) {
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
	
	private int getXY(int x, int y) {
		if(isXYsafe(x,y))
			return actualmatrix[x][y];
		return 0;
	}
	
	public int getmatrixvalue(int x, int y) {
		if(isXYsafe(x-1,y-1))
			return getXY(x-1,y-1);
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
