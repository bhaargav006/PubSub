import java.net.InetAddress;
import java.net.UnknownHostException;
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

    Map<String, Integer> clientList = new HashMap<String, Integer>();

    Map<String, ArrayList<String>> clientSubscriptionList = new HashMap<String, ArrayList<String>>();
    String currArticle;

    static final int MAX_CLIENTS = 20;

    public boolean join(String IP, int Port) {
        System.out.println("Join request from Client: " + IP + " at port: "+ Port);
        if(clientList.size() == MAX_CLIENTS){
            System.out.println("Reached maximum number of clients");
            return false;
        }

        if(clientList.get(IP) != null){
            clientList.remove(IP);
        }
        clientList.put(IP,Port);

        return true;
    }

    public boolean leave(String IP, int Port) {
        System.out.println("Leave request from Client: " + IP + " at port: "+ Port);

        clientSubscriptionList.remove(IP);
        clientList.remove(IP);
        System.out.println("Client " + IP + " has left the building");

        return true;
    }

    public boolean subscribe(String IP, int Port, String article) {
        if(!CommunicateHelper.validateString(article)){
            System.out.println("Invalid subscribe request: " + article);
            return false;
        }

        if(!clientSubscriptionList.containsKey(IP)){
            clientSubscriptionList.put(IP, new ArrayList<String>());
        }
        clientSubscriptionList.get(IP).add(article);
        System.out.println("Client: " + IP + " is subscribed to " + article);

        return true;
    }

    public boolean unSubscribe(String IP, int port, String article) {
        /*A particular article can be removed from the list of subscriptions present for the client.
          If the request article for unsubscribing does not match with the value present in the list,
          we term it as invalid*/


        if(clientSubscriptionList.containsKey(IP)){
            ArrayList<String> clientSubscribedArticles = clientSubscriptionList.get(IP);
            if(clientSubscribedArticles.contains(article)){
                clientSubscribedArticles.remove(article);
                clientSubscriptionList.put(IP, clientSubscribedArticles);
                System.out.println("Client: " + IP + " is unsubscribed from " + article);
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

        return true;
    }

    public boolean publish(String Article, String IP, int Port) {
        if(!CommunicateHelper.validatePublisherString(Article)){
            System.out.println("Invalid publish request: " + Article);
            return false;
        }

        currArticle = Article;
        List<String> subscribers = CommunicateHelper.getListOfClients(clientSubscriptionList, Article);
        System.out.println("There are " + subscribers.size() + " clients who are subscribed to this article");

        CommunicateHelper.udpToClients(subscribers, clientList, CommunicateHelper.getMessage(Article));

        return true;
    }

    public boolean ping() {
        System.out.println("I am alive");
        return true;
    }
}
