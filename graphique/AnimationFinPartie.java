/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphique;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;
import moteur.Action;
import moteur.Pion;
import moteur.Plateau;

/**
 *
 * @author gervaila
 */
public class AnimationFinPartie implements ActionListener {

	private Timer timer;
	protected MyGlassPane myGlassPane;
	protected Plateau plateau;
	protected int dureeAnimation;
	private int numeroFrame;
	protected int nbFrames;
	private float[][][] vitesse = new float[2][7][2];
	private Random rand = new Random();
	private int acceleration;
	private Point[][] anciennePosition = new Point[2][7];

	public AnimationFinPartie(MyGlassPane myGlassPane, Plateau plateau) {
		this.myGlassPane = myGlassPane;
		this.plateau = plateau;
		dureeAnimation = 5000;
		numeroFrame = 0;
		nbFrames = dureeAnimation / 20;
		acceleration = 2;
	}

	public void commencer() {
		myGlassPane.enCoursFinPartie = true;
		myGlassPane.activerEcouteurs(false);
		initialiser();
		myGlassPane.repaint();
		myGlassPane.aireDeJeu.repaint();
		int dureeFrame = (int) ((float) dureeAnimation / (float) nbFrames);
		timer = new Timer(dureeFrame, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (numeroFrame % (nbFrames) == 0) {
			for (int c = 0; c < 2; c++) {
				for (int i = 0; i < 7; i++) {
					vitesse[c][i][0] = rand.nextInt(100) - 100;
					vitesse[c][i][1] = rand.nextInt(100) - 50;
				}
			}
			numeroFrame++;
		} else if (numeroFrame <= nbFrames) {
			for (int c = 0; c < 2; c++) {
				for (int i = 0; i < 7; i++) {
					vitesse[c][i][1] += acceleration;

					myGlassPane.coord[c][i].x += vitesse[c][i][0];
					myGlassPane.coord[c][i].y += vitesse[c][i][1];
					//myGlassPane.coord[c][i].x += 1;
					//myGlassPane.coord[c][i].y -= 1;
					if (myGlassPane.coord[c][i].x <= 0 || myGlassPane.coord[c][i].x >= 300) {
						vitesse[c][i][0] *= -0.6;
						myGlassPane.coord[c][i].x += vitesse[c][i][0];
					}
					if (myGlassPane.coord[c][i].y <= 0) {
						vitesse[c][i][1] *= 0.5;
						myGlassPane.coord[c][i].y += vitesse[c][i][1];
					}

					if (myGlassPane.coord[c][i].y >= 300) {
						vitesse[c][i][1] *= -0.85;
						myGlassPane.coord[c][i].y += vitesse[c][i][1];



					}
				}
			}

			myGlassPane.repaint();
			numeroFrame++;
		} else {
			finir(false);
		}
	}

	public void initialiser() {
		for (int i = 0; i < 7; i++) {
			myGlassPane.coord[0][i] = new Point();
			myGlassPane.coord[1][i] = new Point();
			myGlassPane.coord[0][i].x = plateau.getPion(Pion.BLANC, i).getPosition().x * 50;
			myGlassPane.coord[0][i].y = plateau.getPion(Pion.BLANC, i).getPosition().y * 50;
			myGlassPane.coord[1][i].x = plateau.getPion(Pion.NOIR, i).getPosition().x * 50;
			myGlassPane.coord[1][i].y = plateau.getPion(Pion.NOIR, i).getPosition().y * 50;
			vitesse[0][i][0] = rand.nextInt(40) - 20;
			vitesse[1][i][0] = rand.nextInt(40) - 20;
			vitesse[0][i][1] = rand.nextInt(40) - 20;
			vitesse[1][i][1] = rand.nextInt(40) - 20;

			anciennePosition[Pion.BLANC][i] = new Point(plateau.getPion(Pion.BLANC, i).getPosition());
			anciennePosition[Pion.NOIR][i] = new Point(plateau.getPion(Pion.NOIR, i).getPosition());
			plateau.getPion(Pion.BLANC, i).setPosition(null);
			plateau.getPion(Pion.NOIR, i).setPosition(null);
		}
	}

	public void terminer() {
		for (short c = 0; c < 2; c++) {
			for (int i = 0; i < 7; i++) {
				plateau.getPion(c, i).setPosition(anciennePosition[c][i]);
			}
		}
	}

	public void redemarrer() {
		for (int i = 0; i < 7; i++) {
			plateau.getPion(Pion.BLANC, i).setPosition(new Point(i, 6));
			plateau.getPion(Pion.NOIR, i).setPosition(new Point(i, 0));
		}
		plateau.faireLaPasse(plateau.getPion(Pion.BLANC, 3));
		plateau.faireLaPasse(plateau.getPion(Pion.NOIR, 3));
	}

	public void finir(boolean retablir) {
		timer.stop();
		if (retablir) {
			terminer();
		} else {
			redemarrer();
		}
		myGlassPane.repaint();
		myGlassPane.aireDeJeu.repaint();
		myGlassPane.activerEcouteurs(true);
		myGlassPane.enCoursFinPartie = false;
	}
}