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

    public boolean join(String IP, int Port) throws RemoteException {
        System.out.println("Join request from Client: " + IP + " at port: "+ String.valueOf(Port));
        try {
            clientList.put(IP,Port);
            InetAddress iServer = InetAddress.getLocalHost();
            System.out.println("Server IP is " + iServer.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean leave(String IP, int Port) throws RemoteException {
        System.out.println("Leave request from Client: " + IP + " at port: "+ String.valueOf(Port));
        try {

            String clientIP = RemoteServer.getClientHost();
            clientSubscriptionList.remove(clientIP);
            clientList.remove(clientIP);
            System.out.println("Client " + clientIP + " has left the building");
        } catch (ServerNotActiveException e) {
            System.out.println("Couldn't get Client IP");
            return false;
        }

        return true;
    }

    public boolean subscribe(String IP, int Port, String article) throws RemoteException {
        if(!CommunicateHelper.validateString(article)){
            System.out.println("Invalid subscribe request: " + article);
            return false;
        }
        // Map currently take the key as client IP address and value as the list of subscriptions for the client.
        // If mapping already exists, dont add? -> can add this feature

        //The client doesn't need the IP to connect. We can only figure this out when we
        //test it in multiple machines
        //String clientIP = RemoteServer.getClientHost();
        if(!clientSubscriptionList.containsKey(IP)){
            clientSubscriptionList.put(IP, new ArrayList<String>());
        }
        clientSubscriptionList.get(IP).add(article);
        System.out.println("Client: " + IP + " is subscribed to " + article);

        return true;
    }

    public boolean unSubscribe(String IP, int port, String article) throws RemoteException {
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

    public boolean publish(String Article, String IP, int Port) throws RemoteException {
//        if(!CommunicateHelper.validateString(Article)){
//            System.out.println("Invalid publish request: " + Article);
//            return false;
//        }
        System.out.println("Publishing article: " + Article );
        try{
            String clientIP = RemoteServer.getClientHost();
            currArticle = Article;
            List<String> subscribers = CommunicateHelper.getListOfClients(clientSubscriptionList, Article);
            System.out.println("There are " + subscribers.size() + " clients who are subscribed to this article");

            //InetAddress address = InetAddress.getByName("192.168.1.106");
            CommunicateHelper.udpToClients(subscribers, clientList, CommunicateHelper.getMessage(Article));

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
