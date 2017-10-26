package priv.braycep.receive;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ReceiveFrame extends JFrame{
    //containers
    private static JFrame sendFrame;
    private static JPanel panel;
    private static JLabel ipLbl;
    private static JTextField desIPTxf;
    private static JLabel fileInfoLbl;
    private static JLabel times;
    private static TextArea fileInfoTxa;
    private static JButton closeBtn;
    private static JButton chsFileBtn;
    private static JButton submitBtn;
    private static JButton cancelBtn;
    private static JFileChooser jFileChooser;

    //files
    protected static File chsFile;
    protected static int[] ips = new int[4];

    //flags
    private static boolean mousePressed = true;
    private static boolean isSending = false;

    public ReceiveFrame(){

    }
}
