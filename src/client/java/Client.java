import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;


public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException, SocketException, InterruptedException {

        //Needs to get Server IP and Port from Registry Server
        new PublishedClient().start();
        final String ip = CommunicateHelper.getIP();
        final String registryIP = args[0];

        Communicate comm = null;
        int i = 1;
        while (true) {
            Thread.sleep(10000);
            Map<String, Integer> ipAddrAndPort = CommunicateHelper.getServerList(args[0], ip);
            comm = CommunicateHelper.connectToGroupServer(ipAddrAndPort, ip);
            System.out.println("Got IP address for " + i + "st time");
            if (comm == null) {
                System.out.println("Not able to connect to Group server");
                return;
            }
            comm.subscribe(InetAddress.getLocalHost().getHostAddress(), 1098, "Science;;UMN;");
            Thread.sleep(20000);
            //Wrong unsubscribe request
            comm.unSubscribe(InetAddress.getLocalHost().getHostAddress(), 1098, "Lifestyle;Soumya;UMN;");
            comm.subscribe(InetAddress.getLocalHost().getHostAddress(), 1098, "Sports;;;");

            comm.publish("Science;;UMN;Some message", InetAddress.getLocalHost().getHostAddress(), 1098);
            //    comm.leave(ia.getHostAddress(),9999);

            PingThread thread = new PingThread(comm);
            thread.start();
            thread.join();
            i++;
            System.out.println("Main thread restarted for connecting to new server.");
        }
    }
}
