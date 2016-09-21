/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphique;

import moteur.Passe;
import moteur.Pion;
import moteur.Plateau;

/**
 *
 * @author dodelien
 */
public class AnimationPasse extends Animation {
	
	private Pion futurPorteurDeBallon;
	private Pion ancienPorteurDeBallon;

	public AnimationPasse(MyGlassPane myGlassPane, Passe passe, Plateau plateau) {
		super(myGlassPane, plateau, passe);
		ancienPorteurDeBallon = passe.getAncienPorteurDeBallon();
		futurPorteurDeBallon = passe.getPion();
	}

	@Override
	public void initialiser() {
		myGlassPane.ballon = true;
		ancienPorteurDeBallon.setPossedeBallon(false);
		xdebut = ancienPorteurDeBallon.getPosition().x * 50;
		ydebut = ancienPorteurDeBallon.getPosition().y * 50;
		xfin = futurPorteurDeBallon.getPosition().x * 50;
		yfin = futurPorteurDeBallon.getPosition().y * 50;
	}

	@Override
	public void terminer() {
		myGlassPane.ballon = false;
		action.jouer(plateau);
	}
}
