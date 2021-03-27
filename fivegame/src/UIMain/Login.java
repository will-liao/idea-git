package UIMain;

import dao.Dao;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Login extends JFrame implements ActionListener {
    JFrame login = new JFrame("512的五子棋游戏");
    JPanel login_p1 = new JPanel();
    JPanel login_p1_1 = new JPanel();
    JPanel login_p1_2 = new JPanel();
    JPanel login_p1_3 = new JPanel();

    JButton login_b1 = new JButton("登录");
    JButton login_b2 = new JButton("注册");

    JLabel u1 = new JLabel("                          ");
    JLabel u2 = new JLabel("                               ");
    JLabel u3 = new JLabel("                                              512欢迎您！                                        ");
    JLabel u5 = new JLabel("                                                                                               ");
    JLabel u6 = new JLabel("                                                                                               ");
    JLabel j1 = new JLabel("                                                                       账号");
    JLabel j2 = new JLabel("                                                                       密码");
    JLabel u4 = new JLabel("                ");

    public static JTextField text1 = new JTextField(10);
    JPasswordField text2 = new JPasswordField(10);

    public Login(){
        init();
    };
    public void init(){
        login.setSize(400,500);
        login.setLocation(700,250);
        login.setResizable(false);
        login.setVisible(true);
        login_p1.setPreferredSize(new Dimension(350,200));
        login_p1_2.setPreferredSize(new Dimension(350,150));
        login_p1_3.setPreferredSize(new Dimension(350,200));
        login_p1.setLayout(new BorderLayout());
        login_p1.setBackground(Color.GRAY);
        login_p1_1.setLayout(new BorderLayout());
        login_p1_2.setLayout(new FlowLayout());
        login_p1_3.setLayout(new BorderLayout());
        login_p1_1.setBackground(Color.GRAY);
        login_p1_2.setBackground(Color.GRAY);
        login_p1_3.setBackground(Color.GRAY);


        login_b1.setPreferredSize(new Dimension(150,80));
        login_b2.setPreferredSize(new Dimension(150,80));
        Border bored = BorderFactory.createLineBorder(Color.GRAY);
        login_b1.setBackground(Color.GRAY);
        login_b1.setBorder(bored);
        login_b2.setBackground(Color.GRAY);
        login_b2.setBorder(bored);
        login_b1.addActionListener(this);
        login_b2.addActionListener(this);
        login_p1_1.add(login_b1,BorderLayout.NORTH);
        login_p1_1.add(login_b2);
        login_p1_2.add(u3);
        login_p1_2.add(u5);
        login_p1_2.add(u6);
        login_p1_2.add(j1);
        login_p1_2.add(text1);
        login_p1_2.add(j2);
        login_p1_2.add(text2);

        login_p1_3.add(u4,BorderLayout.NORTH);
        //useless();
        login_p1.add(u1,BorderLayout.WEST);
        login_p1.add(u2,BorderLayout.EAST);
        login_p1.add(login_p1_2,BorderLayout.NORTH);
        login_p1.add(login_p1_3,BorderLayout.SOUTH);
        login_p1.add(login_p1_1,BorderLayout.CENTER);
        login.add(login_p1);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==login_b1) {
            Dao dao = new Dao();
            boolean flag = false;
            try {
                dao.Sql("select username,password from user");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            for(int i=0;i<dao.getUsername().size();i++)
            {
                String password=new String(text2.getPassword());
                if(text1.getText().equals(dao.getUsername().get(i).toString()) && password.equals(dao.getPassword().get(i).toString()))
                {
                    flag=true;
                    break;
                }

            }
            if (flag){
                this.login.setVisible(false);
                new Login1();
            }else if(text1.getText().isEmpty() && text2.getPassword().length==0)
                u3.setText("                                              密码与账号不能为空!！                                        ");
            else if(text1.getText().isEmpty() && text2.getPassword().length!=0)
                u3.setText("                                              请输入账号！                                        ");
            else if(text1.getText().isEmpty()==false && text2.getPassword().length==0)
                u3.setText("                                              请输入密码！                                        ");
            else
                u3.setText("                                              请检查后重新输入！                                        ");
                text1.setText("");
                text2.setText("");

        }if (e.getSource()==login_b2) {
            this.login.setVisible(false);
            new Register();
        }
    }

    public static void main(String[] args) {
        Login login = new Login();
    }


}
