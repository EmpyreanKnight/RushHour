import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;

public class RushHour extends JFrame implements ActionListener {
    // (ID, type, x-coordination, y-coordination, direction) of cars in each stage
    // carType 0 indicates short, 1 indicates long
    // carDir 0 indicates horizontal, 1 indicates vertical
    private static int carLocation1[][] = new int[][]{
            {1, 0, 1, 3, 0},
            {2, 1, 3, 1, 1},
            {3, 1, 1, 6, 0},
            {4, 1, 6, 4, 1}
    };
    private static int carLocation2[][] = new int[][]{
            {1, 0, 2, 3, 0},
            {2, 0, 1, 1, 0},
            {3, 1, 1, 2, 1},
            {4, 0, 1, 5, 1},
            {5, 1, 4, 2, 1},
            {6, 1, 3, 6, 0},
            {7, 0, 5, 5, 0},
            {8, 1, 6, 1, 1}
    };
    private static int carLocation22[][] = new int[][]{
            {1, 0, 1, 3, 0},
            {2, 1, 5, 1, 1},
            {3, 1, 4, 5, 0},
            {4, 0, 5, 6, 0},
            {5, 0, 2, 5, 1}};
    private static int carLocation33[][] = new int[][]{
            {1, 0, 3, 3, 0},
            {2, 0, 4, 1, 0},
            {3, 1, 6, 1, 1},
            {4, 0, 5, 4, 0},
            {5, 1, 4, 4, 1},
            {6, 0, 3, 1, 1}};
    /*
    This configuration is claimed to be the hardest one
    Required at least 93 moves to solve it (our solver can do it!)
    Source link:
    http://cs.ulb.ac.be/~fservais/rushhour/index.php?window_size=20&offset=0
    */
    private static int carLocation3[][] = new int[][]{
            {1, 0, 3, 3, 0},
            {2, 0, 1, 2, 1},
            {3, 0, 2, 5, 1},
            {4, 0, 3, 4, 1},
            {5, 0, 4, 1, 1},
            {6, 1, 5, 1, 1},
            {7, 1, 6, 1, 1},
            {8, 1, 1, 1, 0},
            {9, 0, 2, 2, 0},
            {10, 0, 1, 4, 0},
            {11, 0, 3, 6, 0},
            {12, 0, 5, 5, 0},
            {13, 0, 5, 6, 0}
    };

    // record every moved step
    List<Rectangle> startPosRec;
    List<Rectangle> endPosRec;
    List<Car> carRec;
    boolean start;
    int stepCount;

    // record current game info
    private int[][] carLocation;
    private int stage;
    private Car cars[];

    // controller components
    private Speaker speaker;
    private JLabel countDown;
    private GameTimer timer;

    // start button
    private JButton startButton;

    // main window option buttons
    private JButton stageButton;
    private JButton historyButton;
    private JButton tutorialButton;
    private JButton exitButton;

    // select stage option buttons
    private JButton firstStageButton;
    private JButton secondStageButton;
    private JButton thirdStageButton;

    public RushHour() {
        new RushHour(RushHour.carLocation1, 1);
    }

    public RushHour(int[][] carLocation, int stage) {
        init();
        reset(carLocation, stage);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(950, 700);
        setVisible(true);
    }

