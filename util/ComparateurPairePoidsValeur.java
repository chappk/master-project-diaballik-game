/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Point;
import java.util.Comparator;

/**
 *
 * @author dodelien
 */
class ComparateurPairePoidsValeur implements Comparator {
	
	public int compare(Object a, Object b) {
		PairePoidsValeur<Point> A = (PairePoidsValeur<Point>)a;
		PairePoidsValeur<Point> B = (PairePoidsValeur<Point>)b;
		return A.poids - B.poids;
	}
	
}