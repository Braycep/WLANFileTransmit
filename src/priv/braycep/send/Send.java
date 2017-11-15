package priv.braycep.send;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {
    //文件和网络
    private static File srcfile;
    private static FileInputStream fis;
    private static DatagramPacket packet;
    private static DatagramSocket socket;
    private static DatagramPacket confirmPacket;
    private static DatagramSocket confirmSocket;

    private static int[] ip = null;
    private static String ipStr = "255,255,255,255";

    //开始发送文件
    public static void start(){
        try {
            //获取发送目标的IP,合成Ip字符串
            ip = SendFrame.getIps();
            ipStr = ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];

            //获取源文件，并载入流中
            srcfile = SendFrame.getChsFile();
            fis = new FileInputStream(srcfile);
            socket = new DatagramSocket();
            int len;
            byte[] buffer = new byte[32768];

            //从发送目标获取确认信息的套接字
            confirmSocket = new DatagramSocket(13142);
            confirmPacket = new DatagramPacket(buffer,0,buffer.length);

            //发送文件名和文件大小，且作为发送文件开始的标志
            String fileInfo = srcfile.getName();
            fileInfo = fileInfo + "?" + srcfile.length();
            new DatagramSocket().send(new DatagramPacket(fileInfo.getBytes(),fileInfo.getBytes().length,
                    InetAddress.getByName("255.255.255.255"),13141));

            //获取发送目标收到文件名的返回信息
            while (true){
                DatagramPacket tmpPacket = new DatagramPacket(new byte[1024],0,1024);
                new DatagramSocket(13143).receive(tmpPacket);
                if (tmpPacket.getLength() > 0){
                    SendFrame.appendSentMsg("文件名发送完毕！\n");
                    break;
                }
            }

            //等待发送目标处理文件创建等其他事务
            try{
                Thread.sleep(10);
            }catch (InterruptedException i){
                i.printStackTrace();
            }
            SendFrame.setSubmitBtnText("发送中");

            //开始发送文件
            while ((len = fis.read(buffer)) != -1){
                packet = new DatagramPacket(buffer,len, InetAddress.getByName(ipStr),13141);
                socket.send(packet);
                //等待接收方的确认信息
                while(true){
                    confirmSocket.receive(confirmPacket);
                    if(confirmPacket.getLength() == 1){
                        break;
                    }
                }
            }

            //发送文件发送完毕的信息
            packet = new DatagramPacket(new byte[1024],0,InetAddress.getByName(ipStr),13141);
            socket.send(packet);

            SendFrame.appendSentMsg(srcfile.getName()+"文件发送完毕！\n");
            SendFrame.setIsSending(false);

            //关闭流和套接字等操作
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            end();
            if (socket != null || packet != null || confirmSocket != null || confirmPacket != null){
                socket = null;
                confirmSocket = null;
                packet = null;
                confirmPacket = null;
            }
        }
    }

    //关闭流和套接字
    public static void end(){
        if (fis != null){
            try{
                fis.close();
            }catch (Exception e){
                System.err.println("文件输入流关闭失败!");
            }
        }
        socket.close();
        confirmSocket.close();
    }
}
