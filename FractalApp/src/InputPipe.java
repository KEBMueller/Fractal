import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class InputPipe implements ActionListener , MouseListener , KeyListener{
	
	// the different action buffers
	private LinkedList<ActionEvent> actionbuffer;
	private LinkedList<MouseEvent> mousebuffer;
	private LinkedList<KeyEvent> keybuffer;
	
	//Size of the action buffer
	private int maxActionBufferSize = 10;
	private int maxMouseBufferSize = 10;
	private int maxKeyBufferSize = 10;
	private boolean breakflag = false;
	
	//Number of the buffer which contains the time chronological next element  1 = Action, 2 = Mouse, 3 = Key
	private int nextbuffernr = 0;
	
	public InputPipe() {
		//setup

		actionbuffer = new LinkedList<ActionEvent>();
		mousebuffer = new LinkedList<MouseEvent>();
		keybuffer = new LinkedList<KeyEvent>();
	}
	
	/*
	 * returns 0 if no Event has occured,
	 *         1 if an ActionEvent
	 *         2 if an MouseEvent
	 *         3 if an KeyEvent
	 */
	public int getNextEventByTime() {
		
		return 0;
	}
	
	
	/*
	 * Calls .pollFirst() on the actionbuffer
	 */
	public ActionEvent pollNextActionEvent() {
		return actionbuffer.pollFirst();
	}
	
	/*
	 * Calls .pollFirst() on the mousebuffer
	 */
	public MouseEvent pollNextMouseEvent() {
		return mousebuffer.pollFirst();
	}
	
	/*
	 * Calls .pollFirst() on the keybuffer
	 */
	public KeyEvent pollNextKeyEvent() {
		return keybuffer.pollFirst();
	}
	
	/*
	 * Deletes all stored inputevents
	 */
	public void clearAll() {
		clearAllActionEvents();
		clearAllMouseEvents();
		clearAllKeyEvents();
	}

	public void clearAllActionEvents() {
		while(!actionbuffer.isEmpty())
			actionbuffer.remove();
	}
	
	public void clearAllMouseEvents() {
		while(!mousebuffer.isEmpty())
			mousebuffer.remove();
	}

	public void clearAllKeyEvents() {
		while(!keybuffer.isEmpty())
			keybuffer.remove();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(!breakflag && actionbuffer.size() < maxActionBufferSize) {
			actionbuffer.add(ae);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!breakflag && keybuffer.size() < maxKeyBufferSize) {
			keybuffer.add(e);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(!breakflag && keybuffer.size() < maxKeyBufferSize) {
			keybuffer.add(e);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(!breakflag && keybuffer.size() < maxKeyBufferSize) {
			keybuffer.add(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(!breakflag && mousebuffer.size() < maxMouseBufferSize) {
			mousebuffer.add(e);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(!breakflag && mousebuffer.size() < maxMouseBufferSize) {
			mousebuffer.add(e);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(!breakflag && mousebuffer.size() < maxMouseBufferSize) {
			mousebuffer.add(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!breakflag && mousebuffer.size() < maxMouseBufferSize) {
			mousebuffer.add(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!breakflag && mousebuffer.size() < maxMouseBufferSize) {
			mousebuffer.add(e);
		}
	}
}