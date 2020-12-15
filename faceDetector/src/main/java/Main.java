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
                    /* Make a new controller. */
                    FrameController controller = new FrameController();

                    /* Create a ZMQ PUSH socket to PUSH data to the client */
                    Socket socket = context.createSocket(SocketType.PUSH);
                    socket.bind("tcp://*:5555");

                    System.out.println("Server is ready listening on port 5555");;   

                    // Process p = Runtime.getRuntime().exec("python /Users/barendmosch/source/repos/ZeroMQ_ws/faceDetector/src/main/python/move_around.py");

                    /* Run indefinitely untill the program stops. While running, 
                        - keep calling the findFace method to read the frames from the video stream and run the Viola and Jones algorithm. 
                         - If face is found, transform the current Mat to BufferedImage 
                         - crop the image so only the face is sent
                         - transform the image into a byte array
                         - send the byte through the socket so that the recognition software can pick it up */
                    while(!Thread.currentThread().isInterrupted()){
                        if(controller.findFace()){
                            /* Processes are the Python scripts only */
                            // p.destroy();
                            // Process process = Runtime.getRuntime().exec("python /Users/barendmosch/source/repos/ZeroMQ_ws/faceDetector/src/main/python/stop.py");  
                            BufferedImage image = controller.getImage();
                            BufferedImage imageOfFace = controller.cropImage(image);
                            byte[] imageInBytes = controller.getImageByteArray(imageOfFace);

                            System.out.println("Data sent: " + imageInBytes);

                            ZMsg frameDataMsg = new ZMsg();
                            frameDataMsg.add(imageInBytes);
                            frameDataMsg.send(socket);
                        }

                        /* If you want load an image to the socket */
                        // String path = "/Users/barendmosch/source/repos/ZeroMQ_ws/faceDetector/resources/images/fileName.jpg";
                        // BufferedImage image = controller.getFakeImage(path);
                        // if(controller.findFaceFromJPG(image)){
                        //     BufferedImage imageOfFace = controller.cropImage(image);
                        //     byte[] imageInBytes = controller.getImageByteArray(imageOfFace);

                        //     System.out.println("Data sent: " + imageInBytes);

                        //     ZMsg frameDataMsg = new ZMsg();
                        //     frameDataMsg.add(imageInBytes);
                        //     frameDataMsg.send(socket);
                        // }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}