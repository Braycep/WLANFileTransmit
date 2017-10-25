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
            srcfile = SendFrame.getChsFile();
            fis = new FileInputStream(srcfile);
            socket = new DatagramSocket();
            int len;
            byte[] buffer = new byte[32768];
            //receive confirm messege
            confirmSocket = new DatagramSocket(13142);
            confirmPacket = new DatagramPacket(buffer,buffer.length);
            while ((len = fis.read(buffer)) != -1){
                packet = new DatagramPacket(buffer,len, InetAddress.getByName(ipStr),13141);
                socket.send(packet);
                while(true){
                    confirmSocket.receive(confirmPacket);
                    if(confirmPacket.getLength() == 1){
                        break;
                    }
                }
            }
            //send last one message
            packet = new DatagramPacket(buffer,0,InetAddress.getByName(ipStr),13141);
            socket.send(packet);
            SendFrame.appendSentMsg(srcfile.getName()+" Sent Over.");
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

            }
        }
        socket.close();
        confirmSocket.close();
        SendFrame.setIsSending(false);
    }
}
