import nu.pattern.OpenCV;
import vision.FrameController;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Socket;

import java.awt.image.BufferedImage;

/* The main class will:
 - open the camera 
 - keep the camera running indefinetily 
 - uses the Viola and Jones face detection algorithm to find a face 
 - crops the image so that only the face is present
 - converts the facial image into a byte array 
 - send the bytes to the face recogniser client through ZMQ pipeline 
 TODO:
 - implement the python processes so that the camera will correct its position to optimally align the face 
 - implement the processes in python or make a JAVA native library of the python library */
public class Main {
    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally();

        try (ZContext context = new ZContext()) {
            /* The following variables are for the python PTZ scripts, which is not implemented yet */
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

            while(!Thread.currentThread().isInterrupted()){
                /* reading the camera and loading the face frames into the socket */
                if(controller.findFace()){
                    BufferedImage image = controller.getImage();

                    /* Used for zooming in and out accordingly to get a static rectangle */
                    // while (width != face_rectangular_width && height != face_rectangular_height){
                        // f_width = controller.getFaceWidth();
                        // f_height = controller.getFaceHeight();
                        // Process ptz_zoom_in_out = Runtime.getRuntime().exec("python ./src/main/python/zoom.py " + f_width + " " + f_height); 
                    // }
                    /* Used for rotating and tilting the camera so the facial image is centered */
                    // f_x = controller.getFaceX();
                    // f_y = controller.getFaceY();
                    // Process ptz_zoom_in_out = Runtime.getRuntime().exec("python ./src/main/python/centering.py " + f_x + " " + f_y + " " + resolution_x + " " + resolution_y); 

                    BufferedImage image_of_face = controller.cropImage(image);
                    byte[] image_in_bytes = controller.getImageByteArray(image_of_face);

                    System.out.println("Face detected: " + image_in_bytes);
                    ZMsg frame_data_msg = new ZMsg();
                    frame_data_msg.add(image_in_bytes);
                    frame_data_msg.send(socket);
                }

                /* Loading an image file into the socket instead of the camera feed */
                // String path = "./resources/images/fileName.jpg";
                // if(controller.findFaceFromJPG()){
                //     BufferedImage image = controller.getFakeImage();
                //     BufferedImage image_of_face = controller.cropImage(image);
                //     byte[] image_in_bytes = controller.getImageByteArray(image_of_face);

                //     System.out.println("Data sent: " + image_in_bytes);

                //     ZMsg frame_data_msg = new ZMsg();
                //     frame_data_msg.add(image_in_bytes);
                //     frame_data_msg.send(socket);
                // }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}