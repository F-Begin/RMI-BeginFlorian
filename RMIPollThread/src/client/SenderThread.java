package client;

import java.rmi.RemoteException;

import common.MessageServer;

public class SenderThread extends Thread{
	private MessageMonitor user;
	private MessageServer serv;
	
	public SenderThread(MessageMonitor user, MessageServer serv) {
		this.user = user;
		this.serv = serv;
	}
	
	public void run() {
		this.user.afficher("Sender Initializied");
		while(true) {
			try {
				String msg = this.user.input();
				if(!(msg.equals("/quit"))) {
					serv.sendMessage(msg);
				}
				else {
					this.user.stop();
				}
			} catch (RemoteException e) {}
		}
	}
}
