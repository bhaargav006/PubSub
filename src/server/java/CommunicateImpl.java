import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class CommunicateImpl extends UnicastRemoteObject implements Communicate {

    protected CommunicateImpl() throws RemoteException {
    }

    public boolean join(String IP, int Port) throws RemoteException {
        return false;
    }

    public boolean leave(String IP, int Port) throws RemoteException {
        return false;
    }

    public boolean subscribe(String IP, int Port, String Article) throws RemoteException {
        return false;
    }

    public boolean unSubscribe(String IP, int Port, String Article) throws RemoteException {
        return false;
    }

    public boolean publish(String Article, String IP, int Port) throws RemoteException {
        return false;
    }

    public boolean ping() throws RemoteException {
        System.out.println("I am alive");
        return true;
    }
}
