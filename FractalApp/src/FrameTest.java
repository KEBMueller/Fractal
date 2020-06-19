
public class FrameTest {
	
	public void run() {
		loop();
	}
	
	public void loop() {
		FractalMatrix fm = new FractalMatrix(20);
		FractalWindow fw = new FractalWindow();
		StatsWindow window = new StatsWindow(fm, fw);
	}

}
