import nu.pattern.OpenCV;
import vision.FrameController;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Socket;

import java.awt.image.BufferedImage;
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

public class Main {
    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try (ZContext context = new ZContext()) {
                    int i = 0;
                    
                    /* Make a new controller. */
                    FrameController controller = new FrameController();

                    /* Create a ZMQ PUSH socket to PUSH data to the client */
                    Socket socket = context.createSocket(SocketType.PUSH);
                    socket.bind("tcp://*:5555");

                    System.out.println("Server is ready listening on port 5555");;

                    /* Run indefinitely untill the program stops. While running, keep calling the findFace method to read the frames from the video stream
                        and run the facedetection algorithm on it. returns true if a face is found (face array != 0)
                        If face is found, get the current image in BufferedImage format by getting the current Mat and transforming it into an image.
                        Transform the image in a byte array so that it can be sent to the client.
                        The client can run the recognition software in the incoming frames. */
                    while(!Thread.currentThread().isInterrupted()){
                        if(controller.findFace()){
                            BufferedImage image = controller.getImage();
                            BufferedImage imageOfFace = controller.cropImage(image);
                            byte[] imageInBytes = controller.getImageByteArray(imageOfFace);

                            /* THIS IS JUST FOR MAKING FOTOS OF THE FRAME WITH THE FACE */
                            // controller.saveImage(i);
                            // controller.saveFace(i);

                            System.out.println("From main: " + imageInBytes);

                            i++;

                            ZMsg frameDataMsg = new ZMsg();
                            frameDataMsg.add(imageInBytes);
                            frameDataMsg.send(socket);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}