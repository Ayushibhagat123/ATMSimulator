package com.atm.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

public class ATMMain {

    public static void main(String[] args) {
        JFrame frame = new JFrame("ATM Simulator");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 4));
        JTextField cardNumber = new JTextField();
        JTextField username = new JTextField();
        JDateChooser expiryChooser = new JDateChooser();
        expiryChooser.setDateFormatString("yyyy-MM-dd");
        JTextField cvv = new JTextField();
        JComboBox<String> languageBox = new JComboBox<>(new String[]{"English", "Hindi"});
        JComboBox<String> accountTypeBox = new JComboBox<>(new String[]{"Savings", "Current", "Deposit"});

        formPanel.add(new JLabel("Card Number:"));
        formPanel.add(cardNumber);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(username);
        formPanel.add(new JLabel("Expiry (yyyy-mm-dd):"));
        formPanel.add(expiryChooser);
        formPanel.add(new JLabel("CVV:"));
        formPanel.add(cvv);
        formPanel.add(new JLabel("Language:"));
        formPanel.add(languageBox);
        formPanel.add(new JLabel("Account Type:"));
        formPanel.add(accountTypeBox);

        JButton login = new JButton("Proceed");
        login.addActionListener(e -> {
            String card = cardNumber.getText();
            String user = username.getText();
            Date date = expiryChooser.getDate();
            String exp = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String code = cvv.getText();

            try {
                String url = "jdbc:mysql://localhost:3306/atmsimulator";
                String dbUser = "root";
                String dbPass = "admin@123";
                
                Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
                String sql = "SELECT * FROM cards WHERE card_number=? AND username=? AND expiry_date=? AND cvv=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, card);
                ps.setString(2, user);
                ps.setString(3, exp);
                ps.setString(4, code);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    frame.dispose();
                    ATMOperationsPage.showOperationsWindow(user,card);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid card details.");
                }

                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(login, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
