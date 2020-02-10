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
        // Map currently take the key as client IP address. I think we can use the key as type/originator/org value.
        // Need to discuss
        try{
            String clientIP = RemoteServer.getClientHost();
            String[] fieldValues = article.split(";");
            for(int i = 0; i < fieldValues.length; i++){
                if(fieldValues[i] != ""){
                    if(!clientSubscriptionList.containsKey(clientIP)){
                        clientSubscriptionList.put(clientIP, new ArrayList<String>());
                    }
                    clientSubscriptionList.get(clientIP).add(fieldValues[i]);
                }
            }
        } catch (ServerNotActiveException e) {
            System.out.println("Could not get Client IP");
            return false;
        }

        return true;
    }

    public boolean unSubscribe(String IP, int port, String article) throws RemoteException {
        /*Key being type/originator/org makes sense because you can unsubscribe from a particular topic but
        can still be subscribed to another one.
        That's my understanding. We can discuss more on this.*/
        try{
            String clientIP = RemoteServer.getClientHost();
            String[] fieldValues = article.split(";");
            for(int i = 0; i < fieldValues.length; i++){
                if(fieldValues[i] != ""){
                    if(clientSubscriptionList.containsKey(clientIP)){
                        ArrayList<String> clientSubscribedArticles = clientSubscriptionList.get(clientIP);
                        clientSubscribedArticles.remove(fieldValues[i]);
                        clientSubscriptionList.put(clientIP, clientSubscribedArticles);
                    }
                    else{
                        System.out.println("Client is not subscribed!");
                        return false;
                    }
                }
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
