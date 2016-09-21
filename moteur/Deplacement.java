package moteur;

import java.awt.Point;

/**
 *
 * @author dodelien
 */
public class Deplacement extends Action {

    private Point deplacement;

    public Deplacement(Pion pion) {
        super(pion);
        deplacement = new Point(0, 0);
    }

    public Deplacement(Pion pion, Point deplacement) {
        super(pion);
        this.deplacement = deplacement;
    }

    public void ajouterDirection(Direction direction) {
        Point v = direction.getVecteur();
        deplacement.x += v.x;
        deplacement.y += v.y;
    }

    @Override
    public void jouer(Plateau plateau) {
        plateau.deplacerPion(pion.getPosition(), getDestination());
    }

    @Override
    public void annuler(Plateau plateau) {
        plateau.deplacerPion(pion.getPosition(), getPositionInitiale());
    }

    public Point getDestination() {
        Point destination = new Point(pion.getPosition());
        destination.x += deplacement.x;
        destination.y += deplacement.y;
        return destination;
    }
	
	public Point getPositionInitiale() {
        Point positionInitiale = new Point(pion.getPosition());
        positionInitiale.x -= deplacement.x;
        positionInitiale.y -= deplacement.y;
        return positionInitiale;
	}

    @Override
    public boolean legal(Plateau plateau, short couleur) {
        return pion.getCouleur() == couleur && plateau.deplacementPossible(pion, getDestination());
    }

    @Override
    public String toString() {
        return "Deplacement{" + "pion=" + pion + ", deplacement=" + deplacement + '}';
    }

    @Override
    public boolean equals(Object obj) {
        Deplacement dep = (Deplacement) obj;
        return deplacement.equals(dep.deplacement) && pion == dep.pion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash *= getDestination().hashCode();
        return hash;
    }

    public Point getDeplacement() {
        return deplacement;
    }

    public int getNbDeplacements() {
        return Math.abs(deplacement.x) + Math.abs(deplacement.y);
    }
    
    
}
