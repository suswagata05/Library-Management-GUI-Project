

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookManagementForm extends JFrame {
    JTextField titleField, authorField, yearField;
    private final JButton addButton;
    private final JButton updateButton;
    private final JButton deleteButton;
    private final JButton viewButton;


    public BookManagementForm() {
        setTitle("Book Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        titleField = new JTextField(20);
        authorField = new JTextField(20);
        yearField = new JTextField(20);

        addButton = new JButton("Add Book");
        updateButton = new JButton("Update Book");
        deleteButton = new JButton("Delete Book");
        viewButton = new JButton("View Books");

        // add these elements in the panel
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(viewButton);

        // make events here -- lets call these methods now on the basis of events
        addButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        updateButton.addActionListener((ActionEvent e) -> {
            updateBook();
        });

        deleteButton.addActionListener((ActionEvent e) -> {
            deleteBook();
        });

        viewButton.addActionListener((ActionEvent e) -> {
            viewBooks();
        });

        add(panel);

    }

    //  Add book
    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        int year = Integer.parseInt(yearField.getText());

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, year);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book Added Successfully!");  // alert
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    // update
    private void updateBook() {
        // Prompt the user to enter the ID
        String idInput = JOptionPane.showInputDialog(this, "Enter Book ID:", "Update Book",
                JOptionPane.QUESTION_MESSAGE);

        if (idInput == null || idInput.trim().isEmpty()) {
            return; // If the user cancels or leaves the input empty, do nothing
        }

        try {
            int id = Integer.parseInt(idInput); // Parse the ID

            String title = titleField.getText();
            String author = authorField.getText();
            int year = Integer.parseInt(yearField.getText());

            try (Connection conn = DatabaseConnection.getConnection()) {
                String updateSql = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, title);
                updateStmt.setString(2, author);
                updateStmt.setInt(3, year);
                updateStmt.setInt(4, id);

                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Book Updated Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Book with ID " + id + " not found!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID! Please enter a number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // delete
    private void deleteBook() {
        // Prompt the user to enter the ID
        String idInput = JOptionPane.showInputDialog(this, "Enter Book ID:", "Delete Book",
                JOptionPane.QUESTION_MESSAGE);

        if (idInput == null || idInput.trim().isEmpty()) {
            return; // If the user cancels or leaves the input empty, do nothing
        }

        try {
            int id = Integer.parseInt(idInput); // Parse the ID

            try (Connection conn = DatabaseConnection.getConnection()) {
                String deleteSql = "DELETE FROM books WHERE id = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
                deleteStmt.setInt(1, id);

                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Book Deleted Successfully!");
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Book with ID " + id + " not found!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID! Please enter a number.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // view 

     private void viewBooks() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books";  // fetch all books
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder books = new StringBuilder();
            while (rs.next()) {
                books.append("ID: ").append(rs.getInt("id"))
                        .append(", Title: ").append(rs.getString("title"))
                        .append(", Author: ").append(rs.getString("author"))
                        .append(", Year: ").append(rs.getInt("year"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, books.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        yearField.setText("");
    }

}

