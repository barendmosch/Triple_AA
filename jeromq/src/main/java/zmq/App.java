package zmq;

import org.zeromq.ZMQ;

import nu.pattern.OpenCV;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.video.*;
import org.opencv.videoio.VideoCapture;

/* The main class will open the camera, keep the camera running indefinetily and sends the data to the client */

/* Source: https://opencv-java-tutorials.readthedocs.io/en/latest/03-first-javafx-application-with-opencv.html#working-with-scene-builder */
public class App {

    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally
        Camera camera= new Camera();

        camera.doSomeVideoShit();

        // try (ZContext context = new ZContext()) {

        //     ZMQ.Socket socket = context.createSocket(SocketType.PUSH);
        //     socket.bind("tcp://*:5555");

        //     System.out.println("Server is ready listening on port 5555");;

        //     while (!Thread.currentThread().isInterrupted()) {
        //         // Send a response
        //         String response = "Hello, world!";

        //         socket.send(response.getBytes(ZMQ.CHARSET), 0);
        //     }
        // }
    }
}
