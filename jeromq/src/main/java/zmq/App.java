package zmq;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;

public class App {

    public static void main(String[] args) throws Exception {
        try (ZContext context = new ZContext()) {

            ZMQ.Socket socket = context.createSocket(ZMQ.PUSH);
            socket.bind("tcp://*:5555");

            System.out.println("Server is ready listening on port 5555");;

            while (!Thread.currentThread().isInterrupted()) {

                // Send a response
                String response = "Hello, world!";

                socket.send(response.getBytes(ZMQ.CHARSET), 0);
            }
        }
    }
}
