package priv.braycep.receive;

import priv.braycep.FrameSolution;
import priv.braycep.send.Send;
import priv.braycep.shareds.SharedMethods;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

public class ReceiveFrame extends JFrame{
    //组件
    private static JFrame receiveFrame;
    private static JPanel panel;
    private static JLabel ipLbl;
    private static JTextField desIPTxf;
    private static JLabel fileInfoLbl;
    private static JLabel times;
    private static JLabel percent;
    private static TextArea fileInfoTxa;
    private static JButton closeBtn;
    private static JButton chsFileBtn;
    private static JButton submitBtn;
    private static JButton cancelBtn;
    private static JFileChooser jFileChooser;

    //文件
    protected static File chsFile;
    protected static long fileSize = 0;

    //标志
    private static boolean mousePressed = true;
    private static boolean isReceiving = false;

    //other value
    private static int time = 0;

    //getter&setter
    protected static void appendFileInfo(String info){
        fileInfoTxa.append(info+"\n");
    }

    protected static void setSubmitBtnText(String msg){ submitBtn.setText(msg); }

    protected static void setIsReceiving(boolean Receiving){
        isReceiving = Receiving;
    }
    protected static boolean isReceiving(){
        return isReceiving;
    }

    //main
    public static void main(String[] args) {
        new ReceiveFrame().setVisible(true);
    }

    public ReceiveFrame(){
        receiveFrame = this;
        setTitle("文件接收端 v2.0");
        setLayout(new BorderLayout());
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(FrameSolution.FRAME_X, FrameSolution.FRAME_Y, FrameSolution.FRAME_WIDTH, FrameSolution.FRAME_HEIGHT);
        setUndecorated(true);

        //0,150,136
        panel = new JPanel(null);
        panel.setBackground(new Color(0,150,136));
        add(panel,BorderLayout.CENTER);

        //初始化所有组件
        initPanel();

        //所有事件
        events();

        //多个IP
        JOptionPane.showMessageDialog(this,"如果本机有多个IP，请使用与发送端在同一局域网的IP地址",
                "Warning",JOptionPane.WARNING_MESSAGE);
    }

    //components
    private void initPanel() {
        //close button
        closeBtn = new JButton();
        closeBtn.setBackground(new Color(0,150,136));
        closeBtn.setFont(new Font("",Font.PLAIN,5));
        closeBtn.setBorder(new LineBorder(new Color(0,150,136)));
        closeBtn.setBounds(FrameSolution.FRAME_WIDTH-20,0,20,20);
        panel.add(closeBtn);

        //ip tips
        ipLbl = new JLabel("本机IP:");
        ipLbl.setForeground(Color.white);
        ipLbl.setFont(new Font("",Font.BOLD,15));
        ipLbl.setBounds(25,20, FrameSolution.FRAME_WIDTH - 50,20);
        panel.add(ipLbl);

        //my ip
        desIPTxf = new JTextField(myIP());
        desIPTxf.setFont(new Font("courier new",Font.PLAIN,15));
        desIPTxf.setEditable(false);
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
        fileInfoLbl = new JLabel("保存路径:");
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
        fileInfoTxa.setBounds(25,110, FrameSolution.FRAME_WIDTH - 50,FrameSolution.FRAME_HEIGHT - 150);
        fileInfoTxa.setEditable(false);
        panel.add(fileInfoTxa);

        //sent button
        submitBtn = new JButton("确定");
        submitBtn.setFont(new Font("",Font.PLAIN,15));
        submitBtn.setHorizontalAlignment(SwingConstants.CENTER);
        submitBtn.setBackground(Color.white);
        submitBtn.setBorder(new LineBorder(new Color(100,100,100)));
        submitBtn.setBounds(25, FrameSolution.FRAME_HEIGHT - 28,75,20);
        panel.add(submitBtn);

        percent = new JLabel("0.00%");
        percent.setFont(new Font("",Font.BOLD,15));
        percent.setForeground(Color.white);
        percent.setBounds(FrameSolution.FRAME_WIDTH/2 - 30,FrameSolution.FRAME_HEIGHT-28,60,20);
        panel.add(percent);

        //cancel button
        cancelBtn = new JButton("退出");
        cancelBtn.setFont(new Font("",Font.PLAIN,15));
        cancelBtn.setHorizontalAlignment(SwingConstants.CENTER);
        cancelBtn.setBackground(Color.white);
        cancelBtn.setBorder(new LineBorder(new Color(100,100,100)));
        cancelBtn.setBounds(FrameSolution.FRAME_WIDTH - 100, FrameSolution.FRAME_HEIGHT - 28,75,20);
        panel.add(cancelBtn);
    }

