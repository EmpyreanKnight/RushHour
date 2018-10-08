import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class CarMotionListener extends MouseAdapter {
	private int startX, startY; // car initial coordinates
    private int startMouseX, startMouseY; // mouse initial coordinates

	private Car cars[];
	private Car car;
	private RushHour rushHour;

	CarMotionListener(Car cars[], Car car, RushHour rushHour) {
		this.rushHour = rushHour;
		this.cars = cars;
		this.car = car;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startX = car.getX();
		startY = car.getY();
		startMouseX = e.getXOnScreen();
		startMouseY = e.getYOnScreen();

		// when move a car, record the start position
		rushHour.startPosRec.add(car.getBounds());
		rushHour.carRec.add(car);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!rushHour.start)
			return;

		int dx = e.getXOnScreen() - startMouseX;
		int dy = e.getYOnScreen() - startMouseY;

        // if the car collide with other cars, we won't update its position
		if (!collisionDetection(dx, dy)) {
		    // update location
            car.setBounds(
                    car.getCarDir() == 0 ? startX + dx : startX,
                    car.getCarDir() == 1 ? startY + dy : startY,
                    car.getWidth(),
                    car.getHeight()
            );

		    // do boundary checks
			if (car.getCarDir() == 0) { // boundary check for horizontal cars
				// left bound check
				if (car.getX() < 350) {
                    car.setLocation(350, car.getY());
				}

                // right bound check
				if (car.getCarID() == 1) {
					if (car.getX() + car.getWidth() > 910) { // winning a game
                        // update step counter
                        rushHour.stepCount += Math.abs(Car.Px2CarX(car.getX()) - car.getCarX());
                        rushHour.stepCount += Math.abs(Car.Py2CarY(car.getY()) - car.getCarY());

                        rushHour.endPosRec.add(car.getBounds());
						rushHour.winGame();
					}
				} else {
					if (car.getX() + car.getWidth() > 830) {
                        car.setLocation(830 - car.getWidth(), car.getY());
					}
				}
			} else { // boundary check for vertical cars
				// upper bound check
				if (car.getY() < 53) {
                    car.setLocation(car.getX(), 53);
				}
				// lower bound check
				if (car.getY() + car.getHeight() > 533) {
                    car.setLocation(car.getX(), 533 - car.getHeight());
				}
			}
		}
	}

	// add trace when one step made
	public void mouseReleased(MouseEvent e) {
	    int oldCarX = car.getCarX();
	    int oldCarY = car.getCarY();

	    // fix the car location to blocks
        car.setPosition(Car.Px2CarX(car.getX()), Car.Py2CarY(car.getY()));

	    // update step counter when a move completed
        rushHour.stepCount += Math.abs(oldCarX - car.getCarX());
        rushHour.stepCount += Math.abs(oldCarY - car.getCarY());

        // record the end position
        rushHour.endPosRec.add(car.getBounds());
	}

	// detect whether moved cars occurred an collision with existing cars
	private boolean collisionDetection(int dx, int dy) {
	    Rectangle oldRect = car.getBounds();

	    if(car.getCarDir() == 0 && dx != 0) {
            oldRect.setLocation(dx > 0 ? car.getX() + 1 : car.getX() - 1, startY);
        } else if (car.getCarDir() == 1 && dy != 0) {
            oldRect.setLocation(startX, dy > 0 ? car.getY() + 1 : car.getY() - 1);
        }

		boolean isCollide = false;
		for (Car c : cars) {
			if (car.getCarID() != c.getCarID() &&
                    c.getBounds().intersects(oldRect)) {
                isCollide = true;
                break;
			}
		}
		return isCollide;
	}
}
