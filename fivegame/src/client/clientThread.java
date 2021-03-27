package client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class clientThread extends Thread {
    Socket socket = null;
    myFrame frame = null;
    java.io.InputStream stream = null;
    BufferedReader reader = null;
    PrintWriter writer = null;
    public static boolean success = false;

    public clientThread(Socket socket, myFrame frame) {
        this.socket = socket;
        this.frame = frame;
        try {
            this.stream = this.socket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(stream));
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line = null;
        try {
            while ((line = this.reader.readLine()) != null) {
                if (line.contains("ok")) {
                    myFrame.occupy = false;
                }
                if (line.contains(",")) {
                    String[] data = line.split(",");
                    int row = Integer.parseInt(data[0]);
                    int col = Integer.parseInt(data[1]);
                    if (myPanel.board[row][col] == 0) {
                        writer.println("ok");
                        myPanel.board[row][col] = 2;
                        frame.repaint();
                    }
                }

                if (line.contains("win:")) {
                    //System.out.println("你输了");
                    JOptionPane.showMessageDialog(frame, "你输了");
                    frame.repaint();
                }

                if (line.contains("chat:")) {
                    String[] data = line.split(":");
                    frame.text.append("对方：" + data[1] + "\r\n");
                }

                if (line.contains("qiuhe:")) {
                    int result = JOptionPane.showConfirmDialog(frame, "确定要求和？");
                    if (result == 0) {
                        JOptionPane.showMessageDialog(frame,"和局");
                        //清空棋盘
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                myPanel.board[i][j] = 0;
                            }
                        }
                        success = true;

                    }

                }else if (line.contains("qiuhesuccess:")) {
                    JOptionPane.showMessageDialog(frame, "和局");
                    myFrame.isStart=false;
                    //清空棋盘
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            myPanel.board[i][j] = 0;
                        }
                    }
                    clientThread.success = false;
                }
            }

            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
