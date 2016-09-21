package moteur;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Représentation du plateau de jeu dans un état donné
 *
 * @author vernagaa
 */
public class Plateau implements Serializable {

	public final static int TAILLE = 7;
	public Pion[][] cases;
	public Pion[][] pions;
	public Pion[] ballons;


	public Plateau () {
		this(true);
	}
	
	public Plateau (boolean basique) {
		cases = new Pion[TAILLE][TAILLE];
		pions = new Pion[2][TAILLE];
		ballons = new Pion[2];
		
			for (int x = 0; x < TAILLE; x++) {
				for (int y = 0; y < TAILLE; y++) {
					if (y == 0) {
						if(!basique && (x == 1 || x == 5)){
							cases[x][y] = new Pion(x, Pion.BLANC, new Point(x, y));
							pions[Pion.BLANC][x] = cases[x][y];
						} else {
							cases[x][y] = new Pion(x, Pion.NOIR, new Point(x, y));
							pions[Pion.NOIR][x] = cases[x][y];
						}
					}
					if (y == TAILLE - 1) {
						if(!basique && (x == 1 || x == 5)){
							cases[x][y] = new Pion(x, Pion.NOIR, new Point(x, y));
							pions[Pion.NOIR][x] = cases[x][y];
						} else {
							cases[x][y] = new Pion(x, Pion.BLANC, new Point(x, y));
							pions[Pion.BLANC][x] = cases[x][y];
						}
					}
				}
			}
			ballons[Pion.BLANC] = pions[Pion.BLANC][TAILLE / 2];
			ballons[Pion.NOIR] = pions[Pion.NOIR][TAILLE / 2];

			ballons[Pion.BLANC].setPossedeBallon(true);
			ballons[Pion.NOIR].setPossedeBallon(true);
	}
	
	public ArrayList<Deplacement> deplacementsPossiblesMax(Pion pion, int nbCoups) {
		ArrayList<Deplacement> dp = new ArrayList<Deplacement>();

		if (!pion.possedeBallon() && nbCoups > 0) {
			for (Direction direction1 : Direction.values()) {
				Point destination = new Point(pion.getPosition().x + direction1.getVecteur().x, pion.getPosition().y + direction1.getVecteur().y);

				if (contient(destination) && consulter(destination) == null) {
					Deplacement deplacement = new Deplacement(pion);
					deplacement.ajouterDirection(direction1);
					dp.add(deplacement);

					if (nbCoups == 2) {
						for (Direction direction2 : Direction.values()) {
							Point destination2 = new Point(destination.x + direction2.getVecteur().x, destination.y + direction2.getVecteur().y);
							if (direction2 != direction1.oppose() && contient(destination2) && consulter(destination2) == null) {
								Deplacement deplacement2 = new Deplacement(pion);
								deplacement2.ajouterDirection(direction1);
								deplacement2.ajouterDirection(direction2);
								if (!dp.contains(deplacement2)) {
									dp.add(deplacement2);
								}
							}
						}
					}
				}
			}
		}

		return dp;
	}

