import org.zeromq.ZMQ.Socket;

import vision.RecogniseController;
import nu.pattern.OpenCV;
import sql.DatabaseAction;

import java.util.LinkedList;
import java.util.List;

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

            /* MAKE NEW PERSON */
            // String new_person_name = "styn";
            // DatabaseAction.addNewPerson(new_person_name);
            
            /* GOTTA ADD SOME FAILSAFE 
                Also add something that triggers a new SET when the camera cant see a face after a couple of seconds during a set */
            while(!Thread.currentThread().isInterrupted()){
                byte[] image_data = socket.recv(0);

                /* Start the recognition process after a certain amount of time one time only
                    In reality this will go on forever and I will not save outgoing pictures */
                // if (i < 2){
                    System.out.println(i);
                    recognition.setImageAndMakeGrayScale(image_data);
                    recognition.recognise(i);

                /* Initialise new person data 50 times, meaning make 50 histograms of that person */
                    // recognition.initialisePerson(new_person_name);
                // }

                i++;
            }
        }
    }
}
