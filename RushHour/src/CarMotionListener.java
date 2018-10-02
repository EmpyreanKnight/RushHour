
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

class CarMotionListener extends MouseAdapter {
	private int newX, newY, oldX, oldY;
	private int startX, startY;
	
	private String direction;
	private Car cars[];
	private Car car;
	private Car dcar;
	private RushHour rushHour;

	public CarMotionListener(String direction, Car cars[], Car car, RushHour rushHour) {
		this.rushHour = rushHour;
		this.direction = direction;
		this.cars = cars;
		this.car = car;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Component cp = (Component) e.getSource();
		startX = cp.getX();
		startY = cp.getY();
		oldX = e.getXOnScreen();
		oldY = e.getYOnScreen();

		// when a move start, record the start position
		RushHour.nowrec.add(cp.getBounds());
		RushHour.nowcar.add(car);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!RushHour.start)
			return;

		Component cp = (Component) e.getSource();
		newX = e.getXOnScreen();
		newY = e.getYOnScreen();

		Rectangle carRect = cp.getBounds();
		if (collisionDetection(cars, car, carRect)) {
			if (direction.equals("0")) {
				cp.setBounds(startX + (newX - oldX), startY, cp.getWidth(), cp.getHeight());
				if (cp.getX() < 350) {
					cp.setLocation(350, cp.getY());
				}
				
				// check whether get to the goal
				if (car.getCarID() == 1) {
					if (cp.getX() + car.getWidth() > 900) { // win a game
						RushHour.timer.end();
						RankList.addRecord(RushHour.user, 
								(int)(RushHour.timer.getTime() / 1000), RushHour.stepCount);
						RushHour.speaker.win();
						JOptionPane.showMessageDialog(null,
								"You Win!" + " Time:" + RushHour.timer.getTime() / 1000 
								+ "s, steps:" + RushHour.stepCount);
						
						// pop replay window
						if (JOptionPane.showConfirmDialog(null, "Replay game?") == 0) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									for (int i = RushHour.nowcar.size() - 1; i >= 0; i--) {
										RushHour.nowcar.get(i).setBounds(
												RushHour.nowrec.get(i));
									}
									for (int i = 0; i < RushHour.nowcarlater.size(); i++) {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e1) {
											e1.printStackTrace();
										}
										RushHour.nowcarlater.get(i).setBounds(
												RushHour.nowreclater.get(i));
									}
									
									try {
										Thread.sleep(1000);
										RushHour.car[0].setBounds(270 + 480, 240 - 27, 160, 80);
										Thread.sleep(1500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									
									new RushHour(RushHour.carLocation, RushHour.user);
									rushHour.dispose();
								}
							}).start();
						} else {
							new RushHour();
							rushHour.dispose();
						}
					}
				} else {
					if (cp.getX() + car.getWidth() > 830) {
						cp.setLocation(830 - car.getWidth(), cp.getY());
					}
				}

			} else {
				cp.setBounds(startX, startY + (newY - oldY), cp.getWidth(), cp.getHeight());
				if (cp.getY() < 50) {
					cp.setLocation(cp.getX(), 50);
				}
				if (cp.getY() + car.getHeight() > 540) {
					cp.setLocation(cp.getX(), 540 - car.getHeight());
				}
			}
			dcar = null;
		}
	}

	// add trace when one stepCount made
	public void mouseReleased(MouseEvent e) {
		Component cp = (Component) e.getSource();
		RushHour.nowreclater.add(cp.getBounds());
		RushHour.nowcarlater.add(car);
		if (dcar != null) {
			if (direction.equals("0")) {
				if (dcar.getX() > car.getX()) {
					cp.setBounds(dcar.getX() - car.getWidth(), startY, 
							cp.getWidth(), cp.getHeight());
				} else {
					cp.setBounds(dcar.getX() + dcar.getWidth(), startY, 
							cp.getWidth(), cp.getHeight());
				}
			} else {
				if (dcar.getY() > car.getY()) {
					cp.setBounds(startX, dcar.getY() - car.getHeight(), 
							cp.getWidth(), cp.getHeight());
				} else {
					cp.setBounds(startX, dcar.getY() + dcar.getHeight(), 
							cp.getWidth(), cp.getHeight());
				}
			}
		}
		RushHour.stepCount++;
	}

	// detect whether moved car occurred an collision with existing cars
	public boolean collisionDetection(Car cars[], Car car, Rectangle carRect) {
		boolean move = true;
		for (int k = 0; k < cars.length; k++) {
			Rectangle cRect = cars[k].getBounds();
			if ((carRect.intersects(cRect) && car.getCarDir() != cars[k].getCarDir())
					&& car.getCarID() != cars[k].getCarID()) {
				dcar = cars[k];
				move = false;
			}
		}
		return move;
	}
}
