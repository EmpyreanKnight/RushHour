import javax.swing.*;
import java.awt.*;

public class StepCounter extends JLabel {
    private int counter;

    StepCounter() {
        super("", JLabel.CENTER);
        setFont(new Font(Font.DIALOG, Font.BOLD, 40));
        setBackground(Color.LIGHT_GRAY);
        setOpaque(true);
    }

    void start() {
        setVisible(true);
        counter = 0;
        setText(Integer.toString(counter));
    }

    void increment(int steps) {
        counter += steps;
        setText(Integer.toString(counter));
    }

    void decrement(int steps) {
        counter -= steps;
        setText(Integer.toString(counter));
    }

    void reset() {
        counter = 0;
        setText(Integer.toString(counter));
    }

    void end() {
        setVisible(false);
    }

    int getSteps() {
        return counter;
    }
}
