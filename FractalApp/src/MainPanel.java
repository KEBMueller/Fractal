import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	Image img;
	
	public MainPanel() {
		img = null;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img.getScaledInstance(Main.resolution.width, Main.resolution.height, 0), 0, 0, null);
	}
}
