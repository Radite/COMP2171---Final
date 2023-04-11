import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class EditItem extends JFrame implements ActionListener {
    private JTextField upcField;
    private JTextField nameField;
    private JTextField unitsField;
    private JTextField unitPriceField;
    private JTextField manufacturerField;
    private JTextField categoryField;
    private JButton updateButton;

    public EditItem(String upc) {
        setTitle("Edit Item");

        // Set up labels and text fields
        setLayout(new GridLayout(7, 2));

        add(new JLabel("UPC:"));
        upcField = new JTextField(upc);
        add(upcField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Units:"));
        unitsField = new JTextField();
        add(unitsField);

        add(new JLabel("Unit Price:"));
        unitPriceField = new JTextField();
        add(unitPriceField);

        add(new JLabel("Manufacturer:"));
        manufacturerField = new JTextField();
        add(manufacturerField);

        add(new JLabel("Category:"));
        categoryField = new JTextField();
        add(categoryField);

        // Set up update button
        updateButton = new JButton("Update Item");
        updateButton.addActionListener(this);
        add(new JLabel(""));
        add(updateButton);

        try {
            // Load item details from the database
            String url = "jdbc:mysql://localhost:3306/DAWNS?autoReconnect=true&useSSL=false";
            String user = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT upc, name, units, unit_price, manufacturer, category FROM items WHERE upc = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, upc);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                unitsField.setText(rs.getString("units"));
                unitPriceField.setText(Double.toString(rs.getDouble("unit_price")));
                manufacturerField.setText(rs.getString("manufacturer"));
                categoryField.setText(rs.getString("category"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set JFrame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }



            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == updateButton) {
                    try {
                        String url = "jdbc:mysql://localhost:3306/DAWNS?autoReconnect=true&useSSL=false";
                        String user = "root";
                        String password = "";
                        Connection conn = DriverManager.getConnection(url, user, password);

                        // Check if another item with the same UPC already exists
                        String sql = "SELECT upc FROM items WHERE upc = ? AND upc <> ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, upcField.getText());
                        pstmt.setString(2, upcField.getText());
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            // Display an error message if a duplicate UPC exists
                            JOptionPane.showMessageDialog(this, "Another item with the same UPC already exists.");
                        } else {
                            // Check that units is a positive whole integer
                            int units = 0;
                            try {
                                units = Integer.parseInt(unitsField.getText());
                                if (units <= 0) {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Units must be a positive whole integer.");
                                return;
                            }

                            // Check that unit price is a valid number
                            double unitPrice = 0;
                            try {
                                unitPrice = Double.parseDouble(unitPriceField.getText());
                                if (unitPrice <= 0) {
                                    throw new NumberFormatException();
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Unit price must be a valid number.");
                                return;
                            }

                            // Update the item in the database
                            sql = "UPDATE items SET upc = ?, name = ?, units = ?, unit_price = ?, manufacturer = ?, category = ? WHERE upc = ?";
                            pstmt = conn.prepareStatement(sql);

                            pstmt.setString(1, upcField.getText());
                            pstmt.setString(2, nameField.getText());
                            pstmt.setInt(3, units);
                            pstmt.setDouble(4, unitPrice);
                            pstmt.setString(5, manufacturerField.getText());
                            pstmt.setString(6, categoryField.getText());
                            pstmt.setString(7, upcField.getText());
                            pstmt.executeUpdate();

                            conn.close();

                            JOptionPane.showMessageDialog(this, "Item updated successfully.");
                            this.dispose();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            public static void main(String[] args) {
                String upc = args[0];
                new EditItem(upc);
            }}

                   
