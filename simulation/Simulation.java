/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulation;

import moteur.*;

/**
 *
 * @author nicolas
 */
public class Simulation {

	private static JoueurIA joueur1;
	private static JoueurIA joueur2;
	private static JoueurIA joueurCourant;
	private static boolean variante;
	private static Plateau plateau;

	public static void main(String[] args) {
		// aleatoire vs aleatoire
		// basique
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 100000);
		// variante
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 100000);

		// aleatoire vs facile
		// basique
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 100);
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 100);
		// variante
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 100);
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 100);

		// aleatoire vs moyen
		// basique
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), false, 100);
//		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 100);
		// variante
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), true, 100);
//		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 100);

		// aleatoire vs difficile
		// basique
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), false, 100);
//		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 100);
		// variante
//		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), true, 100);
//		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 100);
		
		// facile vs facile
		// basique
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 100);
		// variante
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 100);
		
		// facile vs moyen
		// basique
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), false, 100);
//		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 100);
		// variante
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), true, 100);
//		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 100);
		
		// facile vs moyen
		// basique
//		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), false, 100);
//		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 100);
		// variante
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), true, 100);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 100);
		
		// moyen vs moyen
		// basique
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), false, 1);
		// variante
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), true, 1);
		
		// moyen vs difficile
		// basique
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), false, 1);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), false, 1);
		// variante
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), true, 1);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), true, 1);
		
		// difficile vs difficile
		// basique
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), false, 1);
		// variante
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), true, 1);
		
		System.out.println("FIN DU TEST");
		System.out.println("FIN DU TEST");
		System.out.println("FIN DU TEST");
		System.out.println("FIN DU TEST");

		// aleatoire vs facile
		// basique
		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 1000);
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 1000);
		// variante
		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 1000);
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 1000);

		// aleatoire vs moyen
		// basique
		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), false, 1000);
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 1000);
		// variante
		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), true, 1000);
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 1000);

		// aleatoire vs difficile
		// basique
		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), false, 1000);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), false, 1000);
		// variante
		simulerParties(new JoueurAleatoireIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), true, 1000);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurAleatoireIA(Pion.NOIR), true, 1000);
		
		// facile vs facile
		// basique
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 1000);
		// variante
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 1000);
		
		// facile vs moyen
		// basique
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), false, 1000);
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 1000);
		// variante
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxIA(Pion.NOIR), true, 1000);
		simulerParties(new JoueurNegaMaxIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 1000);
		
		// facile vs moyen
		// basique
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), false, 1000);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), false, 1000);
		// variante
		simulerParties(new JoueurMonteCarloLasVegasIA(Pion.BLANC), new JoueurNegaMaxAdaptatifIA(Pion.NOIR), true, 1000);
		simulerParties(new JoueurNegaMaxAdaptatifIA(Pion.BLANC), new JoueurMonteCarloLasVegasIA(Pion.NOIR), true, 1000);
	}

	private static void simulerParties(JoueurIA joueur1, JoueurIA joueur2, boolean variante, int nbParties) {
		System.out.println("--- " + joueur1.getNom() + " vs " + joueur2.getNom() + " ---");
		int nbVictoiresBlanc = 0;
		int nbVictoiresNoir = 0;
		int nbMatchsNuls = 0;
		int n = nbParties / 10;
		if (n == 0)
			n = 1;
		for (int i = 0; i < nbParties; i++) {
//			if (i % n == 0) {
//				System.out.println((int)((float)i / nbParties * 100) + "%");
//			}
			short vainqueur = simulerPartie(joueur1, joueur2, variante);
			if (vainqueur == Pion.BLANC) {
				nbVictoiresBlanc++;
			} else if (vainqueur == Pion.NOIR) {
				nbVictoiresNoir++;
			} else {
				nbMatchsNuls++;
			}
		}
		System.out.println("=================================================");
		System.out.println(variante ? "Mode variante" : "Mode basique");
		System.out.println("Joueur blanc : " + joueur1.getNom());
		System.out.println("\tvictoires : " + nbVictoiresBlanc);
		System.out.println("Joueur noir  : " + joueur2.getNom());
		System.out.println("\tvictoires : " + nbVictoiresNoir);
		System.out.println("\t\tMatchs nuls (+1000 coups) : " + nbMatchsNuls);
		System.out.println("Victoires du blanc : " + ((float) nbVictoiresBlanc / (nbVictoiresBlanc + nbVictoiresNoir)) * 100);
		System.out.println("=================================================");
	}

	private static short simulerPartie(JoueurIA joueur1, JoueurIA joueur2, boolean variante) {
		Simulation.joueur1 = joueur1;
		Simulation.joueur2 = joueur2;
		Simulation.variante = variante;
		initialiserPartie();
		return simulation();
	}

	private static void initialiserPartie() {
		plateau = new Plateau(!variante);
		joueurCourant = joueur1;
		joueur1.setAdversaire(joueur2);
		joueur2.setAdversaire(joueur1);
	}

	private static short simulation() {
		JoueurIA joueurGagnant = null;
		int nbCoups = 0;
		do {
			Coup coup = joueurCourant.choisirCoup(plateau);
			coup.jouer(plateau);
			joueurGagnant = getJoueurGagnant();
			joueurCourant = (JoueurIA) joueurCourant.getAdversaire();
			nbCoups++;
		} while (nbCoups < 1000 && joueurGagnant == null);

		if (joueurGagnant != null) {
			return joueurGagnant.getCouleur();
		} else {
			return -1;
		}
	}

	private static JoueurIA getJoueurGagnant() {
		JoueurIA joueurGagnant = null;
		if (balleAuBut() || antiJeu(joueurCourant.getAdversaire().getCouleur())) {
			joueurGagnant = joueurCourant;
		} else if (antiJeu(joueurCourant.getCouleur())) {
			joueurGagnant = (JoueurIA) joueurCourant.getAdversaire();
		}
		return joueurGagnant;
	}

	private static boolean balleAuBut() {
		return plateau.balleAuBut(joueurCourant.getCouleur());
	}

	private static boolean antiJeu(short couleur) {
		return plateau.antiJeu(couleur);
	}
}
