package moteur;

import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author gervaila
 */
public class Pion implements Serializable {

    public final static short BLANC = 0;
    public final static short NOIR = 1;
    private boolean possedeBallon;
    private int numero;
    private short couleur;
    private Point position;
	private boolean indice;

    public Pion(int numero, short couleur, Point position) {
        possedeBallon = false;
        this.numero = numero;
        this.couleur = couleur;
        this.position = position;
    }

    public short getCouleur() {
        return couleur;
    }

    public int getNumero() {
        return numero;
    }

    public Point getPosition() {
        return position;
    }

    public int getDistanceDepart() {
        if (couleur == NOIR) {
            return position.y;
        } else {
            return Plateau.TAILLE - position.y - 1;
        }
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public boolean possedeBallon() {
        return possedeBallon;
    }

    public void setPossedeBallon(boolean possedeBallon) {
        this.possedeBallon = possedeBallon;
    }

	public boolean isIndice() {
		return indice;
	}

	public void setIndice(boolean indice) {
		this.indice = indice;
	}

	
    @Override
    public String toString() {
        return "Pion{" + "possedeBallon=" + possedeBallon + ", numero=" + numero + ", couleur=" + couleur + ", position=" + position + '}';
    }

    @Override
    public int hashCode() {
        return numero + 2;
    }

}
