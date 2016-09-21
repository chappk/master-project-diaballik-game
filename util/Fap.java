/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author dodelien
 */
public class Fap<T> {
    
    private ArrayList<PairePoidsValeur<T>> valeurs;
    
    public Fap() {
        valeurs = new ArrayList<PairePoidsValeur<T>>();
    }
    
    public PairePoidsValeur<T> extraire() {
        PairePoidsValeur<T> ppv = valeurs.get(0);
        valeurs.remove(0);
        return ppv;
    }
    
    public void inserer(T valeur, int poids) {
        PairePoidsValeur<T> ppv = new PairePoidsValeur<T>();
        ppv.poids = poids;
        ppv.valeur = valeur;
        valeurs.add(ppv);
        Collections.sort(valeurs, new ComparateurPairePoidsValeur());
    }
    
    public boolean contient(T valeur) {
        PairePoidsValeur ppv = new PairePoidsValeur<T>();
        ppv.valeur = valeur;
        return valeurs.contains(ppv);
    }
    
    public boolean estVide() {
        return valeurs.isEmpty();
    }
    
}
