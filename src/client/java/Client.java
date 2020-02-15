import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException, SocketException {

        //Needs to get Server IP and Port from Registry Server


        new PublishedClient().start();

        //Getlist from the remote registry
        //“GetList;RMI;IP;Port”

        StringBuilder getListRequest = new StringBuilder("");
        getListRequest.append("GetList;RMI;");
        InetAddress ia = InetAddress.getLocalHost();
        getListRequest.append(ia.getHostAddress());
        getListRequest.append(";9999");

        CommunicateHelper.udpToRemoteServer(getListRequest.toString());

        //Listen to remote server and get a list



        System.out.println("I am done - Client");



        //System.setSecurityManager(new RMISecurityManager());
        Registry registry = LocateRegistry.getRegistry("localhost");
        Communicate comm = (Communicate) registry.lookup("server.comm");
        comm.ping();

        comm.join(ia.getHostAddress(),1099);
        comm.subscribe(ia.getHostAddress(),1099, "Science;;UMN;");
        comm.subscribe(ia.getHostAddress(),1099, "Lifestyle;Bhaargav;UMN;");
        comm.unSubscribe(ia.getHostAddress(),1099, "Lifestyle;Bhaargav;UMN;");


        //comm.publish("",ia.getHostAddress(),1099);
        comm.leave(ia.getHostAddress(),1099);

    }
}
