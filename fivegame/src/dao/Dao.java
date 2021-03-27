package dao;
import UIMain.Register;

import java.sql.*;
import java.util.Vector;

public class Dao {
    private String dbURL = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT";
    private String userName = "root";
    private String userPwd = "will";
    private Connection ct = null;
    private ResultSet rs = null;
    private Statement st;
    private Vector username = new Vector();
    private Vector password = new Vector();

    //获取数据库中系统登录ID

    public Vector getUsername() {
        return username;
    }

    //设置数据库中系统登录ID

    public void setUsername(Vector username) {
        this.username = username;
    }

    //获取数据库中系统登录密码

    public Vector getPassword() {
        return password;
    }


    //设置数据库中系统登录ID

    public void setPassword(Vector password) {
        this.password = password;
    }

    public void Sql(String sql) throws Exception {
        java.sql.Driver driver =new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);

        ct = DriverManager.getConnection(dbURL, userName, userPwd);
        st = ct.createStatement();
        rs = st.executeQuery(sql);
        while (rs.next()) {
            username.add(rs.getString(1));
            password.add(rs.getString(2));
        }

        if (rs != null)
            rs.close();
        if (st != null)
            st.close();
        if (ct != null)
            ct.close();
    }
    //向数据库添加账户
    public void addAdmin(Register register) throws ClassNotFoundException, SQLException {
        String sql="insert into user (username, password) values (?,?)";
        try {
            Connection conn = DriverManager.getConnection(dbURL, userName, userPwd);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(register.getRegister_username()));
            ps.setString(2, String.valueOf(register.getRegister_password()));
            ps.executeUpdate();
            ps.close();
            conn.close();

        }catch(SQLException ex) {
            System.out.println("添加用户失败！");
        }

    }

    public static void main(String[] args) {
        Dao dao = new Dao();
        try {
            dao.Sql("select username,password from user");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
