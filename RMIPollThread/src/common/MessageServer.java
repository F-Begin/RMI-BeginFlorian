package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MessageServer extends Remote {
	List<String> returnList() throws RemoteException;
	void sendMessage(String msg) throws RemoteException;
}
