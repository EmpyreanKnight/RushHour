import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Car extends JButton {
    final static int CAR_SIZE = 80; // the width of each block

    private int carID; // Number of cars, target car always numbered as 1
    private int carX;
    private int carY;
    private int carType;// 0 indicates short, 1 indicates long
    private int carDir;// 0 indicates horizontal, 1 indicates vertical

    Car(int carId, int carType, int carX, int carY, int carDir) throws HeadlessException {
        this.carID = carId;
        this.carX = carX;
        this.carY = carY;
        this.carType = carType;
        this.carDir = carDir;

        // generate car image randomly
        Random random = new Random();
        // car image name: (carType)(carDir)(number).png
        // 000 for target car, 001~004, 011~016, 101~102, 111~112
        String imgPath = "./img/cars/" + carType + carDir +
                (carId == 1 ? 0 : random.nextInt(
                        carType == 0 ? (carDir == 0 ? 4 : 6) : 2) + 1)
                + ".png";
        setIcon(new ImageIcon(imgPath));
        setPosition();
    }

    private void setPosition() {
        if (carType == 0) {
            if (carDir == 0) {
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE * 2, CAR_SIZE);
            } else {
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE, CAR_SIZE * 2);
            }
        } else {
            if (carDir == 0) {
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE * 3, CAR_SIZE);
            } else {
                this.setBounds(270 + carX * CAR_SIZE, carY * CAR_SIZE - 27, CAR_SIZE, CAR_SIZE * 3);
            }
        }
    }

    void setPosition(int x, int y) {
        carX = x;
        carY = y;
        setPosition();
    }

    int getCarX() {
        return carX;
    }

    int getCarY() {
        return carY;
    }

    int getCarDir() {
        return carDir;
    }

    int getCarID() {
        return carID;
    }

    int getCarType() {
        return carType;
    }
}

