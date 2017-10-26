package priv.braycep.receive;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receive {
    //Net
    private static DatagramSocket socketBegain;
    private static DatagramPacket packetBegain;

    private static DatagramPacket packet;
    private static DatagramSocket socket;

    //file io
    private static FileOutputStream fos;
    private static File dir;

    //start with download location
    public static void start(File dir){

    }
}
