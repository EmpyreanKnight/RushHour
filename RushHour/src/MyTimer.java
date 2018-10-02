import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.Timer;
import javax.swing.JLabel;

public class MyTimer extends JLabel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Date time;
	private Timer timer;
	private DateFormat format;
	
	public MyTimer() {
		super("", JLabel.CENTER);
		time = new Date(0);
		timer = new Timer(1000, this);
		format = new SimpleDateFormat("mm:ss");
		setFont(new Font(Font.DIALOG, Font.BOLD, 40));
		setBackground(Color.LIGHT_GRAY);
	}

	public void start() {
		setVisible(true);
		setOpaque(true);
		time.setTime(0);
		setText(format.format(time));
		timer.start();
	}
	
	public void end() {
		setVisible(false);
		setOpaque(false);
		timer.stop();
	}

	public long getTime() {
		return time.getTime();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		time.setTime(time.getTime() + timer.getDelay());
		setText(format.format(time));
	}
}
