package graphique;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;
import moteur.*;
import util.File;
/*
 *
 *
 *
 * Retour - Eric Ajouter un panneau pemettant d'afficher la signification des
 * couleurs Ajouter une deuxième couleur pour un deplacement de 2 cases Modifier
 * l'apparence des règles Au demarrage, ajouter un calque permettant d'afficher
 * les interactions possibles
 *
 * Ajouter les fonctionnalitees activable/desactivable
 *
 * Afficher le tirroir dès le debut (Le femrer au deuxieme tour? Incite
 * l'utilisateur à le reafficher)
 *
 *
 *
 *
 */

public class MyGlassPane extends JComponent {

	public AireDeJeu aireDeJeu;
	public int x0 = -1, y0 = -1;
	public int x, y;
	private int xcentre, ycentre;
	public int xprime, yprime;
	public boolean ballon;
	private Diabolik diabolik;
	private float lambda, norme;
	private int distance;
	public boolean enCoursDAnimation;
	private Animation animationCourante;
	public int dureeAnimation;
	public float zoom;
	public short couleurAnimation;
	private File<Animation> animations;
	private BufferedImage pionBlancIndice;
	private BufferedImage pionNoirIndice;
	private BufferedImage ballonBlancIndice;
	private BufferedImage ballonNoirIndice;
	int largeurCase;
	public boolean enCoursIndice;
	Coup coupIndice;
	private BufferedImage caseSelectionne;
	private BufferedImage passeSelectionne;
	private BufferedImage pionNoir;
	private BufferedImage pionBlanc;
	public boolean enCoursFinPartie;
	public Point[][] coord;
	AnimationFinPartie anim;
	public boolean annuler;

	// ne pas utiliser ce constructeur
	public MyGlassPane() {
		this(new AireDeJeu(), new Diabolik());
	}

