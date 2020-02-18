import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
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
    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, SocketException {

        //Register

        final StringBuilder request = new StringBuilder("");
        request.append("Register;RMI;");

        InetAddress ib = InetAddress.getLocalHost();

        /* If running locally, from our personal machines, we can use getLocalhost to
        get the local IPs that is passed for both UDP communication or RMI communication.

        But in the case of CSE lab machines, we need to send the public ip for the machines to
        communicate.
         */

        //code for getting the public ip
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
        request.append(ip);


        request.append(";9999;server.comm;1099");
        CommunicateHelper.udpToRemoteServer(request.toString());

        //Heartbeat

        new GroupServerHeartbeat().start();
        LocateRegistry.createRegistry(1099);
        Communicate comm = new CommunicateImpl();
        Naming.rebind("server.comm", comm);

        //Need to register to Registry server
        // Format is “Register;RMI;IP;Port;BindingName;Port for RMI”


        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {

                StringBuilder deregisterRequest = new StringBuilder("");
                deregisterRequest.append("Deregister;RMI;");
                InetAddress ib = null;
                try {
                    ib = InetAddress.getLocalHost();

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

                    deregisterRequest.append(ip);
                } catch (UnknownHostException | MalformedURLException e) {
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
//        scheduler.schedule(task, delay, TimeUnit.SECONDS);
//        scheduler.shutdown();

        request.append(";5105;server.comm;1099");
        CommunicateHelper.udpToRemoteServer(request.toString());

        System.out.println("You have been served");

        //Deregister the server


    }
}
