import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ExpenseBarGraph extends JFrame {

    private static final String MONTH_CSV = "month.csv";
    private static final String FIXED_EXPENSE_CSV = "fixed_expenses.csv";
    private double totalFixedExpenses = 0.0;
    private double[] monthlyExpenses = new double[12];

    public ExpenseBarGraph() {
        setTitle("Monthly Expense Bar Graph");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadFixedExpenses();
        loadMonthlyExpenses();
        add(new BarGraphPanel());

        setVisible(true);
    }

    private void loadFixedExpenses() {
        File file = new File(FIXED_EXPENSE_CSV);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    double amount = Double.parseDouble(parts[1]);
                    totalFixedExpenses += amount;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading fixed_expenses.csv", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMonthlyExpenses() {
        File file = new File(MONTH_CSV);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",", -1);
                if (parts.length == 3) {
                    String month = parts[2];
                    double amount = Double.parseDouble(parts[1]);

                    if (month.equalsIgnoreCase("January")) {
                        monthlyExpenses[0] += amount;
                    } else if (month.equalsIgnoreCase("February")) {
                        monthlyExpenses[1] += amount;
                    } else if (month.equalsIgnoreCase("March")) {
                        monthlyExpenses[2] += amount;
                    } else if (month.equalsIgnoreCase("April")) {
                        monthlyExpenses[3] += amount;
                    } else if (month.equalsIgnoreCase("May")) {
                        monthlyExpenses[4] += amount;
                    } else if (month.equalsIgnoreCase("June")) {
                        monthlyExpenses[5] += amount;
                    } else if (month.equalsIgnoreCase("July")) {
                        monthlyExpenses[6] += amount;
                    } else if (month.equalsIgnoreCase("August")) {
                        monthlyExpenses[7] += amount;
                    } else if (month.equalsIgnoreCase("September")) {
                        monthlyExpenses[8] += amount;
                    } else if (month.equalsIgnoreCase("October")) {
                        monthlyExpenses[9] += amount;
                    } else if (month.equalsIgnoreCase("November")) {
                        monthlyExpenses[10] += amount;
                    } else if (month.equalsIgnoreCase("December")) {
                        monthlyExpenses[11] += amount;
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading month.csv", "Error", JOptionPane.ERROR_MESSAGE);
        }

        for (int i = 0; i < 12; i++) {
            monthlyExpenses[i] += totalFixedExpenses;
        }
    }

    class BarGraphPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int barWidth = 50;
            int gap = 30;
            int x = 100;
            int maxBarHeight = 400;
            int bottomY = 500;

            double maxExpense = 0;
            for (int i = 0; i < 12; i++) {
                if (monthlyExpenses[i] > maxExpense) {
                    maxExpense = monthlyExpenses[i];
                }
            }

            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Monthly Expenses (Fixed + Variable)", 300, 50);

            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            for (int i = 0; i < 12; i++) {
                double total = monthlyExpenses[i];

                int barHeight = (int) ((total / maxExpense) * maxBarHeight);

                g.setColor(new Color(70, 130, 180));
                g.fillRect(x, bottomY - barHeight, barWidth, barHeight);

                g.setColor(Color.BLACK);
                g.drawString(months[i], x + 10, bottomY + 20);

                g.drawString("$" + String.format("%.2f", total), x + 10, bottomY - barHeight - 10);

                x += barWidth + gap;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseBarGraph());
    }
}