	public MyGlassPane(AireDeJeu aire, Diabolik d) {
		aireDeJeu = aire;
		diabolik = d;
		animations = new File<Animation>();
		dureeAnimation = 100;
		zoom = 1;
		try {
			caseSelectionne = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			caseSelectionne = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/caseSelectionne.png"));
			passeSelectionne = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			passeSelectionne = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/passeSelection.png"));
		} catch (IOException ex) {
			Logger.getLogger(MyGlassPane.class.getName()).log(Level.SEVERE, null, ex);
		}
		enCoursIndice = false;
		enCoursFinPartie = false;
		coord = new Point[2][7];
		int largeurCase = aireDeJeu.getLargeurCase();
		{
			try {
				pionBlancIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
				pionBlancIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionBlancTransparent.png"));
				pionNoirIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
				pionNoirIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionNoirTransparent.png"));
				ballonBlancIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
				ballonBlancIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonBlancTransparent.png"));
				ballonNoirIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
				ballonNoirIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonNoirTransparent.png"));
				pionBlanc = aireDeJeu.getPionBlanc();
				pionNoir = aireDeJeu.getPionNoir();
			} catch (IOException ex) {
				Logger.getLogger(MyGlassPane.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		int largeurCase = aireDeJeu.getLargeurCase();
		Graphics2D drawable = (Graphics2D) g;
		if (enCoursFinPartie) {
			for (int i = 0; i < 7; i++) {
				drawable.drawImage(pionBlanc, coord[0][i].x, coord[0][i].y, null);
			}
			for (int i = 0; i < 7; i++) {
				drawable.drawImage(pionNoir, coord[1][i].x, coord[1][i].y, null);
			}
		}
		if (enCoursDAnimation) {
			BufferedImage image = null;
			if (ballon) {
				if (couleurAnimation == Pion.BLANC) {
					if (enCoursIndice) {
						image = ballonBlancIndice;
					} else {
						image = aireDeJeu.getBallonBlanc();
					}


				} else {
					if (enCoursIndice) {
						image = ballonNoirIndice;
					} else {
						image = aireDeJeu.getBallonNoir();
					}
				}
			} else {

				if (couleurAnimation == Pion.BLANC) {
					if (enCoursIndice) {
						image = pionBlancIndice;
					} else {
						image = aireDeJeu.getPionBlanc();
					}
				} else {
					if (enCoursIndice) {
						image = pionNoirIndice;
					} else {
						image = aireDeJeu.getPionNoir();
					}
				}
			}
			int a = (int) ((largeurCase * zoom - largeurCase) / 2);
			BufferedImage scaledImage = aireDeJeu.scale(image, (int) (zoom * largeurCase), (int) (zoom * largeurCase));
			drawable.drawImage(scaledImage, x - a, y - a, null);
		} else {
			Point coord = aireDeJeu.getCase(x0, y0);
			int xCase = x / largeurCase;
			int yCase = y / largeurCase;

			if (ballon) {
				Pion pion = diabolik.getPlateau().consulter(coord);
				xprime = x;
				yprime = y;

				if (diabolik.getFenetrePrincipale().isDeplacementPossible()) {
					int abs = 0;
					int ord = 0;
					ArrayList<Passe> passes = diabolik.getPlateau().passesPossibles(pion.getCouleur());
					for (Passe passe : passes) {
						abs = passe.getPion().getPosition().x * largeurCase;
						ord = passe.getPion().getPosition().y * largeurCase;
						drawable.drawImage(aireDeJeu.ballonTransparent, abs, ord, this);
					}
				}

				zoom = 1.15f;
				Point tmp = aireDeJeu.getCase(xprime, yprime);
				Pion tmpPion = null;
				int xSel = 0, ySel = 0;
				if (tmp != null) {
					tmpPion = diabolik.getPlateau().consulter(aireDeJeu.getCase(xprime, yprime));
				}

				if (tmp != null && diabolik.getPlateau().passePossible(tmpPion) && tmpPion.getCouleur() == diabolik.getJoueurCourant().getCouleur()) {
					xSel = tmp.x * largeurCase;
					ySel = tmp.y * largeurCase;
				} else {
					xSel = aireDeJeu.getCase(x0, y0).x * largeurCase;
					ySel = aireDeJeu.getCase(x0, y0).y * largeurCase;
				}
				drawable.drawImage(passeSelectionne, xSel, ySel, null);
				if (diabolik.getJoueurCourant().getCouleur() == Pion.BLANC) {
					drawable.drawImage(aireDeJeu.getPionBlanc(), xSel, ySel, null);
				} else {
					drawable.drawImage(aireDeJeu.getPionNoir(), xSel, ySel, null);
				}



				int a = (int) ((largeurCase * zoom - largeurCase) / 2);
				if (diabolik.getJoueurCourant().getCouleur() == Pion.BLANC) {
					BufferedImage ballonBlanc = aireDeJeu.scale(aireDeJeu.getBallonBlanc(), (int) (zoom * largeurCase), (int) (zoom * largeurCase));
					drawable.drawImage(ballonBlanc, x - largeurCase / 2 - a, y - largeurCase / 2 - a, null);
				} else {
					BufferedImage ballonNoir = aireDeJeu.scale(aireDeJeu.getBallonNoir(), (int) (zoom * largeurCase), (int) (zoom * largeurCase));
					drawable.drawImage(ballonNoir, x - largeurCase / 2 - a, y - largeurCase / 2 - a, null);
				}
			} else if (diabolik.getNbMouvementsRestants() != 0 && coord != null && diabolik.getPlateau().consulter(coord) != null && !diabolik.getPlateau().consulter(coord).possedeBallon()) {
				Pion pion = diabolik.getPlateau().consulter(coord);
				/*
				 * Test si le pion est sur un cote
				 */
				if ((coord.y == 6 && y / largeurCase > 6) || (coord.y == 0 && y / largeurCase < 0)
						|| (coord.x == 6 && x / largeurCase > 6) || (coord.x == 0 && x / largeurCase < 0)) {
					distance = largeurCase - largeurCase / 5;
				} else {
					/*
					 * Calcul de la distance
					 */
					switch (diabolik.getNbMouvementsRestants()) {
						case 0:
							distance = largeurCase - largeurCase / 5;
							break;
						case 1:
							if (xCase == coord.x || yCase == coord.y) {
								distance = (largeurCase - largeurCase / 5) * 3;
							} else {
								distance = largeurCase - largeurCase / 5;
							}
							break;
						case 2:
							if (xCase == coord.x || yCase == coord.y) {
								distance = (largeurCase - largeurCase / 5) * 5;
							} else {
								distance = (largeurCase - largeurCase / 5) * 11 / 3;
							}
							break;

					}
				}

				calculerCoordonnees();

				/*
				 *
				 * Diminuer la distance si il y a des pions ou si le pion sort
				 * du plateau
				 *
				 */
				pion.setPosition(coord);
				if ((aireDeJeu.getCase(xprime, yprime) != null && !aireDeJeu.getCase(xprime, yprime).equals(aireDeJeu.getCase(x0, y0)))
						|| (aireDeJeu.getCase(xprime, yprime) == null)) {
					if (aireDeJeu.getCase(xprime, yprime) == null) {
						if (diabolik.getNbMouvementsRestants() == 2) {
							if (xCase == coord.x || yCase == coord.y) {
								distance = (largeurCase - largeurCase / 5) * 3;
							} else {
								distance = (largeurCase - largeurCase / 5);
							}
							calculerCoordonnees();
							if (aireDeJeu.getCase(xprime, yprime) == null || (aireDeJeu.getCase(xprime, yprime) != null && !diabolik.getPlateau().deplacementPossible(pion, aireDeJeu.getCase(xprime, yprime)))) {
								distance = (largeurCase - largeurCase / 5);
								calculerCoordonnees();
							}
						} else {
							if (aireDeJeu.getCase(xprime, yprime) == null) {
								distance = (largeurCase - largeurCase / 5);
								calculerCoordonnees();
							}
						}

					} else {

						if (!diabolik.getPlateau().deplacementPossible(pion, aireDeJeu.getCase(xprime, yprime))) {
							if (diabolik.getNbMouvementsRestants() == 2) {
								if (!diabolik.getPlateau().deplacementPossible(pion, aireDeJeu.getCase(xprime, yprime))) {
									if (xCase == coord.x || yCase == coord.y) {
										distance = (largeurCase - largeurCase / 5) * 3;
									} else {
										distance = (largeurCase - largeurCase / 5);
									}
									calculerCoordonnees();
									if (!diabolik.getPlateau().deplacementPossible(pion, aireDeJeu.getCase(xprime, yprime))) {
										distance = (largeurCase - largeurCase / 5);
										calculerCoordonnees();
									}
								}
							} else {
								if (!diabolik.getPlateau().deplacementPossible(pion, aireDeJeu.getCase(xprime, yprime))) {
									distance = (largeurCase - largeurCase / 5);
									calculerCoordonnees();
								}
							}
						}
					}
				}

				/*
				 *
				 * Afficher Possibilité
				 *
				 */



				if (diabolik.getFenetrePrincipale().isDeplacementPossible() && pion != null && pion.getCouleur() == diabolik.getJoueurCourant().getCouleur()) {
					Point tmp = aireDeJeu.getCase(xprime, yprime);
					if (tmp != null) {
						drawable.drawImage(caseSelectionne, tmp.x * largeurCase, tmp.y * largeurCase, null);
					} else {
						drawable.drawImage(caseSelectionne, aireDeJeu.getCase(x0, y0).x * largeurCase, aireDeJeu.getCase(x0, y0).y * largeurCase, null);
					}

					int abs = 0;
					int ord = 0;
					pion.setPosition(coord);
					ArrayList<Deplacement> depls = diabolik.getPlateau().deplacementsPossiblesMax(pion, diabolik.getNbMouvementsRestants());
					for (Deplacement depl : depls) {
						abs = depl.getDestination().x * largeurCase;
						ord = depl.getDestination().y * largeurCase;
						if (tmp != null && !(tmp.x == depl.getDestination().x && tmp.y == depl.getDestination().y)) {
							if (depl.getNbDeplacements() == 2) {
								drawable.drawImage(aireDeJeu.pionTransparent2, abs + 5, ord + 5, null);
							} else {
								drawable.drawImage(aireDeJeu.pionTransparent, abs + 5, ord + 5, null);
							}
						}
					}
					pion.setPosition(null);
				} else if (pion != null && pion.getCouleur() == diabolik.getJoueurCourant().getCouleur()) {
					pion.setPosition(null);
				}
				/*
				 * Afficher pion
				 */
				if (pion != null) {
					zoom = 1.15f;
					int a = (int) ((largeurCase * zoom - largeurCase) / 2);


					if (diabolik.getJoueurCourant().getCouleur() == Pion.BLANC && pion.getCouleur() == Pion.BLANC) {
						BufferedImage pionBlanc = aireDeJeu.scale(aireDeJeu.getPionBlanc(), (int) (zoom * largeurCase), (int) (zoom * largeurCase));
						drawable.drawImage(pionBlanc, xprime - largeurCase / 2 - a, yprime - largeurCase / 2 - a, null);
					} else if (diabolik.getJoueurCourant().getCouleur() == Pion.NOIR && pion.getCouleur() == Pion.NOIR) {
						BufferedImage pionNoir = aireDeJeu.scale(aireDeJeu.getPionNoir(), (int) (zoom * largeurCase), (int) (zoom * largeurCase));
						drawable.drawImage(pionNoir, xprime - largeurCase / 2 - a, yprime - largeurCase / 2 - a, null);
					}
				}
			}
		}
	}

	public void dessiner(int x, int y) {
		this.x = x;
		this.y = y;
		if (x0 == -1) {
			int largeurCase = aireDeJeu.getLargeurCase();
			x0 = x;
			y0 = y;
			Point coord = aireDeJeu.getCase(x0, y0);
			if (coord != null) {
				xcentre = coord.x * largeurCase + (largeurCase / 2);
				ycentre = coord.y * largeurCase + (largeurCase / 2);
			}
		}
		repaint();
	}

	public void setAireDeJeu(AireDeJeu aireDeJeu1) {
		this.aireDeJeu = aireDeJeu1;
	}

	private void calculerCoordonnees() {
		norme = (int) Math.sqrt(Math.pow((x - x0), 2) + Math.pow((y - y0), 2));
		lambda = (float) (distance / 2) / (float) norme;
		xprime = (int) (xcentre + lambda * (x - xcentre));
		yprime = (int) (ycentre + lambda * (y - ycentre));
		if (norme < (distance / 2)) {
			xprime = x;
			yprime = y;
		}
	}

	public void animer() {
		if (!animations.isEmpty()) {
			if (!enCoursDAnimation) {
				animationCourante = animations.pop();
				//TODO a supprimer si probleme de ici
				if(animationCourante.action instanceof Deplacement){
					if(!enCoursIndice && ((Deplacement) animationCourante.action).getNbDeplacements() == 2){
						diabolik.getFenetrePrincipale().setImageMouvement(0);
					} else if(((Deplacement) animationCourante.action).getNbDeplacements() == 1){ 
						if(diabolik.getFenetrePrincipale().getDeplacementIcon1().isEnabled()){
							diabolik.getFenetrePrincipale().getDeplacementIcon2().setEnabled(false);
						} else {
							diabolik.getFenetrePrincipale().getDeplacementIcon1().setEnabled(false);
						}
					}
				} else if (!enCoursIndice && animationCourante.action instanceof Passe){
					diabolik.getFenetrePrincipale().setImagePasse(false);
				}
				// a la
				animationCourante.commencer();
			}
		} else {
			if (enCoursIndice) {
				enCoursIndice = false;
				for (Action action : coupIndice.getActions()) {
					action.getPion().setIndice(false);
				}
				coupIndice.annuler(diabolik.getPlateau());
			}
			boolean partieTerminee = diabolik.verifierGagne();
			if (!partieTerminee && ((diabolik.demiCoupTermine() && diabolik.isFinDeTourAutomatique()) || diabolik.joueurCourantIA())) {

				if (!(animationCourante instanceof AnimationAnnulerPasse) && !(animationCourante instanceof AnimationAnnulerDeplacement)) {
					diabolik.changementJoueur();
					if (diabolik.getNbMouvementsRestants() == 0 && diabolik.isPasseEffectuee()) {
						diabolik.getFenetrePrincipale().getSurbrillanceFinDuTour().setVisible(true);
					} else {
						diabolik.getFenetrePrincipale().getSurbrillanceFinDuTour().setVisible(false);
					}
				}
				if (diabolik.getModeDeJeu() == Diabolik.Reseau) {
					diabolik.prevenirCoupFini();
				} else if (diabolik.joueurCourantIA()) {
					Timer t = new Timer(0, new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							diabolik.jouerIA();
						}
					});
					t.setInitialDelay(1000);
					t.setRepeats(false);
					t.start();
				}
			}
		}
	}

	public void logobi(Coup coup, Plateau plateau) {
		for (Action action : coup.getActions()) {
			if (action instanceof Passe) {
				animations.add(new AnimationPasse(this, (Passe) action, plateau));
			} else {
				animations.add(new AnimationDeplacement(this, (Deplacement) action, plateau));
			}
		}
		animer();
	}

	public void logobi(Action action, Plateau plateau) {
		if (action instanceof Passe) {
			animations.add(new AnimationPasse(this, (Passe) action, plateau));
		} else {
			animations.add(new AnimationDeplacement(this, (Deplacement) action, plateau));
		}
		animer();
	}

	public void logobiAnnuler(Action action, Plateau plateau) {
		if (action instanceof Passe) {
			animations.add(new AnimationAnnulerPasse(this, (Passe) action, plateau));
		} else {
			animations.add(new AnimationAnnulerDeplacement(this, (Deplacement) action, plateau));
		}
		animer();
	}

	public void logobiIndice(Coup coup, Plateau plateau) {
		coupIndice = coup;
		for (Action action : coup.getActions()) {
			if (action instanceof Passe) {
				animations.add(new AnimationIndicePasse(this, (Passe) action, plateau));
			} else {
				animations.add(new AnimationIndiceDeplacement(this, (Deplacement) action, plateau));
			}
		}
		enCoursIndice = true;
		animer();
	}

	public void animFinPartie(Plateau plateau) {
		enCoursFinPartie = true;
		anim = new AnimationFinPartie(this, plateau) ;
		anim.commencer();
	}
	public void stopAnimFinPartie(boolean retablir) {
		anim.finir(retablir);
	}
	
	public void activerEcouteurs(boolean activer) {
		diabolik.getFenetrePrincipale().activerGlassPane(activer);
	}
}
