import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class MyPanel extends JPanel{
	Image img;
	
	public MyPanel() {
		img = null;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img.getScaledInstance(Main.resolution.width, Main.resolution.height, 0), 0, 0, null);
	}
}
