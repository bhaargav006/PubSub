import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException, SocketException, InterruptedException {

        //Needs to get Server IP and Port from Registry Server


        new PublishedClient().start();

        //Getlist from the remote registry
        //“GetList;RMI;IP;Port”

        StringBuilder getListRequest = new StringBuilder("");
        getListRequest.append("GetList;RMI;");
        InetAddress ia = InetAddress.getLocalHost();
        getListRequest.append(ia.getHostAddress());
        getListRequest.append(";1098");

        String ipAddressOfGSrvs = CommunicateHelper.udpToAndFromRemoteServer(getListRequest.toString());
        String [] splitAdresses = ipAddressOfGSrvs.split(";");
        List<String>ipAddrAndPort = new ArrayList<>();
        for(int i = 0; i < splitAdresses.length; i=i+2) {
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
            String[] addr = ipAddrAndPort.get(i).split(":");
            grpServerIP = addr[0];
            port = Integer.parseInt(addr[1]);
            registry = LocateRegistry.getRegistry(grpServerIP);
            comm = (Communicate) registry.lookup("server.comm");

            joinAllowed = comm.join(grpServerIP,port);
        }
        comm.ping();
        comm.subscribe(grpServerIP,port, "Lifestyle;Soumya;UMN;");
    //    comm.unSubscribe(ia.getHostAddress(),9999, "Lifestyle;Soumya;UMN;");
        comm.publish("Science;;UMN; You're jobless",ia.getHostAddress(),1099);
    //    comm.leave(ia.getHostAddress(),9999);

    }
}
