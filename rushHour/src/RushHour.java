
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RushHour extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	// ID, type, coordinate, vector of cars in each stage
	// carType 0 indicates short, 1 indicates long
	// carDirec 0 indicates horizontal, 1 indicates vertical
	static int carLocation1[][] = new int[][] { { 1, 0, 1, 3, 0 }, { 2, 1, 3, 1, 1 }, { 3, 1, 1, 6, 0 },
			{ 4, 1, 6, 4, 1 } };
	static int carLocation2[][] = new int[][] { { 1, 0, 1, 3, 0 }, { 2, 1, 5, 1, 1 }, { 3, 1, 4, 5, 0 },
			{ 4, 0, 5, 6, 0 }, { 5, 0, 2, 5, 1 } };
	static int carLocation3[][] = new int[][] { { 1, 0, 3, 3, 0 }, { 2, 0, 4, 1, 0 }, { 3, 1, 6, 1, 1 },
			{ 4, 0, 5, 4, 0 }, { 5, 1, 4, 4, 1 }, { 6, 0, 3, 1, 1 } };

	// record every moved step
	static List<Rectangle> nowrec = new ArrayList<Rectangle>();
	static List<Car> nowcar = new ArrayList<Car>();
	static List<Rectangle> nowreclater = new ArrayList<Rectangle>();
	static List<Car> nowcarlater = new ArrayList<Car>();
	
	// record current game info
	static int[][] carLocation;
	static boolean start = false;
	static String user;
	static int step;
	static Car car[];
	
	// audio controller
	static Speaker speaker = new Speaker();
	
	//game timer
	static MyTimer timer = new MyTimer();
	
	// background components
	ImageIcon backgoundPic = new ImageIcon("img/rushhour.png");
	JLabel title = new JLabel(new ImageIcon("img/title.png"));
	JLabel imgLabel = new JLabel(backgoundPic);
	JLabel background = new JLabel(backgoundPic);
	
	// cubic buttons in the main window
	JButton restart = new JButton("restart");
	JButton undo = new JButton("undo");
	JButton hint = new JButton("hint");
	
	// square buttons in the main window
	JButton selectstage = new JButton("select stage");
	JButton history = new JButton("history");
	JButton turorial = new JButton("turorial");
	JButton exit = new JButton("exit");
	
	//start game button
	JButton startgame = new JButton("startgame");

	// select stage buttons
	JButton firststage = new JButton("firststage");
	JButton secondstage = new JButton("secondstage");
	JButton thirdstage = new JButton("thirdstage");

	public RushHour(int[][] carlocation, String user) {
		step = 0;
		start = false;
		nowrec = new ArrayList<Rectangle>();
		nowreclater = new ArrayList<Rectangle>();
		nowcar = new ArrayList<Car>();
		nowcarlater = new ArrayList<Car>();
		RushHour.carLocation = carlocation;
		RushHour.user = user;
		setBounds(100, 30, 950, 700);
		init();
		setVisible(true);
		// new StartlFrame(this);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		// initialize buttons
		setLayout(null);
		title.setBounds(10, 20, 300, 100);
		add(title);

		restart.setIcon(new ImageIcon("img/reset.png"));
		restart.setBounds(20, 530, 90, 70);
		restart.addActionListener(this);
		add(restart);

		undo.setIcon(new ImageIcon("img/undo.png"));
		undo.setBounds(120, 530, 90, 70);
		undo.addActionListener(this);
		add(undo);

		hint.setIcon(new ImageIcon("img/skip.png"));
		hint.setBounds(220, 530, 90, 70);
		hint.addActionListener(this);
		add(hint);

		selectstage.setIcon(new ImageIcon("img/STAGE.png"));
		selectstage.setBounds(50, 150, 190, 50);
		selectstage.addActionListener(this);
		add(selectstage);
		
		history.setIcon(new ImageIcon("img/HISTORY.png"));
		history.setBounds(50, 230, 190, 50);
		history.addActionListener(this);
		add(history);
		
		turorial.setIcon(new ImageIcon("img/TUTORIAL.png"));
		turorial.setBounds(50, 310, 190, 50);
		turorial.addActionListener(this);
		add(turorial);
		
		exit.setIcon(new ImageIcon("img/EXIT.png"));
		exit.setBounds(50, 390, 190, 50);
		add(exit);
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		
		firststage.setIcon(new ImageIcon("img/STAGE1.png"));
		firststage.setBounds(50, 200, 190, 50);
		firststage.setVisible(false);
		firststage.addActionListener(this);
		add(firststage);

		secondstage.setIcon(new ImageIcon("img/STAGE2.png"));
		secondstage.setBounds(50, 280, 190, 50);
		secondstage.setVisible(false);
		secondstage.addActionListener(this);
		add(secondstage);

		thirdstage.setIcon(new ImageIcon("img/STAGE3.png"));
		thirdstage.addActionListener(this);
		thirdstage.setVisible(false);
		thirdstage.setBounds(50, 360, 190, 50);
		add(thirdstage);

		startgame.setIcon(new ImageIcon("img/START.png"));
		startgame.addActionListener(this);
		startgame.setVisible(true);
		startgame.setBounds(440, 240, 300, 100);
		add(startgame);

		speaker.open();
		speaker.setVisible(true);
		speaker.setBounds(870, 30, 44, 44);
		add(speaker);
		
		timer.setBounds(50, 460, 190, 50);
		timer.end();
		add(timer);
		
		((JPanel) this.getContentPane()).setOpaque(false);
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(300, 0, backgoundPic.getIconWidth(), backgoundPic.getIconHeight());

		Container c = getContentPane();
		JPanel jp = new JPanel();
		jp.setOpaque(false);
		c.add(jp);

		// set cars on game board
		car = new Car[carLocation.length];
		for (int k = 0; k < car.length; k++) {
			car[k] = new Car(carLocation[k][0], carLocation[k][1], carLocation[k][2],
					carLocation[k][3], carLocation[k][4]);
			if (carLocation[k][4] == 0) {
				MyListener mh = new MyListener("0", car, car[k], this);
				car[k].addMouseListener(mh);
				car[k].addMouseMotionListener(mh);
			} else {
				MyListener mz = new MyListener("1", car, car[k], this);
				car[k].addMouseListener(mz);
				car[k].addMouseMotionListener(mz);
			}
			add(car[k]);
		}
	}

	// action listener of button click
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == restart) {
			dispose();
			new RushHour(carLocation, user);
		} else if (e.getSource() == undo) {
			if (nowcar != null && nowcar.size() > 0 && nowrec != null && nowrec.size() > 0) {
				nowcar.get(nowcar.size() - 1).setBounds(nowrec.get(nowrec.size() - 1));
				nowrec.remove(nowrec.size() - 1);
				nowcar.remove(nowcar.size() - 1);
				RushHour.nowreclater.remove(RushHour.nowreclater.size() - 1);
				RushHour.nowcarlater.remove(RushHour.nowcarlater.size() - 1);
			}
		} else if (e.getSource() == selectstage) {
			firststage.setVisible(true);
			secondstage.setVisible(true);
			thirdstage.setVisible(true);
			selectstage.setVisible(false);
			history.setVisible(false);
			turorial.setVisible(false);
			exit.setVisible(false);
		} else if (e.getSource() == firststage) {
			dispose();
			new RushHour(carLocation1, user);
		} else if (e.getSource() == secondstage) {
			dispose();
			new RushHour(carLocation2, user);
		} else if (e.getSource() == thirdstage) {
			dispose();
			new RushHour(carLocation3, user);
		} else if (e.getSource() == history) {
			JOptionPane.showMessageDialog(null, RankList.readRank());
		} else if (e.getSource() == turorial) {
			this.setVisible(false);
			new TurorialFrame(this);
		} else if (e.getSource() == startgame) {
			speaker.start();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			RushHour.start = true;
			startgame.setVisible(false);
			timer.start();
		} else if (e.getSource() == hint) {
			if (!start)
				return;
			Map<Integer, Steps> position = new HashMap<Integer, Steps>();
			for (int i = 0; i < car.length; i++) {
				Car c = car[i];
				int fromX = c.getCarY() - 1;
				int fromY = c.getCarX() - 1;
				int toX = fromX + (c.getCarDirec() == 1 ? (c.getCarType() == 1 ? 2 : 1) : 0);
				int toY = fromY + (c.getCarDirec() == 0 ? (c.getCarType() == 1 ? 2 : 1) : 0);
				position.put(c.getCarID(), new Steps(c.getCarID(), fromX, fromY, toX, toY));
			}
			
			Vector<Steps> rec = Solver.solve(position);
			final Vector<Steps> res = rec;
			new Thread(new Runnable() {
				@Override
				public void run() {
					for(Steps pos : res) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						Car c = car[pos.ID - 1];
						nowcar.add(c);
						nowrec.add(c.getBounds());
						int width = c.getCarDirec() == 0 ? c.getCarType() == 1 ? 3 : 2 : 1;
						int height = c.getCarDirec() == 1 ? c.getCarType() == 1 ? 3 : 2 : 1;
						c.setBounds(350 + pos.toY * Car.CAR_WIDTH, pos.toX * Car.CAR_WIDTH + 53, 
								width * Car.CAR_WIDTH, height * Car.CAR_WIDTH);
						nowcarlater.add(c);
						nowreclater.add(c.getBounds());
					}
				}
			}).start();
		}
	}
}
