package priv.braycep.send;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Send {
    //file and io
    private static File srcfile;
    private static FileInputStream fis;
    private static DatagramPacket packet;
    private static DatagramSocket socket;
    private static DatagramPacket confirmPacket;
    private static DatagramSocket confirmSocket;

    private static int[] ip = SendFrame.getIps();
    private static String ipStr = ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];
    private Send(){}

    public static void start(){
        try {
            //get Target IP
            ip = SendFrame.getIps();
            ipStr = ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3];

            //get src File
            srcfile = SendFrame.getChsFile();
            fis = new FileInputStream(srcfile);
            socket = new DatagramSocket();
            int len;
            byte[] buffer = new byte[32768];

            //to receive confirm messege from a reveiver
            confirmSocket = new DatagramSocket(13142);
            confirmPacket = new DatagramPacket(buffer,0,buffer.length);

            //sent src file name
            new DatagramSocket().send(new DatagramPacket(srcfile.getName().getBytes(),srcfile.getName().getBytes().length,
                    InetAddress.getByName("255.255.255.255"),13141));
            while (true){
                DatagramPacket tmpPacket = new DatagramPacket(new byte[1024],0,1024);
                new DatagramSocket(13143).receive(tmpPacket);
                if (tmpPacket.getLength() > 0){
                    break;
                }
            }
            //begain to sent the src file
            while ((len = fis.read(buffer)) != -1){
                packet = new DatagramPacket(buffer,len, InetAddress.getByName(ipStr),13141);
                socket.send(packet);
                //get target's reply message
                while(true){
                    confirmSocket.receive(confirmPacket);
                    if(confirmPacket.getLength() == 1){
                        break;
                    }
                }
            }

            //send the lastest message
            packet = new DatagramPacket(buffer,0,InetAddress.getByName(ipStr),13141);
            socket.send(packet);
            SendFrame.appendSentMsg(srcfile.getName()+" Sent Over.\n");
            SendFrame.setIsSending(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //end
    public static void end(){
        if (fis != null){
            try{
                fis.close();
            }catch (Exception e){
                System.err.println("FileInputStream fis Close Error!");
            }
        }
        socket.close();
        confirmSocket.close();
    }
}
