import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SavingsCalculator extends JFrame {

    private JTextField salaryField, savingsField;
    private JTextArea resultArea;
    private static final String MONTH_CSV = "month.csv";
    private static final String FIXED_EXPENSE_CSV = "fixed_expenses.csv";

    public SavingsCalculator() {
        setTitle("Savings Calculator");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel salaryLabel = new JLabel("Monthly Salary:");
        salaryLabel.setBounds(50, 50, 150, 30);
        salaryField = new JTextField(10);
        salaryField.setBounds(200, 50, 150, 30);

        JLabel savingsLabel = new JLabel("Savings %:");
        savingsLabel.setBounds(50, 100, 150, 30);
        savingsField = new JTextField(10);
        savingsField.setBounds(200, 100, 150, 30);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(50, 150, 300, 30);
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateSavings();
            }
        });

        JButton btnNext = new JButton("Next »");
        btnNext.setBounds(50, 450, 400, 30);

        btnNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new ExpenseBarGraph().setVisible(true);
                dispose();
            }
        });

        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(50, 250, 400, 150);

        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setBounds(50, 200, 150, 30);

        panel.add(salaryLabel);
        panel.add(salaryField);
        panel.add(savingsLabel);
        panel.add(savingsField);
        panel.add(calculateButton);
        panel.add(resultLabel);
        panel.add(scrollPane);
        panel.add(btnNext);
        add(panel, BorderLayout.CENTER);
    }

    private void calculateSavings() {
        try {
          
            double salary = Double.parseDouble(salaryField.getText());
            double savingsPercentage = Double.parseDouble(savingsField.getText());

          
            double totalExpenses = getTotalExpenses(MONTH_CSV) + getFixedExpenses(FIXED_EXPENSE_CSV);

       
            double remainingMoney = salary - totalExpenses;

     
            double savingsGoal = (salary * savingsPercentage) / 100;

          
            String result = "Remaining Money: " + remainingMoney + "\n";
            result += "Savings Goal: " + savingsGoal + "\n";

            if (remainingMoney >= savingsGoal) {
                result += "You have achieved your savings goal!";
            } else {
                result += "You have not achieved your savings goal.";
            }

            resultArea.setText(result);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV files.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getTotalExpenses(String fileName) throws IOException {
        double total = 0.0;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
            
                String[] columns = line.split(",");
                if (columns.length > 1) {
                    try {
                        total += Double.parseDouble(columns[1].trim());
                    } catch (NumberFormatException e) {
            
                    }
                }
            }
        }
        return total;
    }

    private double getFixedExpenses(String fileName) throws IOException {
        double total = 0.0;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] columns = line.split(",");
                if (columns.length > 1) {
                    try {
                        total += Double.parseDouble(columns[1].trim());
                    } catch (NumberFormatException e) {

                    }
                }
            }
        }
        return total;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SavingsCalculator().setVisible(true);
            }
        });
    }
}