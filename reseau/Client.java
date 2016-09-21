package reseau;

import java.rmi.Remote;
import java.rmi.RemoteException;
import moteur.Coup;

public interface Client extends Remote {

	public void recevoirMessage(String message, String pseudo) throws RemoteException;

	public void recevoirInformation(String info) throws RemoteException;

	public void recevoirMessagePrive(String message, String pseudo) throws RemoteException;

	public void recevoirMessagePriveAccuse(String message, String pseudo) throws RemoteException;

	public void recevoirInformationPartie(String info) throws RemoteException;

	public void recevoirMessagePartie(String message, String pseudo) throws RemoteException;

	public void inviterUnJoueur(int idClient, int idPartie, int idJoueurAInvite) throws RemoteException;

	public void setIdClient(int idClient) throws RemoteException;

	public int getId() throws RemoteException;

	public String getPseudo() throws RemoteException;

	public Partie getPartie() throws RemoteException;

	public void setPartie(Partie p) throws RemoteException;

	public void setCouleur(short couleur) throws RemoteException;

	public void demarrerPartie() throws RemoteException;

	public void partieGagnee(boolean b) throws RemoteException;
	
	public Coup getCoup() throws RemoteException;

	public void recevoirCoup(Coup coup) throws RemoteException;

	public void annulerCoup() throws RemoteException;

	public void adversairePerdu() throws RemoteException;

	public void ping() throws RemoteException;
}
