import com.mysql.cj.jdbc.DatabaseMetaData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class GUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;

    private JTextField regNameField, regDescriptionField, regSumField, regDataField;

    public GUI() {

        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();

        String url = "jdbc:mysql://localhost:3306/your_dir";
        String userid = "your_usr";
        String password = "your_pass";

        try {
            connection = DriverManager.getConnection( url, userid, password );
        } catch (SQLException e) {
            e.printStackTrace();
        }

        setTitle("Izdevumu Reistrēšana");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Panels
        createRegisterPanel();
        createStartPanel();
        createBeginPanel();

        add(cardPanel);
        cardLayout.show(cardPanel, "Start");

    }

    // Method to load data from the database with a custom SQL query
    public void loadDataFromDatabase(String sql) {

        // SQL connection to the table
        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();

        String url = "jdbc:mysql://localhost:3306/your_dir";
        String userid = "your_usr";
        String password = "your_pass";

        try (Connection connection = DriverManager.getConnection( url, userid, password );
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery( sql ))
        {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            for (int i = 1; i <= columns; i++)
            {
                columnNames.add( md.getColumnName(i) );
            }

            while (rs.next())
            {
                ArrayList row = new ArrayList(columns);

                for (int i = 1; i <= columns; i++)
                {
                    row.add( rs.getObject(i) );
                }

                data.add( row );
            }
        }
        catch (SQLException e)
        {
            System.out.println( e.getMessage() );
        }

        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++)
        {
            ArrayList subArray = (ArrayList)data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++)
            {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }

        for (int i = 0; i < columnNames.size(); i++ )
            columnNamesVector.add(columnNames.get(i));

        // Update the table model with the new data
        tableModel.setDataVector(dataVector, columnNamesVector);

    }

    // Deletion method
    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {

            int idToDelete = (int) tableModel.getValueAt(selectedRow, 0);

            try {
                PreparedStatement pstmt = connection.prepareStatement("DELETE FROM expenses WHERE id = ?");
                pstmt.setInt(1, idToDelete);
                pstmt.executeUpdate();

                // Remove the row from the table model
                tableModel.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lūdzu izvēlēties dzēšamo rindu");
        }
    }

    // Start panel
    private void createStartPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Begin button
        JButton startButton = new JButton("Sākt darbību");
        startButton.addActionListener(e -> cardLayout.show(cardPanel, "Beginning"));
        panel.add(startButton, BorderLayout.CENTER);

        cardPanel.add(panel, "Start");

    }

    // Beginning panel
    private void createBeginPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel nameLabel = new JLabel("Izdevumi");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        panel.add(nameLabel, BorderLayout.NORTH);

        // Toolbar
        JToolBar toolBar = new JToolBar("Funkcijas");
        panel.add(toolBar, BorderLayout.NORTH);

        // Delete button
        JButton deleteButton = new JButton("Dzēst");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });
        toolBar.add(deleteButton);

        // Group by ID
        JButton groupidButton = new JButton("Grupēt pēc ID");
        groupidButton.addActionListener(e ->loadDataFromDatabase("SELECT * FROM EXPENSES ORDER BY ID ASC"));
        toolBar.add(groupidButton);

        // Group by sum
        JButton groupsmButton = new JButton("Grupēt pēc summas");
        groupsmButton.addActionListener(e ->loadDataFromDatabase("SELECT * FROM EXPENSES ORDER BY SUM ASC"));
        toolBar.add(groupsmButton);

        // Group by date
        JButton groupdtButton = new JButton("Grupēt pēc datuma");
        groupdtButton.addActionListener(e ->loadDataFromDatabase("SELECT * FROM EXPENSES ORDER BY DATE ASC"));
        toolBar.add(groupdtButton);

        // Sum up
        JButton sumButton = new JButton("Kopēja summa");
        sumButton.addActionListener(e ->loadDataFromDatabase("SELECT SUM(sum) AS TotalSum FROM expenses"));
        toolBar.add(sumButton);

        // Register button
        JButton registerButton = new JButton("Reģistrēt jaunu izdevumu");
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Registration"));
        panel.add(registerButton, BorderLayout.SOUTH);

        // Table
        String strSql = "SELECT * FROM EXPENSES";
        tableModel = new DefaultTableModel();
        loadDataFromDatabase(strSql);
        table = new JTable(tableModel)
        {
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };
        JScrollPane scrollPane = new JScrollPane( table );
        panel.add(scrollPane, BorderLayout.CENTER);

        cardPanel.add(panel, "Beginning");
    }

    // Registration panel
    private void createRegisterPanel() {
        JPanel panel = new JPanel(null);

        // izdevumu nosaukums - name
        JLabel nameLabel = new JLabel("Nosaukums: ");
        nameLabel.setBounds(50, 20, 80, 25);
        panel.add(nameLabel);

        regNameField = new JTextField();
        regNameField.setBounds(140, 20, 200, 25);
        panel.add(regNameField);

        // izdevumu apraksts - description
        JLabel descriptionLabel = new JLabel("Apraksts: ");
        descriptionLabel.setBounds(50, 60, 80, 25);
        panel.add(descriptionLabel);

        regDescriptionField = new JTextField();
        regDescriptionField.setBounds(140, 60, 200, 25);
        panel.add(regDescriptionField);

        // izdevumu summa - sum
        JLabel sumLabel = new JLabel("Summa: ");
        sumLabel.setBounds(50, 100, 80, 25);
        panel.add(sumLabel);

        regSumField = new JTextField();
        regSumField.setBounds(140, 100, 200, 25);
        panel.add(regSumField);

        // izdevumu datums - date
        JLabel dateLabel = new JLabel("Datums: ");
        dateLabel.setBounds(50, 140, 80, 25);
        panel.add(dateLabel);

        regDataField = new JTextField();
        regDataField.setBounds(140, 140, 200, 25);
        panel.add(regDataField);

        // Save button
        JButton saveButton = new JButton("Saglabāt");
        saveButton.setBounds(50, 190, 100, 25);
        saveButton.addActionListener(e -> saveExpense());
        panel.add(saveButton);

        // Return button
        JButton backToBeginningButton = new JButton("Atpakaļ");
        backToBeginningButton.setBounds(200, 190, 100, 25);
        backToBeginningButton.addActionListener(e -> cardLayout.show(cardPanel, "Beginning"));

        panel.add(backToBeginningButton);

        cardPanel.add(panel, "Registration");
    }

    // Expense registration
    private void saveExpense() {

        String name = regNameField.getText();
        String description = regDescriptionField.getText();
        String sum = regSumField.getText();
        String date = regDataField.getText();

        // Validate registration
        if (name.isEmpty() || description.isEmpty() || sum.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lūdzu aizpildiet visus laukus", "Reģistrācījas kļūda", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Try to register
        if (InputManager.register(name, description, sum, date)) {
            JOptionPane.showMessageDialog(this, "Reģistrācija veiksmīga! Jūsu izdevums ir saglabāts!", "Veiksmīgi", JOptionPane.INFORMATION_MESSAGE);
            loadDataFromDatabase("SELECT * FROM EXPENSES");
            cardLayout.show(cardPanel, "Beginning");
        } else {
            JOptionPane.showMessageDialog(this, "Reģistrācija neizdevās! Mēģiniet vēlreiz!", "Kļūda", JOptionPane.ERROR_MESSAGE);

        }
    }

}