    //events
    private void events() {
        //window close
        receiveFrame.addWindowListener(new WindowAdapter() {
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
        SharedMethods.windowDragMove(receiveFrame,panel);

        //choose dir
        chsFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (chsFileBtn.isEnabled()) {
                    jFileChooser = new JFileChooser("D:\\");
                    jFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
                    if (Locale.getDefault().getLanguage().toLowerCase().equals("zh")){
                        jFileChooser.setDialogTitle("保存到: ");
                        jFileChooser.showDialog(new JLabel(),"选择");
                    }else{
                        jFileChooser.setDialogTitle("Download Location: ");
                        jFileChooser.showDialog(new JLabel(),"Submit");
                    }
                    //user can find directory only
                    if (jFileChooser.getSelectedFile() == null) {
                        JOptionPane.showMessageDialog(receiveFrame,"请选择一个保存路径！","Warning",JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (!jFileChooser.getSelectedFile().canWrite()) {
                            JOptionPane.showMessageDialog(receiveFrame,"这个路径不能写入文件！");
                        } else {
                            chsFile = jFileChooser.getSelectedFile();
                            showChsFile(chsFile);
                        }
                    }
                }
            }
        });

        //submit
        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (submitBtn.isEnabled()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (chsFile == null){
                                JOptionPane.showMessageDialog(receiveFrame,"请选择一个保存路径！","Warning",JOptionPane.WARNING_MESSAGE);
                            } else {
                                SharedMethods.changeSubmitBtnStatus(submitBtn,chsFileBtn,false);
                                submitBtn.setText("等待中");
                                Receive.start(chsFile);
                            }
                        }
                    }).start();
                }
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

    /**
     * get self ip address
     * @return return Self host IP (IPv4)
     */
    private String myIP() {
        String ip = null;
        /*try {
            Process process = Runtime.getRuntime().exec("ipconfig");
            String line = null;
            String[] result = null;
            InputStream bis = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(bis));
            while ((line = br.readLine()) != null){
                if (!line.contains("VPN") && (!line.contains("VM") && (line.contains("IPv4")))){
                    result = line.split("\\:");
                }
            }
            if (result != null){
                ip = result[result.length - 1].trim();
            } else {
                JOptionPane.showMessageDialog(receiveFrame,"Network Unreachable!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * for confirm your exit option
     */
    private void confirmExit() {
        if (isReceiving){
            Object[] options = { "确定", "取消" };
            int status = JOptionPane.showOptionDialog(receiveFrame, "确定退出？",
                    "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            //user select value 0 = OK , 1 = Cancel
            if (status == 0){
                System.exit(1);
            }
        } else {
            System.exit(0);
        }
    }

    /**
     * count time when transmiting
     */
    protected static  void countTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isReceiving){
                    try{
                        Thread.sleep(1000);
                        time++;
                        times.setText(time+" S");
                        percent.setText(SharedMethods.formatDoubleValue(Receive.trgFile.length(),(fileSize*0.01),2)+"%");
                    }catch (InterruptedException i){
                        i.printStackTrace();
                    }
                }
                fileInfoTxa.append("使用时间: "+time+" 秒\n");
                percent.setText("100.0%");
                SharedMethods.changeSubmitBtnStatus(submitBtn,chsFileBtn,true);
                submitBtn.setText("确定");
            }
        }).start();
    }

    /**
     * to show the directory that you have chosen
     * @param chsFile
     */
    private void showChsFile(File chsFile) {
        if (chsFile == null){
            JOptionPane.showMessageDialog(receiveFrame,"请选择一个保存路径","Warning",JOptionPane.WARNING_MESSAGE);
            fileInfoTxa.setText("");
        } else {
            fileInfoTxa.setText("保存路径: "+chsFile.getAbsolutePath());
        }
    }
}
