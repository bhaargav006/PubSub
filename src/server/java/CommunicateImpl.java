import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunicateImpl extends UnicastRemoteObject implements Communicate {

    protected CommunicateImpl() throws RemoteException {
    }

    List<String> clientList = new ArrayList<>();
    Map<String,String> clientSubscription = new HashMap<>();
    String currArticle;

    public boolean join(String IP, int Port) throws RemoteException {
        System.out.println("Trying to join to server: " + IP + " at port: "+ String.valueOf(Port));
        try {
            clientList.add(RemoteServer.getClientHost());
            System.out.println("IP is " + RemoteServer.getClientHost());
        } catch (ServerNotActiveException e) {
            System.out.println("Couldn't get Client IP");
            return false;
        }
        return true;
    }

    public boolean leave(String IP, int Port) throws RemoteException {
        System.out.println("Trying to leave from server: " + IP + " at port: "+ String.valueOf(Port));
        try {

            String clientIP = RemoteServer.getClientHost();
            clientSubscription.remove(clientIP);
            clientList.remove(clientList.indexOf(clientIP));
            System.out.println("Client " + clientIP + " has left the building");
        } catch (ServerNotActiveException e) {
            System.out.println("Couldn't get Client IP");
            return false;
        }

        return true;
    }

    public boolean subscribe(String IP, int Port, String Article) throws RemoteException {
        return false;
    }

    public boolean unSubscribe(String IP, int Port, String Article) throws RemoteException {
        return false;
    }

    public boolean publish(String Article, String IP, int Port) throws RemoteException {
        System.out.println("Publishing article: " + Article + " to server: "+ IP + " at port: "+ Port);
        try{
            String clientIP = RemoteServer.getClientHost();
            currArticle = Article;
        } catch (ServerNotActiveException e){
            System.out.println("Couldn't get Client IP");
        }
        return false;
    }

    public boolean ping() throws RemoteException {
        System.out.println("I am alive");
        return true;
    }
}
