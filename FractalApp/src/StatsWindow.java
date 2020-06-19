import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * Das Hauptfenster welches die Tabelle welche das Fractal repräsentiert dastellt und veränderbar macht 
 */
public class StatsWindow implements TableModelListener, ActionListener , ChangeListener{
	
	private FractalMatrix matrix;
	private FractalWindow fractalwindow;
	
	private JFrame frame;
	private JTable table;
	 private int[] lsi = {1,1};
	private JButton addslider;
	private JSlider mainslider;
	private TableModel model;
	public static String[] tablenames = {"Frac", "n.Frac","a°","L %","2.a°","2.L%","Color"};
	public static double[][] minmaxtable = {{   0,  9},
			    							{   0,  9},
			    							{-90,90},
			    							{-200,200},
			    							{-90,90},
			    							{-200,200},
			    							{   0,  9}};
	
	//Position der ersten beiden anhaltspunkte
	private int x1,y1,x2,y2;
	
	
	/*
	 * Erstellt das Statswindow
	 */
	public StatsWindow(FractalMatrix fm, FractalWindow fw) {
		matrix = fm;
		fractalwindow = fw; 
		
		frame = new JFrame();
		addslider = new JButton();
		mainslider = new JSlider();
		
		DefaultTableModel model = new DefaultTableModel();
		
		// Benennt die Spalten der Tabele
		model.addColumn("Frac");
					model.addColumn("n.Frac");
								model.addColumn("1.a %");
											model.addColumn("1.L %");
														model.addColumn("2.a %");
																	model.addColumn("2.L %");
																				model.addColumn("Color");

																				
		//Setzt den Angfangswert auf 0
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		model.addRow(new Integer[] {0,0,0,0,0,0,0});
		
		model.addTableModelListener(this);
		
		frame.setLayout(new GridBagLayout());
		
		table = new JTable(model);
		table.setVisible(true);
		
		
		
		GridBagConstraints c = new GridBagConstraints();
		
		//fügt die tabele in einem scrollpane dem fenster hinzu
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0.7;
		
		frame.add(new JScrollPane(table), c);
		
		//fügt den hauptslider hinzu
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 0.8;
		mainslider.addChangeListener(this);
		frame.add(mainslider, c);
		
		//Fügt den knopf für gesicherte knöpfe dem frame hinzu
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 2;
		c.weightx = 0.2;
		c.weighty = 0.3;
		
		frame.add(addslider, c);
		
		
		
		frame.revalidate();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		x1 = (int)(fw.getWidth() * 0.5);
		x2 = (int)(fw.getWidth() * 0.5);
		
		y1 = (int)(fw.getHeight());
		y2 = (int)(fw.getHeight() * 0.75);
	}
	
	public void updateStats() {
		table.repaint();
	}
	

	/*
	 * Updated die Tabelle wenn der slider bewegt wurde, oder ändert den slider um dem neu ausgewählten tabelleneintrag zu entsprechen.
	 * ODER updated einen tabelleneintrag wenn es von einem dedicatedslider geändert wurde.
	 */
	@Override
	public void stateChanged(ChangeEvent sc) {
		if(sc.getSource().equals((mainslider))) {
			int x = table.getSelectedColumn();
			int y = table.getSelectedRow();
			if(x == lsi[0] && lsi[1] == y) { 
				table.getModel().setValueAt( mainslider.getValue(),y,x);
			}
			else {
				lsi[0] = x;
				lsi[1] = y;
				mainslider.setMinimum((int) minmaxtable[x][0]);
				mainslider.setMaximum((int) minmaxtable[x][1]);
				mainslider.setValue(matrix.getmatrixvalue(x, y));
			}
		}
		else if(sc.getSource().getClass() == DedicatedSlider.class) {
			DedicatedSlider slider = (DedicatedSlider) sc.getSource();
			int x = slider.getX();
			int y = slider.getY();
			table.getModel().setValueAt(slider.getValue(), y, x);
		}	

	}
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {

	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
		int x = e.getFirstRow();
		int y = e.getColumn();
		int n = -1;
		Object s = table.getValueAt(x, y);
		try {
		n = Integer.parseInt("" + s);
		matrix.editmatrix(x, y, n);
		redrawFractal();
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.out.println(x + "  " + y + " parserror |" +  table.getValueAt(x, y) +"|  Class :" + s.getClass());
		}
	}
	
