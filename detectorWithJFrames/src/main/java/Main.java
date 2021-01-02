
// import nu.pattern.OpenCV;
import vision.Frame;

import javax.swing.JFrame;

import org.opencv.core.Core;

import nu.pattern.OpenCV;

import java.awt.EventQueue;

/* The main class will open the camera, keep the camera running indefinetily and sends the data to the client */

/* Source: https://opencv-java-tutorials.readthedocs.io/en/latest/03-first-javafx-application-with-opencv.html#working-with-scene-builder */

/* Used Code source: http://rapidprogrammer.com/how-to-access-camera-with-opencv-and-java
    - Mat2Image.java
    - App(), paint(), myThread()
*/

/* Interesting Code source:
    - https://github.com/eugenp/tutorials/blob/master/image-processing/src/main/java/com/baeldung/imageprocessing/opencv/FaceDetection.java

    - Face detection on pictures
*/

public class Main extends JFrame {
    private static final long serialVersionUID = -7187650202518938678L;
    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    Frame frame = new Frame();
                    frame.setVisible(true);
                    while(true){
                        /* ugly wau because repaint in Frame class isnt working */
                        frame.paint(frame.getGraphics());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
