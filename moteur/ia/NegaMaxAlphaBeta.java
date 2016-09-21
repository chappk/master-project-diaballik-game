package moteur.ia;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.HashSet;
import moteur.*;
import moteur.ia.exception.ExPasDeCoupPossible;

/**
 *
 * @author dodelien
 */
public class NegaMaxAlphaBeta {

	private static int INFINI = Integer.MAX_VALUE / 2;
	public static int n;
	public static int m;

	public static Coup negamax(Plateau plateau, Joueur joueur) throws ExPasDeCoupPossible {
		Coup meilleurCoup = new Coup();
		negamax(plateau, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, joueur, meilleurCoup);
		return meilleurCoup;
	}

	public static int negamax(Plateau plateau, int profondeur, int alpha, int beta, Joueur joueur, Coup meilleurCoup) throws ExPasDeCoupPossible {
		if (profondeur == 0) {
			return evaluation(plateau, joueur);
		}
		n++;

		if (plateau.balleAuBut(joueur.getAdversaire().getCouleur())) {
			return -INFINI;
		}

		HashSet<Coup> listeCoups = joueur.coupsLegaux(plateau);
		if (listeCoups.isEmpty()) {
			throw new ExPasDeCoupPossible(joueur);
		}

		Joueur adversaire = joueur.getAdversaire();
		int meilleurScore = -INFINI;

		for (Coup coup : listeCoups) {
			coup.jouer(plateau);
			alpha = -negamax(plateau, profondeur - 1, -beta, -alpha, adversaire, null);
			coup.annuler(plateau);

			if (beta <= alpha) {
				return alpha;
			}
			if (alpha > meilleurScore) {
				meilleurScore = alpha;
				if (meilleurCoup != null) {
					meilleurCoup.copier(coup);
				}
			}
		}
		return meilleurScore;
	}

	private static int evaluation(Plateau plateau, Joueur joueur) {
		m++;
		int eval = 0;
		short couleur = joueur.getCouleur();
		if (plateau.balleAuBut(couleur)) {
			return INFINI;
		}
		for (Pion pion : plateau.getPions(couleur)) {
			eval += pion.getDistanceDepart();
		}
		eval += plateau.getBallon(couleur).getDistanceDepart() * 2;
		return eval;
	}
}
