import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Car extends JButton {
    final static int CAR_SIZE = 80; // the width of each block

    private int carID; // No. of cars, target car always be 1
    private int carX;
    private int carY;
    private int carType;// 0 indicates short, 1 indicates long
    private int carDir;// 0 indicates horizontal, 1 indicates vertical

    public Car(int carId, int carType, int carX, int carY, int carDir) throws HeadlessException {
        this.carID = carId;
        this.carX = carX;
        this.carY = carY;
        this.carType = carType;
        this.carDir = carDir;

        // generate color randomly
        Random color = new Random();
        if (carType == 0) {
            if (carDir == 0) {
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE * 2, CAR_SIZE);
                String imgPath = "img/cars/" + String.valueOf(carId == 1 ? 1 : color.nextInt(4) + 2) + ".png";
                this.setIcon(new ImageIcon(imgPath));
            } else {
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE, CAR_SIZE * 2);
                String imgPath = "img/cars/" + String.valueOf(color.nextInt(6) + 6) + ".png";
                this.setIcon(new ImageIcon(imgPath));
            }

        } else {
            if (carDir == 0) {
                String imgPath = "img/cars/" + String.valueOf(color.nextInt(2) + 12) + ".png";
                this.setIcon(new ImageIcon(imgPath));
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE * 3, CAR_SIZE);
            } else {
                String imgPath = "img/cars/" + String.valueOf(color.nextInt(2) + 14) + ".png";
                this.setIcon(new ImageIcon(imgPath));
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE, CAR_SIZE * 3);
            }
        }
    }

    public int getCarX() {
        return carX;
    }

    public int getCarY() {
        return carY;
    }

    public int getCarDir() {
        return carDir;
    }

    public int getCarID() {
        return carID;
    }

    public int getCarType() {
        return carType;
    }
}

