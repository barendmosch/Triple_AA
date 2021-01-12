import org.zeromq.ZMQ.Socket;

import vision.RecogniseController;
import nu.pattern.OpenCV;
import sql.DatabaseAction;

import org.zeromq.SocketType;
import org.zeromq.ZContext;

public class Main {

    public static void main(String[] args) throws Exception {
        OpenCV.loadLocally(); // initialize the library locally
        
        try (ZContext context = new ZContext()) {
            int i = 0;
            int AMOUNT_OF_HISTOGRAMS = 50;
            Socket socket = context.createSocket(SocketType.PULL);
            socket.connect("tcp://*:5555");

            System.out.println("Connected to the server: ");

            RecogniseController recognition = new RecogniseController();

            /* MAKE NEW PERSON */
            String new_person_name = "barend";
            DatabaseAction.addNewPerson(new_person_name);
            
            /* Also add something that triggers a new SET when the camera cant see a face after a couple of seconds during a set */
            while(!Thread.currentThread().isInterrupted()){
                byte[] image_data = socket.recv(0);

                /* Initialise new person data 50 times, meaning make 50 histograms of that person and save them in the mysql local database */
                if (i < AMOUNT_OF_HISTOGRAMS){
                    System.out.println(i);
                    recognition.setImageAndMakeGrayScale(image_data);
                    recognition.initialisePerson(new_person_name);
                }

                i++;
            }
        }
    }
}
