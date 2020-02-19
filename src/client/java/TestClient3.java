import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClient3 {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException, SocketException, InterruptedException {

        new PublishedClient().start();
        String ip = CommunicateHelper.getIP();

        Map<String,Integer>  ipAddrAndPort = CommunicateHelper.getServerList(args[0], ip);
        Communicate comm = CommunicateHelper.connectToGroupServer(ipAddrAndPort, ip);
        if(comm==null){
            System.out.println("Not able to connect to Group server");
            return;
        }
        comm.subscribe(ip,1098, ";;;WrongFormat");
        Thread.sleep(10000);
        comm.publish("Science;;UMN;DistributesSystemsLab", ip,1098);
        comm.subscribe(ip, 1098, "Science;;UMN;");
        comm.subscribe(ip,1098, "Sports;Garima;UMN;");

        //    comm.leave(ia.getHostAddress(),9999);

    }
}