    private void init() {
        setLayout(null);

        JLabel label = new JLabel(new ImageIcon("img/title.png"));
        label.setBounds(10, 20, 300, 100);
        add(label);

        stageButton = new JButton();
        stageButton.setName("STAGE");
        stageButton.setIcon(new ImageIcon("img/buttons/STAGE.png"));
        stageButton.setBounds(50, 150, 200, 50);
        stageButton.setMargin(new Insets(0, 0, 0, 0));
        stageButton.setBorder(null);
        stageButton.addActionListener(this);
        add(stageButton);

        historyButton = new JButton();
        historyButton.setName("HISTORY");
        historyButton.setIcon(new ImageIcon("img/buttons/HISTORY.png"));
        historyButton.setBounds(50, 230, 200, 50);
        historyButton.addActionListener(this);
        add(historyButton);

        tutorialButton = new JButton();
        tutorialButton.setName("TUTORIAL");
        tutorialButton.setIcon(new ImageIcon("img/buttons/TUTORIAL.png"));
        tutorialButton.setBounds(50, 310, 200, 50);
        tutorialButton.addActionListener(this);
        add(tutorialButton);

        exitButton = new JButton();
        exitButton.setName("EXIT");
        exitButton.setIcon(new ImageIcon("img/buttons/EXIT.png"));
        exitButton.setBounds(50, 390, 200, 50);
        exitButton.addActionListener(this);
        add(exitButton);

        JButton resetButton = new JButton();
        resetButton.setName("RESET");
        resetButton.setIcon(new ImageIcon("img/buttons/reset.png"));
        resetButton.setBounds(20, 530, 90, 70);
        resetButton.addActionListener(this);
        add(resetButton);

        JButton undoButton = new JButton();
        undoButton.setName("UNDO");
        undoButton.setIcon(new ImageIcon("img/buttons/undo.png"));
        undoButton.setBounds(120, 530, 90, 70);
        undoButton.addActionListener(this);
        add(undoButton);

        JButton skipButton = new JButton();
        skipButton.setName("SKIP");
        skipButton.setIcon(new ImageIcon("img/buttons/skip.png"));
        skipButton.setBounds(220, 530, 90, 70);
        skipButton.addActionListener(this);
        add(skipButton);

        firstStageButton = new JButton();
        firstStageButton.setName("STAGE1");
        firstStageButton.setIcon(new ImageIcon("img/buttons/STAGE1.png"));
        firstStageButton.setBounds(50, 200, 200, 50);
        firstStageButton.setVisible(false);
        firstStageButton.addActionListener(this);
        add(firstStageButton);

        secondStageButton = new JButton();
        secondStageButton.setName("STAGE2");
        secondStageButton.setIcon(new ImageIcon("img/buttons/STAGE2.png"));
        secondStageButton.setBounds(50, 280, 200, 50);
        secondStageButton.setVisible(false);
        secondStageButton.addActionListener(this);
        add(secondStageButton);

        thirdStageButton = new JButton();
        thirdStageButton.setName("STAGE3");
        thirdStageButton.setIcon(new ImageIcon("img/buttons/STAGE3.png"));
        thirdStageButton.addActionListener(this);
        thirdStageButton.setVisible(false);
        thirdStageButton.setBounds(50, 360, 200, 50);
        add(thirdStageButton);

        speaker = new Speaker();
        speaker.setVisible(true);
        speaker.setBounds(870, 30, 44, 44);
        add(speaker);

        timer = new GameTimer();
        timer.setBounds(50, 460, 200, 50);
        timer.end();
        add(timer);

        countDown = new JLabel();
        countDown.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        countDown.setVerticalAlignment(SwingConstants.CENTER);
        countDown.setHorizontalAlignment(SwingConstants.CENTER);
        countDown.setBackground(Color.WHITE);
        countDown.setOpaque(true);
        countDown.setBounds(515, 220, 150, 150);
        countDown.setVisible(false);
        getLayeredPane().add(countDown, JLayeredPane.POPUP_LAYER);

        startButton = new JButton();
        startButton.setName("START");
        startButton.setIcon(new ImageIcon("img/buttons/START.png"));
        startButton.addActionListener(this);
        startButton.setBounds(450, 240, 280, 100);

        ((JPanel) this.getContentPane()).setOpaque(false);
        JLabel background = new JLabel(new ImageIcon("img/rushhour.png"));
        getLayeredPane().add(background, JLayeredPane.DEFAULT_LAYER);
        getLayeredPane().add(startButton, JLayeredPane.POPUP_LAYER);
        background.setBounds(300, 0, 581, 614);
    }

