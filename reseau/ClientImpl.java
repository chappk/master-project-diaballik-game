package reseau;

import graphique.FenetrePrincipale;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import moteur.Coup;
import moteur.Diabolik;
import moteur.Plateau;

/**
 * 
 * @author vernagaa
 */
public class ClientImpl extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = 1L;
	private int idClient;
	private String pseudo;
	private Partie partie;
	private Coup dernierCoup;
	private Serveur serveur;
	private short couleur;
	private final Diabolik diabolik;
	private final FenetrePrincipale fenetre;

	public ClientImpl(Diabolik diabolik, Serveur serveur, String pseudo) throws RemoteException {
		this.diabolik = diabolik;
		this.serveur = serveur;
		this.pseudo = pseudo;
		this.idClient = 0;
		fenetre = diabolik.getFenetrePrincipale();
	}

	public int creerPartie(String nomPartie, boolean variante) throws RemoteException {
		quitterPartieEnCours();
		return serveur.creerPartie(idClient, nomPartie, variante);
	}

	public int rejoindrePartie(int idPartie) throws RemoteException {
		quitterPartieEnCours();
		return serveur.rejoindrePartie(idClient, idPartie);
	}

	public void envoyerMessageGeneral(String message) throws RemoteException {
		serveur.envoyerMessageGeneral(message, idClient);
	}

	public boolean envoyerMessagePrive(String message, String pseudo) throws RemoteException {
		return serveur.envoyerMessagePrive(message, idClient, pseudo);
	}

	public int connexionServeur() throws RemoteException {
		return serveur.connexionClient(this);
	}

	public void quitterPartieEnCours() {
		if(partie != null) {
			if(diabolik.getCoupReseau() != null) {
				diabolik.setCoupReseau(null);
				diabolik.prevenirCoupFini();
			}
			try {
				partie.quitterPartie(idClient);
			} catch (RemoteException ex) {
			}
			supprimerPartie();
			diabolik.partieEnCours(false);
			fenetre.afficherInformationPartie("Vous avez quitté la partie");
		}
	}
	
	public HashMap<Integer, String> getNomPartiesDispo() throws RemoteException {
		return serveur.getNomPartiesDispo();
	}

	public short getCouleur() {
		return couleur;
	}
	
	@Override
	public void recevoirMessage(String message, String pseudo) {
		fenetre.afficherMessage(message, pseudo);
	}

	@Override
	public void recevoirInformation(String info) {
		fenetre.afficherInformation(info);
	}
	
	@Override
	public void recevoirMessagePrive(String message, String pseudo) {
		fenetre.afficherMessagePrive(message, pseudo);
	}

	@Override
	public void recevoirMessagePriveAccuse(String message, String pseudo) {
		fenetre.afficherMessagePriveAccuse(message, pseudo);
	}

	@Override
	public void recevoirInformationPartie(String info) {
		fenetre.afficherInformationPartie(info);
	}
	
	@Override
	public void recevoirMessagePartie(String message, String pseudo) {
		fenetre.afficherMessagePartie(message, pseudo);
	}
			
	@Override //TODO inviterUnJoueur
	public void inviterUnJoueur(int idClient, int idPartie, int idJoueurAInvite) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getId() {
		return idClient;
	}

	@Override
	public Partie getPartie() {
		return partie;
	}

	@Override
	public String getPseudo() {
		return pseudo;
	}

	@Override
	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	@Override
	public void setPartie(Partie p) {
		if (partie != null) {
			try {
				partie.quitterPartie(idClient);
			} catch (RemoteException ex) {
				Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
			supprimerPartie();
		}
		partie = p;
	}

	@Override
	public void setCouleur(short couleur) {
		this.couleur = couleur;
	}

	@Override
	public void demarrerPartie() throws RemoteException {
		diabolik.nouvellePartie();
	}
	
	@Override
	public void partieGagnee(boolean b) {
		supprimerPartie();
		diabolik.partieEnCours(false);
		new Thread(new FinPartie(b)).start(); // ouverture du popup
		
		new Thread(new Runnable() {

			@Override
			public void run() { // utile pour désactiver le gif de chargement chez le joueur gagnant
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					Logger.getLogger(ClientImpl.class.getName()).log(Level.SEVERE, null, ex);
				}
				diabolik.partieEnCours(false);
			}
		}).start();

	}
	
	@Override
	public Coup getCoup() throws RemoteException {
		fenetre.afficherInformationPartie("À vous de jouer");
		fenetre.activerAstuce(true);
		dernierCoup = diabolik.getCoup();
		fenetre.activerAstuce(false);
		return dernierCoup;
	}

	@Override
	public void recevoirCoup(Coup coup) {
		Plateau plateau = diabolik.getPlateau();
		
		//version sans animation
//		Coup.conversion(coup, plateau).jouer(plateau);
//		fenetre.repaintAireDeJeu();
		
		coup.convertir(plateau);
		fenetre.animer(coup);
	}

	@Override
	public void annulerCoup() {
		dernierCoup.annuler(diabolik.getPlateau());
		fenetre.repaintAireDeJeu();
	}

	@Override
	public void adversairePerdu() {
		if (partie != null) {
			supprimerPartie();
			diabolik.partieEnCours(false);
			new Thread(new Runnable() {

				@Override
				public void run() {
					fenetre.afficherAdversairePerdu();
				}
			}).start();
		}
	}
	
	@Override
	public void ping() {
	}

	private void supprimerPartie() {
		partie = null;
		dernierCoup = null;
	}

	private class FinPartie implements Runnable {

		private boolean gagne;
		private FinPartie(boolean b) {
			gagne = b;
		}

		@Override
		public void run() {
			fenetre.afficherPartieGagnee(gagne);
		}
	}

}
