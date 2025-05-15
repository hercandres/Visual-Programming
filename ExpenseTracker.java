
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


class Expense {
    String name;
    double amount;
    boolean fixed;
    String month;


    Expense(String n,double a,boolean f){
        name=n;
        amount=a;
        fixed=f;
        month="";
    }
 
    Expense(String n,double a,boolean f,String m){
        name=n;
        amount=a;
        fixed=f;
        month=m;
    }
}


public class ExpenseTracker extends JFrame {

    JTextField txtName = new JTextField();
    JTextField txtAmount = new JTextField();
    JButton    btnAdd = new JButton("Add Fixed");

    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list  = new JList<>(model);
    JScrollPane scroll = new JScrollPane(list);

    JButton btnDelete = new JButton("Delete");
    JButton btnNext   = new JButton("Next »");

    ArrayList<Expense> fixed = new ArrayList<>();

    int w=500,h=30,g=15;
    final String CSV="fixed_expenses.csv";

    public ExpenseTracker(){
        super("Fixed Expenses");
        setSize(550,650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        init();
        loadCSV();  
        setVisible(true);
    }

    private void init(){
        JLabel l1=new JLabel("Name:");
        JLabel l2=new JLabel("Amount:");

        l1.setBounds(g,g,w,h);
        txtName.setBounds(g,g+h,w,h);
        l2.setBounds(g,g+2*h,w,h);
        txtAmount.setBounds(g,g+3*h,w,h);

        btnAdd.setBounds(g,g+4*h,w,h);
        scroll.setBounds(g,g+5*h,w,200);
        btnDelete.setBounds(g,g+5*h+210,w,h);
        btnNext.setBounds(g,g+6*h+210,w,h);

        add(l1);
        add(txtName);
        add(l2);
        add(txtAmount);
        add(btnAdd);
        add(scroll);
        add(btnDelete);
        add(btnNext);

        btnAdd.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ 
                addFixed(); }
        });
        btnDelete.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){ 
                del(); }
        });
        btnNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                saveCSV(); dispose();
                new MonthlyWindow(fixed);
            }
        });
    }

    private void addFixed(){
        String n=txtName.getText().trim();
        String a=txtAmount.getText().trim();
        if(n.isEmpty()||a.isEmpty()) return;
        try{
            double v=Double.parseDouble(a);
            fixed.add(new Expense(n,v,true));
            model.addElement(n+" - $"+v);
            txtName.setText(""); txtAmount.setText("");
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this,"Enter number.");
        }
    }

    private void del(){
        int i=list.getSelectedIndex();
        if(i>=0){ fixed.remove(i); model.remove(i); }
    }

    private void loadCSV(){
        File f=new File(CSV); if(!f.exists()) return;
        try(Scanner sc=new Scanner(f)){
            while(sc.hasNextLine()){
                String[] p=sc.nextLine().split(",",-1);
                if(p.length>=2){
                    fixed.add(new Expense(p[0],Double.parseDouble(p[1]),true));
                    model.addElement(p[0]+" - $"+p[1]);
                }
            }
        }catch(Exception e){}
    }
    private void saveCSV(){
        try(FileWriter w=new FileWriter(CSV)){
            for(Expense ex:fixed)
                w.write(ex.name+","+ex.amount+"\n");
        }catch(IOException e){}
    }

    public static void main(String[]a){
        new ExpenseTracker(); }
}
