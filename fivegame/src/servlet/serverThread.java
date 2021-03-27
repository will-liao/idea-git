package servlet;

import client.clientThread;
import client.myPanel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class serverThread extends Thread {
    private Socket socket = null;
    private myFrame2 frame = null;

    public serverThread(Socket socket, myFrame2 frame2) {
        this.socket = socket;
        this.frame = frame2;
    }

    @Override
    public void run() {

        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.contains("ok")) {
                    myFrame2.occupy = false;
                }
                if (line.contains(",")) {
                    String[] data = line.split(",");
                    int row = Integer.parseInt(data[0]);
                    int col = Integer.parseInt(data[1]);
                    if (myPanel.board[row][col] == 0) {
                        writer.println("ok");
                        myPanel.board[row][col] = 1;
                        frame.repaint();
                        //writer.println(frame.getRow()+","+frame.getCol());
                    }
                }


                if (line.contains("win:")) {
                    JOptionPane.showMessageDialog(frame, "你输了");
                    frame.setWin(true);
                    frame.repaint();
                }

                if (line.contains("chat:")) {
                    String[] data = line.split(":");
                    //frame.text.setText("对方："+data[1]);
                    frame.text.append("对方：" + data[1] + "\r\n");
                }
                if (line.contains("qiuhesuccess:")) {
                    JOptionPane.showMessageDialog(frame,"和局");
                    //清空棋盘
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            myPanel.board[i][j] = 0;
                        }
                    }
                    clientThread.success=false;

                }else if (line.contains("qiuhe:")) {
                    int result = JOptionPane.showConfirmDialog(frame, "确定要同意求和？");
                    if (result == 0) {
                        JOptionPane.showMessageDialog(frame, "和局");
                        myFrame2.isStart=false;
                        //清空棋盘
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                myPanel.board[i][j] = 0;
                            }
                        }

                        clientThread.success = true;

                    }
                }
            }
            this.socket.close(); // 连接关闭，所有的流都会随之关闭

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}