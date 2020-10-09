package zmq;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.SocketType;
import org.zeromq.ZContext;

public class client {
    public static void main(String[] args) throws Exception {
        try (ZContext context = new ZContext()) {
                Socket socket = context.createSocket(SocketType.PULL);
                socket.connect("tcp://*:5555");

                System.out.println("Connected to the server: ");

                byte[] reply = socket.recv(0);

                // Print the message
                System.out.println("Received: " + new String(reply, ZMQ.CHARSET));
        }
    }
}
