package zmq;

import nu.pattern.OpenCV;

import javax.swing.JFrame;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;

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

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Frame frame = new Frame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // try (ZContext context = new ZContext()) {

        //     Socket socket = context.createSocket(SocketType.PUSH);
        //     socket.bind("tcp://*:5555");

        //     System.out.println("Server is ready listening on port 5555");;

        //     while (!Thread.currentThread().isInterrupted()) {
        //         // Send a response
        //         String response = "Hello, Camera Person!";

        //         socket.send(response.getBytes(ZMQ.CHARSET), 0);
        //     }
        // }
    }
}