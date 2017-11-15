package priv.braycep.send;

import priv.braycep.FrameSolution;
import priv.braycep.shareds.SharedMethods;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Locale;

public class SendFrame extends JFrame{
    //组件
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

    //f文件
    protected static File chsFile;
    protected static int[] ips = new int[4];

    //标志
    private static boolean mousePressed = true;
    private static boolean isSending = false;
    private static boolean submitBtnClicked = false;

    //计时器
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

    protected static void setSubmitBtnText(String msg){
        submitBtn.setText(msg);
    }

    public static void appendSentMsg(String msg){
        fileInfoTxa.append("进度: "+msg);
    }

    public static void main(String[] args) {
        new SendFrame().setVisible(true);
    }

    public SendFrame(){
        sendFrame = this;
        setTitle("文件发送端 v2.0");
        setLayout(new BorderLayout());
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(FrameSolution.FRAME_X, FrameSolution.FRAME_Y, FrameSolution.FRAME_WIDTH, FrameSolution.FRAME_HEIGHT);
        setUndecorated(true);

        //0,150,136
        panel = new JPanel(null);
        panel.setBackground(new Color(0,150,136));
        add(panel,BorderLayout.CENTER);

        //对所有组件的初始化
        initPanel();

        //所有事件
        events();
    }

    private void initPanel() {
        //关闭按钮
        closeBtn = new JButton();
        closeBtn.setBackground(new Color(0,150,136));
        closeBtn.setFont(new Font("",Font.PLAIN,5));
        closeBtn.setBorder(new LineBorder(new Color(0,150,136)));
        closeBtn.setBounds(FrameSolution.FRAME_WIDTH-20,0,20,20);
        panel.add(closeBtn);

        //IP提示信息
        ipLbl = new JLabel("目标IP地址:");
        ipLbl.setForeground(Color.white);
        ipLbl.setFont(new Font("",Font.BOLD,15));
        ipLbl.setBounds(25,20, FrameSolution.FRAME_WIDTH - 50,20);
        panel.add(ipLbl);

        //IP输入框
        desIPTxf = new JTextField("255.255.255.255");
        desIPTxf.setFont(new Font("courier new",Font.PLAIN,15));
        desIPTxf.setBackground(Color.white);
        desIPTxf.setForeground(Color.black);
        desIPTxf.setHorizontalAlignment(SwingConstants.CENTER);
        desIPTxf.setBounds(25,50, FrameSolution.FRAME_WIDTH - 50,20);
        desIPTxf.setBorder(new LineBorder(new Color(100,100,100)));
        panel.add(desIPTxf);

        //文件选择按钮
        chsFileBtn = new JButton("> > >");
        chsFileBtn.setBackground(Color.white);
        chsFileBtn.setFont(new Font("",Font.PLAIN,15));
        chsFileBtn.setBorder(new LineBorder(new Color(100,100,100)));
        chsFileBtn.setBounds(FrameSolution.FRAME_WIDTH - 100,80,75,20);
        panel.add(chsFileBtn);

        //文件信息提示标签
        fileInfoLbl = new JLabel("选择的文件:");
        fileInfoLbl.setFont(new Font("",Font.BOLD,15));
        fileInfoLbl.setForeground(Color.white);
        fileInfoLbl.setBounds(25,80, FrameSolution.FRAME_WIDTH - 50,20);
        panel.add(fileInfoLbl);

        //显示计时
        times = new JLabel();
        times.setForeground(Color.white);
        times.setHorizontalAlignment(SwingConstants.CENTER);
        times.setBounds(FrameSolution.FRAME_WIDTH - 175,80,75,20);
        panel.add(times);

        //文件信息显示框
        fileInfoTxa = new TextArea("",0,0,TextArea.SCROLLBARS_BOTH);
        fileInfoTxa.setFont(new Font("",Font.PLAIN,10));
        fileInfoTxa.setBackground(Color.white);
        fileInfoTxa.setForeground(Color.black);
        fileInfoTxa.setBounds(25,110, FrameSolution.FRAME_WIDTH - 50,FrameSolution.FRAME_HEIGHT - 150);
        fileInfoTxa.setEditable(false);
        panel.add(fileInfoTxa);

        //发送按钮
        submitBtn = new JButton("确定");
        submitBtn.setFont(new Font("",Font.PLAIN,15));
        submitBtn.setHorizontalAlignment(SwingConstants.CENTER);
        submitBtn.setBackground(Color.white);
        submitBtn.setBorder(new LineBorder(new Color(100,100,100)));
        submitBtn.setBounds(25, FrameSolution.FRAME_HEIGHT - 28,75,20);
        panel.add(submitBtn);

        //取消按钮
        cancelBtn = new JButton("退出");
        cancelBtn.setFont(new Font("",Font.PLAIN,15));
        cancelBtn.setHorizontalAlignment(SwingConstants.CENTER);
        cancelBtn.setBackground(Color.white);
        cancelBtn.setBorder(new LineBorder(new Color(100,100,100)));
        cancelBtn.setBounds(FrameSolution.FRAME_WIDTH - 100, FrameSolution.FRAME_HEIGHT - 28,75,20);
        panel.add(cancelBtn);
    }

