import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;



public class GroupServer  {
    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, SocketException, AlreadyBoundException {

        final String ipRegistry = args[0];

        System.out.println("Registry Service " + ipRegistry);

        final StringBuilder request = new StringBuilder("");
        request.append("Register;RMI;");

        /* If running locally, from our personal machines, we can use getLocalhost to
        get the local IPs that is passed for both UDP communication or RMI communication.

        But in the case of CSE lab machines, we need to send the public ip for the machines to
        communicate.
         */

        //code for getting the public ip
        String ip = CommunicateHelper.getIP();
        request.append(ip);

        /*
            Setting property so that the Registry is created at the
         */

        System.setProperty("java.rmi.server.hostname",ip);
        request.append(";9999;server.comm;1099");
        CommunicateHelper.udpToRemoteServer(request.toString(),ipRegistry);

        //Heartbeat

        new GroupServerHeartbeat().start();
        Registry r = LocateRegistry.createRegistry(1099);
        Communicate comm = new CommunicateImpl();
        // Communicate communicate = (Communicate) UnicastRemoteObject.exportObject(comm, 0);
        r.bind("server.comm", comm);

        //Need to register to Registry server
        // Format is “Register;RMI;IP;Port;BindingName;Port for RMI”


        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {

                StringBuilder deregisterRequest = new StringBuilder("");
                deregisterRequest.append("Deregister;RMI;");
                String ip= null;
                try {
                    ip = CommunicateHelper.getIP();
                    deregisterRequest.append(ip);
                    deregisterRequest.append(";9999");
                    CommunicateHelper.udpToRemoteServer(deregisterRequest.toString(),ipRegistry);
                } catch (UnknownHostException | MalformedURLException e) {
                    System.out.println("Couldn't get IP");
                }
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
//        scheduler.schedule(task, delay, TimeUnit.SECONDS);
//        scheduler.shutdown();

        System.out.println("You have been served");


    }
}
