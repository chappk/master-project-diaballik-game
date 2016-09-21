/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author dodelien
 */
public class PairePoidsValeur<T> {
	
	public int poids;
	public T valeur;
	
	public boolean equals(Object obj) {
		PairePoidsValeur<T> ppv = (PairePoidsValeur<T>)obj;
		return ppv.valeur.equals(valeur);
	}
	
}
