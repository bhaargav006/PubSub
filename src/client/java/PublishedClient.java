import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class PublishedClient extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public PublishedClient() throws SocketException {
        socket = new DatagramSocket(1098);
    }
    public void run() {
        running = true;
        while(running) {
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            try {
                socket.receive(packet);

            System.out.print("Message Received");
            String received
                    = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);

            } catch (IOException e) {
                running = false;
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
