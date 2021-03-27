package servlet;

import client.clientThread;

import javax.swing.*;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class serverChess extends JFrame {
    public static myFrame2 frame = null;

    public serverChess(myFrame2 frame) {
        this.frame = frame;
    }

    public void start() throws Exception {

        ServerSocket server = new ServerSocket(10000);
        System.out.println("服务器端启动");
        while (true) {
            Socket socket = server.accept();
            final PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);
            new Thread(new Runnable() {

                String line;

                @Override
                public void run() {
                    while ((line = sc.nextLine()) != null) {
                        frame.text.append("我：" + line + "\r\n");
                        writer.println("chat:" + line);
                    }

                }

            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!frame.isWin()) {
                        writer.println(frame.getRow() + "," + frame.getCol());
                        if (myFrame2.qiuheserver) {
                            writer.println("qiuhe:" + frame.getRow() + ":" + frame.getCol());
                        }
                    }
                    writer.println("win:" + frame.isWin());
                }

            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true)
                        if (clientThread.success==true){
                            writer.println("qiuhesuccess:" + frame.getRow() + ":" + frame.getCol());
                        } else {

                        }
                }
            });

            new serverThread(socket, frame).start();
        }

    }

    public static void main(String[] args) throws Exception {
        myFrame2 frame = new myFrame2();
        serverChess server = new serverChess(frame);
        server.start();
    }
}
