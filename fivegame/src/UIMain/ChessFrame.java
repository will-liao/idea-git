package UIMain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessFrame extends JFrame implements MouseListener,Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //取得屏幕的宽度和高度
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    //背景图片
    BufferedImage bgImage = null;
    //保存棋子的坐标
    int x = 0;
    int y = 0;
    //保存之前下过的全部棋子的坐标    数组中默认初始值为0，代表没有棋子   1代表黑子   2代表  白子
    int[][] allChess = new int[19][19];
    //标识当前是黑棋还是白棋
    boolean isBack = true;
    int i, j;
    //标识当前游戏是否可以继续
    boolean canPlay = true;
    //显示的提示信息
    String message = "黑方先行";
    //点击开始游戏之后才可以进行操作棋盘
    boolean start = false;
    //保存最多拥有多少时间
    int maxTime = 0;
    //做倒计时的线程类
    Thread t = new Thread(this);
    //保存黑方和白方的剩余时间
    int blackTime = 0;
    int whiteTime = 0;
    //保存黑白方剩余时间的显示信息
    String blackMessage = "无限制";
    String whiteMessage = "无限制";

    public ChessFrame() {
        //设置标题
        this.setTitle("五子棋");
        //设置窗体大小
        this.setSize(700, 690);
        //设置窗体出现位置
        this.setLocation((width - 500) / 2, (height - 500) / 2);
        //将窗体设置为大小不可改变
        this.setResizable(false);
        //将窗体的关闭方式设置为默认关闭后程序结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //将窗体显示出来
        this.setVisible(true);
        //为窗体加入监听器
        this.addMouseListener(this);
        //开启线程
        t.start();
        //线程挂起
        t.suspend();
        //刷新屏幕，防止游戏开始时出现无法显示的情况
        this.repaint();
        try {
            bgImage = ImageIO.read(new File("src\\img\\棋盘背景.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @Override
    public void paint(Graphics g) {
        // 双缓冲技术防止屏幕闪烁
        BufferedImage bi = new BufferedImage(1024, 1024,BufferedImage.TYPE_INT_RGB);
        Graphics g2 = bi.createGraphics();
        //绘制背景
        g2.drawImage(bgImage, 0, 0, this);
        //输出标题信息
        g2.setFont(new Font("黑体",Font.BOLD,20));
        g2.drawString("游戏信息: "+message, 50, 55);
        //输出时间信息
        g2.setFont(new Font("宋体", 0, 14));
        g2.drawString("黑方时间："+blackMessage, 50, 650);
        g2.drawString("白方时间："+whiteMessage, 450, 650);

        g2.drawString("开始游戏", 610, 160);
        g2.drawString("游戏设置", 610, 230);
        g2.drawString("游戏说明", 610, 300);
        g2.drawString("认输", 610, 400);
        g2.drawString("关于", 610, 470);
        g2.drawString("退出", 610, 540);

        //绘制棋盘
        for(int i = 0;i < 19;i++){
            g2.drawLine(30, 80+i*30, 570, 80+i*30);
            g2.drawLine(30+i*30, 80, 30+i*30, 620);
        }

        //标注点位
        g2.setColor(Color.BLACK);
        g2.fillOval(120, 170, 4, 4);
        g2.fillOval(480, 170, 4, 4);
        g2.fillOval(120, 530, 4, 4);
        g2.fillOval(480, 530, 4, 4);

        g2.fillOval(300, 170, 4, 4);
        g2.fillOval(120, 350, 4, 4);
        g2.fillOval(480, 350, 4, 4);
        g2.fillOval(300, 530, 4, 4);

        g2.fillOval(300, 350, 4, 4);

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19;j++){
                //棋子下的位置的位置
                int tempX = i * 30 + 30;
                int tempY = j * 30 + 90;
                //绘制黑子
                if(allChess[i][j] == 1){
                    g2.fillOval(tempX-10,tempY-20, 20, 20);
                }
                //绘制白子
                if(allChess[i][j] == 2){
                    g2.setColor(Color.WHITE);
                    g2.fillOval(tempX-10,tempY-20, 20, 20);
                    g2.setColor(Color.BLACK);
                    g2.drawOval(tempX-10,tempY-20, 20, 20);
                }
            }
        }
        g.drawImage(bi, 0, 0, this);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {  //鼠标按下时调用
        if (canPlay == true && start == true) {
            //获取鼠标点击的坐标
            x = e.getX();
            y = e.getY();
            if (x >= 30 && x <= 570 && y >= 80 && y <= 640) {
                //棋子下的棋盘线条中心位置坐标    网格（30）的整数倍
                x = ((x + 15) / 30) * 30;
                y = ((y + 15) / 30) * 30;
                //最后棋子位置的坐标转换为    数组的索引
                i = (x - 30) / 30;
                j = (y - 80) / 30;
                //当前位置没有棋子  二维数组默认0
                if (allChess[i][j] == 0) {
                    if (isBack == true) {//二维数组赋值，1代表黑色
                        allChess[i][j] = 1;
                        //改变棋子颜色
                        isBack = false;
                        message = "请白方落子";
                    } else {  //赋值 2 代表白色
                        allChess[i][j] = 2;
                        //改变棋子颜色
                        isBack = true;
                        message = "请黑方落子";
                    }
                    //是否赢得游戏
                    boolean flag = this.checkWin();
                    if (flag == true) {
                        JOptionPane.showMessageDialog(this, "五子连珠," + (allChess[i][j] == 1 ? "黑方获胜！" : "白方获胜！"));
                        //胜利之后，棋盘不允许再进行操作
                        canPlay = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "当前位置已经有棋子，请重新落子！");
                }
                //重新执行一次paint方法
                this.repaint();
            }
        }

        //开始游戏
        if (e.getX() >= 610 && e.getX() <= 650 && e.getY() >= 140 && e.getY() <= 170) {
            int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏?");
            //重新开始游戏
            if (result == 0) {
                //清空棋盘   数组中的数据全部为0
                for (int i = 0; i < 19; i++) {
                    for (int j = 0; j < 19; j++) {
                        allChess[i][j] = 0;
                    }
                }
                //游戏信息的显示为默认值
                message = "黑方先行";
                isBack = true;
                blackTime = maxTime;
                whiteTime = maxTime;

                //输入的设置时间大于0
                if (maxTime > 0) {
                    //blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                    blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                    //t.resume();
                } else {
                    blackMessage = "无限制";
                    whiteMessage = "无限制";
                }
                this.canPlay = true;
                //点击开始游戏以后才可以下棋
                start = true;
                this.repaint();
            }

        }

        //设置
        if (e.getX() >= 610 && e.getX() <= 650 && e.getY() >= 200 && e.getY() <= 230) {
            String sc = JOptionPane.showInputDialog("请输入游戏的最大时间(分钟),输入0，表示没有时间限制:");
            try {
                //换算为   秒
                maxTime = Integer.parseInt(sc) * 60;
                //设置时间小于0时
                if (maxTime < 0) {
                    JOptionPane.showMessageDialog(this, "请正确输入信息!不允许输入负数!");
                }
                //设置时间等于0时
                if (maxTime == 0) {
                    int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏");
                    //重新开始游戏
                    if (result == 0) {
                        //清空棋盘
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                allChess[i][j] = 0;
                            }
                        }
                        //恢复默认设置
                        message = "黑方先行";
                        isBack = true;
                        blackMessage = "无限制";
                        whiteMessage = "无限制";
                        this.canPlay = true;
                        start = true;
                        this.repaint();
                    }
                }
                //设置时间大于0
                if (maxTime > 0) {
                    int result = JOptionPane.showConfirmDialog(this, "设置完成,是否重新开始游戏?");
                    //重新开始游戏
                    if (result == 0) {
                        //清空棋盘
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                allChess[i][j] = 0;
                            }
                        }
                        //恢复默认设置
                        message = "黑方先行";
                        isBack = true;
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        whiteMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        t.resume();
                        this.canPlay = true;
                        start = true;
                        this.repaint();
                    }
                }
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(this, "请正确输入信息!");
            }

        }

        //游戏说明
        if (e.getX() >= 610 && e.getX() <= 650 && e.getY() >= 280 && e.getY() <= 300) {
            JOptionPane.showMessageDialog(this, "随便下，开心就好！");
        }

        //认输
        if (e.getX() >= 610 && e.getX() <= 630 && e.getY() >= 380 && e.getY() <= 410) {
            int result = JOptionPane.showConfirmDialog(this, "确认认输？");
            if (result == 0) {
                if (isBack) {
                    JOptionPane.showMessageDialog(this, "黑方认输，游戏结束！");
                } else {
                    JOptionPane.showMessageDialog(this, "白方认输，游戏结束！");
                }
                canPlay = false;
            }
        }

        //关于
        if (e.getX() >= 610 && e.getX() <= 630 && e.getY() >= 450 && e.getY() <= 480) {
            JOptionPane.showMessageDialog(this, "随便做做，请多多指教");
        }

        //退出
        if (e.getX() >= 610 && e.getX() <= 630 && e.getY() >= 520 && e.getY() <= 550) {
            int result = JOptionPane.showConfirmDialog(this, "确定退出并返回上一界面？");
            if (result == 0) {
                this.setVisible(false);
                new Login1();
            } else {

            }
        }
    }

    private boolean checkWin() {
        // TODO Auto-generated method stub
        boolean flag = false;
        //判断横向是否有五个棋子是相同的颜色
        int count1 = 1;//相同颜色棋子的个数
        int color = allChess[i][j]; //刚下的棋子的颜色
        int a = 1;  //棋子索引的增量
        while (color == allChess[i + a][j]) {
            count1++;
            a++;
        }
        a = 1;
        while (color == allChess[i - a][j]) {
            count1++;
            a++;
        }
        if (count1 >= 5) {
            flag = true;
        }

        //判断纵向是否有五个棋子是相同的颜色
        int count2 = 1;
        a = 1;
        while (color == allChess[i][j + a]) {
            count2++;
            a++;
        }
        a = 1;
        while (color == allChess[i][j - a]) {
            count2++;
            a++;
        }
        if (count2 >= 5) {
            flag = true;
        }

        //右上    左下 是否有五个棋子是相同的颜色
        int count3 = 1;
        a = 1;
        while (color == allChess[i + a][j - a]) {
            count3++;
            a++;
        }
        a = 1;
        while (color == allChess[i - a][j + a]) {
            count3++;
            a++;
        }
        if (count3 >= 5) {
            flag = true;
        }

        //左上  右下  是否有五个棋子是相同的颜色
        int count4 = 1;
        a = 1;
        while (color == allChess[i - a][j - a]) {
            count4++;
            a++;
        }
        a = 1;
        while (color == allChess[i + a][j + a]) {
            count4++;
            a++;
        }
        if (count4 >= 5) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        //判断是否有时间限制
        if (maxTime > 0) {
            while (true) {
                //当前是黑棋
                if (isBack) {
                    //时间减少
                    blackTime--;
                    //当时间到的时候
                    if (blackTime == 0) {
                        JOptionPane.showMessageDialog(this, "黑方游戏超时，游戏结束");
                        canPlay = false;
                        return;
                    }
                    //	blackMessage = blackTime/3600+":"+(blackTime/60 - blackTime/3600*60)+":"+(blackTime - blackTime /60 * 60);
                    //blackTime = maxTime;
                } else {
                    whiteTime--;
                    if(whiteTime == 0){JOptionPane.showMessageDialog(this, "白方游戏超时，游戏结束");
                    canPlay = false;
                    return;}
                }
                blackMessage = blackTime/3600+":"+(blackTime/60 - blackTime/3600*60)+":"+(blackTime - blackTime /60 * 60);
                whiteMessage = whiteTime/3600+":"+(whiteTime/60 - whiteTime/3600*60)+":"+(whiteTime - whiteTime /60 * 60);
                this.repaint();
                try {Thread.sleep(1000);
                } catch (InterruptedException e) {
                            e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new ChessFrame();
    }
}




