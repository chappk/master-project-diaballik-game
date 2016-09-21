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
public class NegaMaxIndice {

	public static int n;
	public static int m;
	public static int profondeurInitiale;

	public static Coup negamaxDebut(Plateau plateau, Joueur joueur, int nbMouvementsRestants, boolean passeEffectuee) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		profondeurInitiale = 2;
		negamaxDebut(plateau, 2, joueur, meilleurCoup, nbMouvementsRestants, passeEffectuee);
		return meilleurCoup;
	}

	public static int negamaxDebut(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup, int nbMouvementsRestants, boolean passeEffectuee) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluationDebut(plateau, joueur);
		}
		n++;
		HashSet<Coup> listeCoups;
		if (profondeur == profondeurInitiale) {
			listeCoups = joueur.coupsLegauxIndice(plateau, nbMouvementsRestants, passeEffectuee);
		} else {
			listeCoups = joueur.coupsLegaux(plateau);
		}

		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = Integer.MIN_VALUE;
		int val;

		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			val = -negamaxDebut(plateau, profondeur - 1, adversaire, null, nbMouvementsRestants, passeEffectuee);
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

	public static Coup negamaxMilieu(Plateau plateau, Joueur joueur, int nbMouvementsRestants, boolean passeEffectuee) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		profondeurInitiale = 2;
		negamaxMilieu(plateau, 2, joueur, meilleurCoup, nbMouvementsRestants, passeEffectuee);
		return meilleurCoup;
	}

	public static int negamaxMilieu(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup, int nbMouvementsRestants, boolean passeEffectuee) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluationMilieu(plateau, joueur);
		}
		n++;

		HashSet<Coup> listeCoups;
		if (profondeur == profondeurInitiale) {
			listeCoups = joueur.coupsLegauxIndice(plateau, nbMouvementsRestants, passeEffectuee);
		} else {
			listeCoups = joueur.coupsLegaux(plateau);
		}
		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = Integer.MIN_VALUE;
		int val;

		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			val = -negamaxMilieu(plateau, profondeur - 1, adversaire, null, nbMouvementsRestants, passeEffectuee);
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

	public static Coup negamaxFin(Plateau plateau, Joueur joueur, int nbMouvementsRestants, boolean passeEffectuee) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		profondeurInitiale = 2;
		negamaxFin(plateau, 2, joueur, meilleurCoup, nbMouvementsRestants, passeEffectuee);
		return meilleurCoup;
	}

	public static int negamaxFin(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup, int nbMouvementsRestants, boolean passeEffectuee) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluationFin(plateau, joueur);
		}
		n++;

		HashSet<Coup> listeCoups;
		if (profondeur == profondeurInitiale) {
			listeCoups = joueur.coupsLegauxIndice(plateau, nbMouvementsRestants, passeEffectuee);
		} else {
			listeCoups = joueur.coupsLegaux(plateau);
		}
		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = Integer.MIN_VALUE;
		int val;

		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			val = -negamaxFin(plateau, profondeur - 1, adversaire, null, nbMouvementsRestants, passeEffectuee);
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

		int ck = evaluationCoupsKasparov(plateau, joueur);
		eval = ck;

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

//        ArrayList<Passe> passes = plateau.passesPossibles(couleur);
//        for (Passe passe : passes) {
//            int d = passe.getPion().getDistanceDepart();
//            eval += d * d;
//            if (d == 6) {
//                eval += 1000;
//            }
//        }

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
			return 4000;
		} else if (plateau.balleAuBut(couleurAd)) {
			return -4000;
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
