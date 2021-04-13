import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
	GridBagLayout layout;
	GridBagConstraints constrains;
	public MainWindow() {
		super();
		
		layout = new GridBagLayout();
		constrains = new GridBagConstraints();
		
		this.setLayout(layout);
		this.setPreferredSize(Main.resolution);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
