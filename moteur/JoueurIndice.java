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
import moteur.ia.NegaMaxIndice;
import moteur.ia.exception.ExPasDeCoupPossible;

/**
 *
 * @author gervaila
 */
public class JoueurIndice extends JoueurIA {

	int nbMouvementsRestants;
	boolean passeEffectuee;
	public JoueurIndice(short couleur, int nbMouvementsRestants, boolean passeEffectuee) {
		super("Garry Kasparov", couleur);
		this.nbMouvementsRestants = nbMouvementsRestants;
		this.passeEffectuee = passeEffectuee;
	}

	@Override
	public Coup choisirCoup(Plateau plateau) {
		Coup meilleurCoup = new Coup();
		try {
			NegaMaxIndice.n = 0;
			NegaMaxIndice.m = 0;

			EtatPartie.Avancement avancement = EtatPartie.avancement(plateau, couleur);

			if (avancement == EtatPartie.Avancement.DEBUT) {
				meilleurCoup = NegaMaxIndice.negamaxDebut(plateau, this,nbMouvementsRestants,passeEffectuee);
			} else if (avancement == EtatPartie.Avancement.MILIEU) {
				meilleurCoup = NegaMaxIndice.negamaxMilieu(plateau, this,nbMouvementsRestants,passeEffectuee);
			} else {
				meilleurCoup = NegaMaxIndice.negamaxFin(plateau, this,nbMouvementsRestants,passeEffectuee);
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
