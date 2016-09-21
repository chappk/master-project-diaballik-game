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
public class AnimationAnnulerDeplacement extends Animation {

	private Point positionPion;
	private Pion pion;
	private Point positionInitiale;
	private Deplacement deplacement;

	public AnimationAnnulerDeplacement(MyGlassPane myGlassPane, Deplacement deplacement, Plateau plateau) {
		super(myGlassPane, plateau, deplacement);
		this.deplacement = deplacement;

	}

	@Override
	public void initialiser() {
		pion = deplacement.getPion();
		positionPion = pion.getPosition();
		positionInitiale = deplacement.getPositionInitiale();
		myGlassPane.ballon = false;
		pion.setPosition(null);
		xdebut = positionPion.x * 50;
		ydebut = positionPion.y * 50;
		xfin = positionInitiale.x * 50;
		yfin = positionInitiale.y * 50;
	}

	@Override
	public void terminer() {
		pion.setPosition(positionPion);
		action.annuler(plateau);
	}
}
