package com.atm.swing;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EnterPinPage {

    public static void showPinPage(String username, int amount) {
        JFrame frame = new JFrame("Enter PIN");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Enter PIN:"));

        JPasswordField pinField = new JPasswordField();
        panel.add(pinField);

        JButton enterButton = new JButton("Enter");
        panel.add(new JLabel());
        panel.add(enterButton);

        enterButton.addActionListener(e -> {
            String pin = new String(pinField.getPassword());
            if (pin.matches("\\d{4}")) {
                boolean success = validateAndWithdraw(username, pin, amount);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Withdrawal successful!");
                    frame.dispose();
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to see your balance?", "Show Balance", JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                    	try (Connection conn = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/atmsimulator", "root", "admin@123")) {

                            PreparedStatement ps = conn.prepareStatement(
                                    "SELECT balance FROM cards WHERE username=? AND pin=?");
                            ps.setString(1, username);
                            ps.setString(2, pin);
                            ResultSet rs = ps.executeQuery();
                            if (rs.next()) {
								double updatedBalance = rs.getDouble("balance");
								JOptionPane.showMessageDialog(null, "Available Balance: ₹" + updatedBalance);
								frame.dispose();
								ATMMain.main(null);
							}
                    	}catch (Exception e2) {
                    		e2.printStackTrace();
						}
                    } else {
                    	frame.dispose();
                    	new ATMMain();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid PIN or insufficient balance.");
                    frame.dispose();
                    ATMMain.main(null);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Enter 4-digit PIN");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
    
    public static void enterPin(String card) {
        JFrame frame = new JFrame("Enter PIN");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Enter Current PIN:"));

        JPasswordField pinField = new JPasswordField();
        panel.add(pinField);

        JButton enterButton = new JButton("Enter");
        panel.add(new JLabel()); // filler
        panel.add(enterButton);

        enterButton.addActionListener(e -> {
            String pin = new String(pinField.getPassword());
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/atmsimulator", "root", "admin@123")) {

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT pin FROM cards WHERE card_number=? AND pin=?");
                ps.setString(1, card);
                ps.setString(2, pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    frame.dispose(); // close the first frame

                    JFrame frame2 = new JFrame("Change PIN");
                    frame2.setSize(300, 180);
                    frame2.setLocationRelativeTo(null);

                    JPanel panel2 = new JPanel(new GridLayout(3, 2, 10, 10));
                    panel2.add(new JLabel("New PIN:"));
                    JPasswordField newPin1 = new JPasswordField();
                    panel2.add(newPin1);

                    panel2.add(new JLabel("Re-enter New PIN:"));
                    JPasswordField newPin2 = new JPasswordField();
                    panel2.add(newPin2);

                    JButton changeButton = new JButton("Change");
                    panel2.add(new JLabel()); // filler
                    panel2.add(changeButton);

                    changeButton.addActionListener(c -> {
                        String np1 = new String(newPin1.getPassword());
                        String np2 = new String(newPin2.getPassword());

                        if (!np1.equals(np2)) {
                            JOptionPane.showMessageDialog(frame2, "PINs do not match!");
                            return;
                        }

                        try (Connection con = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/atmsimulator", "root", "admin@123")) {

                            PreparedStatement pst = con.prepareStatement(
                                    "UPDATE cards SET pin = ? WHERE card_number = ?");
                            pst.setString(1, np1);
                            pst.setString(2, card);

                            int updated = pst.executeUpdate();
                            if (updated > 0) {
                                JOptionPane.showMessageDialog(frame2, "PIN Updated Successfully.");
                                frame2.dispose();
                            } else {
                                JOptionPane.showMessageDialog(frame2, "Failed to update PIN.");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame2, "Error: " + ex.getMessage());
                        }
                    });

                    frame2.add(panel2);
                    frame2.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect PIN.");
                }

            } catch (Exception e3) {
                e3.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + e3.getMessage());
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

    private static boolean validateAndWithdraw(String username, String pin, int amount) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/atmsimulator", "root", "admin@123")) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT balance FROM cards WHERE username=? AND pin=?");
            ps.setString(1, username);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int balance = rs.getInt("balance");
                if (balance >= amount) {
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE cards SET balance = balance - ? WHERE username=? AND pin=?");
                    update.setInt(1, amount);
                    update.setString(2, username);
                    update.setString(3, pin);
                    update.executeUpdate();
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
