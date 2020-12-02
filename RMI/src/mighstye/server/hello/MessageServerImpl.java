package mighstye.server.hello;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import mighstye.common.MessageServer;
import mighstye.common.MsgListener;

public class MessageServerImpl extends UnicastRemoteObject implements MessageServer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MsgListener> listeners = new ArrayList<>(); //Liste des clients connect� -> Un client = Un MsgListener
	
	protected MessageServerImpl() throws RemoteException {} //Constructeur par defaut de l'objet serveur
	/*
	 * Methode d'envoie de message
	 * String msg -> Le message
	 * MsgListener sender -> Le client qui envoie le message
	 * 
	 * Execute la methode notifyMsgListener avec les memes parametres
	 */
	@Override
	public void sendMessage(String msg, MsgListener sender) throws RemoteException {
		notifyMsgListener(msg, sender);
	}
	
	/*
	* Methode pour notifier tout les client (MsgListener) de l'arriv�e d'un nouveau message
	*
	* String msg -> Le message
	* MsgListener sender -> Le client qui envoie le message
	* 
	* Parcourt une boucle de tout les Listeners et leur envoie le nouveau message en faisant appel a leur methode newMsg() avec pour argument le message
	* SEULEMENT si Mlistenr != sender -> Evite que l'envoyeur recoivent son propre message
	* newMsg() -> Implement� cote client
	* 
	* catch(RemoteException e)
	* -> Permet de g�rer l'erreur g�n�r� si un client a quitt� de mani�re non conventionnel (et ne s'est donc pas retir� de la liste)
	* --> Si l'exception est catch cela signifie que le client en question (le mListener) n'est plus joignable -> On l'enleve de la liste des Listener
	*/
	private void notifyMsgListener(String msg, MsgListener sender) throws RemoteException {
		for(MsgListener mListener : listeners) {
			try {
				if(!mListener.equals(sender)) {
					mListener.newMsg(msg);
				}
			} catch(RemoteException e) {
				this.removeMessageListener(mListener);
			}
		}
	}
	
	/*
	 * Methode pour ajouter un Listener
	 * 
	 * MsgListener msgListener -> Client souhaitant s'ajouter a la liste
	 * 
	 * on sayAll() -> On envoie un message a tout le monde pour annoncer l'arriv�e du nouvel utilisateur -> .returnName() implemente cote client
	 * listener.add -> On ajoute le client a la liste
	 */
	@Override
	public void addMessageListener(MsgListener msgListener) throws RemoteException {
		sayAll(msgListener.returnName()+" vient de rejoindre le chat !");
		listeners.add(msgListener);
	}
	
	/*
	 * Methode pour enlever un Listener
	 * 
	 * MsgListener msgListener -> Client a enlever
	 * 
	 * listener.remove -> On enleve le client de la liste
	 * on sayAll() -> On informe tout le monde que le client en question est partie -> .returnName() implemente cote client
	 */
	@Override
	public void removeMessageListener(MsgListener msgListener) throws RemoteException {
		listeners.remove(msgListener);
		sayAll(msgListener.returnName()+" vient de quitter le chat !");
	}
	
	/*
	 * Fonctionne comme notifyMsgListener mais sans distinction de Listener 
	 * (Sans le if) donc litt�ralement tout le monde re�oit le message
	 * 
	 * catch(Remote Exception e)
	 * 
	 * voir notifyMsgListener()
	 */
	public void sayAll(String msg) throws RemoteException {
		for(MsgListener mListener : listeners) {
			try {
				mListener.newMsg(msg);
			}catch (RemoteException e) {
				this.removeMessageListener(mListener);
			}
		}
	}
	
	/*
	 * Systen.setProperty -> On definit l'adresse IP du serveur RMI
	 * 
	 * createRegistry -> On definie le port o� le registre RMI est cr�e 
	 * MessageServerImpl serveur = new MessageServerImpl() -> On cr�e l'objet serveur
	 * Naming.rebind -> on lie le lien du serveur a l'objet serveur precedemment cr�e
	 * 
	 * On catch les potentielles erreurs
	 */
	public static void main(String args[]) {
		System.setProperty("java.rmi.server.hostname", "192.168.1.20");
		try {
			LocateRegistry.createRegistry(1099);
			
			MessageServerImpl serveur = new MessageServerImpl();
			Naming.rebind("//localhost/RmiServer", serveur);
			
			System.err.println("Serveur d�marr� !");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
