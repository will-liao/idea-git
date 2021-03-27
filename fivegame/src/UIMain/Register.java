package UIMain;

import dao.Dao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

public class Register extends JFrame implements ActionListener{

    private String register_username;
    private String register_password;

    public String getRegister_username() {
        return register_username;
    }

    public void setRegister_username(String register_username) {
        this.register_username = register_username;
    }

    public String getRegister_password() {
        return register_password;
    }

    public void setRegister_password(String register_password) {
        this.register_password = register_password;
    }

    final JFrame frame = new JFrame("注册账号");
    JLabel nameStr = new JLabel("用户名:");
    JLabel passwordStr = new JLabel("密码:");
    JLabel confrimStr = new JLabel("确认密码:");
    final JTextField userName = new JTextField();
    final JPasswordField password = new JPasswordField();
    JButton buttonregister = new JButton("注册");
    final JPasswordField confrimPassword = new JPasswordField();
    Login login;
    Dao dao = new Dao();
    public Register () {
        frame.setLayout(null);


        nameStr.setBounds(250, 200, 100, 25);
        frame.add(nameStr);


        passwordStr.setBounds(250, 250, 100, 25);
        frame.add(passwordStr);


        confrimStr.setBounds(250, 300, 100, 30);
        frame.add(confrimStr);


        userName.setBounds(320, 200, 150, 25);
        frame.add(userName);

        password.setBounds(320, 250, 150, 25);
        frame.add(password);

        confrimPassword.setBounds(320, 300, 150, 25);
        frame.add(confrimPassword);

        buttonregister.setBounds(350, 350, 70, 25);
        buttonregister.addActionListener(this);
        frame.add(buttonregister);

        frame.setBounds(400, 100, 800, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    //判断注册的账号是否符合规则
    boolean JudgeRegister() throws SQLException, ClassNotFoundException {
        for(int i=0;i<dao.getUsername().size();i++)
        {

            if(Login.text1.getText().equals(dao.getUsername().get(i).toString()))
            {
                JOptionPane.showMessageDialog(null, "用户已存在", "用户名", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        }

        if(this.userName.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "用户名不能为空！", "用户名", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(this.password.getPassword().length==0) {
            JOptionPane.showMessageDialog(null, "密码不能为空！", "密码为空", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!new String(this.password.getPassword()).equals(new String(this.confrimPassword.getPassword()))){
            JOptionPane.showMessageDialog(null, "两次输入的密码不一致!", "密码不一致", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //符合规则，弹出注册成功的窗口，并将账号添加数据库
        JOptionPane.showMessageDialog(null, "注册成功！");
        dao.addAdmin(this);
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String name = userName.getText();
        String passwd = new String (password.getPassword());

        //创建Register类
        this.setRegister_username(name);
        this.setRegister_password(passwd);

        //如果注册成功，返回登录界面
        try {
            if(this.JudgeRegister()) {
                this.frame.setVisible(false);
                new Login();
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}