import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPage extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JLabel userLabel, passwordLabel, messageLabel;
    private JTextField userText;
    private JPasswordField passwordText;
    private JButton loginButton, resetButton;
    private ImageIcon backgroundImage;

    public LoginPage() {
        setTitle("Dawn's Grocery Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        backgroundImage = new ImageIcon("background_image.jpg");
        JLabel background = new JLabel(backgroundImage);
        background.setBounds(0, 0, 800, 600);
        add(background);

        userLabel = new JLabel("Username:");
        userLabel.setBounds(300, 200, 100, 25);
        background.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(400, 200, 200, 25);
        background.add(userText);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(300, 250, 100, 25);
        background.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(400, 250, 200, 25);
        background.add(passwordText);

        messageLabel = new JLabel();
        messageLabel.setBounds(400, 300, 200, 25);
        background.add(messageLabel);

        loginButton = new JButton("Login");
        loginButton.setBounds(350, 350, 100, 25);
        loginButton.addActionListener(this);
        background.add(loginButton);

        resetButton = new JButton("Reset");
        resetButton.setBounds(500, 350, 100, 25);
        resetButton.addActionListener(this);
        background.add(resetButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginButton) {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (username.equals("admin") && password.equals("admin")) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login successful.");
                HomePage homePage = new HomePage();
                homePage.setVisible(true);
                dispose();
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Invalid username or password.");
            }
        } else if (ae.getSource() == resetButton) {
            userText.setText("");
            passwordText.setText("");
            messageLabel.setText("");
        }
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
