import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupServer  {

    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, SocketException {

        //Starting a thread which keeps on listening for the heartbeat and sending it back
        new GroupServerHeartbeat().start();


        LocateRegistry.createRegistry(1099);
        Communicate comm = new CommunicateImpl();
        Naming.rebind("server.comm", comm);

        //Need to register to Registry server
        // Format is “Register;RMI;IP;Port;BindingName;Port for RMI”

        StringBuilder request = new StringBuilder("");
        request.append("Register;RMI;");

        InetAddress ib = InetAddress.getLocalHost();
        request.append(ib.getHostAddress());

        request.append(";9999;server.comm;1099");
        //CommunicateHelper.udpToRemoteServer(request.toString());

        System.out.println("You have been served");

    }
}
