import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException, SocketException, InterruptedException {

        //Needs to get Server IP and Port from Registry Server

        new PublishedClient().start();
        String ip = CommunicateHelper.getIP();

        Map<String,Integer>  ipAddrAndPort = CommunicateHelper.getServerList(args[0], ip);
        Communicate comm = CommunicateHelper.connectToGroupServer(ipAddrAndPort, ip);
        if(comm==null){
            System.out.println("Not able to connect to Group server");
            return;
        }
//        comm.subscribe(InetAddress.getLocalHost().getHostAddress(),1098, "Science;;UMN;");
//        Thread.sleep(20000);
//        //Wrong unsubscribe request
//        comm.unSubscribe(InetAddress.getLocalHost().getHostAddress(),1098, "Lifestyle;Soumya;UMN;");
        comm.subscribe(InetAddress.getLocalHost().getHostAddress(), 1098, "Sports;;;");

        //comm.publish("Science;;UMN;Some message", InetAddress.getLocalHost().getHostAddress(), 1098);
        //    comm.leave(ia.getHostAddress(),9999);

    }
}
