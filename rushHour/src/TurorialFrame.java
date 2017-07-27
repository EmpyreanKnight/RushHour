
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TurorialFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public TurorialFrame(final RushHour rushHour) {
		this.setLayout(null);
		this.setBounds(100, 30, 1050, 700);
		JLabel turoialimg = new JLabel();
		turoialimg.setBounds(0, 0, 1050, 700);
		turoialimg.setIcon(new ImageIcon("img/rule.jpg"));
		this.add(turoialimg);
		
		// click to return to the main interface
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rushHour.setVisible(true);
				close();
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void close() {
		this.dispose();
	}
}