    private void events() {
        //窗口关闭操作
        sendFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SharedMethods.confirmExit(sendFrame);
            }
        });

        //按钮关闭操作
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
                SharedMethods.confirmExit(sendFrame);
            }
        });

        //拖拽移动
        SharedMethods.windowDragMove(sendFrame,panel);

        //选择文件
        chsFileBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (chsFileBtn.isEnabled()){
                    jFileChooser = new JFileChooser("D:\\");
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    if (Locale.getDefault().getLanguage().toLowerCase().equals("zh")){
                        jFileChooser.showDialog(new JLabel(),"选择");
                    }else{
                        jFileChooser.showDialog(new JLabel(),"Choose");
                    }
                    //if not chosen jFileChooser return null
                    if (jFileChooser.getSelectedFile() == null){
                        JOptionPane.showMessageDialog(sendFrame,"请选择一个文件！","Warning",JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (jFileChooser.getSelectedFile().isDirectory()){
                            JOptionPane.showMessageDialog(sendFrame,"请选择文件！");
                        } else {
                            chsFile = jFileChooser.getSelectedFile();
                            showChsFile(chsFile);
                        }
                    }
                }
            }
        });

        //确认
        submitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (submitBtn.isEnabled()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (chsFile == null){
                                JOptionPane.showMessageDialog(sendFrame,"请选择文件！","Warning",JOptionPane.WARNING_MESSAGE);
                            } else {
                                Object[] objects = new Object[]{"OK","Cancel"};
                                int status = JOptionPane.showOptionDialog(sendFrame,"请确保有接收端正在等待接收！","Warning",JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.WARNING_MESSAGE,null,objects,objects[1]);
                                if (status == 0 && getIP()){
                                    submitBtnClicked = true;
                                    SharedMethods.changeSubmitBtnStatus(submitBtn,chsFileBtn,false);
                                    //JOptionPane.showMessageDialog(sendFrame,"Make Sure the Receiver Is Waitting!","Warning",JOptionPane.WARNING_MESSAGE);
                                    isSending = true;
                                    countTime();
                                    Send.start();
                                }
                            }
                        }
                    }).start();
                }
            }
        });


        //取消
        cancelBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SharedMethods.confirmExit(sendFrame);
            }
        });
    }

    //计时方法
    private void countTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isSending){
                    try{
                        Thread.sleep(1000);
                        time++;
                        times.setText(time+" S");
                    }catch (InterruptedException i){
                        i.printStackTrace();
                    }
                }
                fileInfoTxa.append("使用时间: "+time+" S\n");
                submitBtn.setText("发送");
                SharedMethods.changeSubmitBtnStatus(submitBtn,chsFileBtn,true);
            }
        }).start();
    }

    //获取ip
    private boolean getIP() {
        String[] ip = desIPTxf.getText().split("\\.");
        try{
            for(int i = 0;i<ips.length;i++){
                ips[i] = Integer.parseInt(ip[i]);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        for(int i:ips){
            if (i < 0 || i > 255){
                JOptionPane.showMessageDialog(sendFrame, "IP 地址不正确！", "Warning", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    //显示选择的文件信息
    private void showChsFile(File chsFile) {
        String[] name = chsFile.getName().split("\\.");
        fileInfoTxa.setText("文件名: "+chsFile.getName()+"\n");
        fileInfoTxa.append("扩展名: "+name[name.length-1]+"\n");
        long len = chsFile.length();
        fileInfoTxa.append("文件大小: "+SharedMethods.formatFileSize(len)+"\n");
        fileInfoTxa.append("本地路径: "+chsFile.getAbsolutePath()+"\n");
    }
}
