package moteur;

import java.io.Serializable;



/**
 *
 * @author dodelien
 */
abstract public class Action implements Serializable {

    protected Pion pion;

    public Action(Pion pion) {
        this.pion = pion;
    }

    public Pion getPion() {
        return pion;
    }

    abstract public void jouer(Plateau plateau);
    abstract public void annuler(Plateau plateau);
    abstract public boolean legal(Plateau plateau, short couleur);
    
    @Override
    public String toString() {
        return "Action{" + "pion=" + pion + '}';
    }
    
    // transforme l'action pour la rendre utilisable avec ce plateau
    public void convertir(Plateau plateau) {
        pion = plateau.getPion(pion.getCouleur(), pion.getNumero());
    }

}
