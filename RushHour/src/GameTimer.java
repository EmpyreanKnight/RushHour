import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.Timer;
import javax.swing.JLabel;

public class GameTimer extends JLabel implements ActionListener {
    private Date time;
    private Timer timer;
    private DateFormat format;

    GameTimer() {
        super("", JLabel.CENTER);
        time = new Date(0);
        timer = new Timer(1000, this);
        format = new SimpleDateFormat("mm:ss");
        setFont(new Font(Font.DIALOG, Font.BOLD, 40));
        setBackground(Color.LIGHT_GRAY);
    }

    void start() {
        setVisible(true);
        setOpaque(true);
        time.setTime(0);
        setText(format.format(time));
        timer.start();
    }

    void end() {
        setVisible(false);
        setOpaque(false);
        timer.stop();
    }

    long getTime() {
        return time.getTime();
    }

    @Override
    // update timer when triggered by internal timer event
    public void actionPerformed(ActionEvent e) {
        time.setTime(time.getTime() + timer.getDelay());
        setText(format.format(time));
    }
}
