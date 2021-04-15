import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MainPanel extends JPanel{
	BufferedImage img;
	public MainPanel() {
		super();
		img = new BufferedImage(Main.mainpanelResolution.width, Main.mainpanelResolution.height, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(img != null) {
			g.drawImage(img.getScaledInstance(this.getWidth(), this.getHeight(), 0), 0, 0, null);
		}
		else 
			super.paintComponent(g);
	}
}
