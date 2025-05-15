import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MonthlyWindow extends JFrame {

    ArrayList<Expense> fixed;
    ArrayList<Expense> monthList = new ArrayList<>();

    JTextField txtName = new JTextField();
    JTextField txtAmount = new JTextField();
    JTextField txtMonth = new JTextField();
    JButton btnAdd = new JButton("Add Expense");
    JButton btnNext = new JButton("Next »");

    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    JScrollPane scroll = new JScrollPane(list);

    JButton btnDel = new JButton("Delete");

    JLabel lblSum = new JLabel("");

    int w = 500, h = 30, g = 15;
    final String CSV = "month.csv";

    public MonthlyWindow(ArrayList<Expense> fx) {
        super("Monthly Expenses");
        fixed = fx;
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        init();
        loadCSV();
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveCSV();
                System.exit(0);
            }
        });
    }

    private void init() {
        JLabel l1 = new JLabel("Name:");
        JLabel l2 = new JLabel("Amount:");
        JLabel l3 = new JLabel("Month:");

        l1.setBounds(g, g, w, h);
        txtName.setBounds(g, g + h, w, h);
        l2.setBounds(g, g + 2 * h, w, h);
        txtAmount.setBounds(g, g + 3 * h, w, h);
        l3.setBounds(g, g + 4 * h, w, h);
        txtMonth.setBounds(g, g + 5 * h, w, h);

        btnAdd.setBounds(g, g + 6 * h, w, h);
        scroll.setBounds(g, g + 7 * h, w, 200);
        btnDel.setBounds(g, g + 7 * h + 210, w, h);
        lblSum.setBounds(g, g + 8 * h + 210, w, h * 2);

        btnNext.setBounds(g, g + 10 * h + 210, w, h);

        add(l1);
        add(txtName);
        add(l2);
        add(txtAmount);
        add(l3);
        add(txtMonth);
        add(btnAdd);
        add(scroll);
        add(btnDel);
        add(lblSum);
        add(btnNext);

        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExp();
            }
        });
        btnDel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                del();
            }
        });
        btnNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new SavingsCalculator().setVisible(true);
                dispose();
            }
        });
    }

    private void addExp() {
        String n = txtName.getText().trim();
        String a = txtAmount.getText().trim();
        String m = txtMonth.getText().trim();
        if (n.isEmpty() || a.isEmpty() || m.isEmpty()) return;
        try {
            double v = Double.parseDouble(a);
            monthList.add(new Expense(n, v, false, m));
            model.addElement(m + " | " + n + " - $" + v);
            txtName.setText("");
            txtAmount.setText("");
            txtMonth.setText("");
            updateSum();
            saveCSV();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter number.");
        }
    }

    private void del() {
        int i = list.getSelectedIndex();
        if (i >= 0) {
            monthList.remove(i);
            model.remove(i);
            updateSum();
            saveCSV();
        }
    }

    private void updateSum() {
        double tot = 0;
        for (Expense e : monthList) tot += e.amount;
        lblSum.setText("Total variable: $" + tot);
    }

    private void loadCSV() {
        File f = new File(CSV);
        if (!f.exists()) return;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(",", -1);
                if (p.length == 3) {
                    monthList.add(new Expense(p[0], Double.parseDouble(p[1]), false, p[2]));
                    model.addElement(p[2] + " | " + p[0] + " - $" + p[1]);
                }
            }
            updateSum();
        } catch (Exception e) {
        }
    }

    private void saveCSV() {
        try (FileWriter w = new FileWriter(CSV)) {
            for (Expense ex : monthList) {
                w.write(ex.name + "," + ex.amount + "," + ex.month + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
