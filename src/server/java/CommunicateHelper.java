import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunicateHelper {

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
}
