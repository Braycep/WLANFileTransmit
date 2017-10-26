package priv.braycep.receive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receive {
    //Net
    private static DatagramSocket socketBegain;
    private static DatagramPacket packetBegain;

    private static DatagramPacket packet;
    private static DatagramSocket socket;

    //file io
    private static FileOutputStream fos;
    private static String trgFileName;
    private static File trgFile;

    //start with download location
    public static void start(File dir){
        try{
            socketBegain = new DatagramSocket(13141);
            packetBegain = new DatagramPacket(new byte[10240],10240);
            socketBegain.receive(packetBegain);
            while (true) {
                if (packetBegain.getLength() > 0){
                    trgFileName = new String(packetBegain.getData(),0,packetBegain.getLength());
                    socketBegain.close();
                    packetBegain = null;
                    socketBegain = null;
                    break;
                }
            }
            //reply
            new DatagramSocket().send(new DatagramPacket("Received File Name".getBytes(),10240,
                    InetAddress.getByName("255.255.255.255"),13142));

            //receive data
            trgFile = new File(dir,trgFileName);
            fos = new FileOutputStream(trgFile);
            int len;
            byte[] buf = new byte[32768];
            socket = new DatagramSocket(13141);
            packet = new DatagramPacket(buf,buf.length);
            socket.receive(packet);
            ReceiveFrame.setIsReceiving(true);
            while ((len = packet.getLength()) > 0) {
                fos.write(buf,0,len);
                new DatagramSocket().send(new DatagramPacket("Reply".getBytes(),1,
                        InetAddress.getByName("255.255.255.255"),13142));
            }
            fos.flush();

            //append msg
            ReceiveFrame.appendFileInfo("Download File: "+trgFile.getName()+"\n");
            ReceiveFrame.appendFileInfo("File Location: "+trgFile.getAbsolutePath()+"\n");
            ReceiveFrame.appendFileInfo("Received Over.\n");
            ReceiveFrame.setIsReceiving(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
        }
    }
}
