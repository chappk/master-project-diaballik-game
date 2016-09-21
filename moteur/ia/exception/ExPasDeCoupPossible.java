/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package moteur.ia.exception;

import moteur.Joueur;

/**
 *
 * @author dodelien
 */
public class ExPasDeCoupPossible extends Throwable {

    private Joueur joueur;

    public ExPasDeCoupPossible(Joueur joueur) {
        this.joueur = joueur;
    }

    @Override
    public String toString() {
        return "ExPasDeCoupPossible{" + "joueur=" + joueur + '}';
    }
}
