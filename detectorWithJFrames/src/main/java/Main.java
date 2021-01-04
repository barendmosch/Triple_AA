
// import nu.pattern.OpenCV;
import vision.Frame;

import javax.swing.JFrame;

import org.opencv.core.Core;

import nu.pattern.OpenCV;

import java.awt.EventQueue;

/* The main class will open the camera, keep the camera running indefinetily and sends the data to the client */

public class Main extends JFrame {
    private static final long serialVersionUID = -7187650202518938678L;
    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    Frame frame = new Frame();
                    frame.setVisible(true);
                    while(true){
                        frame.paint(frame.getGraphics());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
