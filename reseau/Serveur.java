package reseau;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author vernagaa
 */
public interface Serveur extends Remote {

	public static final int PSEUDO_INVALIDE = -1;
	public static final int PSEUDO_EXISTANT = -2;
	
	public static final int PARTIE_OK = 1;
	public static final int PARTIE_ERREUR = 0;
	public static final int PARTIE_PLEINE = -1;
	public static final int PARTIE_DEJA_CONNECTE = -2;
	public static final int PARTIE_NON_TROUVE = -3;
	
	public int connexionClient(Client c) throws RemoteException;
	
	public int creerPartie(int idClient, String nomPartie, boolean variante) throws RemoteException;

	public int rejoindrePartie(int idJoueur, int idPartie) throws RemoteException;

	public void envoyerMessageGeneral(String message, int idEmetteur) throws RemoteException;

	public boolean envoyerMessagePrive(String message, int idEmetteur, String pseudoDestinataire) throws RemoteException;
	
	public HashMap<Integer, String> getPseudos() throws RemoteException;

	public HashMap<Integer, Partie> getPartiesDispo() throws RemoteException;

	public HashMap<Integer, String> getNomPartiesDispo() throws RemoteException;
}
