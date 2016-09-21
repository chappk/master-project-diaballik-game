/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphique;

import java.awt.Point;
import moteur.Deplacement;
import moteur.Pion;
import moteur.Plateau;

/**
 *
 * @author dodelien
 */
public class AnimationIndiceDeplacement extends Animation {
	
	private Point positionPion;
	private Pion pion;
	private Point destination;
	private Point positionInitiale;
	
	public AnimationIndiceDeplacement(MyGlassPane myGlassPane, Deplacement deplacement, Plateau plateau) {
		super(myGlassPane, plateau, deplacement);
		pion = deplacement.getPion();
		positionPion = pion.getPosition();
		destination = deplacement.getDestination();
		positionInitiale = deplacement.getPositionInitiale();
		dureeAnimation = 400;
		nbFrames = dureeAnimation / 5;
	}

	@Override
	public void initialiser() {
		myGlassPane.ballon = false;
		pion.setPosition(null);
		pion.setIndice(true);
		xdebut = positionPion.x * 50;
		ydebut = positionPion.y * 50;
		xfin = destination.x * 50;
		yfin = destination.y * 50;
	}

	@Override
	public void terminer() {
		pion.setPosition(positionPion);
		action.jouer(plateau);
	}

	@Override
	protected float getF() {
		return Math.min(super.getF()*2, 1);
	}
	
	
	
	
}
