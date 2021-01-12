import org.zeromq.ZMQ.Socket;

import vision.RecogniseController;
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

            RecogniseController recognition = new RecogniseController();

            /* Also add something that triggers a new SET when the camera cant see a face after a couple of seconds during a set */
            while(!Thread.currentThread().isInterrupted()){
                byte[] image_data = socket.recv(0);

                /* Start the recognition process */
                recognition.setImageAndMakeGrayScale(image_data);
                recognition.recognise(i);
            }
        }
    }
}
