package mighstye.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import mighstye.common.MessageServer;
import mighstye.common.MsgListener;

public class MessageMonitor extends UnicastRemoteObject implements MsgListener{
	/*
	 * String name -> Le nom d'utilisateur
	 */
	private static final long serialVersionUID = 1L;
	private static String name;
	
	/*
	 * Constructeur par defaut du client (Et seul constructeur)
	 */
	protected MessageMonitor() throws RemoteException{}

	/*
	 * Methode qui affiche un nouveau message dans la console client
	 * 
	 * String msg -> Nouveau message
	 */
	@Override
	public void newMsg(String msg) throws RemoteException {
		System.out.println(">>>"+msg);
	}
	
	/*
	 * Methode qui retourne le nom d'utilisateur du client
	 */
	@Override
	public String returnName() throws RemoteException {
		return name;
	}
	
	/*
	 * MessageServer messageServer ... -> Connexion au serveur a l'adresse en argument (//localhost/RmiServer)
	 * 
	 * MessageMonitor client = new MessageMonitor() -> Creation d'un objet client
	 * 
	 * Scanner sc... -> Création et initialisation d'un scanner pour recuperer la saisit utilisateur
	 * -> On recupere ensuite le pseudo que l'utilisateur désire avoir
	 * messageServer.addListener(client) -> Appelle a la methode implementé côté serveur pour que le client s'ajoute a la liste des listeners du serveur
	 * 
	 * do[...]while -> Boucle pour envoyer les messages
	 * -> n'envoie pas le message si ce dernier est vide ou "/quit"
	 * -> quitte la boucle si l'utilisateur entre /quit
	 * 
	 * -> Attends que l'utilisateur rentre un nouveau message via un scanner 
	 * --> Envoie ce dernier au serveur via messageServer.sendMessage qui est une methode implementé côté serveur
	 * --> Le message est un peu retravaillé pour etre de la forme : [nom d'utilisateur] : [message]
	 * 
	 * sc.close() -> Nous sommes sortie de la boucle -> Cela signifie que le client désire quitter
	 * --> On ferme le scanner
	 * messageServer.removeMessageListener(client) -> Le client s'enleve de la liste des listeners cote serveur
	 * -> Methode implemente cote serveur
	 * 
	 * finally 
	 * -> Si (Il y a une erreur) ou (tout se termine correctement)
	 * --> System.close(0) -> On quitte le programme
	 */
	public static void main(String args[]) {
		try {
			MessageServer messageServer = (MessageServer)Naming.lookup("//localhost/RmiServer");
			
			System.err.println("Connexion réussis !"); 
			
			MessageMonitor client = new MessageMonitor();
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Pseudo ?"); 
			name=sc.nextLine();
			messageServer.addMessageListener(client);
			String msg = "";
			do {
				msg="";
				msg = sc.nextLine();
				if(!msg.equals("") && (!msg.equals("/quit")))
					messageServer.sendMessage(name+" : "+msg, client); 
			}while(!msg.equals("/quit")); 
			
			sc.close(); 
			messageServer.removeMessageListener(client);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace(); 
		}
		finally {
			System.exit(0); //On quitte le programme avec un code 0 -> Code normal -> Pas d'erreur
		}
	}
}