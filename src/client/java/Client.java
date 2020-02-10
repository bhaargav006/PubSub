import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost");
        Communicate comm = (Communicate) registry.lookup("server.comm");
        comm.ping();

        comm.join("localhost",1099);
        // why do you need ip and host ? Is it going to be used by udp? Does that mean that is the client's ?
        comm.leave("localhost",1099);

        System.out.println("I am done - Client");
    }
}
