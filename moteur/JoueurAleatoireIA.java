package moteur;

import java.util.ArrayList;

/**
 *
 * @author dodelien
 */
public class JoueurAleatoireIA extends JoueurIA {

	public JoueurAleatoireIA(short couleur) {
		super("Jean Bon", couleur);
	}

	@Override
	public Coup choisirCoup(Plateau plateau) {
		Coup coup = coupAleatoire(plateau);
		coup.annuler(plateau);
		return coup;
	}
}
