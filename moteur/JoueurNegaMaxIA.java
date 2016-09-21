/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moteur;

import java.util.logging.Level;
import java.util.logging.Logger;
import moteur.ia.NegaMaxAlphaBeta;
import moteur.ia.exception.ExPasDeCoupPossible;

/**
 *
 * @author dodelien
 */
public class JoueurNegaMaxIA extends JoueurIA {

	public JoueurNegaMaxIA(short couleur) {
		super("Les Frères Bogdanov", couleur);
	}

	@Override
	public Coup choisirCoup(Plateau plateau) {
		Coup meilleurCoup = new Coup();
		try {
			NegaMaxAlphaBeta.n = 0;
			NegaMaxAlphaBeta.m = 0;
			meilleurCoup = NegaMaxAlphaBeta.negamax(plateau, this);
//            System.out.println("NegaMaxAlphaBeta.n = " + NegaMaxAlphaBeta.n + " NegaMaxAlphaBeta.m = " + NegaMaxAlphaBeta.m);
//            System.out.println(EtatPartie.avancement(plateau, couleur));
		} catch (ExPasDeCoupPossible ex) {
			Logger.getLogger(JoueurNegaMaxIA.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return meilleurCoup;
	}
}
