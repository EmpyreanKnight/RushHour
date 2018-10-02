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
    private static final long serialVersionUID = 1L;

    // (ID, type, x-coordination, y-coordination, direction) of cars in each stage
    // carType 0 indicates short, 1 indicates long
    // carDirec 0 indicates horizontal, 1 indicates vertical
    private static int carLocation1[][] = new int[][]{
            {1, 0, 1, 3, 0},
            {2, 1, 3, 1, 1},
            {3, 1, 1, 6, 0},
            {4, 1, 6, 4, 1}};
    private static int carLocation2[][] = new int[][]{
            {1, 0, 1, 3, 0},
            {2, 1, 5, 1, 1},
            {3, 1, 4, 5, 0},
            {4, 0, 5, 6, 0},
            {5, 0, 2, 5, 1}};
    private static int carLocation3[][] = new int[][]{
            {1, 0, 3, 3, 0},
            {2, 0, 4, 1, 0},
            {3, 1, 6, 1, 1},
            {4, 0, 5, 4, 0},
            {5, 1, 4, 4, 1},
            {6, 0, 3, 1, 1}};

    // record every moved stepCount
    static List<Rectangle> nowrec = new ArrayList<Rectangle>();
    static List<Car> nowcar = new ArrayList<Car>();
    static List<Rectangle> nowreclater = new ArrayList<Rectangle>();
    static List<Car> nowcarlater = new ArrayList<Car>();

    // record current game info
    static int[][] carLocation;
    static boolean start = false;
    static String user;
    static int stepCount;
    static Car car[];

    // audio controller
    static Speaker speaker = new Speaker();

    // game timer
    static MyTimer timer = new MyTimer();

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
        new RushHour(RushHour.carLocation1, "");
    }

    public RushHour(int[][] carlocation, String user) {
        stepCount = 0;
        start = false;
        nowrec = new ArrayList<Rectangle>();
        nowreclater = new ArrayList<Rectangle>();
        nowcar = new ArrayList<Car>();
        nowcarlater = new ArrayList<Car>();
        RushHour.carLocation = carlocation;
        RushHour.user = user;

        init();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(950, 700);
        setVisible(true);
    }

    public void init() {
        setLayout(null);
        JButton button;

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

        speaker.setVisible(true);
        speaker.setBounds(870, 30, 44, 44);
        add(speaker);

        timer.setBounds(50, 460, 200, 50);
        timer.end();
        add(timer);

        startButton = new JButton();
        startButton.setName("START");
        startButton.setIcon(new ImageIcon("img/buttons/START.png"));
        startButton.addActionListener(this);
        startButton.setBounds(450, 240, 280, 100);

        ((JPanel) this.getContentPane()).setOpaque(false);
        JLabel background = new JLabel(new ImageIcon("img/rushhour.png"));
        getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        getLayeredPane().add(startButton, new Integer(Integer.MAX_VALUE));
        background.setBounds(300, 0, 581, 614);

        // set cars on game board
        car = new Car[carLocation.length];
        for (int k = 0; k < car.length; k++) {
            car[k] = new Car(carLocation[k][0], carLocation[k][1], carLocation[k][2],
                    carLocation[k][3], carLocation[k][4]);
            if (carLocation[k][4] == 0) {
                CarMotionListener mh = new CarMotionListener("0", car, car[k], this);
                car[k].addMouseListener(mh);
                car[k].addMouseMotionListener(mh);
            } else {
                CarMotionListener mz = new CarMotionListener("1", car, car[k], this);
                car[k].addMouseListener(mz);
                car[k].addMouseMotionListener(mz);
            }
            add(car[k]);
        }
    }

    // action listener of button click
    public void actionPerformed(ActionEvent e) {
        System.out.println(((JButton)e.getSource()).getName());
        switch (((JButton)e.getSource()).getName()) {
            case "RESTART":
                dispose();
                new RushHour(carLocation, user);
                break;
            case "UNDO":
                if (nowcar != null && nowcar.size() > 0 && nowrec != null && nowrec.size() > 0) {
                    nowcar.get(nowcar.size() - 1).setBounds(nowrec.get(nowrec.size() - 1));
                    nowrec.remove(nowrec.size() - 1);
                    nowcar.remove(nowcar.size() - 1);
                    RushHour.nowreclater.remove(RushHour.nowreclater.size() - 1);
                    RushHour.nowcarlater.remove(RushHour.nowcarlater.size() - 1);
                }
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
                JOptionPane.showMessageDialog(null, RankList.readRank());
                break;
            case "TUTORIAL":
                this.setVisible(false);
                new TurorialFrame(this);
                break;
            case "START":
                speaker.start();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                RushHour.start = true;
                startButton.setVisible(false);
                timer.start();
                break;
            case "SKIP":
                if (!start)
                    return;
                Map<Integer, Steps> position = new HashMap<Integer, Steps>();
                for (Car c : car) {
                    int fromX = c.getCarY() - 1;
                    int fromY = c.getCarX() - 1;
                    int toX = fromX + (c.getCarDir() == 1 ? (c.getCarType() == 1 ? 2 : 1) : 0);
                    int toY = fromY + (c.getCarDir() == 0 ? (c.getCarType() == 1 ? 2 : 1) : 0);
                    position.put(c.getCarID(), new Steps(c.getCarID(), fromX, fromY, toX, toY));
                }

                Vector<Steps> rec = Solver.solve(position);
                final Vector<Steps> res = rec;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Steps pos : res) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            Car c = car[pos.ID - 1];
                            nowcar.add(c);
                            nowrec.add(c.getBounds());
                            int width = c.getCarDir() == 0 ? c.getCarType() == 1 ? 3 : 2 : 1;
                            int height = c.getCarDir() == 1 ? c.getCarType() == 1 ? 3 : 2 : 1;
                            c.setBounds(350 + pos.toY * Car.CAR_SIZE, pos.toX * Car.CAR_SIZE + 53,
                                    width * Car.CAR_SIZE, height * Car.CAR_SIZE);
                            nowcarlater.add(c);
                            nowreclater.add(c.getBounds());
                        }
                    }
                }).start();
                break;
            case "EXIT":
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            case "STAGE1":
                dispose();
                new RushHour(carLocation1, user);
                break;
            case "STAGE2":
                dispose();
                new RushHour(carLocation2, user);
                break;
            case "STAGE3":
                dispose();
                new RushHour(carLocation3, user);
                break;
        }
    }

    public static void main(String[] args) {
        RushHour rushHour = new RushHour(RushHour.carLocation1, "");
    }
}
