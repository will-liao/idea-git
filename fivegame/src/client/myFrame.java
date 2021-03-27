package client;

import UIMain.Login1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class myFrame extends JFrame {
    //public static int[][] chess = new int[16][16];
    public static boolean isBlack = true;// 是否轮到黑棋
    public static boolean occupy = false;
    private boolean isWin = false;// 是否获得胜利
    public static boolean isStart = false;// 是否联机成功开始游戏
    public static boolean qiuheclient = false;
    private int row = 0;
    private int col = 0;
    public static JTextArea text;


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


    public myFrame() {
        this.setTitle("联机版五子棋:客户端");
        this.setBounds(100, 100, 500, 580);
        this.setLayout(null);

        JButton start = new JButton("开始");
        start.setBounds(10, 10, 60, 25);
        this.add(start);
        //开始按钮

        JButton qiuhe = new JButton("求和");
        qiuhe.setBounds(80, 10, 60, 25);
        this.add(qiuhe);

        text = new JTextArea();
        JScrollPane pane = new JScrollPane(text);
        pane.setBounds(60, 430, 360, 100);
        this.add(pane);


        JButton end = new JButton("退出");
        end.setBounds(150, 10, 60, 25);
        this.add(end);

        JButton set = new JButton("设置");
        set.setBounds(150, 10, 60, 25);
        this.add(set);

        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(myFrame.this, "确定要退出并返回上一界面吗？");
                if (result==0){
                    setVisible(false);
                    new Login1();
                }
            }
        });


        final myPanel panel = new myPanel();
        panel.setBounds(0, 0, 500, 500);
        this.add(panel);

        //JTextField innfo=new


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isStart && !isWin() && !occupy) {
                    int x = e.getX();
                    int y = e.getY();
                    if (x >= 60 && x <= 415 && y >= 60 && y <= 415) {
                        setRow(Math.round((y - 60) / 25f));
                        setCol(Math.round((x - 60) / 25f));
                        //System.out.println(getRow()+","+getCol());
                        repaint();
                        if (panel.addPiece(getRow(), getCol(), isBlack)) {
                            setWin(panel.isWin(getRow(), getCol(), isBlack));
                            if (!isWin()) {

                            } else {
                                JOptionPane.showMessageDialog(myFrame.this, "你赢了。。。。");
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
                    panel.message = "黑方先行";
                    panel.blackTime = panel.maxTime;
                    panel.whiteTime = panel.maxTime;
                    setWin(false);
                    isStart = true;
                    repaint();

                    //输入的设置时间大于0
                    if (panel.maxTime > 0) {
                        //blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":" + (maxTime - maxTime / 60 * 60);
                        panel.blackMessage = panel.maxTime / 3600 + ":" + (panel.maxTime / 60 - panel.maxTime / 3600 * 60) + ":" + (panel.maxTime - panel.maxTime / 60 * 60);
                        //t.resume();
                    } else {
                        panel.blackMessage = "无限制";
                        panel.whiteMessage = "无限制";
                    }

                }
            }
        });


        //求和按钮监听事件
        qiuhe.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(panel, "确定要求和？");
                if (result == 0) {
                    qiuheclient = true;
                }
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
