package moteur;

import java.util.ArrayList;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author vernagaa
 */
abstract public class Joueur implements Serializable {

	protected short couleur;
	protected static Random rand = new Random();
	protected Joueur adversaire;
	protected String nom;

	public Joueur(short couleur) {
		this.couleur = couleur;
	}

	public Joueur(String nom, short couleur) {
		this.couleur = couleur;
		this.nom = nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public short getCouleur() {
		return couleur;
	}

	public void setCouleur(short couleur) {
		this.couleur = couleur;
	}

	public void setAdversaire(Joueur adversaire) {
		this.adversaire = adversaire;
	}

	public Joueur getAdversaire() {
		return adversaire;
	}

	@Override
	public String toString() {
		if (couleur == Pion.BLANC) {
			return "Joueur blanc";
		} else {
			return "Joueur noir";
		}
	}

	public Coup coupAleatoire(Plateau plateau) {
		Coup coup = new Coup();
		int r = rand.nextInt(8);
		switch (r) {
			case 0: { // un deplacement
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
				if (dp.size() > 0) {
					Deplacement d = dp.get(rand.nextInt(dp.size()));
					d.jouer(plateau);
					coup.ajouterAction(d);
				}
			}
			break;
			case 1: { // une passe
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				if (pp.size() > 0) {
					Passe p = pp.get(rand.nextInt(pp.size()));
					p.jouer(plateau);
					coup.ajouterAction(p);
				}
			}
			break;
			case 2: { // un deplacement de 2 ou deux deplacements
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				Pion pion2 = pionAleatoireSansLeBallon(plateau, null);
				if (pion1 == pion2) {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 2);
					if (dp.size() > 0) {
						Deplacement d = dp.get(rand.nextInt(dp.size()));
						d.jouer(plateau);
						coup.ajouterAction(d);
					}
				} else {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
					if (dp.size() > 0) {
						Deplacement d1 = dp.get(rand.nextInt(dp.size()));
						d1.jouer(plateau);
						coup.ajouterAction(d1);
					}
					ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
					if (dp2.size() > 0) {
						Deplacement d2 = dp2.get(rand.nextInt(dp2.size()));
						d2.jouer(plateau);
						coup.ajouterAction(d2);
					}
				}
			}
			break;
			case 3: { // deplacemennt 1 + passe
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
				if (dp.size() > 0) {
					Deplacement d = dp.get(rand.nextInt(dp.size()));
					d.jouer(plateau);
					coup.ajouterAction(d);
				}
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				if (pp.size() > 0) {
					Passe p = pp.get(rand.nextInt(pp.size()));
					p.jouer(plateau);
					coup.ajouterAction(p);
				}
			}
			break;
			case 4: { // passe + deplacement 1
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				if (pp.size() > 0) {
					Passe p = pp.get(rand.nextInt(pp.size()));
					p.jouer(plateau);
					coup.ajouterAction(p);
				}
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
				if (dp.size() > 0) {
					Deplacement d = dp.get(rand.nextInt(dp.size()));
					d.jouer(plateau);
					coup.ajouterAction(d);
				}
			}
			break;
			case 5: { // passe + deplacement 1 ou 2
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				if (pp.size() > 0) {
					Passe p = pp.get(rand.nextInt(pp.size()));
					p.jouer(plateau);
					coup.ajouterAction(p);
				}
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				Pion pion2 = pionAleatoireSansLeBallon(plateau, null);
				if (pion1 == pion2) {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 2);
					if (dp.size() > 0) {
						Deplacement d = dp.get(rand.nextInt(dp.size()));
						d.jouer(plateau);
						coup.ajouterAction(d);
					}
				} else {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
					if (dp.size() > 0) {
						Deplacement d1 = dp.get(rand.nextInt(dp.size()));
						d1.jouer(plateau);
						coup.ajouterAction(d1);
					}
					ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
					if (dp2.size() > 0) {
						Deplacement d2 = dp2.get(rand.nextInt(dp2.size()));
						d2.jouer(plateau);
						coup.ajouterAction(d2);
					}
				}
			}
			break;
			case 6: { // deplacement + passe + deplacement
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
				if (dp.size() > 0) {
					Deplacement d1 = dp.get(rand.nextInt(dp.size()));
					d1.jouer(plateau);
					coup.ajouterAction(d1);
				}
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				if (pp.size() > 0) {
					Passe p = pp.get(rand.nextInt(pp.size()));
					p.jouer(plateau);
					coup.ajouterAction(p);
				}
				Pion pion2 = pionAleatoireSansLeBallon(plateau, pion1);
				ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
				if (dp2.size() > 0) {
					Deplacement d2 = dp2.get(rand.nextInt(dp2.size()));
					d2.jouer(plateau);
					coup.ajouterAction(d2);
				}
			}
			break;
			case 7: { // deplacement 2 ou deux deplacement 1 + passe
				Pion pion1 = pionAleatoireSansLeBallon(plateau, null);
				Pion pion2 = pionAleatoireSansLeBallon(plateau, null);
				if (pion1 == pion2) {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 2);
					if (dp.size() > 0) {
						Deplacement d = dp.get(rand.nextInt(dp.size()));
						d.jouer(plateau);
						coup.ajouterAction(d);
					}
				} else {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 1);
					if (dp.size() > 0) {
						Deplacement d1 = dp.get(rand.nextInt(dp.size()));
						d1.jouer(plateau);
						coup.ajouterAction(d1);
					}
					ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
					if (dp2.size() > 0) {
						Deplacement d2 = dp2.get(rand.nextInt(dp2.size()));
						d2.jouer(plateau);
						coup.ajouterAction(d2);
					}
				}
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				if (pp.size() > 0) {
					Passe p = pp.get(rand.nextInt(pp.size()));
					p.jouer(plateau);
					coup.ajouterAction(p);
				}
			}
			break;
		}
		return coup;
	}

	private Pion pionAleatoireSansLeBallon(Plateau plateau, Pion ancienPion) {
		Pion[] pions = plateau.getPions(couleur);
		Pion pion;
		do {
			pion = pions[rand.nextInt(pions.length)];
		} while (pion.possedeBallon() || pion == ancienPion);
		return pion;
	}

	public HashSet<Coup> coupsLegaux(Plateau plateau) {
		HashSet<Coup> coups = new HashSet<Coup>(300);

		// deplacement de 1 pion de 1 case
		{
			for (Pion pion : plateau.getPions(couleur)) {
				if (!pion.possedeBallon()) {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
					for (Deplacement deplacement : dp) {
						deplacement.jouer(plateau);
						Coup coup = new Coup();
						coup.setPositionPions(plateau.getPions(couleur));
						coup.ajouterAction(deplacement);
						coups.add(coup);
						deplacement.annuler(plateau);
					}
				}
			}
		}

		// 1 passe
		{
			ArrayList<Passe> pp = plateau.passesPossibles(couleur);
			for (Passe passe : pp) {
				passe.jouer(plateau);
				Coup coup = new Coup();
				coup.setPositionPions(plateau.getPions(couleur));
				coup.ajouterAction(passe);
				coups.add(coup);
				passe.annuler(plateau);
			}
		}

		// 1 deplacement + 1 passe
		{
			for (Pion pion : plateau.getPions(couleur)) {
				ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
				for (Deplacement deplacement : dp) {
					deplacement.jouer(plateau);
					ArrayList<Passe> pp = plateau.passesPossibles(couleur);
					for (Passe passe : pp) {
						passe.jouer(plateau);
						Coup coup = new Coup();
						coup.setPositionPions(plateau.getPions(couleur));
						coup.ajouterAction(deplacement);
						coup.ajouterAction(passe);
						coups.add(coup);
						passe.annuler(plateau);
					}
					deplacement.annuler(plateau);
				}
			}
		}

		// 1 passe + 1 deplacement
		{
			ArrayList<Passe> pp = plateau.passesPossibles(couleur);
			for (Passe passe : pp) {
				passe.jouer(plateau);
				for (Pion pion : plateau.getPions(couleur)) {
					if (!pion.possedeBallon()) {
						ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
						for (Deplacement deplacement : dp) {
							deplacement.jouer(plateau);
							Coup coup = new Coup();
							coup.setPositionPions(plateau.getPions(couleur));
							coup.ajouterAction(passe);
							coup.ajouterAction(deplacement);
							coups.add(coup);
							deplacement.annuler(plateau);
						}
					}
				}
				passe.annuler(plateau);
			}
		}

		// 2 deplacements
		{
			for (Pion pion : plateau.getPions(couleur)) {
				for (Pion pion2 : plateau.getPions(couleur)) {
					if (pion == pion2) {
						ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 2);
						for (Deplacement deplacement : dp) {
							deplacement.jouer(plateau);
							Coup coup = new Coup();
							coup.setPositionPions(plateau.getPions(couleur));
							coup.ajouterAction(deplacement);
							coups.add(coup);
							deplacement.annuler(plateau);
						}
					} else {
						ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
						for (Deplacement deplacement : dp) {
							deplacement.jouer(plateau);
							ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
							for (Deplacement deplacement2 : dp2) {
								deplacement2.jouer(plateau);
								Coup coup = new Coup();
								coup.setPositionPions(plateau.getPions(couleur));
								coup.ajouterAction(deplacement);
								coup.ajouterAction(deplacement2);
								coups.add(coup);
								deplacement2.annuler(plateau);
							}
							deplacement.annuler(plateau);
						}
					}
				}
			}
		}

		// 1 passe + 2 deplacements
		{
			ArrayList<Passe> pp = plateau.passesPossibles(couleur);
			for (Passe passe : pp) {
				passe.jouer(plateau);
				for (Pion pion1 : plateau.getPions(couleur)) {
					for (Pion pion2 : plateau.getPions(couleur)) {
						if (pion1 == pion2) {
							ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 2);
							for (Deplacement deplacement : dp) {
								deplacement.jouer(plateau);
								Coup coup = new Coup();
								coup.setPositionPions(plateau.getPions(couleur));
								coup.ajouterAction(passe);
								coup.ajouterAction(deplacement);
								coups.add(coup);
								deplacement.annuler(plateau);
							}
						} else {
							ArrayList<Deplacement> dp1 = plateau.deplacementsPossibles(pion1, 1);
							for (Deplacement deplacement1 : dp1) {
								deplacement1.jouer(plateau);
								ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
								for (Deplacement deplacement2 : dp2) {
									deplacement2.jouer(plateau);
									Coup coup = new Coup();
									coup.setPositionPions(plateau.getPions(couleur));
									coup.ajouterAction(passe);
									coup.ajouterAction(deplacement1);
									coup.ajouterAction(deplacement2);
									coups.add(coup);
									deplacement2.annuler(plateau);
								}
								deplacement1.annuler(plateau);
							}
						}
					}
				}
				passe.annuler(plateau);
			}
		}

		// 2 deplacements + 1 passe
		{
			for (Pion pion : plateau.getPions(couleur)) {
				for (Pion pion2 : plateau.getPions(couleur)) {
					if (pion == pion2) {
						ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 2);
						for (Deplacement deplacement : dp) {
							deplacement.jouer(plateau);
							ArrayList<Passe> pp = plateau.passesPossibles(couleur);
							for (Passe passe : pp) {
								passe.jouer(plateau);
								Coup coup = new Coup();
								coup.setPositionPions(plateau.getPions(couleur));
								coup.ajouterAction(deplacement);
								coup.ajouterAction(passe);
								coups.add(coup);
								passe.annuler(plateau);
							}
							deplacement.annuler(plateau);
						}
					} else {
						ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
						for (Deplacement deplacement : dp) {
							deplacement.jouer(plateau);
							ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
							for (Deplacement deplacement2 : dp2) {
								deplacement2.jouer(plateau);
								ArrayList<Passe> pp = plateau.passesPossibles(couleur);
								for (Passe passe : pp) {
									passe.jouer(plateau);
									Coup coup = new Coup();
									coup.setPositionPions(plateau.getPions(couleur));
									coup.ajouterAction(deplacement);
									coup.ajouterAction(deplacement2);
									coup.ajouterAction(passe);
									coups.add(coup);
									passe.annuler(plateau);
								}
								deplacement2.annuler(plateau);
							}
							deplacement.annuler(plateau);
						}
					}
				}
			}
		}

		// 1 deplacement + 1 passe + 1 deplacement
		{
			for (Pion pion : plateau.getPions(couleur)) {
				ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
				for (Deplacement deplacement : dp) {
					deplacement.jouer(plateau);
					ArrayList<Passe> pp = plateau.passesPossibles(couleur);
					for (Passe passe : pp) {
						passe.jouer(plateau);
						for (Pion pion2 : plateau.getPions(couleur)) {
							if (pion2 != pion) {
								ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
								for (Deplacement deplacement2 : dp2) {
									deplacement2.jouer(plateau);
									Coup coup = new Coup();
									coup.setPositionPions(plateau.getPions(couleur));
									coup.ajouterAction(deplacement);
									coup.ajouterAction(passe);
									coup.ajouterAction(deplacement2);
									coups.add(coup);
									deplacement2.annuler(plateau);
								}
							}
						}
						passe.annuler(plateau);
					}
					deplacement.annuler(plateau);
				}
			}
		}

		return coups;
	}

	public HashSet<Coup> coupsLegauxIndice(Plateau plateau, int nbMouvementsRestants, boolean passeEffectuee) {
		HashSet<Coup> coups = new HashSet<Coup>(300);

		// deplacement de 1 pion de 1 case
		{
			if (nbMouvementsRestants > 0) {
				for (Pion pion : plateau.getPions(couleur)) {
					if (!pion.possedeBallon()) {
						ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
						for (Deplacement deplacement : dp) {
							deplacement.jouer(plateau);
							Coup coup = new Coup();
							coup.setPositionPions(plateau.getPions(couleur));
							coup.ajouterAction(deplacement);
							coups.add(coup);
							deplacement.annuler(plateau);
						}
					}
				}
			}
		}

		// 1 passe
		{
			if (!passeEffectuee) {
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				for (Passe passe : pp) {
					passe.jouer(plateau);
					Coup coup = new Coup();
					coup.setPositionPions(plateau.getPions(couleur));
					coup.ajouterAction(passe);
					coups.add(coup);
					passe.annuler(plateau);
				}
			}
		}

		// 1 deplacement + 1 passe
		{
			if (nbMouvementsRestants > 0 && !passeEffectuee) {
				for (Pion pion : plateau.getPions(couleur)) {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
					for (Deplacement deplacement : dp) {
						deplacement.jouer(plateau);
						ArrayList<Passe> pp = plateau.passesPossibles(couleur);
						for (Passe passe : pp) {
							passe.jouer(plateau);
							Coup coup = new Coup();
							coup.setPositionPions(plateau.getPions(couleur));
							coup.ajouterAction(deplacement);
							coup.ajouterAction(passe);
							coups.add(coup);
							passe.annuler(plateau);
						}
						deplacement.annuler(plateau);
					}
				}
			}
		}

		// 1 passe + 1 deplacement
		{
			if (nbMouvementsRestants > 0 && !passeEffectuee) {
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				for (Passe passe : pp) {
					passe.jouer(plateau);
					for (Pion pion : plateau.getPions(couleur)) {
						if (!pion.possedeBallon()) {
							ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
							for (Deplacement deplacement : dp) {
								deplacement.jouer(plateau);
								Coup coup = new Coup();
								coup.setPositionPions(plateau.getPions(couleur));
								coup.ajouterAction(passe);
								coup.ajouterAction(deplacement);
								coups.add(coup);
								deplacement.annuler(plateau);
							}
						}
					}
					passe.annuler(plateau);
				}
			}
		}

		// 2 deplacements
		{
			if (nbMouvementsRestants > 1) {
				for (Pion pion : plateau.getPions(couleur)) {
					for (Pion pion2 : plateau.getPions(couleur)) {
						if (pion == pion2) {
							ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 2);
							for (Deplacement deplacement : dp) {
								deplacement.jouer(plateau);
								Coup coup = new Coup();
								coup.setPositionPions(plateau.getPions(couleur));
								coup.ajouterAction(deplacement);
								coups.add(coup);
								deplacement.annuler(plateau);
							}
						} else {
							ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
							for (Deplacement deplacement : dp) {
								deplacement.jouer(plateau);
								ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
								for (Deplacement deplacement2 : dp2) {
									deplacement2.jouer(plateau);
									Coup coup = new Coup();
									coup.setPositionPions(plateau.getPions(couleur));
									coup.ajouterAction(deplacement);
									coup.ajouterAction(deplacement2);
									coups.add(coup);
									deplacement2.annuler(plateau);
								}
								deplacement.annuler(plateau);
							}
						}
					}
				}
			}
		}

		// 1 passe + 2 deplacements
		{
			if (nbMouvementsRestants > 1 && !passeEffectuee) {
				ArrayList<Passe> pp = plateau.passesPossibles(couleur);
				for (Passe passe : pp) {
					passe.jouer(plateau);
					for (Pion pion1 : plateau.getPions(couleur)) {
						for (Pion pion2 : plateau.getPions(couleur)) {
							if (pion1 == pion2) {
								ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion1, 2);
								for (Deplacement deplacement : dp) {
									deplacement.jouer(plateau);
									Coup coup = new Coup();
									coup.setPositionPions(plateau.getPions(couleur));
									coup.ajouterAction(passe);
									coup.ajouterAction(deplacement);
									coups.add(coup);
									deplacement.annuler(plateau);
								}
							} else {
								ArrayList<Deplacement> dp1 = plateau.deplacementsPossibles(pion1, 1);
								for (Deplacement deplacement1 : dp1) {
									deplacement1.jouer(plateau);
									ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
									for (Deplacement deplacement2 : dp2) {
										deplacement2.jouer(plateau);
										Coup coup = new Coup();
										coup.setPositionPions(plateau.getPions(couleur));
										coup.ajouterAction(passe);
										coup.ajouterAction(deplacement1);
										coup.ajouterAction(deplacement2);
										coups.add(coup);
										deplacement2.annuler(plateau);
									}
									deplacement1.annuler(plateau);
								}
							}
						}
					}
					passe.annuler(plateau);
				}
			}
		}

		// 2 deplacements + 1 passe
		{
			if (nbMouvementsRestants > 1 && !passeEffectuee) {
				for (Pion pion : plateau.getPions(couleur)) {
					for (Pion pion2 : plateau.getPions(couleur)) {
						if (pion == pion2) {
							ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 2);
							for (Deplacement deplacement : dp) {
								deplacement.jouer(plateau);
								ArrayList<Passe> pp = plateau.passesPossibles(couleur);
								for (Passe passe : pp) {
									passe.jouer(plateau);
									Coup coup = new Coup();
									coup.setPositionPions(plateau.getPions(couleur));
									coup.ajouterAction(deplacement);
									coup.ajouterAction(passe);
									coups.add(coup);
									passe.annuler(plateau);
								}
								deplacement.annuler(plateau);
							}
						} else {
							ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
							for (Deplacement deplacement : dp) {
								deplacement.jouer(plateau);
								ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
								for (Deplacement deplacement2 : dp2) {
									deplacement2.jouer(plateau);
									ArrayList<Passe> pp = plateau.passesPossibles(couleur);
									for (Passe passe : pp) {
										passe.jouer(plateau);
										Coup coup = new Coup();
										coup.setPositionPions(plateau.getPions(couleur));
										coup.ajouterAction(deplacement);
										coup.ajouterAction(deplacement2);
										coup.ajouterAction(passe);
										coups.add(coup);
										passe.annuler(plateau);
									}
									deplacement2.annuler(plateau);
								}
								deplacement.annuler(plateau);
							}
						}
					}
				}
			}
		}

		// 1 deplacement + 1 passe + 1 deplacement
		{
			if (nbMouvementsRestants > 1 && !passeEffectuee) {
				for (Pion pion : plateau.getPions(couleur)) {
					ArrayList<Deplacement> dp = plateau.deplacementsPossibles(pion, 1);
					for (Deplacement deplacement : dp) {
						deplacement.jouer(plateau);
						ArrayList<Passe> pp = plateau.passesPossibles(couleur);
						for (Passe passe : pp) {
							passe.jouer(plateau);
							for (Pion pion2 : plateau.getPions(couleur)) {
								if (pion2 != pion) {
									ArrayList<Deplacement> dp2 = plateau.deplacementsPossibles(pion2, 1);
									for (Deplacement deplacement2 : dp2) {
										deplacement2.jouer(plateau);
										Coup coup = new Coup();
										coup.setPositionPions(plateau.getPions(couleur));
										coup.ajouterAction(deplacement);
										coup.ajouterAction(passe);
										coup.ajouterAction(deplacement2);
										coups.add(coup);
										deplacement2.annuler(plateau);
									}
								}
							}
							passe.annuler(plateau);
						}
						deplacement.annuler(plateau);
					}
				}
			}
		}

		return coups;
	}
}
