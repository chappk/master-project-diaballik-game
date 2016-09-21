package moteur;

/**
 *
 * @author vernagaa
 */
abstract public class JoueurIA extends Joueur {
	
	public static final int INFINI = Integer.MAX_VALUE / 2;
	
	public JoueurIA(String nom, short couleur) {
		super(couleur);
		this.nom = nom;
	}

	abstract public Coup choisirCoup(Plateau plateau);
}
