import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class DedicatedSlider extends JSlider{
	JFrame frame;
	private JTable  mintable, maxtable;
	
	private int x,y,min,max;
	
	
	public DedicatedSlider(int x, int y, int min, int max) {
		this.setX(x);
		this.setY(y);
		this.setMin(min);
		this.setMax(max);
		
		frame = new JFrame();
		mintable = new JTable();
		maxtable = new JTable();
		
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn(new String[] {"Minimum"});
		model.addRow(new Double[] {1.0});
		
		DefaultTableModel model2 = new DefaultTableModel();
		model2.addColumn(new String[] {"Minimum"});
		model2.addRow(new Double[] {1.0});
		
		GridBagConstraints c = new GridBagConstraints();
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.1;
		
		frame.add(new JScrollPane(mintable),c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 0.8;
		
		frame.add(this,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 1;
		c.weightx = 0.1;
		
		frame.add(new JScrollPane(maxtable), c);
	}
	
	public void addChangeListener(ChangeListener cl) {
		this.addChangeListener(cl);
	}
	
	public void addTableModelListener(TableModelListener tl) {
		mintable.getModel().addTableModelListener(tl);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
