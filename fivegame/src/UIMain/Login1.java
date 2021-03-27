package UIMain;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import servlet.myFrame2;
import servlet.serverChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Login1 extends JFrame implements ActionListener {


    JFrame login = new JFrame("512的五子棋游戏");
    JPanel login_p1 = new JPanel();
    JPanel login_p1_1 = new JPanel();
    JPanel login_p1_2 = new JPanel();
    JPanel login_p1_3 = new JPanel();
    JButton login_b1 = new JButton("单机模式");
    JButton login_b2 = new JButton("联机模式");


    JLabel u1 = new JLabel("                               ");
    JLabel u2 = new JLabel("                               ");
    JLabel u3 = new JLabel("                                                                      512欢迎您！        ");
    JLabel u4 = new JLabel("                ");
    public Login1(){
        login.setSize(500,600);
        login.setLocation(700,250);
        login.setResizable(false);
        login.setVisible(true);
        login_p1.setPreferredSize(new Dimension(350,200));
        login_p1_2.setPreferredSize(new Dimension(350,150));
        login_p1_3.setPreferredSize(new Dimension(350,200));
        login_p1.setLayout(new BorderLayout());
        login_p1_1.setLayout(new BorderLayout());
        login_p1_2.setLayout(new BorderLayout());
        login_p1_3.setLayout(new BorderLayout());


        login_b1.setPreferredSize(new Dimension(150,80));
        login_b1.addActionListener(this);
        login_b2.setPreferredSize(new Dimension(150,80));
        login_b2.addActionListener(this);
        login_p1_1.add(login_b1,BorderLayout.NORTH);
        login_p1_1.add(login_b2,BorderLayout.SOUTH);
        login_p1_2.add(u3,BorderLayout.SOUTH);
        login_p1_3.add(u4,BorderLayout.NORTH);
        login_p1.add(u1,BorderLayout.WEST);
        login_p1.add(u2,BorderLayout.EAST);
        login_p1.add(login_p1_2,BorderLayout.NORTH);
        login_p1.add(login_p1_3,BorderLayout.SOUTH);
        login_p1.add(login_p1_1,BorderLayout.CENTER);
        login.add(login_p1);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==login_b1){
            this.login.setVisible(false);
            new ChessFrame();
        }else if (e.getSource()==login_b2){
            this.login.setVisible(false);
            new myFrame2();
        }
    }
}

