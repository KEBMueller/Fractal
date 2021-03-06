import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class Main{
	// various constants
	
	// the time 1 logic tick should take	
	public static double ticktimems = 10.0;
	
	//Graphics parameter
	public static double framerate = 30;
	public static Dimension resolution = new Dimension(1900,1000); 
	public static Dimension sidepanelResolution = new Dimension(200,200); 
	public static Dimension mainpanelResolution = new Dimension(500,500); 
	public static Dimension colorpanelResolution = new Dimension(150,40); 
	
	

	private static LinkedList<FractalMatrix> fractaltables;	//a table of all loaded fractals
	private static int currenttable = 0;					//0 = no select table, <= -1 invalid
	private static int currentrow = 0;					//-1 = no select table
	private static int currentdepth = 8;					//-1 = no depth??
	private static double currentmainpanelfractalscale = 1.0;					// 1 = from bottom middle to bottom middle and 1/4 height
	private static boolean fractalTableHasChanged = true;
	private static boolean fractalSelectedBoxHasChanged = false;
	
	private static InputPipe inputpipe;

	
	private static MainWindow mainwindow;
	
	//GUI Objects
	private static Dimension buttonsize = new Dimension(70,15);
	private static JButton safebutton;
	private static JButton loadbutton;
	private static MainPanel mainpanel;
	private static MainPanel sidepanel;
	private static MainPanel colorpanelred;
	private static MainPanel colorpanelgreen;
	private static MainPanel colorpanelblue;
	private static JSlider  depthslider;
	private static JComboBox<String> fractalcombobox;
	private static JTable fractaltable;
	private static String[] fractaltablecolumns =  {"Cur. Frac.","Next Frac","x1","y1","x2","y2","Color"};
	private static int fractaltablelength = 50;
	
	//side frames containing dedicated fractal editors
	//they get closed when a new Fractal is selected/loaded
	private static MainWindow[] editorframes;
	
	

	public static void main(String[] args) {
		
		//	Setup
		fractaltables = new LinkedList<FractalMatrix>();
		fractaltables.add(FractalLoader.loadFractal());
		//fractaltables.add(new FractalMatrix(fractaltablelength, "Default.txt"));
		inputpipe = new InputPipe();
		setupMainWindow();
		
		
		//Start
		double starttime = System.currentTimeMillis();
		double lasttimegraphics = starttime;;
		double currenttimegraphics = starttime;
		double lasttime = starttime;;
		double currenttime = starttime;
		double deltatime = 0;
		//Loop
		while(true) {
			//Logic
			currenttime = System.currentTimeMillis();
			deltatime = currenttime - lasttime;
			deltatime = deltatime/ticktimems;
			if(deltatime > 1) {
				lasttime = currenttime;
				updateLogic(deltatime);
			}
		
			//Graphics
			currenttimegraphics = System.currentTimeMillis();
			if(currenttimegraphics - lasttimegraphics > 1000/framerate) {
				lasttimegraphics = currenttimegraphics;
				updateGraphics();
			}
		}
	}

	private static void setupMainWindow() {
		GridBagConstraints constraints = new GridBagConstraints();
		mainwindow = new MainWindow();
		safebutton = new JButton();
		loadbutton = new JButton();
		mainpanel = new MainPanel();
		sidepanel = new MainPanel();
		colorpanelred = new MainPanel();
		colorpanelgreen = new MainPanel();
		colorpanelblue = new MainPanel();
		depthslider = new JSlider();
		fractaltable = new JTable(fractaltablelength,fractaltablecolumns.length);
		fractalcombobox = new JComboBox<String>();
		
		Color c;
		
		safebutton.setPreferredSize(buttonsize);
		loadbutton.setPreferredSize(buttonsize);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.ipadx = buttonsize.width;
		constraints.ipady = buttonsize.height;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		safebutton.addActionListener(inputpipe);
		safebutton.setVisible(true);
		safebutton.setText("Safe");
		mainwindow.add(safebutton, constraints);


		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.ipadx = buttonsize.width;
		constraints.ipady = buttonsize.height;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		loadbutton.addActionListener(inputpipe);
		loadbutton.setVisible(true);
		loadbutton.setText("Load");
		mainwindow.add(loadbutton, constraints);

		constraints.gridwidth = 2;
		constraints.gridheight = 6;
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.weighty = 1.0;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		mainpanel.setPreferredSize(mainpanelResolution);
		mainpanel.addMouseWheelListener(inputpipe);
		mainpanel.setVisible(true);
		mainwindow.add(mainpanel, constraints);
		

		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.weighty = 0.0;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		fractalcombobox.addActionListener(inputpipe);
		fractalcombobox.setVisible(true);
		mainwindow.add(fractalcombobox, constraints);
		
		
		constraints.gridwidth  = 2;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weightx = 0.1;
		constraints.weighty = 0.7;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTH;
		DefaultTableModel tablemodel = new DefaultTableModel();
		for(String name: fractaltablecolumns) {
			tablemodel.addColumn(name);
		}
		for(int i=0; i<fractaltablelength;i++)
			tablemodel.addRow(new String[] {"0","0","0","0","0","0","0"});
		tablemodel.addTableModelListener(inputpipe);
		fractaltable.setEnabled(true);
		fractaltable.addMouseListener(inputpipe);
		fractaltable.getSelectionModel().addListSelectionListener(inputpipe);
		fractaltable.setModel(tablemodel);
		JScrollPane scrollpane = new JScrollPane(fractaltable);
		scrollpane.setVisible(true);
		mainwindow.add(scrollpane, constraints);
		
		constraints.gridwidth  = 2;
		constraints.gridheight = 4;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.weightx = 0.5;
		constraints.weighty = 0.7;
		constraints.ipadx = sidepanelResolution.width;
		constraints.ipady = sidepanelResolution.height;
		constraints.fill = GridBagConstraints.VERTICAL;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		sidepanel.addMouseListener(inputpipe);
		sidepanel.img = new BufferedImage(sidepanelResolution.width,sidepanelResolution.height,BufferedImage.TYPE_3BYTE_BGR);
		sidepanel.setPreferredSize(sidepanelResolution);
		mainwindow.add(sidepanel, constraints);
		
		constraints.gridwidth  = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.ipadx = colorpanelResolution.width;
		constraints.ipady = colorpanelResolution.height;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		colorpanelred.addMouseListener(inputpipe);
		colorpanelred.img = new BufferedImage(colorpanelResolution.width,colorpanelResolution.height,BufferedImage.TYPE_3BYTE_BGR);
		colorpanelred.setPreferredSize(colorpanelResolution);
		c = new Color(255,0,0,255);
		drawcolorpanel(colorpanelred.img, c);
		mainwindow.add(colorpanelred, constraints);
		
		constraints.gridwidth  = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.ipadx = colorpanelResolution.width;
		constraints.ipady = colorpanelResolution.height;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		colorpanelgreen.addMouseListener(inputpipe);
		colorpanelgreen.img = new BufferedImage(colorpanelResolution.width,colorpanelResolution.height,BufferedImage.TYPE_3BYTE_BGR);
		colorpanelgreen.setPreferredSize(colorpanelResolution);
		 c = new Color(0,255,0,255);
		drawcolorpanel(colorpanelgreen.img, c);
		mainwindow.add(colorpanelgreen, constraints);
		
		constraints.gridwidth  = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.ipadx = colorpanelResolution.width;
		constraints.ipady = colorpanelResolution.height;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		colorpanelblue.addMouseListener(inputpipe);
		colorpanelblue.img = new BufferedImage(colorpanelResolution.width,colorpanelResolution.height,BufferedImage.TYPE_3BYTE_BGR);
		colorpanelblue.setPreferredSize(colorpanelResolution);
		 c = new Color(0,0,255,255);
		drawcolorpanel(colorpanelblue.img, c);
		mainwindow.add(colorpanelblue, constraints);
		
		constraints.gridwidth  = 1;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.ipadx = colorpanelResolution.width;
		constraints.ipady = colorpanelResolution.height;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.SOUTHWEST;
		depthslider.addChangeListener(inputpipe);
		depthslider.setPreferredSize(colorpanelResolution);
		depthslider.setMaximum(13);
		depthslider.setMinimum(0);
		depthslider.setValue(8);
		depthslider.setVisible(true);
		mainwindow.add(depthslider, constraints);
		
		mainwindow.revalidate();
		mainwindow.pack();
		mainwindow.setVisible(true);
		mainwindow.repaint();
	}

	private static void drawcolorpanel(Image img, Color c) {
		Graphics gr = img.getGraphics();
		for(int x=0; x<img.getWidth(null);x++) {
			double s = ((x+0.0)/img.getWidth(null));
			int r = (int)(c.getRed() * s) ;
			int g = (int)(c.getGreen() * s) ;
			int b = (int)(c.getBlue() * s) ;
			Color c2 = new Color(r,g,b);
			gr.setColor( c2);
			for(int y=0; y<img.getHeight(null);y++) {
				gr.drawLine(x, y, x, y);
				
			}
		}
	}

	private static void updateGraphics() {
		if(fractalTableHasChanged || fractalSelectedBoxHasChanged) {
		updateComboBox();
		updateTable();
		

		Graphics g = mainpanel.img.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, mainpanel.getWidth(), mainpanel.getHeight());

		drawCurrentTableToMainWindow(currentdepth ,0 ,mainpanelResolution.width/2, mainpanelResolution.height, mainpanelResolution.width/2,(int)( mainpanelResolution.height - mainpanelResolution.height/4.0 * currentmainpanelfractalscale), g, false);
		
		fractalSelectedBoxHasChanged = false;
		fractalTableHasChanged = false;
		}
		
		Graphics g2 = sidepanel.img.getGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, sidepanel.getWidth(), sidepanel.getHeight());
		
		drawCurrentTableToMainWindow(1 ,currentrow ,sidepanelResolution.width/2, sidepanelResolution.height, sidepanelResolution.width/2, sidepanelResolution.height/2, g2, true);
		
		mainwindow.repaint();
		}


	private static void drawCurrentTableToMainWindow( int depth, int firstrow, int ax, int ay, int bx, int by, Graphics g, boolean isSidepanel) {
		Vector<Double> startpos = new Vector<Double>();
		startpos.add((double)ax);
		startpos.add((double)ay);
		Vector<Double> endpos = new Vector<Double>();
		endpos.add((double)bx);
		endpos.add((double)by);
		
		g.setColor(Color.black);
		g.drawLine(ax, ay, bx, by);
		
		//finds the start of the fractalpart, so it displays the whole subsection
		if(isSidepanel) {
			int fractalpartstart = firstrow;
			while(fractalpartstart > 0 && fractaltables.get(currenttable).getMatrix()[0][fractalpartstart] == fractaltables.get(currenttable).getMatrix()[0][fractalpartstart-1]) {
				fractalpartstart--;
			}
			drawCurrentTableToMainWindow(1,fractalpartstart,startpos,endpos,g, false);
		}

		drawCurrentTableToMainWindow(depth,firstrow,startpos,endpos,g, isSidepanel);
	}
	
	/*
	 * I take the AB vector and rotate it by 90�, then i use those two vectors to navigate the plane
	 * 
	 */
	private static void drawCurrentTableToMainWindow(int depth, int tablerow, Vector<Double> startpos,Vector<Double> endpos, Graphics g, boolean isSidepanel) {
		if(depth <=0 || fractaltables.isEmpty()) return;
		Integer[][] rowvalues = fractaltables.get(currenttable).actualmatrix;
		Integer[][] actualmatrix = fractaltables.get(currenttable).actualmatrix;
		if(rowvalues[0][tablerow] == 0) return;
		
		int currentFrac = rowvalues[0][tablerow];
		int nextFrac = rowvalues[1][tablerow];
		int x1 = rowvalues[2][tablerow];
		int y1 = rowvalues[3][tablerow];//Point c
		int x2 = rowvalues[4][tablerow];
		int y2 = rowvalues[5][tablerow];//Point d
		int color = rowvalues[6][tablerow];
		
		Vector<Double> rotationaxis = new Vector<Double>();
		rotationaxis.add(0.0);
		rotationaxis.add(0.0);
		rotationaxis.add(1.0);
		
		Vector<Double> yvector = new Vector<Double>();
		yvector.add(new Double(endpos.get(0)-startpos.get(0)));
		yvector.add(new Double(endpos.get(1)-startpos.get(1)));
		Vector<Double> xvector = rotateVectorCC(yvector, 90);
		
		int newx1 = (int)(startpos.get(0) + xvector.get(0) * x1/100.0 + yvector.get(0) * y1/100.0);
		int newy1 = (int)(startpos.get(1) + yvector.get(1) * y1/100.0 + xvector.get(1) * x1/100.0);
		int newx2 = (int)(startpos.get(0) + xvector.get(0) * x2/100.0 + yvector.get(0) * y2/100.0);
		int newy2 = (int)(startpos.get(1) + yvector.get(1) * y2/100.0 + xvector.get(1) * x2/100.0);

		Vector<Double> newstart = new Vector<Double>();
		newstart.add(newx1+0.0);
		newstart.add(newy1+0.0);
		Vector<Double> newend = new Vector<Double>();
		newend.add(newx2+0.0);
		newend.add(newy2+0.0);

		if(isSidepanel) {
			g.setColor(Color.RED);
			g.drawLine(newx1, newy1, newx2, newy2);
			return;
		}
		
		g.setColor(new Color(color));
		g.drawLine(newx1, newy1, newx2, newy2);
		
		if(nextFrac != 0) {
			for(int i = 0; i < actualmatrix[0].length; i++ ) {
				if(actualmatrix[0][i] == nextFrac) {
					drawCurrentTableToMainWindow(depth-1, i, newstart, newend,g,isSidepanel);
					break;
				}
			}
		}
		
		if(actualmatrix[0][tablerow+1] == currentFrac) {
			drawCurrentTableToMainWindow(depth,tablerow+1,startpos,endpos,g,isSidepanel);
		}
	}
	
	public static Vector<Double> rotateVectorCC(Vector<Double> vec, double theta){
	    Vector<Double> result = new Vector<Double>();
	    theta = Math.toRadians(theta);
	    result.add(Math.cos(theta) * vec.get(0) - Math.sin(theta) * vec.get(1));
	    result.add(Math.sin(theta) * vec.get(0) - Math.cos(theta) * vec.get(1));
	    return result;
	}

	
	
	private static void updateComboBox() {
		if(fractalcombobox.getItemCount()==0) 
			for(FractalMatrix fm: fractaltables) {
				if(fm!=null)
					fractalcombobox.addItem(fm.name);
			}
		for(int i = 0; i<fractalcombobox.getItemCount() && i<fractaltables.size(); i++) {
			if(!fractalcombobox.getItemAt(i).equals(fractaltables.get(i).name)) {
				fractalcombobox.removeAllItems();
				for(FractalMatrix fm: fractaltables) {
					fractalcombobox.addItem(fm.name);
				}
			}
		}
	}

	private static void updateTable() {
		if(currenttable <0 || fractaltables.isEmpty())
			return;
		FractalMatrix currentmatrix = fractaltables.get(currenttable);
		if(currentmatrix == null) return;
		for(int i = 0; i < currentmatrix.actualmatrix[0].length && i < fractaltablelength; i++) {
			for(int j = 0; j <7 ; j++) {
				int currentvalue = currentmatrix.getXY(j, i);
				fractaltable.getModel().setValueAt(currentvalue, i, j);
			}
		}
	}

	private static void updateLogic(double deltatime) {
		handleActionEvent();
		handleMouseEvent();
		handleKeyEvent();
		handleTableEvent();
		handleSelectionEvent();
		handleChangeEvent();
		handleMouseWheelEvent();
	}

	private static void handleActionEvent() {
		ActionEvent ae = inputpipe.pollNextActionEvent();
		if(ae != null) {
			if(ae.getSource().equals(safebutton)) {
				//safe the current table
				FractalLoader.safeFractal(fractaltables.get(currenttable));
			}
			
			if(ae.getSource().equals(loadbutton)) {
				//load a new table
				loadNewTable();
			}
			
			if(ae.getSource().equals(fractalcombobox) && currenttable != fractalcombobox.getSelectedIndex()) {
				currenttable = fractalcombobox.getSelectedIndex();
				fractalSelectedBoxHasChanged = true;
			}
		}
	}

	
	private static void loadNewTable() {
		FractalMatrix newmatrix = FractalLoader.loadFractal();
		if(newmatrix== null) return;
		for(FractalMatrix fm :fractaltables) {
			if(newmatrix.name.equals(fm.name)) {
				newmatrix.name = newmatrix.name + "*";
			}
		}
		fractaltables.add(currenttable, newmatrix);
		fractalTableHasChanged = true;
		
	}

	

	private static void handleMouseEvent() {
		MouseEvent me = inputpipe.pollNextMouseEvent();
		
		if(me == null) return;
		if( me.getSource().equals(sidepanel) && !fractaltables.isEmpty()) {
			int x = me.getX();
			int y = me.getY();
			int referenclength = sidepanel.getHeight()/4 *3;
			if(me.getButton() == MouseEvent.BUTTON1) {
				fractaltables.get(currenttable).getMatrix()[4][currentrow] = (int)((x-sidepanel.getWidth()/2)/(sidepanel.getWidth()/2.0) *100);
				fractaltables.get(currenttable).getMatrix()[5][currentrow] = (int)((sidepanel.getHeight() - y)/(sidepanel.getHeight()/2.0) * 100);
				fractalTableHasChanged = true;
			}
			if(me.getButton() == MouseEvent.BUTTON3) {
				fractaltables.get(currenttable).getMatrix()[2][currentrow] = (int)((x-sidepanel.getWidth()/2.0)/(sidepanel.getWidth()/2.0) *100);
				fractaltables.get(currenttable).getMatrix()[3][currentrow] = (int)((sidepanel.getHeight() - y)/(sidepanel.getHeight()/2.0) * 100);
				fractalTableHasChanged = true;
			}
		}
		if(me.getSource().equals(colorpanelred) && me.getButton() == MouseEvent.BUTTON1) {
			int rgb = fractaltables.get(currenttable).getMatrix()[6][currentrow];
			Color newColor = new Color(rgb);
			newColor = new Color((int)(255 * (0.0+me.getX())/colorpanelred.getWidth()),
								 newColor.getGreen(),
								 newColor.getBlue());
			fractaltables.get(currenttable).getMatrix()[6][currentrow] = newColor.getRGB();
			fractalTableHasChanged = true;
			
		}if(me.getSource().equals(colorpanelgreen) && me.getButton() == MouseEvent.BUTTON1) {
			int rgb = fractaltables.get(currenttable).getMatrix()[6][currentrow];
			Color newColor = new Color(rgb);
			newColor = new Color(newColor.getRed(),
								(int)(255 * (0.0+me.getX())/colorpanelred.getWidth()),
								 newColor.getBlue());
			fractaltables.get(currenttable).getMatrix()[6][currentrow] = newColor.getRGB();
			fractalTableHasChanged = true;
			
		}if(me.getSource().equals(colorpanelblue) && me.getButton() == MouseEvent.BUTTON1) {
			int rgb = fractaltables.get(currenttable).getMatrix()[6][currentrow];
			Color newColor = new Color(rgb);
			newColor = new Color(newColor.getRed(),
								 newColor.getGreen(),
								 (int)(255 * (0.0+me.getX()) / colorpanelred.getWidth()) );
			fractaltables.get(currenttable).getMatrix()[6][currentrow] = newColor.getRGB();
			fractalTableHasChanged = true;
			
		}
		
	}
	
	private static void handleKeyEvent() {
		
	}
	
	private static void handleTableEvent() {
		TableModelEvent ce = inputpipe.pollNextTableEvent();
		if(ce == null) return;
		if(ce.getSource().equals(fractaltable.getModel())) {
			if(ce.getType() == TableModelEvent.UPDATE) {
				reloadFractalTable(ce);
			}
		}
		
	}
	
	
	/*
	 * If a new row is selected at the main table then currentrow = selectedrow
	 */
	private static void handleSelectionEvent() {
		ListSelectionEvent se = inputpipe.pollNextSelectionEvent();
		if(se == null) return; 
			if(se.getSource().equals(fractaltable.getSelectionModel())) {
			int selectedrow = fractaltable.getSelectedRow();
			if(selectedrow != -1 && selectedrow != currentrow) {
				currentrow = selectedrow;
			}
		}
	}
	

	/*
	 * If a new row is selected at the main table then currentrow = selectedrow
	 */
	private static void handleChangeEvent() {
		ChangeEvent ce = inputpipe.pollNextChangeEvent();
		if(ce == null) return;
		if(ce.getSource().equals(depthslider)) {
			if(currentdepth != depthslider.getValue()) {
				currentdepth =  depthslider.getValue();
				fractalTableHasChanged = true;				
			}	
		}
		
	}
	
	/*
	 * 
	 */
	private static void handleMouseWheelEvent() {
		MouseWheelEvent me = inputpipe.pollNextMouseWheelEvent();
		
		if(me == null) return;
		if(me.getSource().equals(mainpanel)) {
			int rotation = me.getWheelRotation();
			if(rotation  > 0) {
				currentmainpanelfractalscale = currentmainpanelfractalscale * 1.1;
				fractalTableHasChanged = true;		 
			}
			if(rotation  < 0) {
				currentmainpanelfractalscale = currentmainpanelfractalscale * 0.9;
				fractalTableHasChanged = true;		 
			}
		}
	}
	
	/*
	 * Loads the JTable contend to the FractalMAtrix object
	 */
	private static void reloadFractalTable(TableModelEvent ce) {
		FractalMatrix matrix = fractaltables.get(currenttable);
		Integer[][] actualmatrix = matrix.actualmatrix;
		for(int i = 0; i < actualmatrix.length; i++) {
			for(int j = 0; j<actualmatrix[i].length && j <fractaltable.getModel().getRowCount(); j++) {
				try {
					actualmatrix[i][j] = Integer.parseInt(fractaltable.getModel().getValueAt(j, i)+"");
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("Invalid table contend at x:" + i + "  y:"+j+ "  Contend:"+fractaltable.getModel().getValueAt(j, i)+"|");
				}
			}
		}
	}

}
