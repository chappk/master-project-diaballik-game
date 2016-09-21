package reseau.serveur;

import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import moteur.Coup;
import moteur.Pion;
import moteur.Plateau;
import reseau.Partie;

class PartieImpl extends UnicastRemoteObject implements Partie, Runnable {

	private int id;
	private String nom;
	private ServeurImpl serveur;
	private JoueurServ joueur1;
	private JoueurServ joueur2;
	private Plateau plateau;
	private JoueurServ joueurCourant;

	PartieImpl(int id, String nom, boolean variante, ServeurImpl serveur) throws RemoteException {
		this.id = id;
		this.nom = nom;
		this.serveur = serveur;
		
		plateau = new Plateau(!variante);
		joueurCourant = joueur1;
	}

	@Override
	public void run() {
		try {
			System.out.println("Partie "+id+" créée par " + joueur1.getNom());
			System.out.println("Partie "+id+" : attente d'un adversaire...");
			joueur1.recevoirInformation("Attente d'un adversaire...");
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException ex) {
					Logger.getLogger(PartieImpl.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			if (joueur2 != null) {
				//début de la partie...
				joueur1.setAdversaire(joueur2);
				joueur2.setAdversaire(joueur1);
				
				joueur2.recevoirInformation("Bienvenue dans la partie de " + joueur1.getNom());

				joueur1.demarrerPartie();
				joueur2.demarrerPartie();

				System.out.println(joueur2.getNom() + "("+ joueur2.getId() + ") a rejoint la partie " + id);
				joueur1.recevoirInformation(joueur2.getNom() + " a rejoint la partie !");

				deroulementPartie();
				terminerPartie();
			}
		} catch (RemoteException ex) {
		}
	}

	@Override
	public JoueurServ getJoueur(short couleur) {
		return couleur==Pion.BLANC ? joueur1 : joueur2;
	}

	@Override
	public Plateau getPlateau() {
		return plateau;
	}

	@Override
	public String getNom() {
		return nom;
	}

	@Override
	public void envoyerMessage(String message, int idEmetteur) {
		JoueurServ emetteur = null;
		if (joueur1 != null && joueur1.getId() == idEmetteur)
			emetteur = joueur1;
		else if (joueur2 != null && joueur2.getId() == idEmetteur)
			emetteur = joueur2;

		if (emetteur != null) {
			try {
				joueur1.recevoirMessage(message, emetteur);
				joueur2.recevoirMessage(message, emetteur);
			} catch (RemoteException ex) {
			}
		}
	}
	
	@Override
	public void quitterPartie(int idClient) {
		joueurPerdu(idClient);
		terminerPartie();
	}

	private void deroulementPartie() throws RemoteException {
		joueur1.recevoirInformation("Vous avez les pions blancs");
		joueur2.recevoirInformation("Vous avez les pions noirs");
		joueur2.recevoirInformation("Votre adversaire est en train de jouer...");
		
		JoueurServ joueurGagnant = null;

		try {
			while (joueurGagnant == null) {
				//Coup coup = Coup.conversion(joueurCourant.demanderCoup(), plateau);
				Coup coup = joueurCourant.demanderCoup();
				if(coup == null)
					throw new UnmarshalException("Coup nul");
				coup.convertir(plateau);
				// vérification et exécution du coup
				if (coupValide(coup, joueurCourant.getCouleur())) {
					coup.jouer(plateau);
					((JoueurServ) joueurCourant.getAdversaire()).actualiserPlateau(coup);

					joueurGagnant = getJoueurGagnant();
//					joueurGagnant = joueurCourant;//XXX supprimer ligne (test qui fait gagner le 1er à jouer)
					if (joueurGagnant == null) {
						joueurCourant.recevoirInformation("Votre adversaire est en train de jouer...");
						changementJoueur();
					}
				} else {
					joueurCourant.recevoirInformation("Actions invalides, recommencez");
					joueurCourant.annulerCoup();
				}
			}
			// Un joueur a gagné
			joueurGagnant.recevoirInformation("Vous avez gagné !");
			((JoueurServ) joueurGagnant.getAdversaire()).recevoirInformation("Vous avez perdu !");

			joueurGagnant.partieGagnee(true);
			((JoueurServ) joueurGagnant.getAdversaire()).partieGagnee(false);
		} catch (UnmarshalException ex) {
			//joueurCourant semble s'être barré
			System.out.println(joueurCourant.getNom() + " n'a pas envoyé de coup");
			((JoueurServ) joueurCourant.getAdversaire()).adversairePerdu();
		}
		terminerPartie();
	}
	
	private void terminerPartie() {
		serveur.supprimerPartie(this);
	}
	
	private boolean coupValide(Coup coup, short couleur) {
		if (coup == null)
			return false;
		else
			return coup.legal(plateau, couleur);
	}

	private void changementJoueur() {
		if (joueurCourant == joueur1) {
			joueurCourant = joueur2;
		} else {
			joueurCourant = joueur1;
		}
	}

	private JoueurServ getJoueurGagnant() {
		if (plateau.balleAuBut(joueurCourant.getCouleur()) || plateau.antiJeu(joueurCourant.getAdversaire().getCouleur()))
			return joueurCourant;
		else if (plateau.antiJeu(joueurCourant.getCouleur()))
			return (JoueurServ) joueurCourant.getAdversaire();
		else
			return null;
	}

	void initialiser() {
		new Thread(this).start();
	}

	synchronized void demarrer() {
		notify();
	}

	boolean lierJoueur(JoueurServ j) {
		if (joueur1 == null) {
			joueur1 = j;
			joueurCourant = j;
			return true;
		} else if (joueur2 == null) {
			joueur2 = j;
			return true;
		} else {
			return false;
		}
	}

	int getId() {
		return id;
	}

	void setIdPartie(int idPartie) {
		this.id = idPartie;
	}

	void setJoueur1(JoueurServ joueur1) {
		this.joueur1 = joueur1;
	}

	void setJoueur2(JoueurServ joueur2) {
		this.joueur2 = joueur2;
	}

	JoueurServ getJoueurCourant() {
		return joueurCourant;
	}

	void setJoueurCourant(JoueurServ joueurCourant) {
		this.joueurCourant = joueurCourant;
	}

	void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}

	void setNom(String nom) {
		this.nom = nom;
	}

	void joueurPerdu(int idJoueur) {
		JoueurServ joueurRestant = null;
		if (joueur1 != null && joueur1.getId() == idJoueur)
			joueurRestant = joueur2;
		else if (joueur2 != null && joueur2.getId() == idJoueur)
			joueurRestant = joueur1;

		if (joueurRestant != null) {
			try {
				joueurRestant.adversairePerdu();
			} catch (RemoteException ex) {
			}
		}
	}
	
}
