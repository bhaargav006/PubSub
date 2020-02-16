import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class GroupServerHeartbeat extends Thread {
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public GroupServerHeartbeat() throws SocketException {
        socket = new DatagramSocket(9999);
    }

    public void run() {
        running = true;
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                System.out.print("Heartbeat Received by Group Server");
                String received
                        = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
                System.out.println("Now the sending the heartbeat to Registry Server");
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                running = false;
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
