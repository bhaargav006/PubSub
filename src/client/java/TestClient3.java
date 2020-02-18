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

        //Needs to get Server IP and Port from Registry Server


        new PublishedClient().start();

        //Getlist from the remote registry
        //“GetList;RMI;IP;Port”

        StringBuilder getListRequest = new StringBuilder("");
        getListRequest.append("GetList;RMI;");
        InetAddress ia = InetAddress.getLocalHost();

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        String ip="";
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip= in.readLine();
        } catch (IOException e) {
            System.out.println("Public IP not working properly. ");
        }

        System.out.println("My public IP is " + ip);

        getListRequest.append(ip);
        getListRequest.append(";1098");

        String ipAddressOfGSrvs = CommunicateHelper.udpToAndFromRemoteServer(getListRequest.toString());
        String [] splitAdresses = ipAddressOfGSrvs.split(";");
        List<String>ipAddrAndPort = new ArrayList<>();
        for(int i = 0; i < splitAdresses.length; i=i+2) {
            if(i+1 >= splitAdresses.length ) continue;
            ipAddrAndPort.add(splitAdresses[i]+":"+splitAdresses[i+1]);
        }
        //Listen to remote server and get a listen
        System.out.println("I am done - Client");
        boolean joinAllowed = false;
        int i = 0;
        Communicate comm = null;
        Registry registry = null;
        String grpServerIP = null;
        int port = 0;
        while(!joinAllowed) {
            if(ipAddrAndPort.size() <= i) break;
            String[] addr = ipAddrAndPort.get(i).split(":");
            grpServerIP = addr[0];
            port = Integer.parseInt(addr[1]);
            registry = LocateRegistry.getRegistry(grpServerIP);
            comm = (Communicate) registry.lookup("server.comm");


            joinAllowed = comm.join(ip,1098);

            //joinAllowed = comm.join(ia.getHostAddress(),1098);

            i++;
        }
        comm.ping();
        comm.subscribe(ip,1098, ";;;WrongFormat");
        Thread.sleep(10000);
        comm.publish("Science;;UMN;DistributesSystemsLab", ip,1098);
        comm.subscribe(ip, 1098, "Science;;UMN;");
        comm.subscribe(ip,1098, "Sports;Garima;UMN;");

        //    comm.leave(ia.getHostAddress(),9999);

    }
}
