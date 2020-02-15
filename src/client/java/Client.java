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

//        Registry registry = LocateRegistry.getRegistry("10.131.123.169");
//        Communicate comm = (Communicate) registry.lookup("server.comm");
//        comm.ping();
//
//        InetAddress ia = InetAddress.getLocalHost();
//        comm.join(ia.getHostAddress(),1099);
//        comm.subscribe(ia.getHostAddress(),1099, "Science;;UMN;");
//        comm.subscribe(ia.getHostAddress(),1099, "Lifestyle;Bhaargav;UMN;");
//        comm.unSubscribe(ia.getHostAddress(),1099, "Lifestyle;Bhaargav;UMN;");
//        comm.leave(ia.getHostAddress(),1099);

        System.out.println("I am done - Client");


    }
}
