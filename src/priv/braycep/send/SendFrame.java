package priv.braycep.send;

import priv.braycep.FrameSolution;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;

public class SendFrame extends JFrame{
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

    //other value
    private static int time = 0;

    public static JFrame getSendFrame(){
        return sendFrame;
    }

    public static File getChsFile(){
        return chsFile;
    }

    public static int[] getIps(){
        return ips;
    }

    public static void setIsSending(boolean Sending){
        isSending = Sending;
    }

    public static void appendSentMsg(String msg){
        fileInfoTxa.append("priv.braycep.send.Send Status :"+msg);
    }

    public static void main(String[] args) {
        new SendFrame().setVisible(true);
    }

    public SendFrame(){
        sendFrame = this;
        setTitle("priv.braycep.send.SendFrame v1.0");
        setLayout(new BorderLayout());
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(FrameSolution.FRAME_X, FrameSolution.FRAME_Y, FrameSolution.FRAME_WIDTH, FrameSolution.FRAME_HEIGHT);
        setUndecorated(true);

        //0,150,136
        panel = new JPanel(null);
        panel.setBackground(new Color(0,150,136));
        add(panel,BorderLayout.CENTER);

        //containers
        initPanel();

        //Events
        events();
    }

    private void initPanel() {
        //close button
        closeBtn = new JButton();
        closeBtn.setBackground(new Color(0,150,136));
        closeBtn.setFont(new Font("",Font.PLAIN,5));
        closeBtn.setBorder(new LineBorder(new Color(0,150,136)));
        closeBtn.setBounds(FrameSolution.FRAME_WIDTH-20,0,20,20);
        panel.add(closeBtn);

        //ip tips
        ipLbl = new JLabel("Target IP Address :");
        ipLbl.setForeground(Color.white);
        ipLbl.setFont(new Font("",Font.BOLD,15));
        ipLbl.setBounds(25,20, FrameSolution.FRAME_WIDTH - 50,20);
        panel.add(ipLbl);

        //input des ip
        desIPTxf = new JTextField("255.255.255.255");
        desIPTxf.setFont(new Font("courier new",Font.PLAIN,15));
        desIPTxf.setBackground(Color.white);
        desIPTxf.setForeground(Color.black);
        desIPTxf.setHorizontalAlignment(SwingConstants.CENTER);
        desIPTxf.setBounds(25,50, FrameSolution.FRAME_WIDTH - 50,20);
        desIPTxf.setBorder(new LineBorder(new Color(100,100,100)));
        panel.add(desIPTxf);

        //choose file button
        chsFileBtn = new JButton("> > >");
        chsFileBtn.setBackground(Color.white);
        chsFileBtn.setFont(new Font("",Font.PLAIN,15));
        chsFileBtn.setBorder(new LineBorder(new Color(100,100,100)));
        chsFileBtn.setBounds(FrameSolution.FRAME_WIDTH - 100,80,75,20);
        panel.add(chsFileBtn);

        //file info label
        fileInfoLbl = new JLabel("Your File :");
        fileInfoLbl.setFont(new Font("",Font.BOLD,15));
        fileInfoLbl.setForeground(Color.white);
        fileInfoLbl.setBounds(25,80, FrameSolution.FRAME_WIDTH - 50,20);
        panel.add(fileInfoLbl);

        //show times
        times = new JLabel();
        times.setForeground(Color.white);
        times.setHorizontalAlignment(SwingConstants.CENTER);
        times.setBounds(FrameSolution.FRAME_WIDTH - 175,80,75,20);
        panel.add(times);

        //file info textares
        fileInfoTxa = new TextArea("",0,0,TextArea.SCROLLBARS_BOTH);
        fileInfoTxa.setFont(new Font("",Font.PLAIN,10));
        fileInfoTxa.setBackground(Color.white);
        fileInfoTxa.setForeground(Color.black);
        fileInfoTxa.setBounds(25,110, FrameSolution.FRAME_WIDTH - 50,105);
        fileInfoTxa.setEditable(false);
        panel.add(fileInfoTxa);

        //sent button
        submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("",Font.PLAIN,15));
        submitBtn.setHorizontalAlignment(SwingConstants.CENTER);
        submitBtn.setBackground(Color.white);
        submitBtn.setBorder(new LineBorder(new Color(100,100,100)));
        submitBtn.setBounds(25, FrameSolution.FRAME_HEIGHT - 28,75,20);
        panel.add(submitBtn);

