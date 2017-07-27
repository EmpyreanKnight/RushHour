
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class RegisterFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	
	private JPanel panel;
	private JLabel usernameLabel;
	private JLabel passwordLabel1;
	private JLabel passwordLabel2;
	private JTextField usernameField;
	private JPasswordField passwordField1;
	private JPasswordField passwordField2;
	private JButton back;
	private JButton register;

	public RegisterFrame() {
		panel = new JPanel();
		usernameLabel = new JLabel("Enter  username:");
		passwordLabel1 = new JLabel("Enter  password:");
		passwordLabel2 = new JLabel("Repeat password:");
		usernameField = new JTextField(10);
		passwordField1 = new JPasswordField(10);
		passwordField2 = new JPasswordField(10);
		usernameField.setToolTipText("Username must be 3~6 digit alphabet or number.");
		passwordField1.setToolTipText("Password must be 3~6 digit alphabet or number.");
		passwordField2.setToolTipText("Please repeat the password.");

		back = new JButton("Back");
		register = new JButton("Register");
		back.addActionListener(this);
		register.addActionListener(this);

		panel.setLayout(new GridLayout(5, 2));

		panel.add(usernameLabel);
		panel.add(usernameField);

		panel.add(passwordLabel1);
		panel.add(passwordField1);
		panel.add(passwordLabel2);
		panel.add(passwordField2);

		panel.add(back);
		panel.add(register);

		this.add(panel);
		this.setTitle("RushHour Register");
		this.setBounds(200, 100, 250, 150);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Back") {
			this.dispose();
			new LoginFrame();

		} else if (e.getActionCommand() == "Register") {
			this.register();
		}
	}

	@SuppressWarnings("deprecation")
	public void register() {
		String regex1 = "\\w{3,6}"; // limit userName to 3~6 digit
		boolean flag1 = usernameField.getText().matches(regex1);

		String regex2 = "\\w{6}"; // // limit password to 6 digit
		boolean flag2 = passwordField1.getText().matches(regex2);
		boolean flag3 = passwordField2.getText().matches(regex2);

		if (!flag1) {
			JOptionPane.showMessageDialog(null, "Username must be 3~6 digit alphabet or number!",
					"Warning", JOptionPane.WARNING_MESSAGE);
			usernameField.setText("");
			return;
		} else if (!flag2 || !flag3) {
			JOptionPane.showMessageDialog(null, "Password must be 3~6 digit alphabet or number!",
					"Warning", JOptionPane.WARNING_MESSAGE);
			passwordField1.setText("");
			passwordField2.setText("");
			return;
		} else if(!passwordField1.getText().equals(passwordField2.getText())) {
			JOptionPane.showMessageDialog(null, "Input password not same!",
					"Warning", JOptionPane.WARNING_MESSAGE);
			passwordField1.setText("");
			passwordField2.setText("");
			return;
		}
		try {
			File file = new File("user.txt");
			confirmFile(file);
			
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					if(tempString.indexOf(usernameField.getText()) != -1) {
						JOptionPane.showMessageDialog(null, "Username already be registered!",
								"Warning", JOptionPane.WARNING_MESSAGE);
						passwordField1.setText("");
						passwordField2.setText("");
						return;
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
			
			try {
				FileWriter writer = new FileWriter("user.txt", true);
				writer.write(usernameField.getText() + "##" + passwordField1.getText() + "\r\n");
				writer.close();
				JOptionPane.showMessageDialog(null, "Register succeed");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.dispose();
		new LoginFrame();
	}

	// create record file when open game in the first time
	private void confirmFile(File fileName) {
		try {
			if (!fileName.exists()) {
				fileName.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
