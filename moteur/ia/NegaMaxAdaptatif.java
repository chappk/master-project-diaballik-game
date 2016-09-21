package moteur.ia;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.HashSet;
import moteur.*;
import moteur.ia.exception.ExPasDeCoupPossible;
import util.Fap;
import util.PairePoidsValeur;

/** 
 *
 * @author dodelien
 */
public class NegaMaxAdaptatif {
	
	private static int INFINI = Integer.MAX_VALUE / 2;
	public static int n;
	public static int m;

	public static Coup negamaxDebut(Plateau plateau, Joueur joueur) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		negamaxDebut(plateau, 2, joueur, meilleurCoup);
		return meilleurCoup;
	}

	public static int negamaxDebut(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluationDebut(plateau, joueur);
		}
		n++;

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = -INFINI;
		int val;

		if (plateau.balleAuBut(adversaire.getCouleur())) {
			return -INFINI;
		}

		HashSet<Coup> listeCoups = joueur.coupsLegaux(plateau);
		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}



		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			val = -negamaxDebut(plateau, profondeur - 1, adversaire, null);
			coup.annuler(plateau);


			if (val > meilleurScore) {
				meilleurScore = val;
				if (meilleurCoup != null) {
					meilleurCoup.copier(coup);
				}
			}
		}
		return meilleurScore;
	}

	public static Coup negamaxMilieu(Plateau plateau, Joueur joueur) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		negamaxMilieu(plateau, 2, joueur, meilleurCoup);
		return meilleurCoup;
	}

	public static int negamaxMilieu(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluationMilieu(plateau, joueur);
		}
		n++;

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = -INFINI;
		int val;

		if (plateau.balleAuBut(adversaire.getCouleur())) {
			return -INFINI;
		}

		HashSet<Coup> listeCoups = joueur.coupsLegaux(plateau);
		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}



		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			val = -negamaxMilieu(plateau, profondeur - 1, adversaire, null);
			coup.annuler(plateau);

			if (val > meilleurScore) {
				meilleurScore = val;
				if (meilleurCoup != null) {
					meilleurCoup.copier(coup);
				}
			}
		}
		return meilleurScore;
	}

	public static Coup negamaxFin(Plateau plateau, Joueur joueur) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		negamaxFin(plateau, 2, joueur, meilleurCoup);
		return meilleurCoup;
	}

	public static int negamaxFin(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluationFin(plateau, joueur);
		}
		n++;

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = -INFINI;
		int val;

		if (plateau.balleAuBut(adversaire.getCouleur())) {
			return -INFINI;
		}

		HashSet<Coup> listeCoups = joueur.coupsLegaux(plateau);
		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}

		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			val = -negamaxFin(plateau, profondeur - 1, adversaire, null);
			coup.annuler(plateau);

			if (val > meilleurScore) {
				meilleurScore = val;
				if (meilleurCoup != null) {
					meilleurCoup.copier(coup);
				}
			}
		}
		return meilleurScore;
	}

	private static int evaluationDebut(Plateau plateau, Joueur joueur) {
		m++;
		short couleur = joueur.getCouleur();
		int eval;

		eval = evaluationCoupsKasparov(plateau, joueur);

		for (Pion pion : plateau.getPions(couleur)) {
			eval += pion.getDistanceDepart() * pion.getDistanceDepart();
			if (pion.getDistanceDepart() == 6) {
				eval += 500;
			}
		}

		return eval;
	}

	private static int evaluationMilieu(Plateau plateau, Joueur joueur) {
		m++;
		short couleur = joueur.getCouleur();
		int eval;

		eval = evaluationCoupsKasparov(plateau, joueur);

		for (Pion pion : plateau.getPions(couleur)) {
			eval += pion.getDistanceDepart();
			if (pion.getDistanceDepart() == 6) {
				eval += 500;
			}
		}

		eval += evaluationPassesIndirectes(plateau, joueur);

		eval += Math.pow(plateau.getBallon(couleur).getDistanceDepart(), 4);

		return eval;
	}

	private static int evaluationFin(Plateau plateau, Joueur joueur) {
		m++;
		short couleur = joueur.getCouleur();
		int eval;

		eval = evaluationCoupsKasparov(plateau, joueur);
		eval += evaluationPassesIndirectes(plateau, joueur);
		eval += Math.pow(plateau.getBallon(couleur).getDistanceDepart(), 4);

		return eval;
	}

	private static int evaluationCoupsKasparov(Plateau plateau, Joueur joueur) {
		short couleur = joueur.getCouleur();
		short couleurAd = joueur.getAdversaire().getCouleur();

		if (plateau.balleAuBut(couleur)) {
			return INFINI;
		} else if (plateau.balleAuBut(couleurAd)) {
			return -INFINI;
		}

		return (plateau.coupsFinaux(couleur) - plateau.coupsFinaux(couleurAd)) * 1000;
	}

	private static int evaluationPassesIndirectes(Plateau plateau, Joueur joueur) {
		Integer[] distance = new Integer[Plateau.TAILLE];
		short couleur = joueur.getCouleur();
		Pion ballon = plateau.getBallon(couleur);
		Fap<Pion> fap = new Fap<Pion>();
		fap.inserer(ballon, 0);
		distance[ballon.getNumero()] = 0;
		while (!fap.estVide()) {
			PairePoidsValeur<Pion> ppv = fap.extraire();
			plateau.faireLaPasse(ppv.valeur);
			ArrayList<Passe> passesPossibles = plateau.passesPossibles(couleur);
			for (Passe passe : passesPossibles) {
				Pion pion = passe.getPion();
				int dist = distance[ppv.valeur.getNumero()] + 1;
				Integer d = distance[pion.getNumero()];
				if (d == null || dist < d) {
					distance[pion.getNumero()] = dist;
					fap.inserer(pion, dist);
				}
			}
		}
		plateau.faireLaPasse(ballon);

		int eval = 0;

		for (int i = 0; i < Plateau.TAILLE; i++) {
			Integer dist = distance[i];
			if (dist != null && dist != 0) {
				Pion pion = plateau.getPion(couleur, i);
				int d = pion.getDistanceDepart();
				eval += d * d * d / dist;
				if (d == 6) {
					eval += 1000 / dist;
				}
			}
		}

		return eval;
	}
}
