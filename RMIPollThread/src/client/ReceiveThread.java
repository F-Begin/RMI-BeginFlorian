package client;

import java.rmi.RemoteException;

import common.MessageServer;

public class ReceiveThread extends Thread{
	private MessageMonitor client;
	private MessageServer serv;
	
	public ReceiveThread(MessageMonitor client, MessageServer serv) {
		this.client = client;
		this.serv = serv;
	}
	
	public void run() {
		this.client.afficher("Receiver Initialized");
		try {
			for (String message : serv.returnList()) {
				client.returnList().add(message);
			}
		}catch(RemoteException e) {
			e.printStackTrace();
		}
		while(true) {
			try {
				if(client.returnList().size()<serv.returnList().size()) {
					for(int i = client.returnList().size(); i<serv.returnList().size(); i++) {
						this.client.afficher(serv.returnList().get(i));
						this.client.returnList().add(serv.returnList().get(i));
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