    // action listener of button click
    public void actionPerformed(ActionEvent e) {
        System.out.println(((JButton)e.getSource()).getName());
        switch (((JButton)e.getSource()).getName()) {
            case "START":
                startButton.setVisible(false);
                countDown.setVisible(true);
                new Thread(() -> {
                    try {
                        speaker.startMusic();
                        countDown.setText("3");
                        Thread.sleep(1000);
                        countDown.setText("2");
                        Thread.sleep(1000);
                        countDown.setText("1");
                        Thread.sleep(1000);
                        countDown.setText("GO!");
                        Thread.sleep(300);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    countDown.setVisible(false);
                    start = true;
                    timer.start();
                }).start();
                break;
            case "STAGE":
                firstStageButton.setVisible(true);
                secondStageButton.setVisible(true);
                thirdStageButton.setVisible(true);
                stageButton.setVisible(false);
                historyButton.setVisible(false);
                tutorialButton.setVisible(false);
                exitButton.setVisible(false);
                break;
            case "HISTORY":
                JOptionPane.showMessageDialog(null, RankList.readRank(stage));
                break;
            case "TUTORIAL":
                this.setVisible(false);
                new TutorialFrame(this);
                break;
            case "EXIT":
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                break;
            case "RESET":
                reset(carLocation, stage);
                break;
            case "UNDO":
                if (carRec != null && carRec.size() > 0 && startPosRec != null && startPosRec.size() > 0) {
                    carRec.get(carRec.size() - 1).setBounds(startPosRec.get(startPosRec.size() - 1));
                    carRec.remove(carRec.size() - 1);
                    startPosRec.remove(startPosRec.size() - 1);
                    endPosRec.remove(endPosRec.size() - 1);
                }
                break;
            case "SKIP":
                if (!start)
                    return;
                Map<Integer, Steps> position = new HashMap<>();
                for (Car c : cars) {
                    int fromX = c.getCarY() - 1;
                    int fromY = c.getCarX() - 1;
                    int toX = fromX + (c.getCarDir() == 1 ? (c.getCarType() == 1 ? 2 : 1) : 0);
                    int toY = fromY + (c.getCarDir() == 0 ? (c.getCarType() == 1 ? 2 : 1) : 0);
                    position.put(c.getCarID(), new Steps(c.getCarID(), fromX, fromY, toX, toY));
                }

                // the solver runs so fast that we don't need to block
                final Vector<Steps> res = Solver.solve(position);

                new Thread(() -> {
                    for (Steps pos : res) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        Car c = cars[pos.ID - 1];
                        carRec.add(c);
                        startPosRec.add(c.getBounds());
                        c.setPosition(pos.toY + 1, pos.toX + 1);
                        endPosRec.add(c.getBounds());
                    }
                    stepCount += res.size();
                }).start();
                break;
            case "STAGE1":
                reset(carLocation1, 1);
                break;
            case "STAGE2":
                reset(carLocation2, 2);
                break;
            case "STAGE3":
                reset(carLocation3, 3);
                break;
        }
    }

    void winGame() {
        timer.end();
        speaker.winningMusic();
        String username = JOptionPane.showInputDialog(null,
                "You Win!" + " Time:" + timer.getTime() / 1000
                        + "s, steps:" + stepCount + "\nPlease enter your name:",
                "Anonymous");
        if (username != null) {
            RankList.addNewRecord(username,
                    (int) (timer.getTime() / 1000), stepCount, stage);
        }

        replay();
    }

    private void replay() {
        // pop replay window
        if (JOptionPane.showConfirmDialog(null, "Want to replay your game?",
                "Replay?", JOptionPane.YES_NO_OPTION) == 0) {
            new Thread(() -> {
                for (int i = 0; i < cars.length; i++) {
                    cars[i].setPosition(carLocation[i][2], carLocation[i][3]);
                }
                for (int i = 0; i < endPosRec.size(); i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    carRec.get(i).setBounds(endPosRec.get(i));
                }

                try {
                    Thread.sleep(1000);
                    cars[0].setBounds(270 + 480, 240 - 27, 160, 80);
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                replay();
            }).start();
        } else {
            reset(carLocation, stage);
        }
    }

    private void reset(int[][] carLocation, int stage) {
        // initialize status variable
        stepCount = 0;
        start = false;
        this.stage = stage;
        startPosRec = new ArrayList<>();
        endPosRec = new ArrayList<>();
        carRec = new ArrayList<>();
        this.carLocation = carLocation;

        // reset main window button display
        firstStageButton.setVisible(false);
        secondStageButton.setVisible(false);
        thirdStageButton.setVisible(false);
        stageButton.setVisible(true);
        historyButton.setVisible(true);
        tutorialButton.setVisible(true);
        exitButton.setVisible(true);
        startButton.setVisible(true);

        // remove old cars
        if (cars != null) {
            for (Car c : cars) {
                getLayeredPane().remove(c);
            }
        }

        // set cars on game board
        cars = new Car[carLocation.length];
        for (int i = 0; i < carLocation.length; i++) {
            cars[i] = new Car(carLocation[i][0], carLocation[i][1], carLocation[i][2],
                    carLocation[i][3], carLocation[i][4]);

            CarMotionListener listener = new CarMotionListener(cars, cars[i], this);
            cars[i].addMouseListener(listener);
            cars[i].addMouseMotionListener(listener);
            getLayeredPane().add(cars[i], JLayeredPane.PALETTE_LAYER);
        }

        repaint();
    }

    public static void main(String[] args) {
        new RushHour();
    }
}
