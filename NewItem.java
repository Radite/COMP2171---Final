import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class NewItem extends JFrame implements ActionListener {
    private JPanel panel;
    private JLabel upcLabel;
    private JLabel nameLabel;
    private JLabel priceLabel;
    private JLabel unitsLabel;
    private JLabel manufacturerLabel;
    private JLabel categoryLabel;
    private JTextField upcTextField;
    private JTextField nameTextField;
    private JTextField priceTextField;
    private JTextField unitsTextField;
    private JTextField manufacturerTextField;
    private JTextField categoryTextField;
    private JButton saveButton;

    public NewItem() {
        setTitle("Add New Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);

        panel = new JPanel(new GridLayout(7, 2));
        upcLabel = new JLabel("UPC:");
        nameLabel = new JLabel("Name:");
        priceLabel = new JLabel("Price:");
        unitsLabel = new JLabel("Units:");
        manufacturerLabel = new JLabel("Manufacturer:");
        categoryLabel = new JLabel("Category:");
        upcTextField = new JTextField();
        nameTextField = new JTextField();
        priceTextField = new JTextField();
        unitsTextField = new JTextField();
        manufacturerTextField = new JTextField();
        categoryTextField = new JTextField();
        saveButton = new JButton("Save");
        
        panel.add(upcLabel);
        panel.add(upcTextField);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(priceLabel);
        panel.add(priceTextField);
        panel.add(unitsLabel);
        panel.add(unitsTextField);
        panel.add(manufacturerLabel);
        panel.add(manufacturerTextField);
        panel.add(categoryLabel);
        panel.add(categoryTextField);
        panel.add(saveButton);

        saveButton.addActionListener(this);

        add(panel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/DAWNS?autoReconnect=true&useSSL=false", "root", "");
                
                // Check if the UPC already exists in the table
                String checkSql = "SELECT * FROM items WHERE upc = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, upcTextField.getText());
                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "An item with the same UPC already exists.");
                    return; // stop the execution
                }

                // Check if the Units value is a whole number
                if (!unitsTextField.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Units must be a whole number.");
                    return; // stop the execution
                }

                // If the UPC and Units values are valid, insert the new item into the table
                String sql = "INSERT INTO items (UPC, Name, Units, `unit_price`, Manufacturer, Category) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, upcTextField.getText());
                statement.setString(2, nameTextField.getText());
                statement.setString(3, unitsTextField.getText());
                statement.setDouble(4, Double.parseDouble(priceTextField.getText()));
                statement.setString(5, manufacturerTextField.getText());
                statement.setString(6, categoryTextField.getText());
                statement.executeUpdate();

                statement.close();
                connection.close();
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price value");
            }
        }
    }

    public static void main(String[] args) {
        NewItem newItem = new NewItem();
        newItem.setVisible(true);
    }
}
