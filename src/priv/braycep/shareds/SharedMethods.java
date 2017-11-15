package priv.braycep.shareds;

import priv.braycep.send.Send;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.text.DecimalFormat;

public class SharedMethods {
    private static boolean mouseReleased = false;
    private static boolean isSending = false;

    /**
     * 当结果小于0时，没有整数部分
     * @param dividend 被除数
     * @param divsior 除数
     * @param nums 要保留的小数位数
     * @return 返回#.00此类格式
     */
    public static String formatDoubleValue(long dividend,double divsior,int nums){
        String n = "#.";
        if (nums >= 1){
            for (int i = 0;i < nums;i++){
                n+="0";
            }
        }
        return new DecimalFormat(n).format(dividend/(divsior*1.0));
    }

    /**
     * 设置确认按钮的状态
     * @param submitBtn 传入需要设置的确认btn
     * @param chsFileBtn 传入需要设置的文件选择btn
     * @param flag  btn状态
     */
    public static void changeSubmitBtnStatus(JButton submitBtn,JButton chsFileBtn,boolean flag){
        submitBtn.setEnabled(flag);
        chsFileBtn.setEnabled(flag);
    }

    /**
     * 对文件大小进行格式化
     * @param len 传入的文件大小
     * @return 返回被格式化的文件大小
     */
    public static String formatFileSize(long len) {
        if (len < 1024){
            return len+" B";
        }else if (len >= 1024 && len < 1024000) {
            //1K~1000K
            return formatDoubleValue(len,1024,2)+" Kb";
        }else if (len >= 1024000 && len < 1024000000) {
            //1M~1000M
            return formatDoubleValue(len,1048576,2)+" Mb";
        }else {
            //1G~1000G
            return formatDoubleValue(len,1073741824,2)+" Gb";
        }
    }

    /**
     * 拖拽移动
     * @param frame
     * @param panel
     */
    public static void windowDragMove(JFrame frame,JPanel panel){

        new Thread(new Runnable() {
            @Override
            public void run() {
                panel.addMouseMotionListener(new MouseMotionAdapter() {
                    int orgX = -1 , orgY = -1;
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (!mouseReleased){
                            orgX = -1;
                            orgY = -1;
                            mouseReleased = true;
                        }
                        if(orgX == -1 && orgY == -1){
                            orgX = e.getX();
                            orgY = e.getY();
                        }
                        int x = e.getX() - orgX + frame.getLocation().x;
                        int y = e.getY() - orgY + frame.getLocation().y;
                        frame.setLocation(x,y);
                    }
                });

                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mouseReleased = false;
                    }
                });
            }
        }).start();
    }

    /**
     * 确认退出
     * @param frame
     */
    public static void confirmExit(JFrame frame) {
        if (isSending){
            Object[] options = { "确定", "取消" };
            int status = JOptionPane.showOptionDialog(frame, "确认退出？",
                    "警告", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
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
}
