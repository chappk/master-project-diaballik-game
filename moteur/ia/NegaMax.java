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
public class NegaMax { // NON UTILISE

    public static int n;
    public static int m;

    private static int negamax(Plateau plateau, int profondeur, Joueur joueur, Coup meilleurCoup) throws ExPasDeCoupPossible {
        if (profondeur == 0) {
            return evaluation(plateau, joueur);
        }
        n++;

        HashSet<Coup> listeCoups = joueur.coupsLegaux(plateau);
        if (listeCoups.isEmpty()) {
            throw new ExPasDeCoupPossible(joueur);
        }

        Joueur adversaire = joueur.getAdversaire();
        int meilleurScore = Integer.MIN_VALUE;

        for (Coup coup : listeCoups) {
            coup.jouer(plateau);
            int eval = -negamax(plateau, profondeur - 1, adversaire, null);
            coup.annuler(plateau);

            if (eval > meilleurScore) {
                meilleurScore = eval;
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
        for (Pion pion : plateau.getPions(couleur)) {
            eval += pion.getDistanceDepart() * pion.getDistanceDepart();
        }
        eval += plateau.getBallon(couleur).getDistanceDepart() * plateau.getBallon(couleur).getDistanceDepart();
        return eval;
    }
}
