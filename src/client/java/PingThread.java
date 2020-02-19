import java.io.IOException;
import java.rmi.RemoteException;


public class PingThread extends Thread{
    private boolean running;
    private Communicate comm;
    public PingThread(Communicate comm) {
        this.comm = comm;
    }
    public void run() {
        running = true;
        while (running) {
            try {
                if(!comm.ping()){
                    if(comm==null){
                        System.out.println("Not able to connect to Group server");
                        return;
                    }
                }
                else
                {
                    System.out.println("Ping successful to server");
                    Thread.sleep(5000);
                }

            } catch (RemoteException | InterruptedException e) {
                System.out.println("Unable to ping the server! Exiting...");
                running = false;
                comm = null;
                return;
            }
        }
    }

}
