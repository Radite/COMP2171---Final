import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;

public class HomePage extends JFrame implements ActionListener {
    private JTable table;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private DefaultTableModel model;
    

    public HomePage() {
        setTitle("Grocery Store Homepage");
        ImageIcon background = new ImageIcon("C:\\Users\\Peter\\eclipse-workspace\\Dawns\\src\\background_image.jpg");
        JLabel label = new JLabel(background);
        label.setBounds(0, 0, getWidth(), getHeight());

        add(label);

        try {
            // Establish database connection
            String url = "jdbc:mysql://localhost:3306/DAWNS?autoReconnect=true&useSSL=false";
            String user = "root";
            String password = "";
            Connection conn = DriverManager.getConnection(url, user, password);

            // Execute SQL query to retrieve all items from the database
            String sql = "SELECT upc, name, units, unit_price, manufacturer, category FROM items";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Create table model and add data to it
            model = new DefaultTableModel();
            model.addColumn("UPC");
            model.addColumn("Name");
            model.addColumn("Units");
            model.addColumn("Unit Price");
            model.addColumn("Manufacturer");
            model.addColumn("Category");
            while (rs.next()) {
                model.addRow(new Object[] { rs.getString("upc"), rs.getString("name"), rs.getString("units"),
                        rs.getDouble("unit_price"), rs.getString("manufacturer"), rs.getString("category") });
            }

            // Create JTable with the table model and add it to the JFrame
            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane);
            table.setDefaultEditor(Object.class, null);

            // Create JPanel to hold the three buttons and add it to the JFrame
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 3, 10, 0)); // 1 row, 3 columns, 10px horizontal gap
            addButton = new JButton("Add New Item");
            addButton.addActionListener(this);
            buttonPanel.add(addButton);
            deleteButton = new JButton("Delete Selected Item");
            deleteButton.addActionListener(this);
            buttonPanel.add(deleteButton);
            editButton = new JButton("Edit Selected Item");
            editButton.addActionListener(this);
            buttonPanel.add(editButton);
            add(buttonPanel, BorderLayout.SOUTH);

            // Create JTabbedPane and add it to the JFrame
            JTabbedPane tabbedPane = new JTabbedPane();
            JPanel inventoryPanel = new JPanel();
            inventoryPanel.add(scrollPane);
            tabbedPane.addTab("Inventory", inventoryPanel);
            JPanel accountingPanel = new JPanel();
            JButton accountingButton = new JButton("Open Accounting Record");
            accountingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new AccountingRecord();
                }
            });
            accountingPanel.add(accountingButton);
            tabbedPane.addTab("Accounting Record", accountingPanel);
            add(tabbedPane, BorderLayout.WEST);

            JPanel salePanel = new JPanel();
            JButton saleButton = new JButton("Create A Sale");
            saleButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new CreateSale();
                }
            });
            salePanel.add(saleButton);
            tabbedPane.addTab("Create Sale", salePanel);
            add(tabbedPane, BorderLayout.WEST);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        

        // Set JFrame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == addButton) {
    	// Call the AddItem file to add a new item to the database when the JButton is clicked
    	NewItem.main(new String[0]);
    	} else if (e.getSource() == deleteButton) {
    	// Delete selected item from the database and remove it from the table model
    	int selectedRow = table.getSelectedRow();
    	if (selectedRow != -1) { // if a row is selected
    	String upc = (String) model.getValueAt(selectedRow, 0);
    	try {
    	String url = "jdbc:mysql://localhost:3306/DAWNS?autoReconnect=true&useSSL=false";
    	String user = "root";
    	String password = "";
    	Connection conn = DriverManager.getConnection(url, user, password);
        String sql = "DELETE FROM items WHERE upc = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, upc);
        pstmt.executeUpdate();

        model.removeRow(selectedRow);

        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
} else if (e.getSource() == editButton) {
int selectedRow = table.getSelectedRow();
if (selectedRow != -1) { // if a row is selected
    // Call the EditItem file to edit the selected item's information
    String upc = (String) model.getValueAt(selectedRow, 0);
    EditItem.main(new String[] { upc });
}
} 

}

public static void main(String[] args) {
new HomePage();
}}

