package moteur;

/**
 *
 * @author dodelien
 */
public class Passe extends Action {
    
    private Pion ancienPorteurDeBallon;
    
    public Passe(Pion pion, Pion ancienPorteurDeBallon) {
        super(pion);
        this.ancienPorteurDeBallon = ancienPorteurDeBallon;
    }

    @Override
    public void jouer(Plateau plateau) {
        plateau.faireLaPasse(pion);
    }
    
    @Override
    public void annuler(Plateau plateau) {
        plateau.faireLaPasse(ancienPorteurDeBallon);
    }

    @Override
    public boolean legal(Plateau plateau, short couleur) {
        return pion.getCouleur() == couleur && ancienPorteurDeBallon.getCouleur() == couleur
				&& plateau.passePossible(pion);
    }

    @Override
    public String toString() {
        return "Passe{" + "pion Receveur=" + pion + "ancien pion= "+ ancienPorteurDeBallon +'}';
    }

    @Override
    public boolean equals(Object obj) {
        return pion == ((Passe)obj).pion; // ca suffit dans notre cas
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + super.hashCode();
        return hash;
    }

	public Pion getAncienPorteurDeBallon() {
		return ancienPorteurDeBallon;
	}

	@Override
	public void convertir(Plateau plateau) {
		super.convertir(plateau);
		ancienPorteurDeBallon = plateau.getPion(ancienPorteurDeBallon.getCouleur(), ancienPorteurDeBallon.getNumero());
	}

    
}