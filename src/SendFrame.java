import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class SendFrame extends JFrame{
    private static JFrame sendFrame;
    private static JPanel panel;
    private static JLabel chsFileLbl;
    private static JLabel fileInfoLbl;
    private static JTextField chsFileTxf;
    private static JTextArea fileInfoTxa;
    private static JButton closeBtn;
    private static JButton chsFileBtn;
    private static JButton submitBtn;
    private static JButton cancelBtn;

    private static boolean mousePressed = true;

    public static void main(String[] args) {
        new SendFrame().setVisible(true);
    }

    public SendFrame(){
        sendFrame = this;
        setTitle("SendFrame v1.0");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(ScreenSize.FRAME_X,ScreenSize.FRAME_Y,ScreenSize.FRAME_WIDTH,ScreenSize.FRAME_HEIGHT);
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
        closeBtn.setBackground(new Color(255,0,0));
        closeBtn.setFont(new Font("",Font.PLAIN,5));
        closeBtn.setBorder(new LineBorder(new Color(0,150,136)));
        closeBtn.setBounds(ScreenSize.FRAME_WIDTH-20,0,20,20);
        panel.add(closeBtn);

        //choose file's tip
        chsFileLbl = new JLabel("Choose Your File :");
        chsFileLbl.setForeground(Color.white);
        chsFileLbl.setFont(new Font("",Font.BOLD,15));
        chsFileLbl.setBounds(25,20,ScreenSize.FRAME_WIDTH - 50,20);
        panel.add(chsFileLbl);

        //show chosen file
        chsFileTxf = new JTextField("");
        chsFileTxf.setBounds(25,50,ScreenSize.FRAME_WIDTH - 50,20);
        chsFileTxf.setBorder(new LineBorder(new Color(100,100,100)));
        panel.add(chsFileTxf);

        //choose file button
        chsFileBtn = new JButton("> > >");
        chsFileBtn.setBackground(Color.white);
        chsFileBtn.setFont(new Font("",Font.PLAIN,15));
        chsFileBtn.setBorder(new LineBorder(new Color(100,100,100)));
        chsFileBtn.setBounds(ScreenSize.FRAME_WIDTH - 100,80,75,20);
        panel.add(chsFileBtn);

        //file info label
        fileInfoLbl = new JLabel("Your File :");
        fileInfoLbl.setFont(new Font("",Font.BOLD,15));
        fileInfoLbl.setForeground(Color.white);
        fileInfoLbl.setBounds(25,80,ScreenSize.FRAME_WIDTH - 50,20);
        panel.add(fileInfoLbl);

        //file info textares
        fileInfoTxa = new JTextArea();
        fileInfoTxa.setFont(new Font("",Font.PLAIN,15));
        fileInfoTxa.setBackground(Color.white);
        fileInfoTxa.setForeground(Color.black);
        fileInfoTxa.setBorder(new LineBorder(new Color(100,100,100)));
        fileInfoTxa.setBounds(25,110,ScreenSize.FRAME_WIDTH - 50,ScreenSize.FRAME_HEIGHT/2);
        panel.add(fileInfoTxa);

        //sent button
        submitBtn = new JButton("Submit");
        submitBtn.setFont(new Font("",Font.PLAIN,15));
        submitBtn.setHorizontalAlignment(SwingConstants.CENTER);
        submitBtn.setBackground(Color.white);
        submitBtn.setBorder(new LineBorder(new Color(100,100,100)));
        submitBtn.setBounds(25,ScreenSize.FRAME_HEIGHT - 28,75,20);
        panel.add(submitBtn);

        //cancel button
        //sent button
        cancelBtn = new JButton("Cancle");
        cancelBtn.setFont(new Font("",Font.PLAIN,15));
        cancelBtn.setHorizontalAlignment(SwingConstants.CENTER);
        cancelBtn.setBackground(Color.white);
        cancelBtn.setBorder(new LineBorder(new Color(100,100,100)));
        cancelBtn.setBounds(ScreenSize.FRAME_WIDTH - 100,ScreenSize.FRAME_HEIGHT - 28,75,20);
        panel.add(cancelBtn);
    }

    private void events() {
        //close
        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.exit(1);
            }
        });

        //move
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
    }
}
