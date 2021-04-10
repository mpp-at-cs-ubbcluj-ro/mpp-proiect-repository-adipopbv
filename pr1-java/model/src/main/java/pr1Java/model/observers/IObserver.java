package pr1Java.model.observers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void seatsSold(Integer gameId, Integer seatsCount) throws RemoteException;
}

