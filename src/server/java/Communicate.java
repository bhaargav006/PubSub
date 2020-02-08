import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Communicate extends Remote {
    boolean join(String IP, int Port) throws RemoteException;

    boolean leave(String IP, int Port) throws RemoteException;

    boolean subscribe(String IP, int Port, String Article) throws
            RemoteException;

    boolean unSubscribe(String IP, int Port, String Article) throws
            RemoteException;

    boolean publish(String Article, String IP, int Port) throws
            RemoteException;

    boolean ping() throws RemoteException;
}

