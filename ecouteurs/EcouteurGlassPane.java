package ecouteurs;

import graphique.MyGlassPane;
import graphique.AireDeJeu;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.Timer;
import moteur.*;

public class EcouteurGlassPane implements MouseListener, MouseMotionListener {

	private Diabolik diabolik;
	private Point pointPrec, pointSuiv;
	private Pion pion;
	private boolean drag = false;

	public EcouteurGlassPane(Diabolik diabolik) {
		this.diabolik = diabolik;
		pointPrec = null;
		pointSuiv = null;
	}

	// null exception lors de la dÃ©sÃ©lection
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!drag) {
			MyGlassPane myGlassPane = (MyGlassPane) e.getComponent();
			AireDeJeu aire = myGlassPane.aireDeJeu;
			Point coordonnees = aire.getCase(e.getX(), e.getY());

			// cas ou on change de joueur et qu'un pion de l'ancienne couleur etait selectionne
			if (pion != null && pion.getCouleur() != diabolik.getJoueurCourant().getCouleur()) {
				pion = null;
			}
			// si on clique sur une case (dans la grille)
			if (coordonnees != null) {
				Pion pionClique = diabolik.getPlateau().consulter(coordonnees);
				// si on clique sur le pion dejaÂ selectionne
				if (pion == pionClique) {
					pion = null;
				} else if (pionClique != null && pionClique.getCouleur() == diabolik.getJoueurCourant().getCouleur() && (pion == null || !pion.possedeBallon())) {
					// si on clique sur un pion et pionClique de la bonne couleur et que l'ancien pion selectionne est soit nul ou soit ne possedait pas le ballon
					// si le pion cliquÃ© n'a pas la balle ou alors on n'a pas encore fait de passe
					if (!pionClique.possedeBallon() || !diabolik.passeEffectuee()) {
						pion = pionClique;
					}
					if (!pionClique.possedeBallon() && diabolik.getNbMouvementsRestants() == 0) {
						pion = null;
					}
				} else if (pion != null) {
					//sinon si l'ancien pion sÃ©lectionnÃ© n'est pas nul
					// si il possÃ¨de le ballon
					if (pion.possedeBallon()) {
						// si on n'a pas fait de passe et qu'on peut la faire au pion cliquÃ© et que ce dernier est de la couleur courante

						if (!diabolik.passeEffectuee() && diabolik.getPlateau().passePossible(pionClique) && diabolik.getJoueurCourant().getCouleur() == pionClique.getCouleur()) {
							Passe passe = new Passe(pionClique, diabolik.getPlateau().getBallon(diabolik.getJoueurCourant().getCouleur()));
							diabolik.alimenterAnnuler(passe);
							myGlassPane.logobi(passe, diabolik.getPlateau());
							diabolik.setPasseEffectuee(true);
							pion = null;
							aire.setPioncourant(null);
							return;

						} // sinon si le pion cliquÃ© n'est pas nul et qu'il est de la couleur courante
						else if (pionClique != null && diabolik.getJoueurCourant().getCouleur() == pionClique.getCouleur()) {
							pion = pionClique;
						}
						if (pionClique == null) {
							pion = null;
						}
					} // si l'ancien pion ne possÃ¨de pas le ballon
					else {
						int distanceDeplacement = Math.abs(pion.getPosition().x - coordonnees.x) + Math.abs(pion.getPosition().y - coordonnees.y);
						if (distanceDeplacement <= diabolik.getNbMouvementsRestants() && diabolik.getPlateau().deplacementPossible(pion, coordonnees)) {
							//BOUM Direction d = Direction.getDirection(pion1.getPosition(), new Point(c));
							ArrayList<Direction> directions = diabolik.getPlateau().chemin(pion, coordonnees);
							Deplacement depl = new Deplacement(pion);
							for (Direction d : directions) {
								depl.ajouterDirection(d);
							}
							diabolik.alimenterAnnuler(depl);
							myGlassPane.logobi(depl, diabolik.getPlateau());
							diabolik.setNbMouvementsRestants(diabolik.getNbMouvementsRestants() - distanceDeplacement);
							pion = null;
							aire.setPioncourant(null);
							return;

						}
						if (pionClique == null) {
							pion = null;
						}
					}
				} else {
					pion = null;
				}
			}

			aire.setPioncourant(pion);

			aire.repaint();

			diabolik.activerFinDeTour();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
//		glassPane.dessiner(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (drag) {
			MyGlassPane glassPane = (MyGlassPane) e.getComponent();

			glassPane.dessiner(e.getX(), e.getY());

			AireDeJeu aire = glassPane.aireDeJeu;
			aire.setPioncourant(null);
			pion = null;
			pointSuiv = aire.getCase(glassPane.xprime, glassPane.yprime);
			//On a essaye de faire une passe
			if (glassPane.ballon) {
				//On verifie que la case selectionne contient un pion
				if (pointSuiv != null
						//On verifie que pointSuiv n'est pas le pion qui possedait la balle
						&& !(pointSuiv.x == pointPrec.x && pointSuiv.y == pointPrec.y)
						//On verifie que la passe est possbile
						&& diabolik.getPlateau().passePossible(diabolik.getPlateau().consulter(pointSuiv))
						//On verifie que la passe n'a pas Ã©tÃ© effectuÃ©e
						&& !diabolik.passeEffectuee()
						//On verifie que le pion selectionne est de la bonne couleur
						&& diabolik.getJoueurCourant().getCouleur() == diabolik.getPlateau().consulter(pointSuiv).getCouleur()) {
					Passe passe = new Passe(diabolik.getPlateau().consulter(pointSuiv), diabolik.getPlateau().getBallon(diabolik.getJoueurCourant().getCouleur()));
					diabolik.alimenterAnnuler(passe);
					diabolik.getPlateau().faireLaPasse(diabolik.getPlateau().consulter(pointSuiv));
					diabolik.setPasseEffectuee(true);

					//Il faut tester si cette passe permet de gagner
					aire.repaint();
					// on passe le joueur courant en paramètre car il est changé avant l'appel à l'évènement et donc faux
					// dans le cas où : fin de tour auto ET la passe est la 3ème action
					Timer t = new Timer(0, new ActionListenerImpl(diabolik.getJoueurCourant()));
					t.setRepeats(false);
					t.start();
				} else {
					//Sinon, la passe n'est pas valide, il faut rendre le ballon au porteur
					diabolik.getPlateau().consulter(pointPrec).setPossedeBallon(true);
				}
				//Sinon, c'est un deplacement. On verifie qu'on avait selectionne un pion et non une case vide
			} else if (pointPrec != null && diabolik.getPlateau().consulter(pointPrec) != null) {
				Pion pionPrecedent = diabolik.getPlateau().consulter(pointPrec);
				pionPrecedent.setPosition(pointPrec);
				if (pointSuiv != null && diabolik.getPlateau().consulter(pointSuiv) == null) {
					int nbMouvementsRestants = diabolik.getNbMouvementsRestants();
					int distanceDeplacement = Math.abs(pionPrecedent.getPosition().x - pointSuiv.x) + Math.abs(pionPrecedent.getPosition().y - pointSuiv.y);
					if (distanceDeplacement <= nbMouvementsRestants && diabolik.getPlateau().deplacementPossible(pionPrecedent, pointSuiv)) {
						ArrayList<Direction> directions = diabolik.getPlateau().chemin(pionPrecedent, pointSuiv);
						Deplacement depl = new Deplacement(pionPrecedent);
						for (Direction d : directions) {
							depl.ajouterDirection(d);
						}
						diabolik.alimenterAnnuler(depl);
						diabolik.getPlateau().deplacerPion(pionPrecedent.getPosition(), new Point(pointSuiv));
						diabolik.setNbMouvementsRestants(nbMouvementsRestants - distanceDeplacement);
						diabolik.verifierGagne();
					}
				}

			}

			glassPane.ballon = false;
			glassPane.x0 = -1;
			glassPane.y0 = -1;
			pointSuiv = null;
			pointPrec = null;
			drag = false;

			diabolik.activerFinDeTour();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!drag) {
			drag = true;
			pion = null;
			MyGlassPane glassPane = (MyGlassPane) e.getComponent();
			//Si on clique sur une case du plateau
			if (glassPane.aireDeJeu.getCase(e.getX(), e.getY()) != null) {
				AireDeJeu aire = glassPane.aireDeJeu;
				pointPrec = aire.getCase(e.getX(), e.getY());
				aire.setPioncourant(null);
				Pion pionActuel = diabolik.getPlateau().consulter(pointPrec);
				//Si on clique sur une case non vide
				if (pionActuel != null) {
					//On verifie que la passe n'a pas ete effectuee et que le clique porte sur le ballon
					if (pionActuel.possedeBallon() && !diabolik.passeEffectuee()) {
						//Si le pion est le pion du joueur courant
						if (pionActuel.getCouleur() == diabolik.getJoueurCourant().getCouleur()) {
							glassPane.ballon = true;
							pionActuel.setPossedeBallon(false);
							aire.repaint();
						} else {
							pointPrec = null;
						}
					} //Sinon, si le clique porte sur un pion sans le ballon
					else if (!pionActuel.possedeBallon()) {
						if (pionActuel.getCouleur() == diabolik.getJoueurCourant().getCouleur()) {
							if (diabolik.getNbMouvementsRestants() != 0) {
								((MyGlassPane) e.getComponent()).aireDeJeu.setPioncourant(null);
								pionActuel.setPosition(null);
							}
						} else {
							pointPrec = null;
						}
						aire.repaint();
					} else {
						pointPrec = null;
					}
				}
			}
		}
		((MyGlassPane) e.getComponent()).dessiner(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	public void setPion(Pion pion) {
		this.pion = pion;
	}

	class ActionListenerImpl implements ActionListener {
		private final Joueur jCourant;

		private ActionListenerImpl(Joueur joueurCourant) {
			jCourant = joueurCourant;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			diabolik.verifierGagne(jCourant);
		}
	}
}
