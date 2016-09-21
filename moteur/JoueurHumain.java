package moteur;

/**
 *
 * @author vernagaa
 */
public class JoueurHumain extends Joueur {

	public JoueurHumain(short couleur) {
		super(couleur);
		nom = couleur == Pion.BLANC ? "Joueur Blanc" : "Joueur Noir";
	}

	public JoueurHumain(String nom, short couleur) {
		super(nom, couleur);
	}
}
