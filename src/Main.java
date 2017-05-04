import javafx.application.Application;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by julienathomas on 13/02/2017.
 */

public class Main extends Application {
    private static void run()
    {
        boolean ok=true;
        try
        {
            Socket s=new Socket("127.0.0.1",4242);
            DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
            Rectangle rec=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Robot r= new Robot();
            BufferedImage img;
            while (ok)
            {
                img = r.createScreenCapture(rec);
                //s.setSendBufferSize(65536);
                img.flush();

                Image tmp = img.getScaledInstance(img.getWidth() /2 , img.getHeight() / 2, Image.SCALE_SMOOTH);
                BufferedImage dimg = new BufferedImage(img.getWidth() / 2, img.getHeight() / 2, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = dimg.createGraphics();
                g2d.drawImage(tmp, 0, 0, null);
                g2d.dispose();

//                System.out.println(img.getWidth() + " "  + img.getHeight());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(ImageIO.write(dimg, "png",baos))
                {
                    baos.flush();
                    byte[] imageInByte = baos.toByteArray();
                    baos.close();

                    byte[] length = new byte[] {
                    (byte)(imageInByte.length >>> 24),
                            (byte)(imageInByte.length >>> 16),
                            (byte)(imageInByte.length >>> 8),
                            (byte)imageInByte.length};

                    byte[] finalBuf = new byte[imageInByte.length + 4];
                    finalBuf[0] = length[0];
                    finalBuf[1] = length[1];
                    finalBuf[2] = length[2];
                    finalBuf[3] = length[3];
                    for (int i=0; i < imageInByte.length; ++i) {
                        finalBuf[i + 4] = imageInByte[i];
                    }

//                    System.out.println(finalBuf.length + " " + imageInByte.length);

                    dout.write(length);
                    dout.write(imageInByte);

//                    DatagramSocket clientSocket = new DatagramSocket();
//                    InetAddress IPAddress = InetAddress.getByName("localhost");
//                    DatagramPacket sendPacket = new DatagramPacket(imageInByte, length, IPAddress, 9876);
//                    clientSocket.send(sendPacket);
//                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//                    clientSocket.receive(receivePacket);
//                    String modifiedSentence = new String(receivePacket.getData());
//                    System.out.println("FROM SERVER:" + modifiedSentence);
//                    clientSocket.close();

                  //  System.out.println(imageInByte.length);
//                    sleep(1);
                }


            }
            dout.close();
        }
        catch (Exception e)
        {
            System.out.println("msg: " + e.toString());
        }
    }

    public static void main(String[] args) {
        run();
        System.out.println("gfds");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("asdfg");
//        System.setProperty("java.awt.headless", "true");
    }
}
