package reseau.serveur;

import java.rmi.RemoteException;
import moteur.Coup;
import moteur.Joueur;
import reseau.Client;


class JoueurServ extends Joueur {

	private Client client;
	private int id;
	
	JoueurServ(Client client, int id, short couleur) throws RemoteException {
		super(client.getPseudo(), couleur);
		this.client = client;
		this.id = id;
		
		client.setCouleur(couleur);
	}

	void recevoirMessage(String message, JoueurServ joueur) throws RemoteException {
		client.recevoirMessagePartie(message, joueur.getNom());
	}

	void recevoirInformation(String info) throws RemoteException {
		client.recevoirInformationPartie(info);
	}
	
	int getId() {
		return id;
	}

	Coup demanderCoup() throws RemoteException {
		return client.getCoup();
	}

	void actualiserPlateau(Coup coup) throws RemoteException {
		client.recevoirCoup(coup);
	}

	void annulerCoup() throws RemoteException {
		client.annulerCoup();
	}

	void demarrerPartie() throws RemoteException {
		client.demarrerPartie();
	}

	void partieGagnee(boolean b) throws RemoteException {
		client.partieGagnee(b);
	}

	void adversairePerdu() throws RemoteException {
		client.adversairePerdu();
	}
	
}
