package mighstye.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
/*
 * On met ici les deux methode implemente par le client pour qu'elles puissent �tre utilis� par le serveur
 */
public interface MsgListener extends Remote{
	void newMsg(String msg) throws RemoteException;
	String returnName() throws RemoteException;
}
