
import java.awt.HeadlessException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Car extends JButton {
	private static final long serialVersionUID = 1L;
	public static int CAR_WIDTH = 80; // the width of each block

	private int carID; // No. of cars, target car always be 1
	private int carX;
	private int carY;
	private int carType;// 0 indicates short, 1 indicates long
	private int carDirec;// 0 indicates horizontal, 1 indicates vertical

	Random color = new Random(); // generate color randomly

	public Car(int carId, int carType, int carX, int carY, int carDirec) throws HeadlessException {
		this.carID = carId;
		this.carX = carX;
		this.carY = carY;
		this.carType = carType;
		this.carDirec = carDirec;
		
		if (carType == 0) {
			if (carDirec == 0) {
				this.setBounds(270 + carX * CAR_WIDTH, carY * CAR_WIDTH - 27, CAR_WIDTH * 2, CAR_WIDTH);
				String imgstr = "img/" + String.valueOf(carId == 1 ? 1 : color.nextInt(4) + 2) + ".png";
				this.setIcon(new ImageIcon(imgstr));
			} else {
				this.setBounds(270 + carX * CAR_WIDTH, carY * CAR_WIDTH - 27, CAR_WIDTH, CAR_WIDTH * 2);
				String imgstr = "img/" + String.valueOf(color.nextInt(6) + 6) + ".png";
				this.setIcon(new ImageIcon(imgstr));
			}

		} else {
			if (carDirec == 0) {
				String imgstr = "img/" + String.valueOf(color.nextInt(2) + 12) + ".png";
				this.setIcon(new ImageIcon(imgstr));
				this.setBounds(270 + carX * CAR_WIDTH, carY * CAR_WIDTH - 27, CAR_WIDTH * 3, CAR_WIDTH);
			} else {
				String imgstr = "img/" + String.valueOf(color.nextInt(2) + 14) + ".png";
				this.setIcon(new ImageIcon(imgstr));
				this.setBounds(270 + carX * CAR_WIDTH, carY * CAR_WIDTH - 27, CAR_WIDTH, CAR_WIDTH * 3);
			}
		}
	}

	public int getCarX() {
		return carX;
	}

	public int getCarY() {
		return carY;
	}

	public int getCarDirec() {
		return carDirec;
	}

	public int getCarID() {
		return carID;
	}

	public int getCarType() {
		return carType;
	}
}
