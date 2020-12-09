import org.zeromq.ZMQ.Socket;

import vision.ImageController;
import nu.pattern.OpenCV;

import org.zeromq.SocketType;
import org.zeromq.ZContext;

public class Main {

    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally
        
        try (ZContext context = new ZContext()) {
            int i = 0;
            Socket socket = context.createSocket(SocketType.PULL);
            socket.connect("tcp://*:5555");

            System.out.println("Connected to the server: ");

            ImageController imageMaker = new ImageController();

            /* GOTTA ADD SOME FAILSAFE */
            while(!Thread.currentThread().isInterrupted()){
                byte[] imageData = socket.recv(0);

                Thread.sleep(200);

                /* Start the recognition process after 2 second one time only
                    In reality this will go on forever and I will not save outgoing pictures */
                if (i < 10){
                    imageMaker.setImageAndMakeGrayScale(imageData);
                    imageMaker.startRecognition(i);
                }
                /* USE THIS IS YOU WANT TO SAVE THE IMAGE AS A JPG IN THE RESOURCES FOLDER */
                // imageMaker.saveImage(y);

                /* USE THIS IF YOU WANT TO TAKE A PICTURE EVERY 0.5 SECONDS */



                i++;
            }
        }
    }
}
