import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GroupServer  {
    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException {

        LocateRegistry.createRegistry(1099);
        Communicate comm = new CommunicateImpl();
        Naming.rebind("server.comm", comm);

        //Need to register to Registry server
        // Format is “Register;RMI;IP;Port;BindingName;Port for RMI”

        final StringBuilder request = new StringBuilder("");
        request.append("Register;RMI;");

        InetAddress ib = InetAddress.getLocalHost();
        request.append(ib.getHostAddress());

        request.append(";9999;server.comm;1099");
        CommunicateHelper.udpToRemoteServer(request.toString());

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {

                StringBuilder deregisterRequest = new StringBuilder("");
                deregisterRequest.append("Deregister;RMI;");
                InetAddress ib = null;
                try {
                    ib = InetAddress.getLocalHost();
                    deregisterRequest.append(ib.getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                deregisterRequest.append(";9999");
                CommunicateHelper.udpToRemoteServer(deregisterRequest.toString());
                try {
                    Naming.unbind("server.comm");
                } catch (RemoteException | NotBoundException | MalformedURLException e) {
                    e.printStackTrace();
                }
                System.out.println("Server is deregistered from the remote server");
            }
        };

        System.out.println("You have been served");

        int delay = 60;
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
        scheduler.shutdown();

    }
}
