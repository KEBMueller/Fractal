import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class Main{
	// various constants
	
	// the time 1 logic tick should take	
	public static double ticktimems = 10.0;
	
	//Graphics parameter
	public static double framerate = 60;
	public static Dimension resolution = new Dimension(600,400); 
	
	private static FractalTable[] fractaltables;
	//0 = no select table, -1 invalid
	private static int currenttable = 0;
	private static MainWindow mainwindow;
	private static InputPipe inputpipe;

	public static void main(String[] args) {
		//	Setup
		
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
			deltatime = lasttime - currenttime;
			deltatime = deltatime/ticktimems;
			if(deltatime > 1) {
				lasttime = currenttime;
				updateLogic(deltatime);
			}
		
			//Graphics
			currenttimegraphics = System.currentTimeMillis();
			if(currenttimegraphics - lasttimegraphics < 1000/framerate) {
				lasttimegraphics = currenttimegraphics;
				updateGraphics(deltatime);
			}
		}
	}

	private static void setupMainWindow() {
		mainwindow = new MainWindow();
		mainwindow.setVisible(true);
	}

	private static void updateGraphics(double deltatime) {
		// TODO Auto-generated method stub
		
	}

	private static void updateLogic(double deltatime) {
		handleActionEvent();
		handleMouseEvent();
		handleKeyEvent();
		
	}


	private static void handleActionEvent() {
		// TODO Auto-generated method stub
		
	}

	
	private static void handleMouseEvent() {
		// TODO Auto-generated method stub
		
	}
	
	private static void handleKeyEvent() {
		ActionEvent ae = inputpipe.pollNextActionEvent();
		
		if(ae == null) 
			return;
		
		if(ae.getSource().equals(Safe))
	}

}
