package priv.braycep;

import java.awt.*;

public interface FrameSolution {
    public static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static final int WIDTH = toolkit.getScreenSize().width;
    public static final int HEIGHT = toolkit.getScreenSize().height;

    public static final int FRAME_X = WIDTH/3;
    public static final int FRAME_Y = HEIGHT/4;
    public static final int FRAME_WIDTH = WIDTH/4;
    public static final int FRAME_HEIGHT = HEIGHT/3;
}
