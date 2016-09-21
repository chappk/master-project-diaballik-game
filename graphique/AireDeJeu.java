package graphique;

/**
 *
 * @author gervaila
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import moteur.*;

public class AireDeJeu extends JComponent implements Serializable {

	private Diabolik diabolik;
	private int largeurCase = 50;
	public BufferedImage pionBlanc, pionTransparent, pionTransparent2;
	public BufferedImage surbrillance;
	private BufferedImage pionNoir;
	private BufferedImage ballonNoir, ballonBlanc;
	public BufferedImage ballonTransparent;
	private BufferedImage plateauImage;
	private BufferedImage pionBlancIndice;
	private BufferedImage pionNoirIndice;
	private BufferedImage ballonBlancIndice;
	private BufferedImage ballonNoirIndice;
	private Pion pionCourant;

	// ne pas utiliser ce constructeur
	public AireDeJeu() {
		this(new Diabolik());
	}

	public AireDeJeu(Diabolik diabolik) {
		this.diabolik = diabolik;
		try {
			plateauImage = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/plateau.png"));
			pionBlanc = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			pionBlanc = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionBlanc.png"));
			pionTransparent = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			pionTransparent = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionTransparent.png"));
			pionTransparent2 = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			pionTransparent2 = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionTransparent2.png"));
			pionNoir = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			pionNoir = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionNoir.png"));
			ballonNoir = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			ballonNoir = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonNoir.png"));
			ballonBlanc = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			ballonBlanc = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonBlanc.png"));
			ballonTransparent = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			ballonTransparent = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonTransparent.png"));
			surbrillance = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
			surbrillance = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/surbrillance.png"));
			pionBlancIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			pionBlancIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionBlancTransparent.png"));
			pionNoirIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			pionNoirIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/pionNoirTransparent.png"));
			ballonBlancIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			ballonBlancIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonBlancTransparent.png"));
			ballonNoirIndice = new BufferedImage(largeurCase, largeurCase, BufferedImage.TYPE_INT_ARGB);
			ballonNoirIndice = ImageIO.read(getClass().getResource("/graphique/images/secondeVersion/ballonNoirTransparent.png"));

		} catch (IOException ex) {
			Logger.getLogger(AireDeJeu.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D drawable = (Graphics2D) g;

		drawable.drawImage(plateauImage, 0, 0, null);

		if (pionCourant != null) {
			drawable.drawImage(surbrillance, pionCourant.getPosition().x * largeurCase, pionCourant.getPosition().y * largeurCase, this);
		}


		// pions blancs
		for (Pion pion : diabolik.getPlateau().getPions(Pion.BLANC)) {

			if (pion.getPosition() != null) {
				int abs = pion.getPosition().x * largeurCase;
				int ord = pion.getPosition().y * largeurCase;
				if (pion.isIndice()) {
					drawable.drawImage(pionBlancIndice, abs, ord, this);
				} else {
					drawable.drawImage(pionBlanc, abs, ord, this);
				}
				if (pion.possedeBallon()) {
					if (pion.isIndice()) {
						drawable.drawImage(ballonBlancIndice, abs, ord, this);
					} else {
						drawable.drawImage(ballonBlanc, abs, ord, this);
					}
				}

			}
		}
		// pions noirs
		for (Pion pion : diabolik.getPlateau().getPions(Pion.NOIR)) {

			if (pion.getPosition() != null) {
				int abs = pion.getPosition().x * largeurCase;
				int ord = pion.getPosition().y * largeurCase;
				if (pion.isIndice()) {
					drawable.drawImage(pionNoirIndice, abs, ord, this);
				} else {
					drawable.drawImage(pionNoir, abs, ord, this);
				}
				if (pion.possedeBallon()) {
					if (pion.isIndice()) {
						drawable.drawImage(ballonNoirIndice, abs, ord, this);
					} else {
						drawable.drawImage(ballonNoir, abs, ord, this);
					}
				}

			}
		}

		// pion selectionne
		if (pionCourant != null && pionCourant.getPosition() != null && diabolik.getFenetrePrincipale().isDeplacementPossible()) {
			int abs;
			int ord;
			if (pionCourant.possedeBallon()) {
				ArrayList<Passe> passes = diabolik.getPlateau().passesPossibles(pionCourant.getCouleur());
				for (Passe passe : passes) {
					abs = passe.getPion().getPosition().x * largeurCase;
					ord = passe.getPion().getPosition().y * largeurCase;
					drawable.drawImage(ballonTransparent, abs, ord, this);

				}
			} else {
				ArrayList<Deplacement> depls = diabolik.getPlateau().deplacementsPossiblesMax(pionCourant, diabolik.getNbMouvementsRestants());
				for (Deplacement depl : depls) {

					abs = depl.getDestination().x * largeurCase;
					ord = depl.getDestination().y * largeurCase;
					if (depl.getNbDeplacements() == 2) {
						drawable.drawImage(pionTransparent2, abs + 5, ord + 5, null);
					} else {
						drawable.drawImage(pionTransparent, abs + 5, ord + 5, null);
					}

				}
			}
			drawable.setPaint(Color.BLACK);
		}
	}

	/**
	 * Renvoie les coordonÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©es de la case en fonction du clic de souris
	 * @param x Abscisse du curseur
	 * @param y OrdonnÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©e du curseur
	 * @return Point contenant les coordonnÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©es ou null si on sort du tableau
	 */
	public Point getCase(int x, int y) {
		//hors du tableau

		if (x < 0 || x > getWidth() || y < 0 || y > getHeight() - 7) {
			return null;

		}
		int x1 = x / largeurCase;
		int y1 = y / largeurCase;

		if (x1 > Plateau.TAILLE - 1) {
			x1 = Plateau.TAILLE - 1;
		}

		if (y1 > Plateau.TAILLE - 1) {
			y1 = Plateau.TAILLE - 1;
		}
		return new Point(x1, y1);
	}

	/** 
	 * Redimensionne une image.
	 * 
	 * @param source Image ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â  redimensionner.
	 * @param width Largeur de l'image cible.
	 * @param height Hauteur de l'image cible.
	 * @return Image redimensionnÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©e.
	 */
	public static BufferedImage scale(Image source, int width, int height) {
		/* On crÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©e une nouvelle image aux bonnes dimensions. */
		BufferedImage buf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		/* On dessine sur le Graphics de l'image bufferisÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©e. */
		Graphics2D g = buf.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(source, 0, 0, width, height, null);
		g.dispose();

		/* On retourne l'image bufferisÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â©e, qui est une image. */
		return buf;
	}

	// getters
	public int getLargeurCase() {
		return largeurCase;
	}

	public BufferedImage getBallonBlanc() {
		return ballonBlanc;
	}

	public void setBallonBlanc(BufferedImage ballonBlanc) {
		this.ballonBlanc = ballonBlanc;
	}

	public BufferedImage getBallonNoir() {
		return ballonNoir;
	}

	public void setBallonNoir(BufferedImage ballonNoir) {
		this.ballonNoir = ballonNoir;
	}

	public BufferedImage getPionBlanc() {
		return pionBlanc;
	}

	public void setPionBlanc(BufferedImage pionBlanc) {
		this.pionBlanc = pionBlanc;
	}

	public BufferedImage getPionNoir() {
		return pionNoir;
	}

	public void setPionNoir(BufferedImage pionNoir) {
		this.pionNoir = pionNoir;
	}

	public Pion getPioncourant() {
		return pionCourant;
	}

	public void setPioncourant(Pion pionCourant) {
		this.pionCourant = pionCourant;
	}
}