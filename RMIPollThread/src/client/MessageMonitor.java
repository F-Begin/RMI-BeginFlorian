package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.MessageServer;

public class MessageMonitor {
	private List<String> messages = new ArrayList<>();
	private Scanner sc = new Scanner(System.in);
	
	protected MessageMonitor() throws RemoteException {}

	public static void main(String[] args) {
		try {
			MessageServer messageServer = (MessageServer)Naming.lookup("//localhost/Rmiserver");
			
			System.err.println("Connexion réussis !");
			
			MessageMonitor client = new MessageMonitor();
			new SenderThread(client, messageServer).start();
			new ReceiveThread(client, messageServer).start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void afficher(String msg) {
		System.out.println(msg);
	}
	
	public String input() {
		return sc.nextLine();
	}
	
	public List<String> returnList() {
		return messages;
	}

	public void stop() {
		System.exit(0);
	}
}
