package servlet;

import javax.swing.*;
import java.awt.*;

class serverPanel extends JPanel{
    public static int[][] board = new int[16][16];
    public static String message = "黑方先行";
    //保存最多拥有多少时间
    public static int maxTime = 0;

    //保存黑方和白方的剩余时间
    public static int blackTime = 0;
    public static int whiteTime = 0;
    //保存黑白方剩余时间的显示信息
    public static String blackMessage = "无限制";
    public static String whiteMessage = "无限制";

    public serverPanel(int[][] board) {
        this.board = board;
    }


    @Override
    public void paint(Graphics g) {
        g.drawString("游戏信息:"+message,370,20);
        g.drawString("黑方时间:"+blackMessage,370,35);
        g.drawString("白方时间:"+whiteMessage,370,50);

        g.setColor(new Color(165, 185, 75));
        g.fillRect(60, 60, 352, 352);
        g.setColor(Color.black);
        //绘制棋盘
        for (int i = 0; i < 15; i++) {
            g.drawLine(60, 60 + i * 25, 410, 60 + i * 25);
            g.drawLine(60 + i * 25, 60, 60 + i * 25, 410);
        }
        //绘制棋子
        for (int i = 1; i < board.length; i++) {
            for (int j = 1; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    if (board[i][j] == 1) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.fillOval(25 * (j + 2), 25 * (i + 2), 20, 20);

                    if (board[i][j] == 2) {
                        g.setColor(Color.BLACK);
                        g.drawOval(25 * (j + 2), 25 * (i + 2), 20, 20);
                    }
                }
            }
        }
    }

    //初始化数组
    public boolean addPiece(int row, int col, boolean isBlack) {
        if (board[row][col] == 0) {
            board[row][col] = isBlack ? 1 : 2;
            return true;
        }
        return false;

    }

    public boolean reducePiece(int row, int col) {
        if (board[row][col] != 0) {
            board[row][col] = 0;
            return true;
        }
        return false;
    }


    public boolean isWin(int row, int col, boolean isBlack) {

        return checkH(row, col, isBlack) ||
                checkV(row, col, isBlack) ||
                checkX1(row, col, isBlack) ||
                checkX2(row, col, isBlack);

    }

    //判断从右上到左下的斜线上是否连成5颗棋子
    private boolean checkX2(int row, int col, boolean isBlack) {
        int counter = 1;
        int currentRow = row;
        int currentCol = col;
        int v = isBlack ? 1 : 2;
        if (currentRow > 0 && currentCol < 14 && board[--currentRow][++currentCol] == v) {
            counter++;
        }
        currentRow = row;
        currentCol = col;
        if (currentRow < 14 && currentCol > 0 && board[++currentRow][--currentCol] == v) {
            counter++;
        }
        return counter >= 5;
    }

    //判断从左上到右下
    private boolean checkX1(int row, int col, boolean isBlack) {
        int counter = 1;
        int currentRow = row;
        int currentCol = col;
        int v = isBlack ? 1 : 2;
        while (currentRow > 0 && currentCol > 0 && board[--currentRow][--currentCol] == v) {
            counter++;
        }
        currentRow = row;
        currentCol = col;
        while (currentRow < 14 && currentCol < 14 && board[++currentRow][++currentCol] == v) {
            counter++;
        }
        return counter >= 5;
    }

    //判断竖着方向是否连成5颗棋子
    private boolean checkV(int row, int col, boolean isBlack) {
        int counter = 1;
        int currentRow = row;
        int currentCol = col;
        int v = isBlack ? 1 : 2;
        while (currentRow > 0 && board[--currentRow][currentCol] == v) {
            counter++;
        }
        currentRow = row;
        currentCol = col;
        while (currentRow < 14 && board[++currentRow][currentCol] == v) {
            counter++;
        }
        return counter >= 5;
    }

    private boolean checkH(int row, int col, boolean isBlack) {

        int counter = 1;
        int currentRow = row;
        int currentCol = col;
        int v = isBlack ? 1 : 2;
        while (currentCol > 0 && board[currentRow][--currentCol] == v) {
            counter++;
        }
        currentRow = row;
        currentCol = col;
        while (currentCol < 14 && board[currentRow][++currentCol] == v) {
            counter++;
        }
        return counter >= 5;

    }

}