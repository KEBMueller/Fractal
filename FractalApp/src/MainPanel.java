import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	public Image img;
	
	public MainPanel() {
		super();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(img != null)
			g.drawImage(img.getScaledInstance(Main.resolution.width, Main.resolution.height, 0), 0, 0, null);
		else 
			super.paintComponent(g);
	}
}
