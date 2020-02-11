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

    List<String> clientList = new ArrayList<String>();
    Map<String,String> clientSubscription = new HashMap<String, String>();

    Map<String, ArrayList<String>> clientSubscriptionList = new HashMap<String, ArrayList<String>>();
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

    public boolean subscribe(String IP, int Port, String article) throws RemoteException {
        // Map currently take the key as client IP address and value as the list of subscriptions for the client.
        try{
            String clientIP = RemoteServer.getClientHost();
            if(!clientSubscriptionList.containsKey(clientIP)){
                clientSubscriptionList.put(clientIP, new ArrayList<String>());
            }
            clientSubscriptionList.get(clientIP).add(article);
        } catch (ServerNotActiveException e1) {
            System.out.println("Could not get Client IP");
            return false;
        }
        return true;
    }

    public boolean unSubscribe(String IP, int port, String article) throws RemoteException {
        /*A particular article can be removed from the list of subscriptions present for the client.
          If the request article for unsubscribing does not match with the value present in the list,
          we term it as invalid*/
        try{
            String clientIP = RemoteServer.getClientHost();
            if(clientSubscriptionList.containsKey(clientIP)){
                ArrayList<String> clientSubscribedArticles = clientSubscriptionList.get(clientIP);
                if(clientSubscribedArticles.contains(article)){
                    clientSubscribedArticles.remove(article);
                    clientSubscriptionList.put(clientIP, clientSubscribedArticles);
                }
                else{
                    System.out.println("Invalid unsubscribing request");
                    return false;
                }
            }
            else{
                System.out.println("Client is not subscribed or has left the server already");
                return false;
            }
        } catch (ServerNotActiveException e) {
            System.out.println("Could not get Client IP");
            return false;
        }
        return true;
    }

    public boolean publish(String Article, String IP, int Port) throws RemoteException {
        System.out.println("Publishing article: " + Article + " to server: "+ IP + " at port: "+ Port);
        try{
            String clientIP = RemoteServer.getClientHost();
            currArticle = Article;
        } catch (ServerNotActiveException e){
            System.out.println("Couldn't get Client IP");
            return false;
        }
        return true;
    }

    public boolean ping() throws RemoteException {
        System.out.println("I am alive");
        return true;
    }
}
