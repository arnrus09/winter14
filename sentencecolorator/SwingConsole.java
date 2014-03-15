package sentencecolorator;

//from Bruce Eckel (http://www.mindview.net)

//Tool for running Swing demos from the
//console, both applets and JFrames.
import javax.swing.*;

public class SwingConsole {
public static void
run(final JFrame f, final int width, final int height) {
 SwingUtilities.invokeLater(new Runnable() {
   public void run() {
     f.setTitle(f.getClass().getSimpleName());
     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     f.setSize(width, height);
     f.setVisible(true);
   }
 });
}
}
