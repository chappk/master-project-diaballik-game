/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moteur.ia;

import moteur.*;

/**
 *
 * @author dodelien
 */
public class EtatPartie {

	public enum Avancement {

		DEBUT, MILIEU, FIN
	}

	public static Avancement avancement(Plateau plateau, short couleur) {
		int nbPionsLigneMilieu = 0;
		int nbPionsLigneFin = 0;
		for (Pion pion : plateau.getPions(couleur)) {
			int distanceDepart = pion.getDistanceDepart();
			if (distanceDepart >= 5) {
				nbPionsLigneFin++;
			}
			if (distanceDepart >= 3) {
				nbPionsLigneMilieu++;
			}
		}
		if (nbPionsLigneFin >= 1 && nbPionsLigneMilieu >= 2) {
			return Avancement.FIN;
		} else if (nbPionsLigneMilieu >= 2) {// || nbPionsLigneFin >=1) {
			return Avancement.MILIEU;
		} else {
			return Avancement.DEBUT;
		}
	}
}
