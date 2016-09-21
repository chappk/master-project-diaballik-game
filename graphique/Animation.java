/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphique;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import moteur.Action;
import moteur.Plateau;

/**
 *
 * @author dodelien
 */
public abstract class Animation implements ActionListener {

	private Timer timer;
	protected MyGlassPane myGlassPane;
	protected Plateau plateau;
	protected int xdebut;
	protected int ydebut;
	protected int xfin;
	protected int yfin;
	protected int dureeAnimation;
	private int numeroFrame;
	protected int nbFrames;
	protected Action action;

	public Animation(MyGlassPane myGlassPane, Plateau plateau, Action action) {
		this.myGlassPane = myGlassPane;
		this.plateau = plateau;
		this.action = action;
		dureeAnimation = myGlassPane.dureeAnimation;
		numeroFrame = 0;
		nbFrames = dureeAnimation / 5;
	}

	public void commencer() {
		myGlassPane.enCoursDAnimation = true;
		myGlassPane.activerEcouteurs(false);
		initialiser();
		myGlassPane.x = xdebut;
		myGlassPane.y = ydebut;
		myGlassPane.couleurAnimation = action.getPion().getCouleur();
		myGlassPane.zoom = 1;
		myGlassPane.repaint();
		myGlassPane.aireDeJeu.repaint();
		int dureeFrame = (int) ((float) dureeAnimation / (float) nbFrames);
		timer = new Timer(dureeFrame, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (numeroFrame <= nbFrames) {
			if (nbFrames != 0) {
				float f = getF();
				myGlassPane.x = xdebut + (int) ((xfin - xdebut) * f);
				myGlassPane.y = ydebut + (int) ((yfin - ydebut) * f);
				myGlassPane.zoom = getZoom();
			} else {
				myGlassPane.x = xfin;
				myGlassPane.y = yfin;
				myGlassPane.zoom = 1;
			}
			myGlassPane.repaint();
			numeroFrame++;
		} else {
			timer.stop();
			terminer();
			myGlassPane.repaint();
			myGlassPane.aireDeJeu.repaint();
			myGlassPane.activerEcouteurs(true);
			myGlassPane.enCoursDAnimation = false;
			myGlassPane.animer();
		}
	}
	
	protected float getF() {
		float pi2 = (float) (Math.PI * 2);
		float f = (float)numeroFrame / nbFrames;
		return (float) ((f * pi2 - Math.sin(f * pi2)) / pi2);
	}
	
	protected float getZoom() {
		float f = getF();
		return 1 + 1.5f * f - 1.5f * f * f;
	}

	public abstract void initialiser();

	public abstract void terminer();
}