        //cancel button
        cancelBtn = new JButton("Cancle");
        cancelBtn.setFont(new Font("",Font.PLAIN,15));
        cancelBtn.setHorizontalAlignment(SwingConstants.CENTER);
        cancelBtn.setBackground(Color.white);
        cancelBtn.setBorder(new LineBorder(new Color(100,100,100)));
        cancelBtn.setBounds(FrameSolution.FRAME_WIDTH - 100, FrameSolution.FRAME_HEIGHT - 28,75,20);
        panel.add(cancelBtn);
    }

    private void events() {
        //window close
        sendFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        //close operation
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(Color.red);
            }
        });
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(new Color(0,150,136));
            }
        });
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                confirmExit();
            }
        });

        //move and drag
        new Thread(new Runnable() {
            @Override
            public void run() {
                panel.addMouseMotionListener(new MouseMotionAdapter() {
                    int orgX = -1 , orgY = -1;
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (!mousePressed){
                            orgX = -1;
                            orgY = -1;
                            mousePressed = true;
                        }
                        if(orgX == -1 && orgY == -1){
                            orgX = e.getX();
                            orgY = e.getY();
                        }
                        int x = e.getX() - orgX + sendFrame.getLocation().x;
                        int y = e.getY() - orgY + sendFrame.getLocation().y;
                        sendFrame.setLocation(x,y);
                    }
                });
            }
        }).start();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }
        });

        //choose file
        chsFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                jFileChooser = new JFileChooser("D:\\");
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (Locale.getDefault().getLanguage().toLowerCase().equals("zh")){
                    jFileChooser.showDialog(new JLabel(),"选择");
                }else{
                    jFileChooser.showDialog(new JLabel(),"Choose");
                }
                //if not chosen jFileChooser return null
                chsFile = jFileChooser.getSelectedFile();
                showChsFile(chsFile);
            }
        });

        //submit
        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (chsFile == null){
                            JOptionPane.showMessageDialog(sendFrame,"Please Choose Your File First!","Warning",JOptionPane.WARNING_MESSAGE);
                        } else {
                            getIP();
                            isSending = true;
                            countTime();
                            Send.start();
                        }
                    }
                }).start();
            }
        });

        //cancel
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                confirmExit();
            }
        });
    }

    private void confirmExit() {
        if (isSending){
            Object[] options = { "OK", "CANCEL" };
            int status = JOptionPane.showOptionDialog(sendFrame, "Click OK to Continue",
                    "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            //user select value 0 = OK , 1 = Cancel
            if (status == 0){
                System.exit(1);
                Send.end();
            }
        } else {
            System.exit(0);
        }
    }

    private void countTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isSending){
                    try{
                        times.setText(time+" S");
                        time++;
                        Thread.sleep(1000);
                    }catch (InterruptedException i){
                        i.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void getIP() {
        String[] ip = desIPTxf.getText().split("\\.");
        try{
            for(int i = 0;i<ips.length;i++){
                ips[i] = Integer.parseInt(ip[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i:ips){
            if (i < 0 || i > 255){
                JOptionPane.showMessageDialog(sendFrame, "IP Address Incorrect!", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showChsFile(File chsFile) {
        if (chsFile == null){
            fileInfoLbl.setText("File Not Found");
        } else {
            String[] name = chsFile.getName().split("\\.");
            fileInfoTxa.setText("File Name: "+chsFile.getName()+"\n");
            fileInfoTxa.append("Expanded-name: "+name[name.length-1]+"\n");
            long len = chsFile.length();
            if (len < 10240){
                fileInfoTxa.append("Size: "+(chsFile.length())+" B\n");
            }else if (len >= 10240 && len < 1024000) {
                //10K~1000K
                fileInfoTxa.append("Size: "+(chsFile.length()/1024)+" Kb\n");
            }else if (len >= 1024000 && len < 102400000) {
                fileInfoTxa.append("Size: "+(chsFile.length()/1024/1024)+" Mb\n");
            }else {
                fileInfoTxa.append("Size: "+(chsFile.length()/1024/1024/1024)+" Gb\n");
            }
            fileInfoTxa.append("Location: "+chsFile.getAbsolutePath()+"\n");
        }
    }
}
