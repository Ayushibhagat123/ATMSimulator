package com.atm.swing;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WithdrawAmountPage {
    public static void showWithdrawPage(String username) {
        JFrame frame = new JFrame("Withdraw Amount");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Enter Amount:"));

        JTextField amountField = new JTextField();
        panel.add(amountField);

        JButton nextButton = new JButton("Next");
        panel.add(new JLabel());
        panel.add(nextButton);

        nextButton.addActionListener(e -> {
            String amountStr = amountField.getText();
            if (amountStr.matches("\\d+")) {
                frame.dispose();
                EnterPinPage.showPinPage(username, Integer.parseInt(amountStr));
            } else {
                JOptionPane.showMessageDialog(frame, "Enter a valid amount");
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
