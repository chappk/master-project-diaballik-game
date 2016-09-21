package reseau;

import java.rmi.Remote;
import java.rmi.RemoteException;
import moteur.Joueur;
import moteur.Plateau;

/**
 *
 * @author vernagaa
 */
public interface Partie extends Remote {

	public Plateau getPlateau() throws RemoteException;
	
	public Joueur getJoueur(short couleur) throws RemoteException;
	
	public String getNom() throws RemoteException;

	public void envoyerMessage(String message, int idEmetteur) throws RemoteException;

	public void quitterPartie(int idClient) throws RemoteException;
}
