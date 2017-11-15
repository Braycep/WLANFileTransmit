package priv.braycep.receive;

import priv.braycep.shareds.SharedMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receive {
    //网络
    private static DatagramSocket socketBegain;
    private static DatagramPacket packetBegain;

    private static DatagramPacket packet;
    private static DatagramSocket socket;

    //文件
    private static FileOutputStream fos;
    private static String trgFileName;
    protected static File trgFile;

    //标志
    private static boolean isEnd = true;

    //getter
    protected static boolean isIsEnd(){
        return isEnd;
    }

    //开始接收
    public static void start(File dir){
        try{
            socketBegain = new DatagramSocket(13141);
            packetBegain = new DatagramPacket(new byte[10240],10240);
            socketBegain.receive(packetBegain);
            while (true) {
                if (packetBegain.getLength() > 0){
                    trgFileName = new String(packetBegain.getData(),0,packetBegain.getLength());
                    socketBegain.close();
                    isEnd = false;
                    packetBegain = null;
                    socketBegain = null;
                    break;
                }
            }

            //拆分文件名和文件大小
            String[] fileName = trgFileName.split("\\?");
            trgFileName = fileName[0];
            ReceiveFrame.appendFileInfo("接收文件: "+trgFileName);
            ReceiveFrame.fileSize = Integer.parseInt(fileName[1]);
            ReceiveFrame.appendFileInfo("文件大小: "+SharedMethods.formatFileSize(Integer.parseInt(fileName[1])));

            //返回收到文件名的确认信息
            new DatagramSocket().send(new DatagramPacket("Received File Name".getBytes(),"Received File Name".getBytes().length,
                    InetAddress.getByName("255.255.255.255"),13143));

            //准备接收文件数据
            trgFile = new File(dir,trgFileName);
            if (trgFile.exists()) {
                trgFile.delete();
            }
            fos = new FileOutputStream(trgFile);
            int len;
            byte[] buf = new byte[32768];
            socket = new DatagramSocket(13141);
            packet = new DatagramPacket(buf,0,buf.length);
            ReceiveFrame.setIsReceiving(true);
            ReceiveFrame.countTime();
            ReceiveFrame.setSubmitBtnText("接收中");

            //开启垃圾清理进程
          new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(ReceiveFrame.isReceiving());
                    while (ReceiveFrame.isReceiving()) {
                        try{
                            Thread.sleep(1000);
                            System.gc();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            //开始接收
            socket.receive(packet);
            while ((len = packet.getLength()) > 0) {
                fos.write(buf,0,len);
                fos.flush();
                while (true) {
                    new DatagramSocket().send(new DatagramPacket("Reply".getBytes(),1,
                            InetAddress.getByName("255.255.255.255"),13142));
                    socket.receive(packet);
                    break;
                }
            }

            //文件发送完毕提示信息
            ReceiveFrame.appendFileInfo("接收的文件: "+trgFile.getName());
            ReceiveFrame.appendFileInfo("文件的路径: "+trgFile.getAbsolutePath());
            ReceiveFrame.appendFileInfo("接受完毕");
            ReceiveFrame.setIsReceiving(false);
            isEnd = true;
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
            socket = null;
            packet = null;
            socketBegain = null;
            packetBegain = null;
        }
    }
}