	public void redrawFractal() {
		Graphics2D g = fractalwindow.getGraphics();
		
		g.clearRect(0, 0, 1000, 1000);
		g.drawLine(fractalwindow.getWidth()/2, fractalwindow.getHeight(),fractalwindow.getWidth()/2, (int)(fractalwindow.getHeight() * 0.75));
		drawrecursive(x1,y1,x2,y2,1,3,g);
		table.revalidate();
		table.repaint();
		
		fractalwindow.update();
	}
	
	public void drawrecursive (double ax, double ay, double bx, double by, int currentrow, int currentdepth, Graphics2D g) {
		int fractalid = matrix.getmatrixvalue(currentrow, 1);
		
			if(fractalid <= 0)
			{		
				return;
			}
		int nextfractal = matrix.getmatrixvalue(currentrow, 2);
		double firstalpha = matrix.getmatrixvalue(currentrow, 3);
		double firstlength = matrix.getmatrixvalue(currentrow, 4)/100.0;
		double secondalpha = matrix.getmatrixvalue(currentrow, 5);
		double secondlength = matrix.getmatrixvalue(currentrow, 6)/100.0;
		int color = matrix.getmatrixvalue(currentrow, 7);
		

		//UR Vector
		double abx = bx-ax;
		double aby = by-ay;
		
		//Ur-Vector um alpha 1 gedreht und length 1 scalier
		double newAx = ax + firstlength * (abx * Math.cos(Math.toRadians(firstalpha)) - aby * Math.sin(Math.toRadians(firstalpha)));
		double newAy = ay + firstlength * (abx * Math.sin(Math.toRadians(firstalpha)) + aby * Math.cos(Math.toRadians(firstalpha)));
		
		double newxlength = secondlength * (abx * Math.cos(Math.toRadians(secondalpha)) - aby * Math.sin(Math.toRadians(secondalpha)));
		double newylength = secondlength * (abx * Math.sin(Math.toRadians(secondalpha)) + aby * Math.cos(Math.toRadians(secondalpha)));
		
		
		double newBx = newAx + newxlength;
		double newBy = newAy + newylength;
		
		
		
		
		int nextcolumn = findnextfractalcolumn(nextfractal, currentdepth, currentrow);
		
		if(nextcolumn > 0 && currentdepth > 0) {
			//drawrecursive(newAx,newAy,newBx,newBy,nextcolumn, currentdepth-1, g);
			drawrecursive(newAx,newAy,newAx+newxlength,newAy+newylength,nextcolumn, currentdepth-1,g);
		}
		

		if(color > 0) {

			switch (color) {
			case 1:
				g.setColor(Color.black);
				break;
			case 2:
				g.setColor(Color.red);
				break;
			case 3:
				g.setColor(Color.blue);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.yellow);
				break;
			case 6:
				g.setColor(Color.orange);
				break;
			case 7:
				g.setColor(Color.pink);
				break;
			case 8:
				g.setColor(Color.magenta);
				break;
			default:
				System.out.println("Color number error");
			}

			g.drawLine((int)newAx, (int)newAy, (int)newBx, (int)newBy);
		}
		
		if(matrix.getmatrixvalue(currentrow+1, 1) == fractalid) {
			//drawrecursive(ax,ay,bx,by,currentrow+1,currentdepth,g);
			drawrecursive(ax,ay,ax+newxlength,ay+newylength,currentrow+1,currentdepth,g);
		}
	}
	
	public int findnextfractalcolumn(int fractalid, int currentdepth, int currentrow) {
		if(currentdepth <= 0)
			return -1;
		for(int i = currentrow-1; i < matrix.getMatrix()[0].length; i++) {
			if(matrix.getmatrixvalue(1, i) == fractalid) {
				return i;
			}
		}
		
		return -1;
	}

}
