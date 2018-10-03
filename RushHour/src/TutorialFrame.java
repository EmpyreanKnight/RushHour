import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class TutorialFrame extends JFrame {
	TutorialFrame(final RushHour rushHour) {
		setBounds(100, 30, 1050, 700);
		JLabel tutorialImg = new JLabel();
		tutorialImg.setIcon(new ImageIcon("img/rule.jpg"));
		add(tutorialImg);
		
		// click to return to the main window
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                rushHour.setVisible(true);
                dispose();
			}
		});

		// close to return to the main window
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				rushHour.setVisible(true);
                dispose();
			}
		});

		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
}
