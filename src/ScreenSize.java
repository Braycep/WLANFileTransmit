import java.awt.*;

public interface ScreenSize {
    public static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static final int WIDTH = toolkit.getScreenSize().width;
    public static final int HEIGHT = toolkit.getScreenSize().height;
}
