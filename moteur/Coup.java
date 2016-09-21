package moteur;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author dodelien
 */
public class Coup implements Serializable {

    private ArrayList<Action> actions;
    private boolean[][] positionPions;
    private Point positionBallon;

    public Coup() {
        actions = new ArrayList<Action>(3);
    }

    public short getCouleur() {
        return actions.get(0).pion.getCouleur();
    }

    public void setPositionPions(Pion[] pions) {
        positionPions = new boolean[Plateau.TAILLE][Plateau.TAILLE];
        for (Pion pion : pions) {
            positionPions[pion.getPosition().x][pion.getPosition().y] = true;
            if (pion.possedeBallon()) {
                positionBallon = new Point(pion.getPosition());
            }
        }
    }

    public void deplacer(Point avant, Point apres) {
        positionPions[avant.x][avant.y] = false;
        positionPions[apres.x][apres.y] = true;
    }

    public void deplacerBallon(Point positionBallon) {
        this.positionBallon = positionBallon;
    }

    public void ajouterAction(Action action) {
        actions.add(action);
    }

	public void supprimerAction() {
		actions.remove(actions.size()-1);
	}
	
    public void jouer(Plateau plateau) {
        for (Action action : actions) {
            action.jouer(plateau);
        }
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void annuler(Plateau plateau) {
        for (int i = actions.size() - 1; i >= 0; i--) {
            actions.get(i).annuler(plateau);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coup other = (Coup) obj;
        if (!Arrays.deepEquals(this.positionPions, other.positionPions)) {
            return false;
        }
        if (this.positionBallon != other.positionBallon && (this.positionBallon == null || !this.positionBallon.equals(other.positionBallon))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (int x = 0; x < Plateau.TAILLE; x++) {
            for (int y = 0; y < Plateau.TAILLE; y++) {
                if (positionPions[x][y]) {
                    int hash2 = 7;
                    hash2 = 71 * hash2 + x;
                    hash2 = 71 * hash2 + y;
                    hash = hash * 17 + hash2;
                    //hash *= ((new Point(x, y)).hashCode() + 17);
                }
            }
        }
        hash += positionBallon.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        String str = "Coup{\n";

        for (Action action : actions) {
            str += action + "\n";
        }

        str += "}";
        return str;
    }

    public void copier(Coup coup) {
        actions = coup.actions;
        positionPions = coup.positionPions;
        positionBallon = coup.positionBallon;
    }

    public boolean legal(Plateau plateau, short couleur) {
        if (actions.isEmpty()) {
            return false;
        } else {
            boolean legal = true;
            boolean passe = false;
            int nb_depl = 0;
            ArrayList<Action> actionsEffectuees = new ArrayList<Action>();

            for (Action action : actions) {
                if (action instanceof Passe) { // passe
                    if (passe) {
                        legal = false;
                        break;
                    } else {
                        passe = true;
                    }
                } else if (action instanceof Deplacement) { // déplacement
                    nb_depl += ((Deplacement) action).getNbDeplacements();
                    if (nb_depl > 2) {
                        legal = false;
                        break;
                    }
                }

                if (!action.legal(plateau, couleur)) {
                    legal = false;
                    break;
                }

                action.jouer(plateau);
                actionsEffectuees.add(0, action);
            }

            // suppression des coups testés
            for (Action action : actionsEffectuees) {
                action.annuler(plateau);
            }

            return legal;
        }
    }

    /**
     * Conversion des pions des action d'un coup provenant d'un plateau externe
     * vers ceux du plateau local
     *
     * @return nouveau coup
     */
    public static Coup conversion(Coup coupDistant, Plateau plateau) {
        Coup coupLocal = new Coup();
        for (Action act : coupDistant.getActions()) {
            if (act instanceof Deplacement) {
                Deplacement depl = (Deplacement) act;
                coupLocal.ajouterAction(new Deplacement(plateau.getPion(depl.getPion().getCouleur(), depl.getPion().getNumero()), depl.getDeplacement()));
            } else if (act instanceof Passe) {
                Passe passe = (Passe) act;
                coupLocal.ajouterAction(new Passe(plateau.getPion(passe.getPion().getCouleur(), passe.getPion().getNumero()), plateau.getPion(passe.getAncienPorteurDeBallon().getCouleur(), passe.getAncienPorteurDeBallon().getNumero())));
            }
        }
        return coupLocal;
    }

    public int getNbActions() {
        int nb = 0;
        for (Action act : actions) {
            if (act instanceof Deplacement) {
                nb += ((Deplacement) act).getNbDeplacements();
            } else if (act instanceof Passe) {
                nb++;
            }
        }
        return nb;
    }
    
    public void convertir(Plateau plateau) {
        for (Action action : actions) {
            action.convertir(plateau);
        }
    }
}
