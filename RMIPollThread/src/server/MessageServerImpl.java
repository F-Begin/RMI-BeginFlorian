package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import common.MessageServer;

public class MessageServerImpl extends UnicastRemoteObject implements MessageServer{
	private static final long serialVersionUID = 1L;
	private List<String> messages = new ArrayList<>();
	
	protected MessageServerImpl() throws RemoteException {}
	
	public static void main(String args[]) {
		System.setProperty("java.rmi.server.hostname", "localhost");
		try {
			LocateRegistry.createRegistry(1099);
			
			MessageServerImpl serveur = new MessageServerImpl();
			Naming.rebind("//localhost/Rmiserver", serveur);
			
			System.err.println("Serveur démarré");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> returnList() throws RemoteException {
		return messages;
	}

	@Override
	public void sendMessage(String msg) throws RemoteException {
		this.messages.add(msg);
	}
}
