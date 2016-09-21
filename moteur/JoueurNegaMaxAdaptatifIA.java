/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moteur;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import moteur.ia.EtatPartie;
import moteur.ia.NegaMaxAdaptatif;
import moteur.ia.exception.ExPasDeCoupPossible;

/**
 *
 * @author gervaila
 */
public class JoueurNegaMaxAdaptatifIA extends JoueurIA {

	public JoueurNegaMaxAdaptatifIA(short couleur) {
		super("Garry Kasparov", couleur);
	}

	@Override
	public Coup choisirCoup(Plateau plateau) {
		Coup meilleurCoup = new Coup();
		try {
			NegaMaxAdaptatif.n = 0;
			NegaMaxAdaptatif.m = 0;

			EtatPartie.Avancement avancement = EtatPartie.avancement(plateau, couleur);

			if (avancement == EtatPartie.Avancement.DEBUT) {
				meilleurCoup = NegaMaxAdaptatif.negamaxDebut(plateau, this);
			} else if (avancement == EtatPartie.Avancement.MILIEU) {
				meilleurCoup = NegaMaxAdaptatif.negamaxMilieu(plateau, this);
			} else {
				meilleurCoup = NegaMaxAdaptatif.negamaxFin(plateau, this);
			}


//          System.out.println("NegaMaxAlphaBetaAdaptatif.n = " + NegaMaxAlphaBetaAdaptatif.n + " NegaMaxAlphaBetaAdaptatif.m = " + NegaMaxAlphaBetaAdaptatif.m);
//			System.out.println(meilleurCoup);
//			System.out.println(avancement);
		} catch (ExPasDeCoupPossible ex) {
			Logger.getLogger(JoueurNegaMaxIA.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		return meilleurCoup;
	}
}
