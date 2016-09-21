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
public class AnimationDeplacement extends Animation {
	
	private Point positionPion;
	private Pion pion;
	private Point destination;
	private Deplacement deplacement;
	
	public AnimationDeplacement(MyGlassPane myGlassPane, Deplacement deplacement, Plateau plateau) {
		super(myGlassPane, plateau, deplacement);
		this.deplacement = deplacement;
	}

	@Override
	public void initialiser() {
				pion = deplacement.getPion();
		positionPion = pion.getPosition();
		destination = deplacement.getDestination();
		myGlassPane.ballon = false;
		pion.setPosition(null);
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
	
}
