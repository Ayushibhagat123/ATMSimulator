package com.atm.swing;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ATMOperationsPage {
	 public static void showOperationsWindow(String username, String card) {
	        JFrame frame = new JFrame("Select Operation");
	        frame.setSize(300, 200);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLocationRelativeTo(null);

	        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

	        JButton withdrawBtn = new JButton("Withdraw");
	        JButton balanceBtn = new JButton("Balance Enquiry");
	        JButton changePinBtn = new JButton("Change PIN");
	        withdrawBtn.addActionListener(e ->{
	        	frame.dispose();
	        	WithdrawAmountPage.showWithdrawPage(username);});
	        balanceBtn.addActionListener(e ->{
	        	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atmsimulator", "root", "admin@123")) {
	                String sql = "SELECT balance FROM cards WHERE card_number = ?";
	                PreparedStatement ps = conn.prepareStatement(sql);
	                ps.setString(1, card);
	                ResultSet rs = ps.executeQuery();
	                
	                if (rs.next()) {
	                    double balance = rs.getDouble("balance");
	                    JOptionPane.showMessageDialog(null, "Available Balance: ₹" + balance);
	                } else {
	                    JOptionPane.showMessageDialog(null, "Error: Card not found.");
	                }

	            } catch (Exception ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	            }
	        });
	        changePinBtn.addActionListener(e -> {
	        	frame.dispose();
	        	EnterPinPage.enterPin(card);
	        });

	        panel.add(withdrawBtn);
	        panel.add(balanceBtn);
	        panel.add(changePinBtn);

	        frame.add(panel);
	        frame.setVisible(true);
	    }
}
