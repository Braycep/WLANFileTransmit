import javax.swing.*;
import java.awt.*;

public class SendFrame extends JFrame{
    private static JFrame sendFrame;
    private static JPanel panel;
    private static JLabel chsFileLbl;
    private static JLabel fileInfoLbl;
    private static JTextField chsFileTxf;
    private static JTextArea fileInfoTxa;
    private static JButton submitBtn;
    private static JButton cancelBtn;

    public static void main(String[] args) {
        new SendFrame().setVisible(true);
    }

    public SendFrame(){
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(ScreenSize.WIDTH/3, ScreenSize.HEIGHT/4,ScreenSize.WIDTH/4,ScreenSize.HEIGHT/3);
        //setUndecorated(true);

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

    }

    private void events() {

    }
}
