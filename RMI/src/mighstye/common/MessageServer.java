package mighstye.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * On met ici les methode implementé par le serveur pour qu'elles puissent être utilise par le client
 */

public interface MessageServer extends Remote{
	void addMessageListener(MsgListener msgListener) throws RemoteException;
	void removeMessageListener(MsgListener msgListener) throws RemoteException;
	void sendMessage(String msg, MsgListener client) throws RemoteException;
}
