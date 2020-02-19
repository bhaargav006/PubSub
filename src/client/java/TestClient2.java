import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class TestClient2 {

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
            comm.subscribe(ip,1098, "Science;;UMN;");
            Thread.sleep(20000);
            comm.publish("Science;;UMN;PubSubProject1",ip,1098);
            comm.subscribe(ip,1098, "Sports;Garima;;");

            PingThread thread = new PingThread(comm);
            thread.start();
            thread.join();
            i++;
            System.out.println("Main thread restarted for connecting to new server.");
        }
    }
}
