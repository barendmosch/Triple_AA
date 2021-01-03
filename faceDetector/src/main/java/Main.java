import nu.pattern.OpenCV;
import vision.FrameController;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Socket;

import java.awt.image.BufferedImage;
import java.awt.EventQueue;

/* The main class will open the camera, keep the camera running indefinetily and sends the data to the client */
public class Main {
    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try (ZContext context = new ZContext()) {
                    /* Make a new controller. */
                    int i = 0;
                    int face_rectangular_width = 255;
                    int face_rectangular_height = 255;
                    int resolution_x = 1920;
                    int resolution_y = 1080;
                    int f_width = 0;
                    int f_height = 0;
                    int f_x = 0;
                    int f_y = 0;
                    /*  coordinates of the centered rectangle
                        713 =                  1920 / 2           -  255 - 1 / 2  */
                    int x_face_rect_center = (resolution_x / 2) - ((face_rectangular_width - 1) / 2);
                    /*  413 =                  1080 / 2         -    255 - 1 / 2  */
                    int y_face_rect_center = (resolution_y / 2) - ((face_rectangular_width - 1) / 2); 
                    
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
                        // Process ptz_zoom_in_out = Runtime.getRuntime().exec("python /Users/barendmosch/source/repos/ZeroMQ_ws/  faceDetector/src/main/python/zoom.py " + f_width + " " + f_height); 


                        if(controller.findFace()){
                            /* Processes are the Python scripts only */
                            // p.destroy();
                            BufferedImage image = controller.getImage();

                            /* Used for zooming in and out accordingly to get a static rectangle */
                            // while (width != face_rectangular_width && height != face_rectangular_height){
                                // f_width = controller.getFaceWidth();
                                // f_height = controller.getFaceHeight();
                                // Process ptz_zoom_in_out = Runtime.getRuntime().exec("python /Users/barendmosch/source/repos/ZeroMQ_ws/faceDetector/src/main/python/zoom.py " + f_width + " " + f_height); 
                            // }
                            /* Used for rotating and tilting the camera so the facial image is centered */
                            // f_x = controller.getFaceX();
                            // f_y = controller.getFaceY();
                            // Process ptz_zoom_in_out = Runtime.getRuntime().exec("python /Users/barendmosch/source/repos/ZeroMQ_ws/  faceDetector/src/main/python/centering.py " + f_x + " " + f_y + " " + resolution_x + " " + resolution_y); 

                            BufferedImage imageOfFace = controller.cropImage(image);
                            byte[] imageInBytes = controller.getImageByteArray(imageOfFace);
                            System.out.println(i);

                            System.out.println("Face detected: " + imageInBytes);
                            ZMsg frameDataMsg = new ZMsg();
                            frameDataMsg.add(imageInBytes);
                            frameDataMsg.send(socket);
                            // i++;
                        }else{
                            // System.out.println("No Face detected");
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