
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Random;

public class Server {

    private static final int PORT = 8888;

    /*
     * Entry point of the program. Start the server base
     */
    public static void main(String[] args) throws Exception {
        // Start the server and wait for a client to connect
        DatagramSocket socket = new DatagramSocket(PORT);
        Random random = new Random();

        while (true) {
            // Wait for a client to connect and receive the incoming data
            try {
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);

                // Parse the ping message from client
                String clientAddress = packet.getAddress().getHostAddress();
                int clientPort = packet.getPort();
                String message = new String(data).trim();
                System.out.println("connection from ('" + clientAddress + "', " + clientPort + ") " + message);

                // We simulate 30% failure rate where we do not respond back to client
                if (random.nextInt(100) >= 30) {
                    // Respond back to the client with the same message except that 
                    // we put everything to upper case
                    message = message.toUpperCase();
                    data = message.getBytes();
                    socket.send(new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort()));
                }
            } catch (Exception e) {
                // When network error happens then don't respond back to the
                // client...
            }
        }
    }
}
