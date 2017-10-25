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

    private Send(){}

    public static void start(){
        try {
            srcfile = SendFrame.getChsFile();
            fis = new FileInputStream(srcfile);
            socket = new DatagramSocket();
            int len;
            byte[] buffer = new byte[102400];
            confirmSocket = new DatagramSocket(13142);
            confirmPacket = new DatagramPacket(buffer,buffer.length);
            while ((len = fis.read(buffer)) != -1){
                packet = new DatagramPacket(buffer,len, InetAddress.getByName(
                        ip[0]+"."+ip[1]+"."+ip[2]+"."+ip[3]),13141);
                socket.send(packet);
                while(true){
                    confirmSocket.receive(confirmPacket);
                    if(confirmPacket.getLength() == 1){
                        break;
                    }
                }
            }
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
    }
}
