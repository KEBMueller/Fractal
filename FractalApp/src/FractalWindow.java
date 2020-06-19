import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FractalWindow {
	private class MyPanel extends JPanel{
		Image image;
		
		public MyPanel(Image i) {
			image = i;
		}
		
		@Override
		public void paint(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}
	
	private JFrame frame;
	private MyPanel panel;
	private BufferedImage bi;
	
	public FractalWindow() {
		bi = new BufferedImage(1000,1000,BufferedImage.TYPE_3BYTE_BGR);
		
		panel = new MyPanel(bi);
		panel.setPreferredSize(new Dimension (1000,1000));
		panel.setVisible(true);
		
		frame = new JFrame();
		frame.add(panel);
		frame.pack();
		frame.revalidate();
		frame.repaint();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void update() {
		frame.revalidate();
		frame.repaint();
	}
	
	public Graphics2D getGraphics() {
		return (Graphics2D) bi.getGraphics();
	}
	
	public int getWidth() {
		return frame.getWidth();
	}
	
	public int getHeight() {
		return frame.getHeight();
	}
}
