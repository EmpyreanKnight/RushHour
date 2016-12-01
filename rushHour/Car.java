package rushHour;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Car extends JButton implements FocusListener, MouseMotionListener {
	enum MOVE_PATTERN{
		vertical, horizontal;
	}
	
	private Color c = Color.yellow;
	private MOVE_PATTERN movePattern;
	private int length;
	
	public Car(int type) {
		super();
		setBackground(c);
		addFocusListener(this);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		setBackground(Color.CYAN);
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		setBackground(c);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	} 
}
