package moteur;

import graphique.FenetreOptionReseau;
import graphique.FenetrePrincipale;

import graphique.ListeParties;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import reseau.ClientImpl;
import reseau.Serveur;
import reseau.exception.PartieException;
import reseau.exception.PseudoException;
import util.Couple;
import util.Pile;

/**
 *
 * @author dodelien
 */
public class Diabolik implements Runnable, Serializable {

	private FenetrePrincipale fenetrePrincipale;
	private Joueur joueur1;
	private Joueur joueur2;
	private Plateau plateau;
	private Joueur joueurCourant;
	private int nbMouvementsRestants;
	private boolean passeEffectuee;
	private Thread threadIA;
	private boolean shutdown = false;
	private Pile<Action> annuler;
	private Pile<Action> refaire;
	private boolean finDeTourAutomatique;
	//attributs reseau
	//TODO parametrage du port par expérimentés
	private ClientImpl client;
	private int port = 1099;
	private Coup coupReseau;
	private int ancienMode;
	/*
	 * Gestion du mode 
	 */
	private boolean basique = true;
	public final static int JoueurvsJoueur = 0;
	public final static int JoueurvsOrdi = 1;
	public final static int OrdivsOrdi = 2;
	public final static int OrdivsJoueur = 3;
	public final static int Reseau = 4;
	private int modeDeJeu = JoueurvsOrdi;
	private int niveauIA1 = 0;
	private int niveauIA2 = 1;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Diabolik());
	}

	@Override
	public void run() {
		fenetrePrincipale = new FenetrePrincipale(this);
		nouvellePartie();
	}

	public void shutdown() {
		shutdown = true;
	}

	public void setThreadIA(Thread threadIA) {
		this.threadIA = threadIA;
	}

	public Diabolik() {
		//XXX utile pour l'affichage netBeans; à supprimer à la fin
		plateau = new Plateau();
	}

	public void changementJoueur() {
		fenetrePrincipale.getFinDeTour().setEnabled(false);
		joueurCourant = joueurCourant.getAdversaire();
		if (modeDeJeu != Reseau) {
			if (joueurCourantIA()) {
				fenetrePrincipale.activerGlassPane(false);
			} else {
				fenetrePrincipale.activerGlassPane(true);
			}
		}
		nbMouvementsRestants = 2;
		passeEffectuee = false;
		fenetrePrincipale.changementJoueurImage();
		fenetrePrincipale.getEcouteur().setPion(null);
		fenetrePrincipale.getAireDeJeu().setPioncourant(null);
		fenetrePrincipale.repaintAireDeJeu();
	}

	public boolean demiCoupTermine() {
		return passeEffectuee && nbMouvementsRestants == 0;
	}

	public Joueur getJoueur1() {
		return joueur1;
	}

	public Joueur getJoueur2() {
		return joueur2;
	}

	public Joueur getJoueurCourant() {
		return joueurCourant;
	}

	public ClientImpl getClient() {
		return client;
	}

	public boolean joueurCourantIA() {
		return joueurCourant instanceof JoueurIA;
	}

	public Plateau getPlateau() {
		return plateau;
	}

	public int getNbMouvementsRestants() {
		return nbMouvementsRestants;
	}

	public boolean passeEffectuee() {
		return passeEffectuee;
	}

	public void setNbMouvementsRestants(int nbMouvementsRestants) {
		fenetrePrincipale.getFinDeTour().setEnabled(true);
		this.nbMouvementsRestants = nbMouvementsRestants;
		fenetrePrincipale.setImageMouvement(nbMouvementsRestants);
	}

	public void setPasseEffectuee(boolean passeEffectuee) {
		if (nbMouvementsRestants == 2) {
			fenetrePrincipale.getFinDeTour().setEnabled(passeEffectuee);
		}
		this.passeEffectuee = passeEffectuee;
		fenetrePrincipale.setImagePasse(!passeEffectuee);
	}

	public boolean balleAuBut() {
		return balleAuBut(joueurCourant);
	}

	public boolean balleAuBut(Joueur joueur) {
		return plateau.balleAuBut(joueur.getCouleur());
	}

	public boolean antiJeu(short couleur) {
		return plateau.antiJeu(couleur);
	}

	public void jouerIA() {
		Coup coup = ((JoueurIA) joueurCourant).choisirCoup(plateau);
		fenetrePrincipale.animer(coup);
		for (Action action : coup.getActions()) {
			alimenterAnnuler(action);
		}
	}

	public Joueur getJoueurGagnant() {
		return getJoueurGagnant(joueurCourant);
	}

	public Joueur getJoueurGagnant(Joueur joueur) {
		Joueur joueurGagnant = null;
		if (balleAuBut(joueur) || antiJeu(joueur.getAdversaire().getCouleur())) {
			joueurGagnant = joueur;
		} else if (antiJeu(joueur.getCouleur())) {
			joueurGagnant = joueur.getAdversaire();
		}
		return joueurGagnant;
	}

	public void setPlateau(Plateau plateau) {
		this.plateau = plateau;
	}

	public void setJoueur1(Joueur joueur) {
		this.joueur1 = joueur;
	}

	public void setJoueur2(Joueur joueur) {
		this.joueur2 = joueur;
	}

	public void setJoueurCourant(Joueur joueur) {
		this.joueurCourant = joueur;
	}

	public Thread getThreadIA() {
		return threadIA;
	}

	public FenetrePrincipale getFenetrePrincipale() {
		return fenetrePrincipale;
	}

	public void initialiserReseau(String ip, String pseudo) throws NotBoundException, RemoteException, PseudoException {
		if (client != null) {
			arreterConnexion();
		}

		Registry registry = LocateRegistry.getRegistry(ip, port);
		Serveur serveur = (Serveur) registry.lookup("diaballik");

		client = new ClientImpl(this, serveur, pseudo);

		int id = client.connexionServeur();
		if (id > 0) { // connexion ok
			ancienMode = modeDeJeu;
			modeDeJeu = Reseau;
			fenetrePrincipale.setModeReseau(true);
			fenetrePrincipale.afficherListeParties();
		} else { // erreur
			arreterConnexion();
			fenetrePrincipale.setModeReseau(false);

			switch (id) {
				case 0:
					break;
				case Serveur.PSEUDO_INVALIDE: // pseudo trop long
					throw new PseudoException(FenetreOptionReseau.err_pseudo_invalide);
				case Serveur.PSEUDO_EXISTANT: // pseudo déjà existant
					throw new PseudoException(FenetreOptionReseau.err_pseudo_existant);
				default:
					break;

			}
		}
	}

	public Coup getCoupReseau() {
		return coupReseau;
	}

	public void setCoupReseau(Coup coupReseau) {
		this.coupReseau = coupReseau;
	}

	synchronized public Coup getCoup() {
		coupReseau = new Coup();
		changementJoueur();
		fenetrePrincipale.activerGlassPane(true);
		try {
			wait();
		} catch (InterruptedException ex) {
			Logger.getLogger(Diabolik.class.getName()).log(Level.SEVERE, null, ex);
		}
		fenetrePrincipale.activerGlassPane(false);

		setAnnulerPile(new Pile<Action>());
		setRefairePile(new Pile<Action>());
		fenetrePrincipale.activerAnnuler(false);
		fenetrePrincipale.activerRefaire(false);

		Coup coup = coupReseau;
		coupReseau = null;
		return coup;
		
	}

	synchronized public void prevenirCoupFini() {
		notify();
	}

	public void arreterConnexion() {
		if (client != null) {
			client.quitterPartieEnCours();
			try {
				// Arrêt du thread du RMI
				UnicastRemoteObject.unexportObject(client, true);
			} catch (NoSuchObjectException ex) {
			}
			client = null;
		}
		coupReseau = null;
		modeDeJeu = JoueurvsJoueur;
	}

	public String getMonIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
			return "erreur";
		}
	}

	public void retourAncienMode() {
		modeDeJeu = ancienMode;
	}

	public void envoyerMessageGeneral(String message) throws RemoteException {
		client.envoyerMessageGeneral(message);
	}

	public boolean envoyerMessagePrive(String message, String pseudo) throws RemoteException {
		return client.envoyerMessagePrive(message, pseudo);
	}

	public int creerPartie(String nomPartie, boolean variante) throws RemoteException {
		return client.creerPartie(nomPartie, variante);
	}

	public void rejoindrePartie(int idPartie) throws RemoteException, PartieException {
		int code = client.rejoindrePartie(idPartie);
		switch (code) {
			case Serveur.PARTIE_OK:
				break;
			case Serveur.PARTIE_PLEINE:
				throw new PartieException(ListeParties.err_partie_pleine);
			case Serveur.PARTIE_DEJA_CONNECTE:
				throw new PartieException(ListeParties.err_partie_deja_connecte);
			case Serveur.PARTIE_NON_TROUVE:
				throw new PartieException(ListeParties.err_partie_dispo);
			default:
				throw new PartieException(ListeParties.err_connexion);
		}
	}

	public void partieEnCours(boolean b) {
		fenetrePrincipale.setPartieEnCours(b);
	}

	public Couple<Integer, String>[] getParties() throws RemoteException {
		HashMap<Integer, String> partiesDispo = client.getNomPartiesDispo();
		Couple<Integer, String>[] liste = new Couple[partiesDispo.size()];
		int i = 0;
		for (Map.Entry<Integer, String> val : partiesDispo.entrySet()) {
			liste[i++] = new Couple<Integer, String>(val.getKey(), val.getValue());
		}

		return liste;
	}

	public int ancienNbDepl(short couleur) {
		int i = 2;
		for (int j = 0; j < annuler.size() && annuler.get(j).getPion().getCouleur() == couleur; j++) {
			if (annuler.get(j) instanceof Deplacement) {
				i -= ((Deplacement) annuler.get(j)).getNbDeplacements();
			}
		}
		return i;
	}

	public boolean ancienPasse(short couleur) {
		boolean passe = false;
		for (int j = 0; j < annuler.size() && annuler.get(j).getPion().getCouleur() == couleur; j++) {
			if (annuler.get(j) instanceof Passe) {
				passe = true;
			}
		}
		return passe;
	}

	private boolean actionIA(Action action) {
		if (action.getPion().getCouleur() == Pion.NOIR) {
			return joueur2 instanceof JoueurIA;
		} else {
			return joueur1 instanceof JoueurIA;
		}
	}

	public void annuler() {
		fenetrePrincipale.getAireDeJeu().setPioncourant(null);
		if (actionIA(annuler.pic())) {
			while (!annuler.isEmpty() && actionIA(annuler.pic())) {
				annulerAction();
			}
			changementJoueur();
			setNbMouvementsRestants(ancienNbDepl(joueurCourant.getCouleur()));
			setPasseEffectuee(ancienPasse(joueurCourant.getCouleur()));
		} else {
			annulerAction();
			setNbMouvementsRestants(ancienNbDepl(joueurCourant.getCouleur()));
			setPasseEffectuee(ancienPasse(joueurCourant.getCouleur()));	
		}
	}

	private void annulerAction() {
		Action action = annuler.pop();
		if (action.getPion().getCouleur() != joueurCourant.getCouleur()) {
			changementJoueur();
		}
		fenetrePrincipale.animerAnnuler(action);

		refaire.add(action);
		if (action instanceof Deplacement) {
			setNbMouvementsRestants(nbMouvementsRestants + ((Deplacement) action).getNbDeplacements());
		} else {
			setPasseEffectuee(false);
		}
		if (nbMouvementsRestants == 2) {
			fenetrePrincipale.getFinDeTour().setEnabled(false);
		}
		fenetrePrincipale.activerRefaire(true);
		if (annuler.isEmpty()) {
			fenetrePrincipale.activerAnnuler(false);
		}
		if (modeDeJeu == Reseau) {
			coupReseau.supprimerAction();
		}
	}

	public void refaire() {
		fenetrePrincipale.getAireDeJeu().setPioncourant(null);
		if (actionIA(refaire.pic())) {
			while (!refaire.isEmpty() && actionIA(refaire.pic())) {
				refaireAction();
			}
		} else {
			refaireAction();
		}
	}

	private void refaireAction() {
		Action action = refaire.pop();
		if (action.getPion().getCouleur() != joueurCourant.getCouleur()) {
			changementJoueur();
		}
		fenetrePrincipale.animer(action);

		annuler.add(action);
		if (action instanceof Deplacement) {
			setNbMouvementsRestants(nbMouvementsRestants - ((Deplacement) action).getNbDeplacements());
		} else {
			setPasseEffectuee(true);
		}
		fenetrePrincipale.activerAnnuler(true);
		if (refaire.isEmpty()) {
			fenetrePrincipale.activerRefaire(false);
		}
		fenetrePrincipale.getFinDeTour().setEnabled(true);
		fenetrePrincipale.repaintAireDeJeu();
		if (modeDeJeu == Reseau) {
			coupReseau.ajouterAction(action);
		}
	}

	public void alimenterAnnuler(Action action) {
		annuler.add(action);
		fenetrePrincipale.activerAnnuler(true);
		fenetrePrincipale.activerRefaire(false);
		refaire = new Pile<Action>();

		if (modeDeJeu == Reseau) {
			coupReseau.ajouterAction(action);
		}
	}

	public Pile<Action> getAnnulerPile() {
		return annuler;
	}

	public void setAnnulerPile(Pile<Action> annuler) {
		this.annuler = annuler;
	}

	public Pile<Action> getRefairePile() {
		return refaire;
	}

	public void setRefairePile(Pile<Action> refaire) {
		this.refaire = refaire;
	}

	public boolean getShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	public boolean isPasseEffectuee() {
		return passeEffectuee;
	}
	/*
	 * 
	 * Création de partie
	 * 
	 */

	public void nouvellePartie() {
		fenetrePrincipale.getAireDeJeu().setPioncourant(null);
		setShutdown(false);
		setAnnulerPile(new Pile<Action>());
		setRefairePile(new Pile<Action>());
		fenetrePrincipale.activerAnnuler(false);
		fenetrePrincipale.activerRefaire(false);
		fenetrePrincipale.setNouvellePartie(true);
		fenetrePrincipale.activerGlassPane(true);

		try {
			switch (modeDeJeu) {
				case JoueurvsJoueur:
					setJoueur1(new JoueurHumain(Pion.BLANC));
					setJoueur2(new JoueurHumain(Pion.NOIR));
					break;
				case JoueurvsOrdi:
					setJoueur1(new JoueurHumain(Pion.BLANC));
					setJoueur2(selectionnerIA(niveauIA2, Pion.NOIR));
					break;
				case OrdivsJoueur:
					setJoueur1(selectionnerIA(niveauIA1, Pion.BLANC));
					setJoueur2(new JoueurHumain(Pion.NOIR));
					break;
				case OrdivsOrdi:
					setJoueur1(selectionnerIA(niveauIA1, Pion.BLANC));
					setJoueur2(selectionnerIA(niveauIA2, Pion.NOIR));
					break;
				case Reseau:
					fenetrePrincipale.activerGlassPane(false);
					if (client.getCouleur() == Pion.BLANC) {
						setJoueur1(new JoueurHumain(client.getPseudo(), Pion.BLANC));
						setJoueur2(new JoueurHumain(client.getPartie().getJoueur(Pion.NOIR).getNom(), Pion.NOIR));
					} else {
						setJoueur1(new JoueurHumain(client.getPartie().getJoueur(Pion.BLANC).getNom(), Pion.BLANC));
						setJoueur2(new JoueurHumain(client.getPseudo(), Pion.NOIR));
					}
					break;
			}

			fenetrePrincipale.setTextJoueurBlanc(joueur1.getNom());
			fenetrePrincipale.setTextJoueurNoir(joueur2.getNom());

			if (modeDeJeu == Reseau) {
				setPlateau(client.getPartie().getPlateau());
				if (client.getCouleur() == Pion.BLANC) {
					setJoueurCourant(getJoueur1());
				} else {
					setJoueurCourant(getJoueur2());
				}
				fenetrePrincipale.activerAstuce(false);
			} else {
				setPlateau(new Plateau(basique));
				setJoueurCourant(getJoueur2());
				fenetrePrincipale.activerAstuce(true);
			}
			getJoueur1().setAdversaire(getJoueur2());
			getJoueur2().setAdversaire(getJoueur1());
			changementJoueur();
			
			fenetrePrincipale.getFinDeTour().setEnabled(false);
			nbMouvementsRestants = 2;
			passeEffectuee = false;

			fenetrePrincipale.repaintAireDeJeu();
		} catch (RemoteException ex) {
			Logger.getLogger(Diabolik.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (joueurCourantIA()) {
			jouerIA();
		}

	}

	public int getModeDeJeu() {
		return modeDeJeu;
	}

	public JoueurIA selectionnerIA(int level, short couleur) {
		switch (level) {
			case 1:
				return new JoueurMonteCarloLasVegasIA(couleur);
			case 2:
				return new JoueurNegaMaxIA(couleur);
			case 3:
				return new JoueurNegaMaxAdaptatifIA(couleur);
		}
		return null;
	}

	public void setModeDeJeu(int mode) {
		modeDeJeu = mode;
	}

	public void setNiveauIA1(int niveauIA1) {
		this.niveauIA1 = niveauIA1;
	}

	public void setNiveauIA2(int niveauIA2) {
		this.niveauIA2 = niveauIA2;
	}

	public int getNiveauIA1() {
		return niveauIA1;
	}

	public int getNiveauIA2() {
		return niveauIA2;
	}

	public boolean isBasique() {
		return basique;
	}

	public void setBasique(boolean basique) {
		this.basique = basique;
	}

	public void finDePartie(Joueur joueurGagnant) {
		fenetrePrincipale.afficherFin(joueurGagnant.getNom());
	}

	public boolean verifierGagne() {
		return verifierGagne(joueurCourant);
	}
	
	public boolean verifierGagne(Joueur joueur) {
		if (modeDeJeu != Reseau) {
			Joueur joueurGagnant = getJoueurGagnant(joueur);
			if (joueurGagnant != null) {
				finDePartie(joueurGagnant);
				return true;
			}
		}
		return false;
	}

	public void activerFinDeTour() {
		if (finDeTourAutomatique && demiCoupTermine()){
			fenetrePrincipale.getSurbrillanceFinDuTour().setVisible(false);
			changementJoueur();
			if (getModeDeJeu() == Diabolik.Reseau) {
				prevenirCoupFini();
			} else if (joueurCourantIA()) {
				fenetrePrincipale.repaintAireDeJeu();
				Timer t = new Timer(0, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						jouerIA();
					}
				});
				t.setInitialDelay(1000);
				t.setRepeats(false);
				t.start();
			}
		}
	}

	public boolean isFinDeTourAutomatique() {
		return finDeTourAutomatique;
	}

	public void setFinDeTourAutomatique(boolean finDeTourAutomatique) {
		this.finDeTourAutomatique = finDeTourAutomatique;
	}

	public void montrerIndice() {
		JoueurIA joueurIndice = new JoueurIndice(joueurCourant.getCouleur(), nbMouvementsRestants, passeEffectuee);
		joueurIndice.setAdversaire(joueurCourant.getAdversaire());
		Coup coupIndice = joueurIndice.choisirCoup(plateau);

		fenetrePrincipale.getMyGlassPane1().logobiIndice(coupIndice, plateau);
	}
}