	public ArrayList<Deplacement> deplacementsPossibles(Pion pion, int nbCoups) {
		ArrayList<Deplacement> dp = new ArrayList<Deplacement>();

		if (!pion.possedeBallon() && nbCoups > 0) {
			for (Direction direction1 : Direction.values()) {
				if (direction1 != Direction.AUCUN) {
					Point destination = new Point(pion.getPosition().x + direction1.getVecteur().x, pion.getPosition().y + direction1.getVecteur().y);

					if (contient(destination) && consulter(destination) == null) {
						Deplacement deplacement = new Deplacement(pion);
						deplacement.ajouterDirection(direction1);

						if (nbCoups == 1) {
							dp.add(deplacement);
						} else if (nbCoups == 2) {
							for (Direction direction2 : Direction.values()) {
								if (direction2 != Direction.AUCUN) {
									Point destination2 = new Point(destination.x + direction2.getVecteur().x, destination.y + direction2.getVecteur().y);
									if (direction2 != direction1.oppose() && contient(destination2) && consulter(destination2) == null) {
										Deplacement deplacement2 = new Deplacement(pion);
										deplacement2.ajouterDirection(direction1);
										deplacement2.ajouterDirection(direction2);
										if (!dp.contains(deplacement2)) {
											dp.add(deplacement2);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return dp;
	}

	public ArrayList<Deplacement> deplacementsPossiblesAvant(Pion pion, int nbCoups) {
		ArrayList<Deplacement> dp = new ArrayList<Deplacement>();

		if (!pion.possedeBallon() && nbCoups > 0) {

			Direction direction1;
			if (pion.getCouleur() == Pion.NOIR) {
				direction1 = Direction.SUD;
			} else {
				direction1 = Direction.NORD;
			}

			Point destination = new Point(pion.getPosition().x + direction1.getVecteur().x, pion.getPosition().y + direction1.getVecteur().y);

			if (contient(destination) && consulter(destination) == null) {
				Deplacement deplacement = new Deplacement(pion);
				deplacement.ajouterDirection(direction1);

				if (nbCoups == 1) {
					dp.add(deplacement);
				} else if (nbCoups == 2) {
					for (Direction direction2 : Direction.values()) {
						if (direction2 != Direction.AUCUN) {
							Point destination2 = new Point(destination.x + direction2.getVecteur().x, destination.y + direction2.getVecteur().y);
							if (direction2 != direction1.oppose() && contient(destination2) && consulter(destination2) == null) {
								Deplacement deplacement2 = new Deplacement(pion);
								deplacement2.ajouterDirection(direction1);
								deplacement2.ajouterDirection(direction2);
								dp.add(deplacement2);
							}
						}
					}
				}

			}
		}

		return dp;
	}

	public boolean deplacementPossible(Pion pion, Point destination) {
		int px = pion.getPosition().x;
		int py = pion.getPosition().y;
		int dx = destination.x - px;
		int dy = destination.y - py;

		if (Math.abs(dx) + Math.abs(dy) <= 2 && contient(destination) && consulter(destination) == null) {
			if (dx == -2) {
				return consulter(px - 1, py) == null;
			} else if (dx == 2) {
				return consulter(px + 1, py) == null;
			} else if (dy == -2) {
				return consulter(px, py - 1) == null;
			} else if (dy == 2) {
				return consulter(px, py + 1) == null;
			} else if (dx == 0 || dy == 0) {
				return true;
			} else {
				return consulter(px + dx, py) == null || consulter(px, py + dy) == null;
			}
		}


		return false;
	}

	public ArrayList<Passe> passesPossibles(short couleur) {
		ArrayList<Passe> passesPossibles = new ArrayList<Passe>();

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i != 0 || j != 0) {
					passesPossiblesDirection(couleur, passesPossibles, i, j);
				}
			}
		}

		return passesPossibles;
	}

	private void passesPossiblesDirection(short couleur, ArrayList<Passe> passesPossibles, int dx, int dy) {
		Point positionBallon = ballons[couleur].getPosition();
		for (int x = positionBallon.x + dx, y = positionBallon.y + dy; contient(x, y); x += dx, y += dy) {
			Pion pion = cases[x][y];
			if (pion != null) {
				if (pion.getCouleur() != couleur) {
					break;
				} else {
					passesPossibles.add(new Passe(pion, ballons[couleur]));
				}
			}
		}
	}

	public boolean passePossible(Pion receveur) {
		if (receveur != null) {
			short couleur = receveur.getCouleur();
			Pion ballon = ballons[couleur];
			int bx = ballon.getPosition().x;
			int by = ballon.getPosition().y;
			int rx = receveur.getPosition().x;
			int ry = receveur.getPosition().y;
			int dx = Integer.signum(bx - rx);
			int dy = Integer.signum(by - ry);

			if (dx != 0 && dy != 0 && Math.abs(rx - bx) != Math.abs(ry - by)) {
				return false;
			}

			for (int x = rx + dx, y = ry + dy;; x += dx, y += dy) {
				Pion pion = cases[x][y];
				if (pion != null) {
					if (pion.getCouleur() != couleur) {
						return false;
					} else if (pion == ballon) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void deplacerPion(Pion pion, Point apres) {
		deplacerPion(pion.getPosition(), apres);
	}

	public void deplacerPion(Point avant, Point apres) {
		Pion pion = cases[avant.x][avant.y];
		pion.setPosition(apres);
		cases[apres.x][apres.y] = pion;
		cases[avant.x][avant.y] = null;
	}

	public Pion consulter(Point p) {
		return consulter(p.x, p.y);
	}

	public Pion consulter(int px, int py) {
		return cases[px][py];
	}

	public boolean contient(Point p) {
		return contient(p.x, p.y);
	}

	public boolean contient(int px, int py) {
		return px >= 0 && px < TAILLE && py >= 0 && py < TAILLE;
	}

	public Pion getBallon(short couleur) {
		return ballons[couleur];
	}

	public void faireLaPasse(Pion pion) {
		ballons[pion.getCouleur()].setPossedeBallon(false);
		pion.setPossedeBallon(true);
		ballons[pion.getCouleur()] = pion;
	}

	public Pion[] getPions(short couleur) {
		return pions[couleur];
	}

	public Pion getPion(short couleur, int numero) {
		return pions[couleur][numero];
	}

	public boolean balleAuBut(short couleur) {
		return ballons[couleur].getDistanceDepart() == TAILLE - 1;
	}

	public int coupsFinaux(short couleur) {
		int nbCoupsFinaux = 0;
		int yAilier;
		if (couleur == Pion.NOIR) {
			yAilier = TAILLE - 2;
		} else {
			yAilier = 1;
		}
		for (Pion pion : pions[couleur]) {
			if (pion.getDistanceDepart() == TAILLE - 1) {
				Point p = pion.getPosition();
				for (int x = p.x - 1; x <= p.x + 1; x++) {
					if (contient(x, yAilier)) {
						Pion ailier = cases[x][yAilier];
						if (ailier != null && ailier.getCouleur() == couleur) {
							nbCoupsFinaux++;
						}
					}
				}
			}
		}
		return nbCoupsFinaux;
	}

	public boolean antiJeu(short couleur) {
		Pion[] ligne = new Pion[TAILLE];
		for (Pion pion : pions[couleur]) {
			int x = pion.getPosition().x;
			if (ligne[x] == null) {
				ligne[x] = pion;
			} else {
				return false;
			}
		}
		int pionsAdversesColles = 0;
		for (int x = 0; x < TAILLE; x++) {
			Pion pion = ligne[x];
			int y = pion.getPosition().y;
			if (x < TAILLE - 1 && Math.abs(y - ligne[x + 1].getPosition().y) > 1) {
				return false;
			} else if (contient(x, y - 1) && consulter(x, y - 1) != null && consulter(x, y - 1).getCouleur() != couleur) {
				pionsAdversesColles++;
			} else if (contient(x, y + 1) && consulter(x, y + 1) != null && consulter(x, y + 1).getCouleur() != couleur) {
				pionsAdversesColles++;
			}
		}
		return pionsAdversesColles >= 3;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 37 * hash + Arrays.deepHashCode(this.cases);
		hash = 37 * hash + Arrays.deepHashCode(this.pions);
		hash = 37 * hash + Arrays.deepHashCode(this.ballons);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		Plateau plateau = (Plateau) obj;
		return equalsCouleur(plateau, Pion.BLANC) && equalsCouleur(plateau, Pion.NOIR);
	}

	public boolean equalsCouleur(Plateau plateau, short couleur) {
		for (Pion pion : pions[couleur]) {
			Pion pion2 = plateau.consulter(pion.getPosition());
			if (pion2 == null || pion2.getCouleur() != couleur || pion.possedeBallon() != pion2.possedeBallon()) {
				return false;
			}
		}
		return true;
	}

	public Plateau clone() {
		Plateau plateau = new Plateau(true);
		for (int x = 0; x < TAILLE; x++) {
			for (int y = 0; y < TAILLE; y++) {
				Pion pion = cases[x][y];
				if (pion == null) {
					plateau.cases[x][y] = null;
				} else {
					Pion pion2 = plateau.pions[pion.getCouleur()][pion.getNumero()];
					plateau.cases[x][y] = pion2;
					pion2.setPosition(new Point(x, y));
					pion2.setPossedeBallon(pion.possedeBallon());
					if (pion2.possedeBallon()) {
						plateau.ballons[pion2.getCouleur()] = pion2;
					}
				}
			}
		}
		return plateau;
	}

	@Override
	public String toString() {
		String str = "";
		for (int y = 0; y < TAILLE; y++) {
			str += "|";
			for (int x = 0; x < TAILLE; x++) {
				Pion p = consulter(x, y);
				if (p == null) {
					str += "_";
				} else if (p.possedeBallon()) {
					str += "X";
				} else if (p.getCouleur() == Pion.BLANC) {
					str += "B";
				} else if (p.getCouleur() == Pion.NOIR) {
					str += "N";
				}
				str += "|";
			}
			str += "\n";
		}
		return str;
	}

	public ArrayList<Direction> chemin(Pion pion, Point destination) {
		ArrayList<Direction> chemin = new ArrayList<Direction>();
		int px = pion.getPosition().x;
		int py = pion.getPosition().y;
		int dx = destination.x - px;
		int dy = destination.y - py;
		switch (dx) {
			case -2:
				chemin.add(Direction.OUEST);
				chemin.add(Direction.OUEST);
				break;
			case -1:
				switch (dy) {
					case -1:
						if (contient(px - 1, py) && consulter(px - 1, py) == null) {
							chemin.add(Direction.OUEST);
							chemin.add(Direction.NORD);
						} else {
							chemin.add(Direction.NORD);
							chemin.add(Direction.OUEST);
						}
						break;
					case 0:
						chemin.add(Direction.OUEST);
						break;
					case 1:
						if (contient(px - 1, py) && consulter(px - 1, py) == null) {
							chemin.add(Direction.OUEST);
							chemin.add(Direction.SUD);
						} else {
							chemin.add(Direction.SUD);
							chemin.add(Direction.OUEST);
						}
						break;
				}
				break;
			case 0:
				switch (dy) {
					case -2:
						chemin.add(Direction.NORD);
						chemin.add(Direction.NORD);
						break;
					case -1:
						chemin.add(Direction.NORD);
						break;
					case 0:
						chemin.add(Direction.OUEST);
						break;
					case 1:
						chemin.add(Direction.SUD);
						break;
					case 2:
						chemin.add(Direction.SUD);
						chemin.add(Direction.SUD);
						break;
				}
				break;
			case 1:
				switch (dy) {
					case -1:
						if (contient(px + 1, py) && consulter(px + 1, py) == null) {
							chemin.add(Direction.EST);
							chemin.add(Direction.NORD);
						} else {
							chemin.add(Direction.NORD);
							chemin.add(Direction.EST);
						}
						break;
					case 0:
						chemin.add(Direction.EST);
						break;
					case 1:
						if (contient(px + 1, py) && consulter(px + 1, py) == null) {
							chemin.add(Direction.EST);
							chemin.add(Direction.SUD);
						} else {
							chemin.add(Direction.SUD);
							chemin.add(Direction.EST);
						}
						break;
				}
				break;
			case 2:
				chemin.add(Direction.EST);
				chemin.add(Direction.EST);
				break;

		}
		return chemin;
	}
}