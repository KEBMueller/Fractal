import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

public class Main{
	// various constants
	
	// the time 1 logic tick should take	
	public static double ticktimems = 10.0;
	
	//Graphics parameter
	public static double framerate = 60;
	public static Dimension resolution = new Dimension(600,400); 
	public static Dimension sidePanelResolution = new Dimension(600,400); 
	
	

	private static LinkedList<FractalMatrix> fractaltables;	//a table of all loaded fractals
	private static int currenttable = 0;					//0 = no select table, <= -1 invalid
	private static boolean fractalTableHasChanged = true;
	
	private static InputPipe inputpipe;

	
	private static MainWindow mainwindow;
	
	//GUI Objects
	private static Dimension buttonsize = new Dimension(70,15);
	private static JButton safebutton;
	private static JButton loadbutton;
	private static JPanel mainpanel;
	private static JComboBox<String> fractalcombobox;
	private static JTable fractaltable;
	private static String[] fractaltablecolumns =  {"Cur. Frac.","Next Frac","x1","y1","x2","y2","Color"};
	private static int fractaltablelength = 10;
	public static double[][] minmaxtable = {{   0,  9},
											{   0,  9},
											{-200,200},
											{-200,200},
											{-200,200},
											{-200,200},
											{   0,  9}};
	
	//side frames containing dedicated fractal editors
	//they get closed when a new Fractal is selected/loaded
	private static MainWindow[] editorframes;
	
	

	public static void main(String[] args) {
		//	Setup
		fractaltables = new LinkedList<FractalMatrix>();
		fractaltables.add(new FractalMatrix(fractaltablelength, "Default"));
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
		fractaltable = new JTable(fractaltablelength,fractaltablecolumns.length);
		fractalcombobox = new JComboBox<String>();
		
		safebutton.setPreferredSize(buttonsize);
		loadbutton.setPreferredSize(buttonsize);

		constraints.gridx = 0;
		constraints.gridy = 2;
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
		constraints.gridy = 2;
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
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.weightx = 0.5;
		constraints.weighty = 1.0;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		mainpanel.addMouseListener(inputpipe);
		mainpanel.setVisible(true);
		mainwindow.add(mainpanel, constraints);
		

		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 0.1;
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
		constraints.weightx = 0.0;
		constraints.weighty = 100;
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
		fractaltable.setModel(tablemodel);
		JScrollPane scrollpane = new JScrollPane(fractaltable);
		scrollpane.setVisible(true);
		mainwindow.add(scrollpane, constraints);
		
		mainwindow.setVisible(true);
	}

	private static void updateGraphics() {
		if(fractalTableHasChanged) {
		updateComboBox();
		updateTable();

		fractalTableHasChanged = false;
		}
	}

	private static void updateComboBox() {
		if(fractalTableHasChanged) {
			fractalcombobox.removeAllItems();
			for(int i = 0; i<fractaltables.size(); i++) {
				fractalcombobox.addItem(fractaltables.get(i).name);
			}
		}
	}

	private static void updateTable() {
			if(fractalTableHasChanged) {
			if(currenttable <0 || fractaltables.isEmpty())
				return;
			FractalMatrix currentmatrix = fractaltables.get(currenttable);
			for(int i = 0; i < currentmatrix.actualmatrix[0].length && i < fractaltablelength; i++) {
				for(int j = 0; j <7 ; j++) {
					int currentvalue = currentmatrix.getXY(j, i);
					fractaltable.getModel().setValueAt(currentvalue, i, j);
				}
			}
		}
	}

	private static void updateLogic(double deltatime) {
		handleActionEvent();
		handleMouseEvent();
		handleKeyEvent();
		handleTableEvent();
		
	}


	private static void handleActionEvent() {
		ActionEvent ae = inputpipe.pollNextActionEvent();
		if(ae != null) {
			if(ae.getSource().equals(safebutton)) {
				safeCurrentTable();
			}
			
			if(ae.getSource().equals(loadbutton)) {
				loadNewTable();
			}
		}
	}

	
	private static void loadNewTable() {
		FractalMatrix newmatrix = FractalLoader.loadFractal();
		if(newmatrix== null) return;
		fractaltables.addLast(newmatrix);
		fractalTableHasChanged = true;
		
	}

	private static void safeCurrentTable() {
		// TODO Auto-generated method stub
		
	}

	private static void handleMouseEvent() {
		// TODO Auto-generated method stub
		
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
	 * Loads the JTable contend to the FractalMAtrix object
	 */
	private static void reloadFractalTable(TableModelEvent ce) {
		FractalMatrix matrix = fractaltables.get(currenttable);
		Integer[][] actualmatrix = matrix.actualmatrix;
		for(int i = 0; i < actualmatrix.length; i++) {
			for(int j = 0; j<actualmatrix[i].length ; j++) {
				try {
					actualmatrix[i][j] = Integer.parseInt((String)(fractaltable.getModel().getValueAt(j, i)+""));
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("Invalid table contend at x:" + i + "  y:"+j+ "  Contend:"+fractaltable.getModel().getValueAt(j, i)+"|");
				}
			}
		}
		fractalTableHasChanged = true;
	}

}
