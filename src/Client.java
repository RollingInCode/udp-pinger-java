
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {

    private static final int NUM_PINGS = 10;
    private static final int PORT = 8888;

    // Entry point of the program
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("usage: java Client <server address>");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(1000);

        double maximumRTT = 0;
        double minimumRTT = 0;
        double totalRTT = 0;
        int packetsTransmitted = 0;
        int packetsRetransmitted = 0;
        int packetsLost = 0;

        // Ping the server 10 times
        for (int i = 0; i < NUM_PINGS; i++) {

            // Send a ping and attempt of up to 3 times only if failed to get response from the server
            String message = "ping " + (i + 1) + " " + dateFormat.format(new Date());
            System.out.println(message);

            // Give it up to 3 attempts only if no server responds
            int attempts = 0;

            while (true) {
                try {
                    long startTimeNanoSeconds = System.nanoTime();

                    byte[] data = message.getBytes();
                    DatagramPacket packet = new DatagramPacket(
                            data, data.length,
                            InetAddress.getByName(args[0]), PORT);

                    socket.send(packet);

                    // Wait for server response
                    data = new byte[1024];
                    packet = new DatagramPacket(data, data.length);
                    socket.receive(packet);
                    long endTimeNanoSeconds = System.nanoTime();
                    double elapsedTimeSeconds = (endTimeNanoSeconds - startTimeNanoSeconds) / 1000000000.0;

                    // Update the stats
                    packetsTransmitted++;
                    totalRTT += elapsedTimeSeconds;

                    if (i == 0 || elapsedTimeSeconds > maximumRTT) {
                        maximumRTT = elapsedTimeSeconds;
                    }

                    if (i == 0 || elapsedTimeSeconds < minimumRTT) {
                        minimumRTT = elapsedTimeSeconds;
                    }

                    // Display server response
                    String response = new String(data).trim();
                    System.out.println("  server resp: " + response);
                    System.out.println("  Calculated Round Trip Time = " + elapsedTimeSeconds + " seconds");
                    System.out.println();

                    break;
                } catch (Exception e) {
                    System.out.println("  Request timed out");

                    if (attempts < 2) {
                        System.out.println("  Packet retransmitted");
                        packetsRetransmitted++;
                        attempts++;
                    } else {
                        packetsLost++;
                        System.out.println();
                        break;
                    }
                }
            }
        }

        // Print stats
        System.out.println("Maximum RTT = " + maximumRTT);
        System.out.println("Minimum RTT = " + minimumRTT);
        System.out.println("Average RTT = " + totalRTT / packetsTransmitted);
        System.out.println("Packet Loss Percentage = " + ((double) packetsLost / NUM_PINGS * 100));
        System.out.println("Packet retransmitted = " + packetsRetransmitted);
    }
}
