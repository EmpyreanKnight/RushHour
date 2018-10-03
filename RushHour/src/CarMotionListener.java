import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class CarMotionListener extends MouseAdapter {
    private int oldX, oldY; // car last location
	private int startX, startY; // car initial location

	private Car cars[];
	private Car car;
	private Car collidedCar;
	private RushHour rushHour;

	CarMotionListener(Car cars[], Car car, RushHour rushHour) {
		this.rushHour = rushHour;
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

		// when move a car, record the start position
		rushHour.startPosRec.add(cp.getBounds());
		rushHour.carRec.add(car);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!rushHour.start)
			return;

		Component cp = (Component) e.getSource();
        int newX = e.getXOnScreen();
        int newY = e.getYOnScreen();

		if (!collisionDetection()) {
			if (car.getCarDir() == 0) {
				cp.setBounds(startX + (newX - oldX), startY, cp.getWidth(), cp.getHeight());
				if (cp.getX() < 350) {
					cp.setLocation(350, cp.getY());
				}
				
				// check whether get to the goal
				if (car.getCarID() == 1) {
					if (cp.getX() + car.getWidth() > 900) { // winning a game
                        rushHour.endPosRec.add(cp.getBounds());
						rushHour.winGame();
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
			collidedCar = null;
		}
	}

	// add trace when one stepCount made
	public void mouseReleased(MouseEvent e) {
		Car c = (Car) e.getSource();
		if (collidedCar != null) {
			if (car.getCarDir() == 0) {
				if (collidedCar.getX() > car.getX()) {
					c.setBounds(collidedCar.getX() - car.getWidth(), startY,
							c.getWidth(), c.getHeight());
				} else {
					c.setBounds(collidedCar.getX() + collidedCar.getWidth(), startY,
							c.getWidth(), c.getHeight());
				}
			} else {
				if (collidedCar.getY() > car.getY()) {
					c.setBounds(startX, collidedCar.getY() - car.getHeight(),
							c.getWidth(), c.getHeight());
				} else {
					c.setBounds(startX, collidedCar.getY() + collidedCar.getHeight(),
							c.getWidth(), c.getHeight());
				}
			}
		}

        rushHour.stepCount++;
        // when move a car, record the end position
        Component cp = (Component) e.getSource();
        rushHour.endPosRec.add(cp.getBounds());
	}

	// detect whether moved cars occurred an collision with existing cars
	private boolean collisionDetection() {
		boolean isCollide = false;
		for (Car c : cars) {
			Rectangle cRect = c.getBounds();
			if ((car.getBounds().intersects(cRect) && car.getCarDir() != c.getCarDir())
					&& car.getCarID() != c.getCarID()) {
				collidedCar = c;
                isCollide = true;
			}
		}
		return isCollide;
	}
}
