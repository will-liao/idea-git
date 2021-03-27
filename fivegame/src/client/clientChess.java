package client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class clientChess {
    private static myFrame frame = null;

    public clientChess(myFrame frame) {
        this.frame = frame;
    }

    public void start() throws Exception {
        Socket client = new Socket("127.0.0.1", 10000);

        final PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
        java.io.InputStream in = client.getInputStream();
        new clientThread(client, frame).start();
        //没分出胜负时，需要向服务器传棋子的坐标
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
                    if (frame.qiuheclient==true) {
                        writer.println("qiuhe:" + frame.getRow() + ":" + frame.getCol());
                    } else {
                        writer.println(frame.getRow() + "," + frame.getCol());
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


    }

    public static void main(String[] args) throws Exception {
        myFrame frame = new myFrame();
        clientChess client = new clientChess(frame);
        client.start();
    }
}
