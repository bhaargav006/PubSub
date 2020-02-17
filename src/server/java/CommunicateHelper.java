import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommunicateHelper {

    static final int PACKAGE_SIZE = 1024;
    static final ArrayList<String> typeField = new ArrayList<String>(
            Arrays.asList("Sports", "Lifestyle", "Entertainment", "Business", "Technology", "Science", "Politics", "Health"));

    public static void main(String[] args) {
        udpToClients(null, null, "s");
    }

    public static String generateSubRequest(String publishedArticle){
        StringBuilder generateString
                = new StringBuilder("");
        String[] fieldValues = publishedArticle.split(";");
        for(int i = 0; i < 3; i++) {
            generateString.append(fieldValues[i]);
            generateString.append(";");
        }
        return generateString.toString();
    }

    public static Boolean checkAllCases(String toCheck, String article){

        String[] toCheckValues = toCheck.split(";");
        String[] articleValues = article.split(";");
        // No match only if not null and mismatch
        for(int i=0;i< toCheckValues.length;i++){
            if (articleValues[i]!="" && !(toCheckValues[i].equalsIgnoreCase(articleValues[i]))){
                return false;
            }
        }
        return true;
    }

    public static Boolean isClientSubscribed(ArrayList<String> clientSubscribedArticles, String toCheck){
        for(int i=0;i<clientSubscribedArticles.size();i++){
            if(checkAllCases(clientSubscribedArticles.get(i),toCheck))
                return true;
        }
        return false;
    }

    public static ArrayList<String> getListOfClients(Map<String, ArrayList<String>> clientSubscriptionList, String article){
        ArrayList<String> clientList;
        clientList = new ArrayList<String>();
        String toCheck = generateSubRequest(article);

        for(int i=0;i<clientSubscriptionList.size();i++){
            if(isClientSubscribed(clientSubscriptionList.get(i),toCheck)) {
                //Adding IP of client to the list
                clientList.add(String.valueOf(clientSubscriptionList.keySet().toArray()[i]));
            }
        }

        return clientList;
    }

    public static void udpToRemoteServer(String message){
        try {
            DatagramSocket ds = new DatagramSocket();
            byte[] b = message.getBytes();

            //IP and Port, not so sure.
            InetAddress ir = InetAddress.getLocalHost();
            DatagramPacket dp = new DatagramPacket(b,b.length, ir, 5105);
            ds.send(dp);
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Socket Exception trying to connect to Remote Server");
        } catch (IOException e) {
            System.out.println("Couldn't send the package to the Remote Server");
        }

    }

    public static void udpToClients(List<String> subscribers, Map<String, Integer> portLookup, String message) {
        try {
            DatagramSocket ds = new DatagramSocket();
            byte[] b = new byte[PACKAGE_SIZE];
            message = "UDP change";
            b = message.getBytes();
            InetAddress address = InetAddress.getByName("10.131.123.169");
            DatagramPacket dp = new DatagramPacket(b,b.length,address, 9999);
            ds.send(dp);
//            for(int i=0;i<subscribers.size();i++){
//                InetAddress address = InetAddress.getByName(subscribers.get(i));
//                DatagramPacket dp = new DatagramPacket(b,b.length,address, 9999);
//                ds.send(dp);
//            }
        } catch (SocketException | UnknownHostException e) {
            System.out.println("Socket error while sending UDP packets to Clients");
        } catch (IOException e) {
            System.out.println("IO error while sending packets to clients");
        }

    }

    public static String getMessage(String article) {
        String[] fieldValues = article.split(";");
        return fieldValues[fieldValues.length-1];
    }

    public static Boolean validateString(String article) {
        int count = 0;
        String[] stringToCheck = article.split(";");
        if(stringToCheck.length <= 0)
            return false;
        if(stringToCheck.length > 3) {
            if(stringToCheck[3].trim() != "")
                return false;
        }
        if(!typeField.contains(stringToCheck[0].trim()))
            return false;
        for(String string: stringToCheck){
            count += string.trim().length();
        }
        if(count == 0)
            return false;
        return true;
    }

    public Boolean validatePublisherString(String article) {
        String[] stringToCheck = article.split(";");
        if(stringToCheck.length <= 0)
            return false;
        if(stringToCheck.length == 4){
            if(stringToCheck[3].trim().length() == 0)
                return false;
            if(stringToCheck[0].trim() == "" && stringToCheck[1].trim() == "" && stringToCheck[2].trim() == "")
                return false;
        }
        else
            return false;

        return true;
    }
}
