package servlet;

import UIMain.Login1;
import client.myFrame;
import client.myPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class myFrame2 extends JFrame implements Runnable {
    //public static int[][] chess = new int[16][16];


    public static boolean isBlack = false;// 是否轮到黑棋
    public static boolean occupy = false;
    public static String message = "黑方先行";
    //保存最多拥有多少时间
    public static int maxTime = 0;

    //保存黑方和白方的剩余时间
    public static int blackTime = 0;
    public static int whiteTime = 0;
    //保存黑白方剩余时间的显示信息
    public static String blackMessage = "无限制";
    public static String whiteMessage = "无限制";
    private boolean isWin = false;// 是否获得胜利
    public static boolean isStart = false;// 是否联机成功
    private int row = 0;
    private int col = 0;

    public static boolean qiuheserver = false;

    myPanel myPanel;
    public JButton qiuhe;
    final serverPanel panel;
    public static JTextArea text;


    public myFrame2() {

        this.setTitle("联机版五子棋：服务器");
        this.setBounds(800, 100, 500, 580);
        this.setLayout(null);

        JButton start = new JButton("开始");
        start.setBounds(10, 10, 60, 25);
        this.add(start);
        //开始按钮

        qiuhe = new JButton("求和");
        qiuhe.setBounds(80, 10, 60, 25);
        this.add(qiuhe);


        JButton end = new JButton("退出");
        end.setBounds(150, 10, 60, 25);
        this.add(end);

        JButton set = new JButton("设置");
        set.setBounds(220, 10, 60, 25);
        this.add(set);

        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(myFrame2.this, "确定要退出并返回上一界面吗？");
                if (result==0){
                    setVisible(false);
                    new Login1();
                }
            }
        });


        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setBounds(60, 430, 360, 100);
        this.add(pane);


        panel = new serverPanel(myPanel.board);
        panel.setBounds(0, 0, 500, 500);
        this.add(panel);


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isStart && !isWin() && !occupy) {
                    int x = e.getX();
                    int y = e.getY();
                    if (x >= 60 && x <= 415 && y >= 60 && y <= 415) {
                        setRow(Math.round((y - 60) / 25f));
                        setCol(Math.round((x - 60) / 25f));
                        repaint();
                        if (panel.addPiece(getRow(), getCol(), isBlack)) {
                            setWin(panel.isWin(getRow(), getCol(), isBlack));
                            if (!isWin()) {

                            } else {
                                JOptionPane.showMessageDialog(myFrame2.this, "你赢了。。。。。");
                                setWin(true);
                                repaint();
                            }
                        }

                    }
                }

            }
        });


        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(panel, "确定要重新开始游戏？");
                if (result==0) {
                    //清空棋盘   数组中的数据全部为0
                    for (int i = 0; i < 16; i++) {
                        for (int j = 0; j < 16; j++) {
                            myPanel.board[i][j] = 0;
                        }
                    }
                    //游戏信息的显示为默认值
                    message = "黑方先行";
                    blackTime = maxTime;
                    whiteTime = maxTime;
                    setWin(false);
                    isStart = true;

                    repaint();

                    //输入的设置时间大于0
                    if (maxTime > 0) {
                        //blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        //t.resume();
                    } else {
                        blackMessage = "无限制";
                        whiteMessage = "无限制";
                    }

                }
            }
        });

        //求和按钮监听事件
        qiuhe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println(getRow() + "," + getCol());
//                myPanel.board[getRow()][getCol()] = 0;
//                backChess = true;
                int result = JOptionPane.showConfirmDialog(panel, "确定要求和？");
                if (result == 0) {
                    qiuheserver = true;
                }
            }
        });

        set.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sc = JOptionPane.showInputDialog(myFrame2.this, "请输入游戏的最大时间(分钟),输入0，表示没有时间限制:");
                //换算为秒
                maxTime = Integer.parseInt(sc) * 60;
                //做倒计时的线程类
                Thread t = new Thread(myFrame2.this::run);
                //开启线程
                t.start();
                //线程挂起
                t.suspend();
                //设置时间小于0时
                if (maxTime < 0) {
                    JOptionPane.showMessageDialog(myFrame2.this, "请正确输入信息!不允许输入负数!");
                }
                //设置时间等于0时
                if (maxTime == 0) {
                    int result = JOptionPane.showConfirmDialog(myFrame2.this, "是否重新开始游戏");
                    //重新开始游戏
                    if (result == 0) {
                        //清空棋盘
                        for (int i = 0; i < 16; i++) {
                            for (int j = 0; j < 16; j++) {
                                client.myPanel.board[i][j] = 0;
                            }
                        }
                        //恢复默认设置
                        message = "黑方先行";
                        isBlack = true;
                        blackMessage = "无限制";
                        whiteMessage = "无限制";
                        isStart = true;
                        repaint();
                    }
                }
                //设置时间大于0
                if (maxTime > 0) {
                    int result = JOptionPane.showConfirmDialog(myFrame2.this, "设置完成,是否重新开始游戏?");
                    //重新开始游戏
                    if (result == 0) {
                        //清空棋盘
                        for (int i = 0; i < 16; i++) {
                            for (int j = 0; j < 16; j++) {
                                client.myPanel.board[i][j] = 0;
                            }
                        }
                        //恢复默认设置
                        message = "黑方先行";
                        isBlack = true;
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        whiteMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        t.checkAccess();
                        isStart = true;
                        repaint();
                    }
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean isWin) {
        this.isWin = isWin;
    }


    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public static void main(String[] args) {
        new myFrame2();
    }

    @Override
    public void run() {
        //判断是否有时间限制
        if (myPanel.maxTime > 0) {
            while (true) {
                //当前是黑棋
                if (isBlack) {
                    //时间减少
                    myPanel.blackTime--;
                    //当时间到的时候
                    if (myPanel.blackTime == 0) {
                        JOptionPane.showMessageDialog(this, "黑方游戏超时，游戏结束");
                        myFrame.isStart = false;
                        return;
                    }
                    //	blackMessage = blackTime/3600+":"+(blackTime/60 - blackTime/3600*60)+":"+(blackTime - blackTime /60 * 60);
                    //blackTime = maxTime;
                } else {
                    myPanel.whiteTime--;
                    if(myPanel.whiteTime == 0){JOptionPane.showMessageDialog(this, "白方游戏超时，游戏结束");
                        myFrame.isStart = false;
                        return;}
                }
                myPanel.blackMessage = myPanel.blackTime/3600+":"+(myPanel.blackTime/60 - myPanel.blackTime/3600*60)+":"+(myPanel.blackTime - myPanel.blackTime /60 * 60);
                myPanel.whiteMessage = myPanel.whiteTime/3600+":"+(myPanel.whiteTime/60 - myPanel.whiteTime/3600*60)+":"+(myPanel.whiteTime - myPanel.whiteTime /60 * 60);
                this.repaint();
                try {Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}