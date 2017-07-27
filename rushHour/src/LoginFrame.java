
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	// components in the login RegisterFrame
	private JButton login, register, exit;
	private JPanel usernamePanel;
	private JPanel passwordPanel;
	private JPanel buttonPanel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameField;
	private JPasswordField passwordField;

	public static void main(String[] args) {
		new LoginFrame();
	}

	public LoginFrame() {
		login = new JButton("Login");
		register = new JButton("Register");
		exit = new JButton("Exit");
		
		login.addActionListener(this);
		register.addActionListener(this);
		exit.addActionListener(this);

		usernameLabel = new JLabel("Username:");
		passwordLabel = new JLabel("Password:");

		usernameField = new JTextField(10);
		passwordField = new JPasswordField(10);

		usernamePanel = new JPanel();
		passwordPanel = new JPanel();
		buttonPanel = new JPanel();

		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameField);

		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);

		buttonPanel.add(login);
		buttonPanel.add(register);
		buttonPanel.add(exit);
		this.add(usernamePanel);
		this.add(passwordPanel);
		this.add(buttonPanel);

		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("RushHour Login");
		this.setLayout(new GridLayout(3, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(300, 200, 300, 180);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Exit") {
			System.exit(0);
		} else if (e.getActionCommand() == "Login") {
			this.login();
		} else if (e.getActionCommand() == "Register") {
			this.registration();
		}
	}

	public void registration() {
		this.dispose(); // close registration window
		new RegisterFrame(); // open login window
	}

	@SuppressWarnings("deprecation")
	public void login() {
		if (loginValidate(usernameField.getText(), passwordField.getText())) {
			new RushHour(RushHour.carLocation1, usernameField.getText());
			setVisible(false);
		} else {
			JOptionPane.showMessageDialog(null, "Wrong username or password!");
			passwordField.setText("");
		}

	}

	// check user name and password correctness
	public boolean loginValidate(String userid, String password) {
		if(userid == null || password == null)
			return false;
		File file = new File("user.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				String str[] = tempString.split("##");
				if (str[0].equals(userid) && str[1].equals(password)) {
					return true;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return false;
	}
}
